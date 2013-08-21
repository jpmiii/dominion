package com.psygate.civdominion.commands.upgrades.tesseractdisplacement;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;

public class DisplaceCommand extends ACommand {
	private Random rand = new Random();
	
	public DisplaceCommand() {
		super("displace");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(caller.getLocation());

		boolean indom = (dom == null) ? false : dom.getCoordinates().squareDistance2D(caller.getLocation()) < dom
				.getRadius() * dom.getRadius();
		boolean fail = true;
		if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments);
		} else if (dom != null && indom && dom.hasMember(caller.getName())) {
			Dominion dom2 = CivDominion.getInstance().getMap().getDominionForName(args[0]);
			if (dom2 != null) {
				if(dom2.hasLink(dom.getName()) && dom.hasLink(dom2.getName())) {
					fail = false;
					double angle = 0;
					synchronized (rand) {
						angle = Math.PI * 2 * rand.nextDouble();
					}
					double x = (dom2.getCoordinates().getX() + Math.cos(angle) * 800 + 200);
					double z = (dom2.getCoordinates().getY() + Math.sin(angle) * 800 + 200);
					World world = CivDominion.getInstance().getServer().getWorld(dom2.getCoordinates().getWorld());
					double y = world.getHighestBlockYAt((int) x, (int) z) + 1;
					caller.teleport(new Location(world, x, y, z));
					caller.sendMessage(Strings.tesseracted);
				} else {
					fail = false;
					sender.sendMessage(Strings.spacewarp);
				}
			}
		}
		if (fail) {
			sender.sendMessage(Strings.impossibledisplace);
		}

		return true;
	}

	@Override
	public boolean requiresOP() {
		// TODO Auto-generated method stub
		return false;
	}
}
