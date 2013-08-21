package com.psygate.civdominion.commands.uac;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class ModifyUserCommand extends ACommand {

	public ModifyUserCommand() {
		super("dominionmodifyuser");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		if (args.length != 2) {
			sender.sendMessage(Strings.invalidArguments + " Usage: /dominionadduser <name> <rank>");
		} else {
			Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
			if (dom == null) {
				sender.sendMessage(Strings.notPointingAtDominion);
			} else {
				Rank newuserrank = null;
				Rank callerrank = dom.getMemberRank(caller.getName());
				Rank oldrank = dom.getMemberRank(args[0]);
				try {
					newuserrank = Rank.valueOf(args[1]);
				} catch (IllegalArgumentException e) {
					sender.sendMessage(Strings.unknownrank + "(" + args[1] + ")");
					return true;
				}

				if (!dom.hasMember(args[0])) {
					sender.sendMessage(Strings.notamember);
					return true;
				}

				if (!CivDominion.getInstance().isDominionOp(caller.getName())) {
					if (callerrank == null) {
						sender.sendMessage(Strings.notmember);
						return true;
					} else if (callerrank.getID() < Rank.Moderator.getID()) {
						sender.sendMessage(Strings.insufficientPermissions);
						return true;
					} else if (newuserrank.getID() > callerrank.getID()) {
						sender.sendMessage(Strings.invalidrank);
						return true;
					} else if (oldrank.getID() > callerrank.getID()) {
						sender.sendMessage(Strings.demotionfailed);
						return true;
					}
				}

				dom.addMember(args[0], newuserrank);
				sender.sendMessage(Strings.addnewuser);

			}
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}

}
