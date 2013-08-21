package com.psygate.civdominion.configuration;

import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.PreDominion;

public class Configuration {
	private Dominion protoDominion;
	private PreDominion protoPreDominion;
	private long grow = 1000 * 24 * 60 * 60;
	
	public void setPrototypePreDominion(PreDominion pred) {
		protoPreDominion = pred;
	}

	public void setPrototypeDominion(Dominion dom) {
		protoDominion = dom;
	}

	public Dominion getProtoDominion() {
		return protoDominion.clone();
	}

	public PreDominion getProtoPreDominion() {
		return protoPreDominion.clone();
	}

	public void setGrowthInterval(long growthinterval) {
		grow = growthinterval;
	}
	
	public long getGrowthInterval() {
		return grow;
	}
}
