package com.psygate.civdominion.types;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerMap {
	private HashMap<String, PlayerContainer> map = new HashMap<String, PlayerContainer>();
	
	public Listener getListener() {
		return new PlayerListener();
	}
	
	public boolean hasPlayerRecord(String name) {
		return map.containsKey(name);
	}
	
	public long getPlayerTime(String name) {
		return map.get(name).getCumulativeTime();
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
			if(map.containsKey(name)) {
				map.get(name).login();
			} else {
				PlayerContainer c = new PlayerContainer();
				c.login();
				map.put(name, c);
			}
		}
		
		@EventHandler
		public void onPlayerLogout(PlayerQuitEvent ev) {
			String name = ev.getPlayer().getName();
			if(map.containsKey(name)) {
				map.get(name).logout();
			}
		}
	}

	public void clear() {
		map.clear();
	}
}
