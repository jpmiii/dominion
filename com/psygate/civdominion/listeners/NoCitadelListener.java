package com.psygate.civdominion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.upgrades.Upgrade;
import com.untamedears.citadel.events.CreateReinforcementEvent;

public class NoCitadelListener implements Listener {
	@EventHandler
	public void nocitadel(CreateReinforcementEvent ev) {
		if (ev.isCancelled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(Upgrade.NoCitadel))
			return;
		if (dom.hasMember(ev.getPlayer().getName()))
			return;
		if (dom.getCoordinates().squareDistance2D(ev.getBlock()) < dom.getRadius() * dom.getRadius()) {
			ev.setCancelled(true);
		}
	}
}
