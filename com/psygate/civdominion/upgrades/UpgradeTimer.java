package com.psygate.civdominion.upgrades;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class UpgradeTimer {
	private HashMap<Coordinates, Vector> map;
	private ReentrantReadWriteLock maplock = new ReentrantReadWriteLock(true);

	public UpgradeTimer() {
		map = new HashMap<Coordinates, Vector>();
	}

	private UpgradeTimer(HashMap<Coordinates, Vector> map) {
		this.map = map;
	}

	public void addUpgrade(Coordinates coords, Upgrade up) {
		maplock.writeLock().lock();
		map.put(coords, new Vector(up, up.getBuildTime() * 1000 + System.currentTimeMillis()));
		Runnable finish = new FinishUpgradeTask(coords);
		int id = CivDominion.getInstance().scheduleTaskOnce(finish, up.getBuildTime());
		map.get(coords).setFinishjob(id);
		maplock.writeLock().unlock();
	}

	public HashMap<Coordinates, Vector> getMap() {
		maplock.readLock().lock();
		HashMap<Coordinates, Vector> bm = new HashMap<Coordinates, Vector>(map);
		maplock.readLock().unlock();
		return bm;
	}

	public Collection<Integer> getFinishJobs() {
		LinkedList<Integer> run = new LinkedList<Integer>();
		maplock.readLock().lock();
		for (Vector v : map.values()) {
			run.add(v.getFinishjob());
		}
		maplock.readLock().unlock();
		return run;
	}

	public class FinishUpgradeTask implements Runnable {
		private Coordinates coords;

		public FinishUpgradeTask(Coordinates coords) {
			this.coords = coords;
		}

		@Override
		public void run() {
			maplock.readLock().lock();
			Upgrade up = map.get(coords).getUp();
			maplock.readLock().unlock();
			Logger.getLogger("DominionUpgradeTaskManager").info(
					"Upgrade for " + coords + " finished (" + up.name() + ")");
			Dominion dom = CivDominion.getInstance().getMap().getDominion(coords);
			dom.addUpgrade(up);
			for (Entry<String, Rank> entry : dom.getMemberMap().entrySet()) {
				if (Rank.Admin.getID() <= entry.getValue().getID()) {
					Player p = CivDominion.getInstance().getServer().getPlayer(entry.getKey());
					if (p != null) {
						p.sendMessage(Strings.upgradefinish.replace("$d$", up.name()).replace("$c$", coords.toString()));
					}
				}
			}

			maplock.writeLock().lock();
			map.remove(coords);
			maplock.writeLock().unlock();
		}
	}

	public boolean isUpgradePendingFor(Coordinates coordinates) {
		maplock.readLock().lock();
		boolean is = map.containsKey(coordinates);
		maplock.readLock().unlock();
		return is;
	}

	public void addVector(Coordinates key, Vector value) {
		maplock.writeLock().lock();
		map.put(key, value);
		maplock.writeLock().unlock();
		Runnable finish = new FinishUpgradeTask(key);
		long finishsecs = (value.getFinishTime() - System.currentTimeMillis()) / 1000;
		int id = CivDominion.getInstance().scheduleTaskOnce(finish, finishsecs);
		value.setFinishjob(id);
	}

	public void cancel(Coordinates coordinates) {
		maplock.readLock().lock();
		boolean unlocked = false;
		if (map.containsKey(coordinates)) {
			Vector v = map.get(coordinates);
			CivDominion.getInstance().cancelTask(v.getFinishjob());
			maplock.readLock().unlock();
			unlocked = true;
			maplock.writeLock().lock();
			map.remove(coordinates);
			maplock.writeLock().unlock();
		}

		if (!unlocked)
			maplock.readLock().unlock();
	}

	public UpgradeTimer clone() {
		return new UpgradeTimer(this.getMap());
	}

	private Object readResolve() {
		maplock = new ReentrantReadWriteLock(true);
		return this;
	}
}
