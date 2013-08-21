package com.psygate.civdominion.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.psygate.civdominion.types.uac.Rank;

public class PreDominion {
	private int voteThreshold;
	private Map<String, Rank> voters;
	private ItemStack stack;
	private Material mat;
	private Coordinates coords;

	public PreDominion(Coordinates coords, Material mat, int votes, Map<String, Rank> voters, ItemStack stack) {
		super();
		this.voteThreshold = votes;
		this.voters = voters;
		this.stack = stack;
		this.mat = mat;
		this.coords = coords;
	}

	public Map<String, Rank> getVoters() {
		return voters;
	}

	public void setVoters(Map<String, Rank> voters) {
		this.voters = voters;
	}

	public int getVoteThreshold() {
		return voteThreshold;
	}

	public void setVoteThreshold(int voteThreshold) {
		this.voteThreshold = voteThreshold;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public PreDominion clone() {
		return new PreDominion(new Coordinates(0, 0, 0, ""), mat, voteThreshold, new HashMap<String, Rank>(), stack);
	}

	public Material getMaterial() {
		return mat;
	}

	@Override
	public String toString() {
		return "PreDominion: Material: " + mat + " VoteThreshold: " + voteThreshold + " Voters: " + voters
				+ " Appr. Material: " + stack;
	}

	public void addVoter(String name, Rank owner) {
		voters.put(name, owner);
	}

	public Coordinates getCoordinates() {
		return coords;
	}

	public void setCoordinates(Coordinates coords2) {
		coords = coords2;
	}
}
