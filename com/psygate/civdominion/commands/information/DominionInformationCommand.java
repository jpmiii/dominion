package com.psygate.civdominion.commands.information;

import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class DominionInformationCommand extends ACommand {
	public DominionInformationCommand() {
		super("dominionshowinfluence");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom != null) {
			double influence = 2 * Math.PI * dom.getRadius() * dom.getMemberMap().size();
			for (Entry<String, Rank> ent : dom.getMemberMap().entrySet()) {
				influence += ent.getValue().getID() + 1;
			}
			sender.sendMessage(Strings.influence.replace("$i$", Double.toString(influence)));
		} else {
			sender.sendMessage(Strings.notPointingAtDominion);
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}
}
