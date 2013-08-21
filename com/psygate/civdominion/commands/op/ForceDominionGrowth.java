package com.psygate.civdominion.commands.op;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.types.Dominion;

public class ForceDominionGrowth extends ACommand {
	public ForceDominionGrowth() {
		super("forcedominiongrowth");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if(dom != null)
			dom.grow(5);
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}
}
