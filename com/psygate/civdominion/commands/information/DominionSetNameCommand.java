package com.psygate.civdominion.commands.information;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.uac.Rank;

public class DominionSetNameCommand extends ACommand {
	public DominionSetNameCommand() {
		super("dominionsetname");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getDominion(caller.getTargetBlock(null, 10));
		if (dom == null) {
			sender.sendMessage(Strings.notPointingAtDominion);
		} else if(!dom.hasMember(caller.getName()) || dom.getMemberRank(caller.getName()).getID() < Rank.Admin.getID()){
			caller.sendMessage(Strings.insufficientPermissions);
		} else if(args.length != 1) {
			caller.sendMessage(Strings.invalidArguments);
		} else if(CivDominion.getInstance().getMap().hasName(args[0])) {
			caller.sendMessage(Strings.anothername);
		} else {
			CivDominion.getInstance().getMap().removeName(dom.getName());
			dom.setName(args[0]);
			CivDominion.getInstance().getMap().addName(dom);
			caller.sendMessage(Strings.dominionnameset.replace("$n$", args[0]));
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		// TODO Auto-generated method stub
		return false;
	}
}
