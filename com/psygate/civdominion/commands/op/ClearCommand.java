package com.psygate.civdominion.commands.op;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.Dominion;

public class ClearCommand extends ACommand {
	public ClearCommand() {
		super("dominionclear");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {		
		CivDominion.getInstance().getMap().clear();
		sender.sendMessage(Strings.dominionscleared);
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}
}
