package com.psygate.civdominion.types;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerMap {
	private HashMap<String, PlayerContainer> map;
	private transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	public PlayerMap() {
		map = new HashMap<String, PlayerContainer>();
	}

	private PlayerMap(HashMap<String, PlayerContainer> map) {
		this.map = map;
	}

	public Listener getListener() {
		return new PlayerListener();
	}

	public boolean hasPlayerRecord(String name) {
		lock.readLock().lock();
		boolean is = map.containsKey(name);
		lock.readLock().unlock();
		return is;
	}

	public long getPlayerTime(String name) {
		lock.readLock().lock();
		long time = map.get(name).getCumulativeTime();
		lock.readLock().unlock();
		return time;
	}

	public class PlayerContainer {
		private long mark = 0;
		private long cumulativetime = 0;

		public void login() {
			mark = System.currentTimeMillis();
		}

		public void logout() {
			cumulativetime += System.currentTimeMillis() - mark;
		}

		public long getCumulativeTime() {
			return cumulativetime;
		}
	}

	public class PlayerListener implements Listener {
		@EventHandler
		public void onPlayerLogin(PlayerLoginEvent ev) {
			String name = ev.getPlayer().getName();
			lock.writeLock().lock();
			if (map.containsKey(name)) {
				map.get(name).login();
			} else {
				PlayerContainer c = new PlayerContainer();
				c.login();
				map.put(name, c);
			}
			lock.writeLock().unlock();
		}

		@EventHandler
		public void onPlayerLogout(PlayerQuitEvent ev) {
			String name = ev.getPlayer().getName();
			lock.writeLock().lock();
			if (map.containsKey(name)) {
				map.get(name).logout();
			}
			lock.writeLock().unlock();
		}
	}

	public void clear() {
		lock.writeLock().lock();
		map.clear();
		lock.writeLock().unlock();
	}

	public PlayerMap clone() {
		lock.readLock().lock();
		@SuppressWarnings("unchecked")
		HashMap<String, PlayerContainer> cmap = (HashMap<String, PlayerContainer>) map.clone();
		PlayerMap pm = new PlayerMap(cmap);
		lock.readLock().unlock();
		return pm;
	}

	private Object readResolve() {
		lock = new ReentrantReadWriteLock(true); // If this was deserialized,
													// the reentrant lock should
													// be reintroduced.
		return this;
	}
}
