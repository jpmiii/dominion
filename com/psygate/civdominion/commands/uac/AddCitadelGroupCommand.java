package com.psygate.civdominion.commands.uac;

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
import com.untamedears.citadel.Citadel;
import com.untamedears.citadel.entity.Faction;
import com.untamedears.citadel.entity.FactionMember;
import com.untamedears.citadel.entity.Moderator;

public class AddCitadelGroupCommand extends ACommand {
	public AddCitadelGroupCommand() {
		super("dominionaddcitadelgroup");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		if (!CivDominion.CITADEL) {
			sender.sendMessage(Strings.nocitadel);
			return true;
		} else if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments + " Usage: /dominionaddcitadelgroup <name>");
		} else {
			Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
			if (dom == null) {
				sender.sendMessage(Strings.notPointingAtDominion);
				return true;
			} else {
				Rank r = dom.getMemberRank(caller.getName());
				if (r.getID() < Rank.Moderator.getID()) {
					sender.sendMessage(Strings.insufficientPermissions);
					return true;
				} else {
					Faction f = Citadel.getGroupManager().getGroup(args[0]);
					if (f == null) {
						sender.sendMessage(Strings.citadelgroupmissing.replace("$g$", args[0]));
						return true;
					} else {
						Set<FactionMember> s = Citadel.getGroupManager().getMembersOfGroup(f.getName());
						Set<Moderator> mod = Citadel.getGroupManager().getModeratorsOfGroup(f.getName());
						for (Moderator m : mod) {
							if (dom.hasMember(m.getMemberName())) {
								dom.setMemberRank(m.getMemberName(), Rank.Moderator);
							} else {
								dom.addMember(m.getMemberName(), Rank.Moderator);
							}
							sender.sendMessage("Added " + m.getMemberName() + " as Moderator.");
						}

						for (FactionMember m : s) {
							String name = m.getMemberName();
							if (!dom.hasMember(name)) {
								dom.addMember(name, Rank.User);
								sender.sendMessage("Added " + m.getMemberName() + " as User.");
							}
						}
					}
				}
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
