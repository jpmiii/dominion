package com.psygate.civdominion.commands.op;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;

public class DominionSetGrowIntervalCommand extends ACommand {
	public DominionSetGrowIntervalCommand() {
		super("dominionsetgrowthinterval");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		
		if(args.length != 1) {
			sender.sendMessage(Strings.invalidArguments);
			return true;
		}
		try {
			long interval = Long.parseLong(args[0]);
			CivDominion.getInstance().getConfiguration().setGrowthInterval(interval);
			sender.sendMessage(Strings.intervalset.replace("$i$", Long.toString(interval)));
		} catch(Exception e) {
			
		}
		
		return true;
	}

	@Override
	public boolean requiresOP() {
		return true;
	}
	
	
}
