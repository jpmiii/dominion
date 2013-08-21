package com.psygate.civdominion.types;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Coordinates {
	private int x, y, z;
	private String world;

	public Coordinates(int x, int y, int z, String world) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	// TODO
	// Fix this retarded hashing.
	@Override
	public int hashCode() {
		int hash = x;
		hash = x * 31 + y;
		hash = y * 31 + z;
		hash ^= world.hashCode();

		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinates)) {
			return false;
		} else {
			Coordinates cor = (Coordinates) obj;
			return cor.x == this.x && cor.y == this.y && cor.z == this.z;
		}
	}

	@Override
	public String toString() {
		return "(" + x + "/" + y + "/" + z + "/" + world + ")";
	}

	public double squareDistance2D(Block block) {
		return squareDistance2D(block.getLocation());
	}

	public double squareDistance3D(Block block) {
		return squareDistance3D(block.getLocation());
	}
	
	public double squareDistance3D(Location block) {
		return (x - block.getX()) * (x - block.getX()) + (y - block.getY()) * (y - block.getY()) + (z - block.getZ())
				* (z - block.getZ());
	}

	public double squareDistance2D(Location location) {
		return (x - location.getX()) * (x - location.getX()) + (z - location.getZ()) * (z - location.getZ());
	}
}
