package com.psygate.civdominion.commands.upgrades;

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

public class RemoveUpgradeCommand extends ACommand {

	public RemoveUpgradeCommand() {
		super("dominionremoveupgrade");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments + " Usage: /dominionremoveupgrade (<name>/<id>)");
		} else {
			Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
			if (dom == null) {
				sender.sendMessage(Strings.notPointingAtDominion);
			} else {
				if (!CivDominion.getInstance().isDominionOp(caller.getName())) {
					if (!dom.hasMember(caller.getName())) {
						sender.sendMessage(Strings.notmember);
						return true;
					} else if (!Rank.Admin.equals(dom.getMemberRank(caller.getName()))) {
						sender.sendMessage(Strings.insufficientPermissions);
						return true;
					}
				}

				Upgrade up = null;
				try {
					up = Upgrade.valueOf(Integer.parseInt(args[0]));
				} catch (IllegalArgumentException e) {
					// Ignore this, the user may have provided the name of the
					// upgrade.
					// Yes. One could regex this. But why.
				}

				if (up == null) {
					try {
						up = Upgrade.valueOf(args[0]);
					} catch (IllegalArgumentException e) {
						//Last chance bail out.
						sender.sendMessage(Strings.illegalUpgradeName+"("+args[0]+")");
						return true;
					}
				}
				
				if(!dom.hasUpgrade(up)) {
					sender.sendMessage(Strings.domdoesnthaveupgrade);
					return true;
				}
				
				dom.removeUpgrade(up);
			}
		}
		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}
}
