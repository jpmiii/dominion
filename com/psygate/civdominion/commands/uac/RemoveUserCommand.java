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

public class RemoveUserCommand extends ACommand {

	public RemoveUserCommand() {
		super("dominionremoveuser");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments + " Usage: /dominionremoveuser <name>");
		} else {
			Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
			if (dom == null) {
				sender.sendMessage(Strings.notPointingAtDominion);
			} else {
				Rank r = dom.getMemberRank(caller.getName());
				Rank nr = dom.getMemberRank(args[0]);
				if (!dom.hasMember(args[0])) {
					sender.sendMessage(Strings.notamember);
					return true;
				}

				if (!CivDominion.getInstance().isDominionOp(caller.getName())) {
					if (args[0].equals(caller.getName())) {
						sender.sendMessage(Strings.removeselffail);
						return true;
					} else if (r == null) {
						sender.sendMessage(Strings.notmember);
						return true;
					} else if (r.getID() < Rank.Moderator.getID()) {
						sender.sendMessage(Strings.insufficientPermissions);
						return true;
					} else if (nr.getID() > r.getID()) {
						sender.sendMessage(Strings.invalidrank);
						return true;
					}
				}

				dom.removeMember(args[0]);
				CivDominion.getInstance().getMap().removePlayerDominion(args[0], dom);
				sender.sendMessage(Strings.removeduser);
			}
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		// TODO Auto-generated method stub
		return false;
	}

}
