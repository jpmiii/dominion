package com.psygate.civdominion.commands.op;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;

public class DominionForceGlobalGrowthCommand extends ACommand {

	public DominionForceGlobalGrowthCommand() {
		super("dominionforceglobalgrowth");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		CivDominion.getInstance().getGrowthManager().grow(true);
		sender.sendMessage(Strings.forcedgrowth);
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}

}
