package com.psygate.civdominion.commands.information;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;

public class DominionShowNameCommand extends ACommand {
	public DominionShowNameCommand() {
		super("dominionshowname");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			caller.sendMessage("- "+dom.getName());
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		// TODO Auto-generated method stub
		return false;
	}
}
