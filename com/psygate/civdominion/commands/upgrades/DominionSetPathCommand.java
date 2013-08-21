package com.psygate.civdominion.commands.upgrades;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;
import com.psygate.civdominion.upgrades.Upgrade;
import com.psygate.civdominion.upgrades.UpgradeType;

public class DominionSetPathCommand extends ACommand {

	public DominionSetPathCommand() {
		super("dominionsetpath");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments);
			return true;
		}

		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else {
			if (!CivDominion.getInstance().isDominionOp(caller.getName())) {
				if (!dom.hasMember(caller.getName())) {
					sender.sendMessage(Strings.notmember);
					return true;
				} else if (dom.getMemberRank(caller.getName()).getID() < Rank.Admin.getID()) {
					sender.sendMessage(Strings.insufficientPermissions);
					return true;
				}
			}
			
			try {
				UpgradeType t = UpgradeType.valueOf(args[0].toUpperCase());
				dom.setPath(t);
				for(Upgrade u : dom.getUpgrades()) {
					if(!u.getType().equals(t) && !u.getType().equals(UpgradeType.BASIC)) {
						sender.sendMessage(Strings.upgradedroppedpathinc.replace("$u$", u.name()));
						dom.removeUpgrade(u);
					}
				}
				sender.sendMessage(Strings.dominionpathset.replace("$p$", args[0]));
				return true;
			} catch(Exception e) {
				sender.sendMessage(Strings.notavalidpath.replace("$p$", args[0]));
				e.printStackTrace();
				return true;
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
