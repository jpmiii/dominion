package com.psygate.civdominion.commands.uac;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class ListUserCommand extends ACommand {

	public ListUserCommand() {
		super("dominionlistusers");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			if (dom.hasMember(caller.getName()) && !CivDominion.getInstance().isDominionOp(caller.getName())) {
				sender.sendMessage(Strings.notmember);
				return true;
			} else {
				StringBuilder bl = new StringBuilder("Users:\n");
				Set<Entry<String, Rank>> entries = dom.getMemberMap().entrySet();
				int i = 0;
				for(Entry<String, Rank> entry : entries) {
					i++;
					bl.append("- ");
					bl.append(entry.getKey());
					bl.append(" (");
					bl.append(entry.getValue().name());
					bl.append(")");
					if(i < entries.size()) {
						bl.append("\n");
					}
				}
				caller.sendMessage(bl.toString());
			}
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}
}
