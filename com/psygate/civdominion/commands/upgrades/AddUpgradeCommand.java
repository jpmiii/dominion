package com.psygate.civdominion.commands.upgrades;

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
import com.psygate.civdominion.types.uac.Rank;
import com.psygate.civdominion.upgrades.Upgrade;
import com.psygate.civdominion.upgrades.UpgradeType;
import com.psygate.minecraft.PlayerInventoryHelper;

public class AddUpgradeCommand extends ACommand {
	public AddUpgradeCommand() {
		super("dominionaddupgrade");
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
					// Last chance bail out.
					sender.sendMessage(Strings.illegalUpgradeName + "(" + args[0] + ")");
					return true;
				}
			}

			if (!up.isEnabled()) {
				sender.sendMessage(Strings.upgradedisabled);
				return true;
			} else if (!up.getType().equals(UpgradeType.BASIC)
					&& (dom.getPath() == null || !dom.getPath().equals(up.getType()))) {
				sender.sendMessage(Strings.wrongpath);
				return true;
			} else if (dom.hasUpgrade(up)) {
				sender.sendMessage(Strings.upgradealreadythere);
				return true;
			} else if (CivDominion.getInstance().getUpgradeTimer().isUpgradePendingFor(dom.getCoordinates())) {
				sender.sendMessage(Strings.upgradeinthemaking);
				return true;
			} else if (PlayerInventoryHelper.takeFromPlayerInventory(up.getCosts(), caller)) {
				CivDominion.getInstance().getUpgradeTimer().addUpgrade(dom.getCoordinates(), up);
				Date d = new Date(System.currentTimeMillis() + (up.getBuildTime() * 1000));
				SimpleDateFormat mat = new SimpleDateFormat("dd-MM-yyyy H:mm:ss");
				sender.sendMessage(Strings.upgradequeued.replace("$d$", up.name()).replace("$e$", mat.format(d)));
			} else {
				sender.sendMessage(Strings.upgradetooexpensive.replace("$d$", up.getCosts().toString()));
			}
		}

		return true;
	}

	@Override
	public boolean requiresOP() {
		return false;
	}
}
