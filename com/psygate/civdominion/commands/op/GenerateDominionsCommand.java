package com.psygate.civdominion.commands.op;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.events.PreDominionUpgradeEvent;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.PreDominion;
import com.psygate.civdominion.types.uac.Rank;

public class GenerateDominionsCommand extends ACommand {
	public GenerateDominionsCommand() {
		super("generatedominions");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		int num = 0;
		Player caller = super.checkExceptionPlayer(sender);
		try {
			num = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(Strings.notanumber);
			return true;
		}
		
		sender.sendMessage(Strings.generatingDominions.replace("$n$",Integer.toString(num)));
		Random rand = new Random();
		for (int i = 0; i < num; i++) {
			boolean gen = false;
			int ctr = 0;
			while (!gen && ctr < 100) {
				Coordinates coords = new Coordinates(rand.nextInt(30000), rand.nextInt(256), rand.nextInt(30000),
						caller.getWorld().getName());
				PreDominion dom = CivDominion.getInstance().getConfiguration().getProtoPreDominion();
				dom.setCoordinates(coords);
				dom.addVoter(caller.getName(), Rank.Owner);
				Block domb = caller.getWorld().getBlockAt(coords.getX(), coords.getY(), coords.getZ());
				int size = CivDominion.getInstance().getConfiguration().getProtoDominion().getMaxsize();
				Material type = CivDominion.getInstance().getConfiguration().getProtoPreDominion().getMaterial();
				if (!CivDominion.getInstance().getMap().proximityCheck(domb, size)) {
					domb.setType(type);
					CivDominion.getInstance().emitEvent(new PreDominionUpgradeEvent(dom));
					gen = true;
					sender.sendMessage(Strings.generatedxofy+"("+(i+1)+"/"+num+")");
				}
				ctr++;
			}
		}
		
		sender.sendMessage(Strings.generatedDominions);
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}
}
