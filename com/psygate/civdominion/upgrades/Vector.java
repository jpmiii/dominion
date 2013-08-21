package com.psygate.civdominion.upgrades;

public class Vector {
	private Upgrade up;
	private long finishTime;
	private transient int finishid;

	public Vector(Upgrade up, long finish) {
		this.up = up;
		this.finishTime = finish;
	}

	public Upgrade getUp() {
		return up;
	}

	public void setUp(Upgrade up) {
		this.up = up;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finish) {
		this.finishTime = finish;
	}

	public int getFinishjob() {
		return finishid;
	}

	public void setFinishjob(int finishjob) {
		this.finishid = finishjob;
	}
}