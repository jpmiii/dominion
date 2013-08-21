package com.psygate.civdominion.types.uac;

public enum Rank {
	User(0), Moderator(1), Admin(2), Owner(3);
	
	private int id;
	
	Rank(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
