package com.psygate.civdominion.commands.op;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.upgrades.Upgrade;

public class ForceAddAllUpgrades extends ACommand {

	public ForceAddAllUpgrades() {
		super("dominionforceaddallupgrades");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);
		
		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			for(Upgrade up : Upgrade.values()) {
				dom.addUpgrade(up);
			}
			
			sender.sendMessage(Strings.upgradeaddforced);
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}

}
