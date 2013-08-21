package com.psygate.civdominion.commands.op;

import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.upgrades.Upgrade;

public class StripUpgradesCommand extends ACommand {
	public StripUpgradesCommand() {
		super("dominionstripupgrades");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
Player caller = checkExceptionPlayer(sender);
		
		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			dom.setUpgrades(new HashSet<Upgrade>());
			CivDominion.getInstance().getUpgradeTimer().cancel(dom.getCoordinates());
			sender.sendMessage(Strings.upgradesstripped);
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}
}
