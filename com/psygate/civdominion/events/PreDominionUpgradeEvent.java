package com.psygate.civdominion.events;

import com.psygate.civdominion.types.PreDominion;

public class PreDominionUpgradeEvent extends ADominionEvent {
	private PreDominion dom;

	public PreDominionUpgradeEvent(PreDominion dom) {
		this.dom = dom;
		if (dom == null)
			throw new NullPointerException("PreDominion cannot be null.");
	}

	public PreDominion getPreDominion() {
		return dom;
	}
}
