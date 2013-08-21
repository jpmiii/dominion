package com.psygate.civdominion.commands.information;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.events.PreDominionUpgradeEvent;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.PreDominion;

public class DrawBoundariesCommand extends ACommand {

	public DrawBoundariesCommand() {
		super("dominiondrawboundaries");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			// CivDominion.getInstance().emitEvent(new
			// PreDominionUpgradeEvent(dom));
			sender.sendMessage(Strings.drawbounds);
			Coordinates cor = dom.getCoordinates();
			World w = CivDominion.getInstance().getServer().getWorld(cor.getWorld());

			boolean drawinner = true;
			if (dom.getRadius() - dom.getExclusionZone() <= 1) {
				drawinner = false;
			}

			for (float i = 0; i < Math.PI * 2; i += Math.PI * 2 / (dom.getRadius() * 10)) {
				int x = (int) (dom.getCoordinates().getX() + Math.cos(i) * dom.getRadius());
				int z = (int) (dom.getCoordinates().getZ() + Math.sin(i) * dom.getRadius());

				int ix = 0, iz = 0;

				if (drawinner) {
					ix = (int) (dom.getCoordinates().getX() + Math.cos(i) * (dom.getRadius() - dom.getExclusionZone()));
					iz = (int) (dom.getCoordinates().getZ() + Math.sin(i) * (dom.getRadius() - dom.getExclusionZone()));
				}

				int ox, oz;
				ox = (int) (dom.getCoordinates().getX() + Math.cos(i) * (dom.getRadius() + dom.getExclusionZone()));
				oz = (int) (dom.getCoordinates().getZ() + Math.sin(i) * (dom.getRadius() + dom.getExclusionZone()));

				for (int dy = 0; dy < 20; dy += 3) {
					int y = dom.getCoordinates().getY() + dy;
					caller.sendBlockChange(w.getBlockAt(x, y, z).getLocation(), Material.GLOWSTONE, (byte) 0);
					if (drawinner)
						caller.sendBlockChange(w.getBlockAt(ix, y, iz).getLocation(), Material.EMERALD_BLOCK, (byte) 0);
					caller.sendBlockChange(w.getBlockAt(ox, y, oz).getLocation(), Material.REDSTONE_BLOCK, (byte) 0);
				}
			}
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}

}
