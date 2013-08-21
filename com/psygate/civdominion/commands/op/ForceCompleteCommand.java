package com.psygate.civdominion.commands.op;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.events.PreDominionUpgradeEvent;
import com.psygate.civdominion.types.PreDominion;

public class ForceCompleteCommand extends ACommand {
	public ForceCompleteCommand() {
		super("forcecompletedominion");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		PreDominion dom = CivDominion.getInstance().getMap().getPreDominion(caller.getTargetBlock(null, 10));
		if(dom == null) {
			sender.sendMessage(Strings.notPointingAtPreDominion);
		} else {
			CivDominion.getInstance().emitEvent(new PreDominionUpgradeEvent(dom));
			sender.sendMessage(Strings.forcedCompletion);
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}

}
