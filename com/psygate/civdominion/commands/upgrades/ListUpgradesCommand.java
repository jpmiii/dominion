package com.psygate.civdominion.commands.upgrades;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.upgrades.Upgrade;

public class ListUpgradesCommand extends ACommand {

	public ListUpgradesCommand() {
		super("dominionlistupgrades");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			if(!CivDominion.getInstance().isDominionOp(caller.getName())) {
				if(!dom.hasMember(caller.getName())) {
					sender.sendMessage(Strings.notmember);
					return true;
				}
			}
			
			StringBuilder bl = new StringBuilder("Upgrades:\n");
			int i = 0;
			for(Upgrade up : dom.getUpgrades()) {
				i++;
				bl.append("- ");
				bl.append(up.name());
				if(i < dom.getUpgrades().size()) {
					bl.append("\n");
				}
			}
			
			caller.sendMessage(bl.toString());
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}
}
