package com.psygate.civdominion.commands.op;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;

public class DominionSetRadiusCommand extends ACommand {

	public DominionSetRadiusCommand() {
		super("dominionsetradius");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments);
		}
		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			try {
				float f = Float.parseFloat(args[0]);
				if (f > dom.getMaxsize()) {
					dom.setMaxsize((int) (f + 1));
				}
				dom.setRadius(f);
				sender.sendMessage(Strings.newradius.replace("$r$",Float.toString(f)));
			} catch (NumberFormatException e) {
				sender.sendMessage(Strings.mustbenumber);
			}
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}

}
