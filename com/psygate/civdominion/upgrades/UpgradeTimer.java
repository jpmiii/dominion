package com.psygate.civdominion.upgrades;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class UpgradeTimer {
	private HashMap<Coordinates, Vector> map = new HashMap<Coordinates, Vector>();
	
	public void addUpgrade(Coordinates coords, Upgrade up) {
		map.put(coords, new Vector(up, up.getBuildTime() * 1000+ System.currentTimeMillis()));
		Runnable finish = new FinishUpgradeTask(coords);
		int id = CivDominion.getInstance().scheduleTaskOnce(finish, up.getBuildTime());
		map.get(coords).setFinishjob(id);
	}
	
	public HashMap<Coordinates, Vector> getMap() {
		return map;
	}
	
	public Collection<Integer> getFinishJobs() {
		LinkedList<Integer> run = new LinkedList<Integer>();
		for(Vector v : map.values()) {
			run.add(v.getFinishjob());
		}
		
		return run;
	}
	
	public class FinishUpgradeTask implements Runnable {
		private Coordinates coords;
		
		public FinishUpgradeTask(Coordinates coords) {
			this.coords = coords;
		}
		
		@Override
		public void run() {
			Upgrade up = map.get(coords).getUp();
			Logger.getLogger("DominionUpgradeTaskManager").info("Upgrade for "+coords+" finished ("+up.name()+")");
			Dominion dom = CivDominion.getInstance().getMap().getDominion(coords);
			dom.addUpgrade(up);
			for(Entry<String, Rank> entry : dom.getMemberMap().entrySet()) {
				if(Rank.Admin.getID() <= entry.getValue().getID()) {
					Player p = CivDominion.getInstance().getServer().getPlayer(entry.getKey());
					if(p != null) {
						p.sendMessage(Strings.upgradefinish.replace("$d$", up.name()).replace("$c$", coords.toString()));
					}
				}
			}
			map.remove(coords);
		}
	}

	public boolean isUpgradePendingFor(Coordinates coordinates) {
		return map.containsKey(coordinates);
	}

	public void addVector(Coordinates key, Vector value) {
		map.put(key, value);
		Runnable finish = new FinishUpgradeTask(key);
		long finishsecs = (value.getFinishTime() - System.currentTimeMillis()) / 1000;
		int id = CivDominion.getInstance().scheduleTaskOnce(finish, finishsecs);
		value.setFinishjob(id);
	}

	public void cancel(Coordinates coordinates) {
		if(map.containsKey(coordinates)) {
			Vector v = map.get(coordinates);
			CivDominion.getInstance().cancelTask(v.getFinishjob());
			map.remove(coordinates);
		}
	}
}
