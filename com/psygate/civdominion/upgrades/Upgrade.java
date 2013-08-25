package com.psygate.civdominion.upgrades;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import static com.psygate.civdominion.upgrades.UpgradeType.*;

public enum Upgrade {
	NoInvisibility(0, false, 1000, BASIC), Fortification(1, false, 1000, BASIC), NoPearl(2, false, 1000, BASIC),
	MobCull(3, false, 1000, BASIC), NoCitadel(4, false, 1000, BASIC), NoHostileMobs(5, false, 1000, BASIC),
	NoLava(6, false, 1000, BASIC), WheatLeech(7, false, 1000, BASIC), FootInTheGrave(8, false, 1000, WAR),
	ParasiticBond(9, false, 1000, WAR), DeathIntoLife(10, false, 1000, WAR), PotentCorruption(11, false, 1000, WAR),
	FullOfLife(12, false, 1000, WAR), DeathlyPerception(13, false, 1000, WAR), PathOfMidnight(14, false, 1000, WAR),
	WitheringStrikes(15, false, 1000, WAR), PrayerOfIdols(15, false, 1000, ECONOMY), PrayerOfAsh(16, false, 1000, ECONOMY),
	PrayerOfFertility(17, false, 1000, ECONOMY), UnearthlyShackles(18, false, 1000, ECONOMY),
	TesseractDisplacement(19, false, 1000, ECONOMY), BeaconOfMercy(20, false, 1000, ECONOMY);
	
	// "Death into Life"
	// psygate: if you have no bed, and you die, you will spawn within 500 blocks of your dominion, you are part of

	// "Potent Corruption" -> Gain a 10% chance to steal a potion effect from someone you're damaging
	
	// "Full of Life" -> gain a health regen 1 effect for 5 seconds on being hit
	
	// "Deathly Perception" -> On falling below 50% health, you do 150% damage for 3 seconds
	
	// "Path of Midnight" -> Do 200% damage while it's night vs mobs
	
	// "Withering Strikes" -> 10% chance on hit, that your foe does only do 60% damage for 3 secs
	
	// Mineboost -> Doubles unrare material drops.
	
	// Farmerboost -> Chance of wheat / carrots etc growing faster.
	
	// ShepherdBoost -> Chance of animals reproducing twice.
	
	// Spawnmagnet -> Great chance of new people spawning near your dominion.
	
	private int id;
	private Collection<Integer> req;
	private boolean enabled;
	private long buildtime;
	private ItemStack cost;
	private UpgradeType type;

	private Upgrade(int did, boolean isenabled, long btime, UpgradeType type, int... requires) {
		id = did;
		req = new ArrayList<Integer>(requires.length);
		enabled = isenabled;
		buildtime = btime;
		cost = new ItemStack(Material.DIAMOND, 1);
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public Collection<Integer> getReq() {
		return req;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public long getBuildTime() {
		return buildtime;
	}

	public void setBuildtime(long buildtime) {
		this.buildtime = buildtime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setReq(Collection<Integer> req) {
		this.req = req;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public ItemStack getCosts() {
		return cost;
	}

	public static Upgrade valueOf(int parseInt) {
		for(Upgrade up : Upgrade.values()) {
			if(up.getId() == parseInt) return up;
		}
		
		throw new IllegalArgumentException("Not a valid id.");
	}

	public void setCosts(ItemStack itemStack) {
		this.cost = itemStack;
	}
	
	public UpgradeType getType() {
		return type;
	}
}
