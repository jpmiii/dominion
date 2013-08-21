package com.psygate.civdominion.types;

import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class MapStructure {
	private HashMap<Coordinates, Dominion> dommap = new HashMap<Coordinates, Dominion>();
	private HashMap<Coordinates, PreDominion> premap = new HashMap<Coordinates, PreDominion>();
	private HashMap<String, Set<Dominion>> playerd = new HashMap<String, Set<Dominion>>();
	private HashMap<String, Dominion> names = new HashMap<String, Dominion>();

	//TODO Maybe include the exclusion zone in the range check...
	/**
	 * Return true if another dominion MAY overlap in the future. Else false.
	 * @param block
	 * @param maxrad
	 * @return
	 */
	public boolean proximityCheck(Block block, int maxrad) {
		for (Entry<Coordinates, Dominion> entry : dommap.entrySet()) {
			if(entry.getKey().squareDistance2D(block) <= (maxrad + entry.getValue().getMaxsize()) * (maxrad + entry.getValue().getMaxsize())) {
				return true;
			}
		}

		for (Entry<Coordinates, PreDominion> entry : premap.entrySet()) {
			if (entry.getKey().squareDistance2D(block) <= maxrad * 2) {
				return true;
			}
		}

		return false;
	}
	
	//TODO This is a rather retarded method to check for the closest dominion. Use Quadtrees or Octrees for that.
	//I don't have time to code it out now. (Or atleast sort the map or smth...)
	public Dominion getClosestDominion(Block block) {
		return getClosestDominion(block.getLocation());
	}
	
	public Dominion getClosestDominion(Location location) {
		double dist = Double.MAX_VALUE;
		Dominion chosen = null;
		
		for(Coordinates coords : dommap.keySet()) {
			double cdist = coords.squareDistance2D(location);
			if(cdist < dist) {
				chosen = dommap.get(coords);
				dist = cdist;
			}
		}
		
		return chosen;
	}

	public void addPreDominion(PreDominion dom) {
		premap.put(dom.getCoordinates(), dom);
	}

	public boolean isPending(Block clickedBlock) {
		Coordinates cor = new Coordinates(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ(), clickedBlock
				.getWorld().getName());
		return premap.containsKey(cor);
	}

	public PreDominion getPreDominion(Block clickedBlock) {
		Coordinates cor = new Coordinates(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ(), clickedBlock
				.getWorld().getName());
		return premap.get(cor);
	}

	public void removePreDominion(PreDominion preDominion) {
		premap.remove(preDominion.getCoordinates());
	}

	public void addDominion(Dominion dom) {
		dommap.put(dom.getCoordinates(), dom);
		addName(dom);
	}

	public Dominion getDominion(Block targetBlock) {
		Coordinates cor = new Coordinates(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock
				.getWorld().getName());
		return dommap.get(cor);
	}

	public Dominion getDominion(Coordinates coords) {
		return dommap.get(coords);
	}

	public Collection<Dominion> getDominionSet() {
		Collection<Dominion> col = new LinkedList<Dominion>();
		col.addAll(dommap.values());
		return col;
	}

	public Collection<PreDominion> getPreDominionSet() {
		Collection<PreDominion> col = new LinkedList<PreDominion>();
		col.addAll(premap.values());
		return col;
	}

	public int getDominionNumber() {
		return dommap.size();
	}
	
	public int getPreDominionNumber() {
		return premap.size();
	}

	public boolean isDominion(Block targetBlock) {
		Coordinates cor = new Coordinates(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock
				.getWorld().getName());
		return dommap.containsKey(cor);
	}
	
	public boolean isPreDominion(Block targetBlock) {
		Coordinates cor = new Coordinates(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock
				.getWorld().getName());
		return premap.containsKey(cor);
	}

	public void clear() {
		dommap.clear();
		premap.clear();
	}

	public void removeDominion(Coordinates coordinates) {
		if(dommap.get(coordinates) != null) {
			removeName(dommap.get(coordinates).getName());
		}
		dommap.remove(coordinates);
	}
	
	public void removePreDominion(Coordinates cord) {
		premap.remove(cord);
	}
	
	public void addPlayerDominion(String pname, Dominion dom) {
		Set<Dominion> doms = playerd.get(pname);
		if(doms == null) {
			doms = new HashSet<Dominion>();
			doms.add(dom);
			playerd.put(pname, doms);
		} else {
			doms.add(dom);
		}
	}
	
	public void removePlayerDominion(String pname, Dominion dom) {
		Set<Dominion> doms = playerd.get(pname);
		if(doms != null) {
			doms.remove(dom);
		}
	}
	
	public Set<Dominion> getPlayerDominion(String name) {
		Set<Dominion> doms = playerd.get(name);
		return (doms == null) ? new HashSet<Dominion>() : doms;
	}

	public boolean hasName(String string) {
		return names.containsKey(string);
	}

	public void removeName(String name) {
		names.remove(name);
	}

	public void addName(Dominion dom) {
		names.put(dom.getName(), dom);
	}

	public Dominion getDominionForName(String string) {
		return names.get(string);
	}
}
