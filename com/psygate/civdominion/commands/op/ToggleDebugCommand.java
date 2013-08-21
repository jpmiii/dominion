package com.psygate.civdominion.commands.op;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;

public class ToggleDebugCommand extends ACommand {
	public ToggleDebugCommand() {
		super("dominiontoggledebug");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		CivDominion.DEBUG = !CivDominion.DEBUG;
		if(CivDominion.DEBUG) {
			sender.sendMessage(Strings.debugOn);
		} else {
			sender.sendMessage(Strings.debugOff);
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}
}
