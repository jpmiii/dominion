package com.psygate.civdominion.commands.upgrades.xpleech;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;

public class XPInfoCommand extends ACommand {
	public XPInfoCommand() {
		super("dominionxpinfo");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			if (!CivDominion.getInstance().isDominionOp(caller.getName())) {
				if (!dom.hasMember(caller.getName())) {
					sender.sendMessage(Strings.notmember);
					return true;
				}
			}
			
			Long xp = CivDominion.getInstance().getXP(dom.getCoordinates());
			if(xp == null) {
				sender.sendMessage(Strings.xpinfo.replace("$x$","0"));
			} else {
				sender.sendMessage(Strings.xpinfo.replace("$x$",xp.toString()));
			}
		}

		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}
}
