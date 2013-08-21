package com.psygate.civdominion.listeners;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.configuration.Configuration;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.events.DominionGrowthEvent;
import com.psygate.civdominion.events.PreDominionUpgradeEvent;
import com.psygate.civdominion.events.SendChatEvent;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.MapStructure;
import com.psygate.civdominion.types.PreDominion;
import com.psygate.civdominion.types.uac.Rank;
import com.psygate.minecraft.PlayerInventoryHelper;
import com.untamedears.citadel.Citadel;
import com.untamedears.citadel.entity.IReinforcement;

public class DominionListener implements Listener {
	private PreDominion pred;
	private Dominion dom;
	private Configuration conf;
	private MapStructure map;

	public DominionListener(Configuration conf, MapStructure map) {
		this.conf = conf;
		pred = conf.getProtoPreDominion();
		dom = conf.getProtoDominion();
		this.map = map;
	}

	/**
	 * This method handles the basic placement of a dominion block. If the
	 * player is not sneaking, the proximity is checked for other dominions. If
	 * another one is nearby, and they would overlap, the setting of the block
	 * is cancelled.
	 * 
	 * @param ev
	 */
	@EventHandler
	public void onBlockPlacement(BlockPlaceEvent ev) {
		if (ev.isCancelled())
			return;
		if (!ev.getPlayer().isSneaking() && ev.getBlock().getType().equals(pred.getMaterial())) {
			if (map.proximityCheck(ev.getBlock(), dom.getMaxsize())) {
				ev.getPlayer().sendMessage(Strings.closeby);
				ev.setCancelled(true);
			} else {
				Coordinates coords = new Coordinates(ev.getBlock().getX(), ev.getBlock().getY(), ev.getBlock().getZ(),
						ev.getBlock().getWorld().getName());
				PreDominion dom = pred.clone();
				dom.setCoordinates(coords);
				dom.addVoter(ev.getPlayer().getName(), Rank.Owner);
				map.addPreDominion(dom);
				ev.getPlayer().sendMessage(Strings.predomcreated + (dom.getVoteThreshold() - 1));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent ev) {
		if (ev.isCancelled())
			return;
		if (CivDominion.CITADEL) {
			IReinforcement r = Citadel.getReinforcementManager().getReinforcement(ev.getBlock());

			if (r != null && r.getDurability() > 1) {
				return;
			}
		}

		MapStructure str = CivDominion.getInstance().getMap();
		if (str.isDominion(ev.getBlock())) {
			Dominion dom = str.getDominion(ev.getBlock());
			str.removeDominion(dom.getCoordinates());
			for (String name : dom.getMemberMap().keySet()) {
				map.removePlayerDominion(name, dom);
				Player p = CivDominion.getInstance().getServer().getPlayer(name);
				if (p != null) {
					p.sendMessage(Strings.brokendominion.replace("$c$", dom.getCoordinates().toString()));
				}
			}

		} else if (str.isPreDominion(ev.getBlock())) {
			PreDominion dom = str.getPreDominion(ev.getBlock());
			str.removePreDominion(dom.getCoordinates());
			for (String name : dom.getVoters().keySet()) {
				Player p = CivDominion.getInstance().getServer().getPlayer(name);
				if (p != null) {
					p.sendMessage(Strings.brokenpredominion.replace("$c$", dom.getCoordinates().toString()));
				}
			}
		}
	}

	/**
	 * Handles the voting on blocks. Sneaking voting is ignored.
	 * 
	 * @param ev
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent ev) {
		if (ev.isCancelled())
			return;
		if (!ev.getAction().equals(Action.LEFT_CLICK_BLOCK) || ev.getPlayer().isSneaking())
			return;
		PreDominion dom = map.getPreDominion(ev.getClickedBlock());
		if (dom != null) {
			boolean debug = CivDominion.DEBUG && CivDominion.getInstance().isDominionOp(ev.getPlayer().getName());
			if (!debug && dom.getVoters().containsKey(ev.getPlayer().getName())) {
				CivDominion.getInstance().emitEvent(new SendChatEvent(Strings.doubleVote, ev.getPlayer()));
				return;
			} else {
				if (!dom.getStack().getType().equals(ev.getPlayer().getItemInHand().getType())) {
					ev.getPlayer().sendMessage(Strings.wrongvotingmaterial);
					return;
				}

				PlayerInventoryHelper.takeFromPlayerInventory(dom.getStack(), ev.getPlayer());
				String name = (debug) ? (new BigInteger(130, new Random()).toString(32)) : ev.getPlayer().getName();
				dom.addVoter(name, Rank.User);
				ev.getPlayer().sendMessage(Strings.voteRegistered);
				if (dom.getVoters().size() == dom.getVoteThreshold()) {
					CivDominion.getInstance().emitEvent(new PreDominionUpgradeEvent(dom));
				}
			}
		}
	}

	/**
	 * This creates the dominion if enough people have voted on it.
	 * 
	 * @param ev
	 */
	@EventHandler
	public void createDominion(PreDominionUpgradeEvent ev) {
		Dominion dom = this.dom.clone();
		dom.setMembers(ev.getPreDominion().getVoters());
		dom.setCoordinates(ev.getPreDominion().getCoordinates());

		map.removePreDominion(ev.getPreDominion());
		map.addDominion(dom);
		for (String name : dom.getMemberMap().keySet()) {
			map.addPlayerDominion(name, dom);
			Player p = CivDominion.getInstance().getServer().getPlayer(name);
			if (p != null) {
				CivDominion.getInstance().emitEvent(new SendChatEvent(Strings.domcreate, p));
			}
		}
	}

	@EventHandler
	public void growEvent(DominionGrowthEvent ev) {
		if (ev.isCancelled())
			return;
		OfflinePlayer[] op = CivDominion.getInstance().getServer().getOfflinePlayers();
		for (OfflinePlayer p : op) {
			if (p.getBedSpawnLocation() != null) {
				Location l = p.getBedSpawnLocation();
				Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(l.getBlock());
				if (dom == null)
					continue;
				if (dom.getCoordinates().squareDistance2D(l.getBlock()) < dom.getRadius() * dom.getRadius()) {
					if (CivDominion.getInstance().getPlayerMap().hasPlayerRecord(p.getName())) {
						if (CivDominion.getInstance().getPlayerMap().getPlayerTime(p.getName()) > TimeUnit.MINUTES
								.toMicros(30)) {
							float newrad = dom.getRadius() + dom.getGwpu();
							if (newrad > dom.getMaxsize()) {
								dom.setRadius(dom.getMaxsize());
							} else {
								dom.setRadius(dom.getRadius() + dom.getGwpu());
							}
						} else if (System.currentTimeMillis() - p.getLastPlayed() > TimeUnit.DAYS.toMillis(31)) {
							float newrad = dom.getRadius() - dom.getGwpu();
							if (newrad <= 0) {
								CivDominion.getInstance().getMap().removeDominion(dom.getCoordinates());
							} else {
								dom.setRadius(newrad);
							}
						}
					}
				}
			}
		}
		CivDominion.getInstance().getPlayerMap().clear();

	}
}
