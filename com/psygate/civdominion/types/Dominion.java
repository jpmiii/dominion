package com.psygate.civdominion.types;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.block.Block;

import com.psygate.civdominion.types.uac.Rank;
import com.psygate.civdominion.upgrades.Upgrade;
import com.psygate.civdominion.upgrades.UpgradeType;

public class Dominion {
	private float radius;
	private Map<String, Rank> members;
	private int maxsize;
	private int exclusionZone;
	private float gwpu;
	private Set<Upgrade> upgrades = new HashSet<Upgrade>();
	private Coordinates coords;
	private UpgradeType path = null;
	private String name = null;
	private Set<String> links = new HashSet<String>();
	private transient Object upgradelock = new Object();
	private transient Object linklock = new Object();

	public Dominion(Coordinates coords, float radius, Map<String, Rank> members, int maxsize, int exclusionZone,
			float gwpu) {
		super();
		this.radius = radius;
		this.members = members;
		this.maxsize = maxsize;
		this.exclusionZone = exclusionZone;
		this.gwpu = gwpu;
		this.coords = coords;
		this.name = generateName();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	// public Map<String, Rank> getMembers() {
	// return members;
	// }

	public void setMembers(Map<String, Rank> members) {
		this.members = members;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(int maxsize) {
		this.maxsize = maxsize;
	}

	public int getExclusionZone() {
		return exclusionZone;
	}

	public void setExclusionZone(int exclusionZone) {
		this.exclusionZone = exclusionZone;
	}

	public float getGwpu() {
		return gwpu;
	}

	public void setGwpu(float gwpu) {
		this.gwpu = gwpu;
	}

	public Coordinates getCoordinates() {
		return coords;
	}

	public Collection<Upgrade> getUpgrades() {
		HashSet<Upgrade> set = new HashSet<Upgrade>();
		synchronized (upgradelock) {
			set.addAll(upgrades);
		}
		return set;
	}

	public void setUpgrades(Set<Upgrade> upgrades) {
		synchronized (upgradelock) {
			this.upgrades = upgrades;
		}
	}

	public void setCoordinates(Coordinates coords) {
		String oln = name;
		String gen = generateName();
		this.coords = coords;
		if (oln.equals(gen)) {
			name = generateName();
		}

	}

	private String generateName() {
		return "Dominion(" + coords.getWorld() + "," + coords.getX() + "," + coords.getY() + "," + coords.getZ() + ")";
	}

	@Override
	public Dominion clone() {
		return new Dominion(new Coordinates(0, 0, 0, ""), radius, new HashMap<String, Rank>(), maxsize, exclusionZone,
				gwpu);
	}

	@Override
	public String toString() {
		return name + " Pos: " + coords + " Radius: " + radius + " Max. Radius: " + maxsize + " Exclusion Zone: "
				+ exclusionZone + " Users: " + members + " growth per user: " + gwpu;
	}

	public boolean hasUpgrade(Upgrade up) {
		return upgrades.contains(up) && up.isEnabled();
	}

	public Rank getMemberRank(String name) {
		return members.get(name);
	}

	public boolean hasMember(String string) {
		return members.containsKey(string);
	}

	public void addMember(String string, Rank nr) {
		members.put(string, nr);
	}

	public Map<String, Rank> getMemberMap() {
		return members;
	}

	public void removeMember(String string) {
		members.remove(string);
	}

	public void removeUpgrade(Upgrade up) {
		synchronized (upgradelock) {
			upgrades.remove(up);
		}
	}

	public void addUpgrade(Upgrade up) {
		synchronized (upgradelock) {
			upgrades.add(up);
		}
	}

	public boolean inExclusionZone(Block b) {
		double dist = coords.squareDistance2D(b);
		double outl = (radius + exclusionZone);
		outl *= outl;
		double inl = 0;
		if (radius - exclusionZone > 0) {
			inl = (radius - exclusionZone);
			inl *= inl;
		}

		if (radius - exclusionZone < 10) {
			return dist <= outl && dist >= radius * radius;
		} else {
			return dist <= outl && dist >= inl;
		}
	}

	public void grow(float rad) {
		radius += rad;
	}

	public void setMemberRank(String memberName, Rank moderator) {
		members.put(memberName, moderator);
	}

	public UpgradeType getPath() {
		return path;
	}

	public void setPath(UpgradeType path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addLink(String name) {
		synchronized (linklock) {
			this.links.add(name);
		}
	}

	public void removeLink(String name) {
		synchronized (linklock) {
			this.links.remove(name);
		}
	}

	public boolean hasLink(String name) {
		synchronized (linklock) {
			return this.links.contains(name);
		}
	}

	private Object readResolve() throws ObjectStreamException {
		upgradelock = new Object();
		linklock = new Object();
		return this;
	}
}
