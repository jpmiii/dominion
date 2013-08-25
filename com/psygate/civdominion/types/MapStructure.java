package com.psygate.civdominion.types;

import java.util.HashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class MapStructure {
	private HashMap<Coordinates, Dominion> dommap = new HashMap<Coordinates, Dominion>();
	private HashMap<Coordinates, PreDominion> premap = new HashMap<Coordinates, PreDominion>();
	private HashMap<String, Set<Dominion>> playerd = new HashMap<String, Set<Dominion>>();
	private HashMap<String, Dominion> names = new HashMap<String, Dominion>();
	private final ReentrantReadWriteLock domlock = new ReentrantReadWriteLock(true);
	private final ReentrantReadWriteLock prelock = new ReentrantReadWriteLock(true);
	private final ReentrantReadWriteLock namelock = new ReentrantReadWriteLock(true);
	private final ReentrantReadWriteLock playerlock = new ReentrantReadWriteLock(true);
	
	//TODO Maybe include the exclusion zone in the range check...
	/**
	 * Return true if another dominion MAY overlap in the future. Else false.
	 * @param block
	 * @param maxrad
	 * @return
	 */
	public boolean proximityCheck(Block block, int maxrad) {
		domlock.readLock().lock();
		for (Entry<Coordinates, Dominion> entry : dommap.entrySet()) {
			if(entry.getKey().squareDistance2D(block) <= (maxrad + entry.getValue().getMaxsize()) * (maxrad + entry.getValue().getMaxsize())) {
				domlock.readLock().unlock();
				return true;
			}
		}
		domlock.readLock().unlock();
		
		prelock.readLock().lock();
		for (Entry<Coordinates, PreDominion> entry : premap.entrySet()) {
			if (entry.getKey().squareDistance2D(block) <= maxrad * 2) {
				prelock.readLock().unlock();
				return true;
			}
		}
		prelock.readLock().unlock();

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
		
		domlock.readLock().lock();
		for(Coordinates coords : dommap.keySet()) {
			double cdist = coords.squareDistance2D(location);
			if(cdist < dist) {
				chosen = dommap.get(coords);
				dist = cdist;
			}
		}
		domlock.readLock().unlock();
		
		return chosen;
	}

	public void addPreDominion(PreDominion dom) {
		prelock.writeLock().lock();
		premap.put(dom.getCoordinates(), dom);
		prelock.writeLock().unlock();
	}

	public boolean isPending(Block clickedBlock) {
		Coordinates cor = new Coordinates(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ(), clickedBlock
				.getWorld().getName());
		prelock.readLock().lock();
		boolean is = premap.containsKey(cor);
		prelock.readLock().unlock();
		return is;
	}

	public PreDominion getPreDominion(Block clickedBlock) {
		Coordinates cor = new Coordinates(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ(), clickedBlock
				.getWorld().getName());
		prelock.readLock().lock();
		PreDominion dom = premap.get(cor);
		prelock.readLock().unlock();
		return dom;
	}

	public void removePreDominion(PreDominion preDominion) {
		prelock.writeLock().lock();
		premap.remove(preDominion.getCoordinates());
		prelock.writeLock().unlock();
	}

	public void addDominion(Dominion dom) {
		domlock.writeLock().lock();
		dommap.put(dom.getCoordinates(), dom);
		domlock.writeLock().unlock();
		addName(dom);
	}

	public Dominion getDominion(Block targetBlock) {
		Coordinates cor = new Coordinates(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock
				.getWorld().getName());
		domlock.readLock().lock();
		Dominion dom = dommap.get(cor);
		domlock.readLock().unlock();
		return dom;
	}

	public Dominion getDominion(Coordinates coords) {
		domlock.readLock().lock();
		Dominion dom = dommap.get(coords);
		domlock.readLock().unlock();
		return dom;
	}

	public Collection<Dominion> getDominionSet() {
		domlock.readLock().lock();
		Collection<Dominion> col = new LinkedList<Dominion>();
		col.addAll(dommap.values());
		domlock.readLock().unlock();
		return col;
	}

	public Collection<PreDominion> getPreDominionSet() {
		prelock.readLock().lock();
		Collection<PreDominion> col = new LinkedList<PreDominion>();
		col.addAll(premap.values());
		prelock.readLock().unlock();
		return col;
	}

	public int getDominionNumber() {
		domlock.readLock().lock();
		int size = dommap.size();
		domlock.readLock().unlock();
		return size;
	}
	
	public int getPreDominionNumber() {
		prelock.readLock().lock();
		int size = premap.size();
		prelock.readLock().unlock();
		return size;
	}

	public boolean isDominion(Block targetBlock) {
		Coordinates cor = new Coordinates(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock
				.getWorld().getName());
		domlock.readLock().lock();
		boolean is = dommap.containsKey(cor);
		domlock.readLock().unlock();
		return is;
	}
	
	public boolean isPreDominion(Block targetBlock) {
		Coordinates cor = new Coordinates(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), targetBlock
				.getWorld().getName());
		prelock.readLock().lock();
		boolean is = premap.containsKey(cor);
		prelock.readLock().unlock();
		return is;
	}

	public void clear() {
		domlock.writeLock().lock();
		prelock.writeLock().lock();
		dommap.clear();
		premap.clear();
		prelock.writeLock().unlock();
		domlock.writeLock().unlock();
	}

	public void removeDominion(Coordinates coordinates) {
		domlock.writeLock().lock();
		if(dommap.get(coordinates) != null) {
			removeName(dommap.get(coordinates).getName());
		}
		dommap.remove(coordinates);
		domlock.writeLock().unlock();
	}
	
	public void removePreDominion(Coordinates cord) {
		prelock.writeLock().lock();
		premap.remove(cord);
		prelock.writeLock().unlock();
	}
	
	public void addPlayerDominion(String pname, Dominion dom) {
		playerlock.writeLock().lock();
		Set<Dominion> doms = playerd.get(pname);
		if(doms == null) {
			doms = new HashSet<Dominion>();
			doms.add(dom);
			playerd.put(pname, doms);
		} else {
			doms.add(dom);
		}
		playerlock.writeLock().unlock();
	}
	
	public void removePlayerDominion(String pname, Dominion dom) {
		playerlock.writeLock().lock();
		Set<Dominion> doms = playerd.get(pname);
		if(doms != null) {
			doms.remove(dom);
		}
		playerlock.writeLock().unlock();
	}
	
	public Set<Dominion> getPlayerDominion(String name) {
		playerlock.readLock().lock();
		Set<Dominion> doms = playerd.get(name);
		playerlock.readLock().unlock();
		return (doms == null) ? new HashSet<Dominion>() : doms;
	}

	public boolean hasName(String string) {
//		System.out.println(names);
		namelock.readLock().lock();
		boolean is = names.containsKey(string);
		namelock.readLock().unlock();
		return is;
	}

	public void removeName(String name) {
//		System.out.println("Dropping name "+name+".");
		namelock.writeLock().lock();
		names.remove(name);
		namelock.writeLock().unlock();
//		if(names.remove(name) != null) {
//			System.out.println("removed "+name);
//		}
	}

	public void addName(Dominion dom) {
		namelock.writeLock().lock();
		names.put(dom.getName(), dom);
		namelock.writeLock().unlock();
	}

	public Dominion getDominionForName(String string) {
		namelock.readLock().lock();
		Dominion dom = names.get(string);
		namelock.readLock().unlock();
		return dom;
	}
}
