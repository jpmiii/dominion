package com.psygate.civdominion.commands.upgrades.tesseractdisplacement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class LinkCommand extends ACommand {
	public LinkCommand() {
		super("link");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);
		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else if (!dom.hasMember(caller.getName()) || dom.getMemberRank(caller.getName()).getID() < Rank.Admin.getID()) {
			sender.sendMessage(Strings.insufficientPermissions);
		}  else if(args.length != 1) {
			caller.sendMessage(Strings.invalidArguments);
		} else if(!CivDominion.getInstance().getMap().hasName(args[0])) {
			sender.sendMessage(Strings.invalidname);
		} else {
			dom.addLink(args[0]);
			sender.sendMessage(Strings.linked);
		}
		return false;
	}

	@Override
	public boolean requiresOP() {
		// TODO Auto-generated method stub
		return false;
	}
}
