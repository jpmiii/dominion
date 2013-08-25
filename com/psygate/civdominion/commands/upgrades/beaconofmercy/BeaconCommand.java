package com.psygate.civdominion.commands.upgrades.beaconofmercy;

import java.awt.Polygon;

import org.bukkit.Location;
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
import com.psygate.civdominion.types.Vector3;
import com.psygate.civdominion.upgrades.Upgrade;
import com.psygate.math.euclidian.point.Point2D;
import com.psygate.math.euclidian.polygon.Triangle2D;
import com.psygate.math.euclidian.vector.Vector2D;

public class BeaconCommand extends ACommand {

	public BeaconCommand() {
		super("beacon");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
			throws CommandException {
		Player caller = checkExceptionPlayer(sender);

		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(caller.getLocation());
		boolean indom = (dom == null) ? false : dom.getCoordinates().squareDistance2D(caller.getLocation()) < dom
				.getRadius() * dom.getRadius();
		if (args.length != 1) {
			sender.sendMessage(Strings.invalidArguments);
			return true;
		} else if (dom != null && indom && dom.hasMember(caller.getName())) {
			Dominion dom2 = CivDominion.getInstance().getMap().getDominionForName(args[0]);
			if (dom2 != null) {
				if (dom2.hasLink(dom.getName()) && dom.hasLink(dom2.getName())) {
					if (dom.hasUpgrade(Upgrade.BeaconOfMercy) && dom2.hasUpgrade(Upgrade.BeaconOfMercy)) {
						double x = dom2.getCoordinates().getX() - dom.getCoordinates().getX();
						double y = dom2.getCoordinates().getY() - dom.getCoordinates().getY();
						double z = dom2.getCoordinates().getZ() - dom.getCoordinates().getZ();
						Vector2D vec = new Vector2D(x, z).normalize();
						for (int i = 0; i < 10; i++) {
							Vector2D scl = vec.scale(i);
							double xl = caller.getLocation().getX() + scl.getX();
							double yl = caller.getLocation().getY();
							double zl = caller.getLocation().getZ() + scl.getY();

							Location loc = new Location(caller.getWorld(), xl, yl, zl);
							caller.sendBlockChange(loc, Material.GLOWSTONE, (byte) 0);
						}

						Point2D a = new Point2D(dom2.getCoordinates().getX(), dom2.getCoordinates().getZ());

						// Turn the vector and construct a triangle pointing at
						// the destination dominion.
						Point2D domp = new Point2D(dom.getCoordinates().getX(), dom.getCoordinates().getZ());

						Vector2D dist = a.sub(domp); // This vector points from
														// dom to dom2.
						Vector2D back = dist.inv().normalize().scale(40); // points
																			// 20
																			// blocks
																			// back
																			// from
																			// dom.
						Point2D backpillar = domp.add(back); // this is the back
																// pillar. Just
																// here for
																// testing
																// purposes.

						Vector2D left = back.leftNormal();
						Vector2D right = back.rightNormal();

						Point2D b = backpillar.add(left);
						Point2D c = backpillar.add(right);

//						drawPillar(backpillar, caller, Material.EMERALD_BLOCK);
						drawPillar(b, caller, Material.REDSTONE_BLOCK);
						drawPillar(c, caller, Material.LAPIS_BLOCK);
						drawLine(b, a.sub(b), caller, Material.REDSTONE_BLOCK, 100);
						drawLine(c, a.sub(c), caller, Material.LAPIS_BLOCK, 100);

						Triangle2D tri = new Triangle2D(a, b, c);
						CivDominion.getUpgradeListener().addMercy(caller.getName(), tri);
						sender.sendMessage(Strings.beacon);
					} else {
						sender.sendMessage("Not enough mercy. " + dom.hasUpgrade(Upgrade.BeaconOfMercy) + "/"
								+ dom2.hasUpgrade(Upgrade.BeaconOfMercy));
					}
				} else {
					sender.sendMessage(Strings.mercyfail);
				}
			} else {
				sender.sendMessage("No dominion with that name.");
			}
		} else {
			sender.sendMessage("The ether is not attuned right here.");
		}

		return true;
	}

	private static void drawPillar(Point2D pill, Player p, Material m) {
//		System.out.println("Drawing pillar: " + m.name() + "@" + pill);
		int starty = (int) (p.getLocation().getY() - 20);
		int endy = (int) (p.getLocation().getY() + 20);

		int vx = (int) pill.getX();
		int vz = (int) pill.getY();

		for (int vy = starty; vy < endy; vy++) {
			Location loc = new Location(p.getWorld(), vx, vy, vz);
			p.sendBlockChange(loc, m, (byte) 0);
		}
	}

	private static void drawLine(Point2D start, Vector2D vec, Player p, Material m, int length) {
		Vector2D v = vec.normalize();
		for (int i = 0; i < length; i++) {
			Point2D draw = start.add(v.scale(i));
			Location loc = new Location(p.getWorld(), draw.getX(), p.getLocation().getY(), draw.getY());
			p.sendBlockChange(loc, m, (byte) 0);
		}
	}

	@Override
	public boolean requiresOP() {
		// TODO Auto-generated method stub
		return false;
	}

}
