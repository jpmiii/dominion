package com.psygate.civdominion.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.commands.error.NotAPlayerException;

public abstract class ACommand {
	public final String commandString;

	public ACommand(String commandString) {
		this.commandString = commandString;
	}

	public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException;
	
	protected Player checkExceptionPlayer(CommandSender sender) throws NotAPlayerException {
		if(!(sender instanceof Player)) {
			throw new NotAPlayerException();
		} else {
			return (Player)sender;
		}
	}
	
	public abstract boolean requiresOP();
}
