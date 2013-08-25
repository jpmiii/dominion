package com.psygate.civdominion.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;

import static org.bukkit.entity.EntityType.*;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.Vector3;
import com.psygate.civdominion.upgrades.Upgrade;
import com.psygate.math.euclidian.point.Point2D;
import com.psygate.math.euclidian.polygon.Triangle2D;

import static com.psygate.civdominion.upgrades.Upgrade.*;

public class UpgradeListener implements Listener {
	Set<EntityType> host = new HashSet<EntityType>();
	private Random rand = new Random();
	private HashMap<String, Long> fitg = new HashMap<String, Long>();
	private HashMap<String, Long> fol = new HashMap<String, Long>();
	private HashMap<String, Location> newspawn = new HashMap<String, Location>();
	private HashMap<String, Long> dp = new HashMap<String, Long>();
	private HashMap<String, Long> pom = new HashMap<String, Long>();
	private HashMap<String, Long> wp = new HashMap<String, Long>();
	private Set<Material> raremat = new HashSet<Material>();
	private Set<Material> unraremat = new HashSet<Material>();
	private HashSet<Location> mineboost = new HashSet<Location>();
	private Set<EntityType> fertility = new HashSet<EntityType>();
	private HashSet<Location> spwn = new HashSet<Location>();
	private HashMap<String, Vector3<Long, AtomicInteger, Triangle2D>> mercy = new HashMap<String, Vector3<Long, AtomicInteger, Triangle2D>>();

	private static final String[] strength = new String[] { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };

	public UpgradeListener() {
		host.add(ZOMBIE);
		host.add(CREEPER);
		host.add(BLAZE);
		host.add(SPIDER);
		host.add(SKELETON);
		host.add(CAVE_SPIDER);
		host.add(ENDERMAN);
		host.add(ENDER_DRAGON);
		host.add(MAGMA_CUBE);
		host.add(SLIME);
		host.add(WITCH);
		host.add(GHAST);
		host.add(WITHER);
		host.add(PIG_ZOMBIE);
		host.add(GIANT);
		fertility.add(COW);
		fertility.add(SHEEP);
		fertility.add(CHICKEN);
		fertility.add(PIG);
		fertility.add(HORSE);

		raremat.add(Material.IRON_ORE);
		raremat.add(Material.DIAMOND);
		raremat.add(Material.GOLD_ORE);
		raremat.add(Material.COAL);

		unraremat.add(Material.STONE);
		unraremat.add(Material.COBBLESTONE);
		unraremat.add(Material.DIRT);
		unraremat.add(Material.LOG);
	}

	@EventHandler
	public void fortification(BlockPlaceEvent ev) {
		if (ev.isCancelled() || !Fortification.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(Fortification))
			return;
		if (!dom.hasMember(ev.getPlayer().getName()) && dom.inExclusionZone(ev.getBlock())) {
			ev.setCancelled(true);
			ev.getPlayer().sendMessage(Strings.inexclnobuild);
		}
	}

	@EventHandler
	public void noinvisibility(PlayerItemConsumeEvent ev) {
		if (ev.isCancelled() || !NoInvisibility.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getPlayer().getLocation().getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(NoInvisibility))
			return;
		if (ev.getItem().getType().equals(Material.POTION)) {
			short id = ev.getItem().getDurability();
			if (id == 8238 || id == 8270) {
				if (dom.getCoordinates().squareDistance2D(ev.getPlayer().getLocation().getBlock()) < dom.getRadius()
						* dom.getRadius()) {
					if (!dom.hasMember(ev.getPlayer().getName())) {
						ev.setCancelled(true);
						ev.getPlayer().sendMessage(Strings.nopotions);
						ev.getPlayer().damage(2);
					}
				}
			}
		}
	}

	@EventHandler
	public void nosplashinvisibility(PotionSplashEvent ev) {
		if (ev.isCancelled() || !NoInvisibility.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getEntity().getLocation().getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(NoInvisibility))
			return;
		short id = ev.getPotion().getItem().getDurability();

		if (id == 16462 || id == 16430) {
			if (dom.getCoordinates().squareDistance2D(ev.getEntity().getLocation().getBlock()) < dom.getRadius()
					* dom.getRadius()) {
				ev.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void nopearl(PlayerTeleportEvent ev) {
		if (ev.isCancelled() || !NoPearl.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getTo().getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(NoPearl))
			return;
		if (dom.hasMember(ev.getPlayer().getName()))
			return;
		if (ev.getCause().equals(TeleportCause.ENDER_PEARL)) {
			float rs = dom.getRadius() * dom.getRadius();
			if (dom.getCoordinates().squareDistance2D(ev.getFrom().getBlock()) > rs
					&& dom.getCoordinates().squareDistance2D(ev.getTo().getBlock()) < rs) {
				ev.setCancelled(true);
				ev.getPlayer().sendMessage(Strings.nopearlport);
				ev.getPlayer().damage(2);
			}
		}
	}

	@EventHandler
	public void mobcull(CreatureSpawnEvent ev) {
		if (ev.isCancelled() || !MobCull.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getEntity().getLocation().getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(MobCull))
			return;
		if (!(ev.getEntity() instanceof LivingEntity))
			return;
		if (dom.getRadius() * dom.getRadius() >= dom.getCoordinates().squareDistance2D(ev.getLocation().getBlock())) {
			if (host.contains(ev.getEntityType()))
				ev.setCancelled(true);
		}
	}

	@EventHandler
	public void nohostilemobs(EntityTargetLivingEntityEvent ev) {
		if (ev.isCancelled() || !NoHostileMobs.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getTarget().getLocation().getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(NoHostileMobs))
			return;

		float rs = dom.getRadius() * dom.getRadius();
		if (dom.getCoordinates().squareDistance2D(ev.getEntity().getLocation().getBlock()) > rs
				&& dom.getCoordinates().squareDistance2D(ev.getTarget().getLocation().getBlock()) < rs) {
			if (ev.getEntity() instanceof LivingEntity) {
				((LivingEntity) ev.getEntity()).damage(20);
				((LivingEntity) ev.getEntity()).damage(100);
			}
		}
	}

	@EventHandler
	public void nolava(PlayerBucketEmptyEvent ev) {
		if (ev.isCancelled() || !NoLava.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getBlockClicked());
		if (dom == null || !dom.hasUpgrade(NoLava))
			return;
		if (dom.hasMember(ev.getPlayer().getName())) {
			return;
		}
		float rs = dom.getRadius() * dom.getRadius();
		if (dom.getCoordinates().squareDistance2D(ev.getBlockClicked()) < rs) {
			ev.getPlayer().sendMessage(Strings.notallowedhere);
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void wheatLeech(BlockGrowEvent ev) {
		if (ev.isCancelled() || !WheatLeech.isEnabled())
			return;
		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getBlock());
		if (dom == null)
			return;
		if (!dom.hasUpgrade(WheatLeech))
			return;
		if (dom != null) {
			synchronized (rand) {
				if (rand.nextInt(100) != 35)
					return;
			}
			if (dom.getCoordinates().squareDistance2D(ev.getBlock()) < dom.getRadius() * dom.getRadius()) {
				CivDominion.getInstance().addXP(dom.getCoordinates(), 1);
				ev.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void wheatRemove(PlayerInteractEvent ev) {
		if (ev.isCancelled() || !WheatLeech.isEnabled())
			return;
		if (Action.LEFT_CLICK_BLOCK.equals(ev.getAction())) {
			if (ev.getPlayer().getItemInHand().getType().equals(Material.GLASS_BOTTLE)) {
				Dominion dom = CivDominion.getInstance().getMap().getDominion(ev.getClickedBlock());
				if (dom != null) {
					if (dom.hasUpgrade(Upgrade.WheatLeech)) {
						if (dom.hasMember(ev.getPlayer().getName())) {
							Long xp = CivDominion.getInstance().getXP(dom.getCoordinates());
							if (xp == null)
								return;
							CivDominion.getInstance().removeXP(dom.getCoordinates(), xp);
							ev.getPlayer().giveExp(xp.intValue());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void footInTheGrave(EntityDamageByEntityEvent ev) {
		if (ev.isCancelled() || !FootInTheGrave.isEnabled())
			return;
		if (ev.getEntity() instanceof Player) {
			Player dmgd = (Player) ev.getEntity();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgd.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(FootInTheGrave) || !dom.hasMember(dmgd.getName()))
					return;
				if (dom.getCoordinates().squareDistance2D(dmgd.getLocation()) < dom.getRadius() * dom.getRadius()) {
					Long last = fitg.get(dmgd.getName());
					if (last != null && System.currentTimeMillis() - last < TimeUnit.MINUTES.toMillis(30)) {
						return;
					}
					double health = dmgd.getHealth();
					if (dmgd.getHealth() < dmgd.getMaxHealth() * 0.3) {
						dmgd.sendMessage(Strings.deathsbreath);
						dmgd.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
						dmgd.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 1));
						dmgd.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2 * 20, 0));

						fitg.put(dmgd.getName(), System.currentTimeMillis());
						dmgd.setHealth(health);
					}
				}
			}
		}
	}

	@EventHandler
	public void parasiticBond(EntityDeathEvent ev) {
		if (!ParasiticBond.isEnabled())
			return;
		Event de = ev.getEntity().getLastDamageCause();
		if (!(de instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) de;
		if (ed.getDamager() instanceof Player) {
			Player dmgd = (Player) ed.getDamager();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgd.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(Upgrade.ParasiticBond) || !dom.hasMember(dmgd.getName()))
					return;
				if (dom.getCoordinates().squareDistance2D(dmgd.getLocation()) < dom.getRadius() * dom.getRadius()) {

					double health = dmgd.getHealth() + 2;

					dmgd.setHealth((health > 20) ? 20 : health);
					dmgd.sendMessage(Strings.parasiticbond);
				}
			}
		}
	}

	@EventHandler
	public void deathIntoLife(EntityDeathEvent ev) {
		if (!DeathIntoLife.isEnabled())
			return;
		Entity ved = ev.getEntity();
		if (ved instanceof Player) {
			Player ed = (Player) ved;
			Set<Dominion> pdoms = CivDominion.getInstance().getMap().getPlayerDominion(ed.getName());
			System.out.println("Player doms: " + pdoms);
			for (Dominion dom : pdoms) {
				if (dom.hasUpgrade(DeathIntoLife) && ed.getBedSpawnLocation() == null) {
					if (!dom.getCoordinates().getWorld().equals(ed.getWorld().getName()))
						continue;
					double angle = 0;
					synchronized (rand) {
						angle = Math.PI * 2 * rand.nextFloat();
					}
					double x = (dom.getCoordinates().getX() + Math.cos(angle) * 800 + 200);
					double z = (dom.getCoordinates().getY() + Math.sin(angle) * 800 + 200);
					double y = CivDominion.getInstance().getServer().getWorld(dom.getCoordinates().getWorld())
							.getHighestBlockYAt((int) x, (int) z) + 1;

					newspawn.put(ed.getName(), new Location(ed.getWorld(), x, y, z));
				}
			}
		}
	}

	@EventHandler
	public void deathIntoLifeSpawn(PlayerRespawnEvent ev) {
		if (!DeathIntoLife.isEnabled())
			return;
		Player ed = ev.getPlayer();
		if (newspawn.containsKey(ed.getName())) {
			ev.setRespawnLocation(newspawn.get(ed.getName()));
			ed.sendMessage(Strings.deathintolife);
		}
	}

	@EventHandler
	public void potentCorruption(EntityDamageByEntityEvent ev) {
		if (ev.isCancelled() || !PotentCorruption.isEnabled())
			return;
		// Entity ldc = ev.getEntity().getLastDamageCause();
		Event de = ev.getEntity().getLastDamageCause();
		if (!(de instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) de;

		if (ed.getDamager() instanceof Player && ed.getEntity() instanceof Player) {
			Player dmgr = (Player) ed.getDamager();
			Player dmgd = (Player) ed.getEntity();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgr.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(PotentCorruption) || !dom.hasMember(dmgr.getName()))
					return;
				if (dom.getCoordinates().squareDistance2D(dmgr.getLocation()) < dom.getRadius() * dom.getRadius()) {
					for (PotionEffect eff : dmgd.getActivePotionEffects()) {
						synchronized (rand) {
							if (rand.nextInt(100) == 10) { // 1% chance
								if (!dmgr.hasPotionEffect(eff.getType())) {
									dmgr.addPotionEffect(eff);
									dmgd.removePotionEffect(eff.getType());
									String potion = eff.getType().getName() + " ";
									potion += strength[eff.getAmplifier() % strength.length];
									dmgd.sendMessage(Strings.potentcurroptionloss.replace("$e$", potion));
									int secs = (eff.getDuration() / 20);
									int min = secs / 60;
									secs = secs - (min * 60);
									potion += " for " + ((min > 0) ? min + "min" + secs : secs) + "secs";
									dmgr.sendMessage(Strings.potentcorrupted.replace("$e$", potion));
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void fullOfLife(EntityDamageByEntityEvent ev) {
		if (ev.isCancelled() || !FullOfLife.isEnabled())
			return;
		// Entity ldc = ev.getEntity().getLastDamageCause();
		Event de = ev.getEntity().getLastDamageCause();
		if (!(de instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) de;

		if (ed.getDamager() instanceof Player && ed.getEntity() instanceof Player) {
			Player dmgr = (Player) ed.getDamager();
			Player dmgd = (Player) ed.getEntity();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgr.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(FullOfLife) || !dom.hasMember(dmgd.getName()))
					return;
				boolean apply = false;
				if (dom.getCoordinates().squareDistance2D(dmgd.getLocation()) < dom.getRadius() * dom.getRadius()) {
					if (!fol.containsKey(dmgd.getName())) {
						apply = true;
					} else {
						if (System.currentTimeMillis() - fol.get(dmgd.getName()) > TimeUnit.SECONDS.toMillis(10)) {
							apply = true;
						}
					}

					if (apply) {
						dmgd.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 0));
						fol.put(dmgd.getName(), System.currentTimeMillis());
						dmgd.sendMessage(Strings.fulloflife);
					}
				}
			}
		}
	}

	@EventHandler
	public void deathlyPerception(EntityDamageByEntityEvent ev) {
		if (ev.isCancelled() || !DeathlyPerception.isEnabled())
			return;

		// Entity ldc = ev.getEntity().getLastDamageCause();
		Event de = ev.getEntity().getLastDamageCause();
		if (!(de instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) de;

		if (ed.getDamager() instanceof Player && ed.getEntity() instanceof Player) {
			Player dmgr = (Player) ed.getDamager();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgr.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(DeathlyPerception) || !dom.hasMember(dmgr.getName()))
					return;
				Long lastapp = new Long(0);
				boolean apply = false;
				if (dom.getCoordinates().squareDistance2D(dmgr.getLocation()) < dom.getRadius() * dom.getRadius()) {
					lastapp = dp.get(dmgr.getName());

					if (lastapp == null || System.currentTimeMillis() - lastapp > TimeUnit.MINUTES.toMillis(30)) {
						if (dmgr.getHealth() < (dmgr.getMaxHealth() * 0.5))
							apply = true;
					} else if (System.currentTimeMillis() - lastapp < TimeUnit.SECONDS.toMillis(2)) {
						if (dmgr.getHealth() < (dmgr.getMaxHealth() * 0.5))
							apply = true;
					}

					if (apply) {
						dp.put(dmgr.getName(), System.currentTimeMillis());
						dmgr.sendMessage(Strings.deathlyperception);
						int newdmg = (int) (ev.getDamage() * 1.2);
						ev.setDamage((newdmg == 1) ? 2 : newdmg);
					}
				}
			}
		}
	}

	@EventHandler
	public void pathOfMidnight(EntityDamageByEntityEvent ev) {
		if (ev.isCancelled() || !PathOfMidnight.isEnabled())
			return;
		// Entity ldc = ev.getEntity().getLastDamageCause();
		Event de = ev.getEntity().getLastDamageCause();
		if (!(de instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) de;

		if (ed.getDamager() instanceof Player && !(ed.getEntity() instanceof Player)) {
			Player dmgr = (Player) ed.getDamager();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgr.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(PathOfMidnight) || !dom.hasMember(dmgr.getName()))
					return;
				if (dom.getCoordinates().squareDistance2D(dmgr.getLocation()) <= dom.getRadius() * dom.getRadius()) {
					long time = CivDominion.getInstance().getServer().getWorld(dmgr.getWorld().getName()).getTime();
					if (time > 12300 && time < 23850) {
						ev.setDamage(ev.getDamage() * 3);
						if (pom.get(dmgr.getName()) == null
								|| System.currentTimeMillis() - pom.get(dmgr.getName()) > TimeUnit.MINUTES.toMillis(7)) {
							dmgr.sendMessage(Strings.pathofmidnight);
							pom.put(dmgr.getName(), System.currentTimeMillis());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void witheringStrikes(EntityDamageByEntityEvent ed) {
		if (ed.isCancelled() || !WitheringStrikes.isEnabled())
			return;

		if (ed.getDamager() instanceof Player && ed.getEntity() instanceof Player) {
			Player dmgr = (Player) ed.getDamager();
			Player dmgd = (Player) ed.getEntity();
			Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(dmgr.getLocation().getBlock());
			if (dom != null) {
				if (!dom.hasUpgrade(WitheringStrikes) || !dom.hasMember(dmgr.getName()))
					return;
				int randn = 0;
				synchronized (rand) {
					randn = rand.nextInt(100);
				}

				long lastapp = TimeUnit.MINUTES.toMillis(11);
				if (!(wp.get(dmgd.getName()) == null)) {
					lastapp = System.currentTimeMillis() - wp.get(dmgd.getName());
				}

				if (randn == 22 && lastapp > TimeUnit.MINUTES.toMillis(10)) {
					dmgd.sendMessage(Strings.witheringprecision);
					wp.put(dmgd.getName(), System.currentTimeMillis());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void wither(EntityDamageByEntityEvent ev) {
		if (ev.isCancelled() || !WitheringStrikes.isEnabled())
			return;
		if (!(ev.getDamager() instanceof Player)) {
			return;
		}
		Player dmgr = (Player) ev.getDamager();
		if (wp.get(dmgr.getName()) != null) {
			long app = System.currentTimeMillis() - wp.get(dmgr.getName());
			if (app < TimeUnit.SECONDS.toMillis(3)) {
				ev.setDamage((int) (ev.getDamage() * 0.8));
				dmgr.sendMessage(Strings.withered);
			}
		}
	}

	@EventHandler
	public void playerDrop(PlayerDropItemEvent ev) {
		if (ev.isCancelled() || !PrayerOfIdols.isEnabled())
			return;
		mineboost.add(ev.getItemDrop().getLocation());
	}

	@EventHandler
	public void prayerOfIdols(ItemSpawnEvent ev) {
		if (ev.isCancelled() || !PrayerOfIdols.isEnabled())
			return;

		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getEntity().getLocation());

		if (dom != null && !mineboost.contains(ev.getLocation())) {
			if (raremat.contains(ev.getEntity().getItemStack().getType())) {
				ev.getEntity().getItemStack().setAmount(ev.getEntity().getItemStack().getAmount() + 1);
			} else if (unraremat.contains(ev.getEntity().getItemStack().getType())) {
				ev.getEntity().getItemStack().setAmount(ev.getEntity().getItemStack().getAmount() + 2);
			}
		}
	}

	public void prayerOfIdols2(ItemDespawnEvent ev) {
		if (ev.isCancelled() || !PrayerOfIdols.isEnabled())
			return;
		mineboost.remove(ev.getLocation());
	}

	// @EventHandler
	// public void unearthlyShackles(PlayerRespawnEvent ev) {
	// if (!UnearthlyShackles.isEnabled())
	// return;
	// ArrayList<Dominion> doms = new ArrayList<Dominion>();
	//
	// for (Dominion dom : CivDominion.getInstance().getMap().getDominionSet())
	// {
	// if (dom.hasUpgrade(UnearthlyShackles)) {
	// doms.add(dom);
	// }
	// }
	//
	// if (doms.size() == 0)
	// return;
	// int ind;
	// double angle = 0;
	// synchronized (rand) {
	// ind = rand.nextInt(doms.size());
	// angle = Math.PI * 2 * rand.nextDouble();
	// }
	// Dominion here = doms.get(ind);
	//
	// double x = (here.getCoordinates().getX() + Math.cos(angle) * 800 + 200);
	// double z = (here.getCoordinates().getY() + Math.sin(angle) * 800 + 200);
	// World world =
	// CivDominion.getInstance().getServer().getWorld(here.getCoordinates().getWorld());
	// double y = world.getHighestBlockYAt((int) x, (int) z) + 1;
	//
	// ev.setRespawnLocation(new Location(world, x, y, z));
	// CivDominion.getInstance().getServer().broadcastMessage("RESPAWN!");
	// ev.getPlayer().sendMessage(Strings.shackles);
	// }

	@EventHandler
	public void unearthlyShackles(PlayerLoginEvent ev) {
		if (!UnearthlyShackles.isEnabled() || ev.getPlayer().hasPlayedBefore() || ev.getPlayer().getLastPlayed() != 0
				|| ev.getPlayer().getBedSpawnLocation() != null)
			return;

		ArrayList<Dominion> doms = new ArrayList<Dominion>();

		for (Dominion dom : CivDominion.getInstance().getMap().getDominionSet()) {
			if (dom.hasUpgrade(UnearthlyShackles)) {
				doms.add(dom);
			}
		}

		if (doms.size() == 0)
			return;
		int ind;
		double angle = 0;
		synchronized (rand) {
			ind = rand.nextInt(doms.size());
			angle = Math.PI * 2 * rand.nextDouble();
		}
		Dominion here = doms.get(ind);

		double x = (here.getCoordinates().getX() + Math.cos(angle) * 800 + 200);
		double z = (here.getCoordinates().getZ() + Math.sin(angle) * 800 + 200);
		World world = CivDominion.getInstance().getServer().getWorld(here.getCoordinates().getWorld());
		double y = world.getHighestBlockYAt((int) x, (int) z) + 1;

		CivDominion.getInstance().scheduleTaskOnce(new SpawnPort(new Location(world, x, y, z), ev.getPlayer()), 1);
	}

	@EventHandler
	public void prayerOfFertility(CreatureSpawnEvent ev) {
		if (ev.isCancelled() || !PrayerOfFertility.isEnabled())
			return;

		if (spwn.contains(ev.getEntity().getLocation())) {
			spwn.remove(ev.getEntity().getLocation());
			return;
		}
		if (ev.getEntityType().isAlive() && fertility.contains(ev.getEntityType())) {
			if (ev.getEntity() instanceof Ageable) {
				Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getLocation());
				if (dom != null && dom.hasUpgrade(PrayerOfFertility) && !((Ageable) ev.getEntity()).isAdult()) {
					int srand;
					synchronized (rand) {
						srand = rand.nextInt(100);
					}

					if (srand < 20) {
						spwn.add(ev.getEntity().getLocation());
						Entity e = ev.getEntity().getWorld().spawnEntity(ev.getLocation(), ev.getEntityType());

						if (e instanceof Ageable) {
							((Ageable) e).setBaby();
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void prayerOfAsh(BlockGrowEvent ev) {
		if (ev.isCancelled() || !PrayerOfAsh.isEnabled())
			return;

		Dominion dom = CivDominion.getInstance().getMap().getClosestDominion(ev.getBlock());
		if (dom != null && dom.hasUpgrade(PrayerOfAsh)) {
			int srand = 0;
			synchronized (rand) {
				srand = rand.nextInt(100);
			}

			if (srand == 10) {
				int[] viable = new int[] { -1, 1 };
				int x = viable[rand.nextInt(viable.length)];
				int z = viable[rand.nextInt(viable.length)];

				Block ad = ev.getBlock().getRelative(x, 0, z);
				if (ad.getType().equals(Material.CROPS)) {
					if (ad.getData() < 7) {
						ad.setData((byte) (ad.getData() + 1));

					}
				}
			}
		}
	}

	private class SpawnPort implements Runnable {
		private Location loc;
		private Player pl;

		public SpawnPort(Location loc, Player pl) {
			this.loc = loc;
			this.pl = pl;
		}

		public void run() {
			pl.teleport(loc, TeleportCause.PLUGIN);
			pl.sendMessage(Strings.shackles);
		}
	}

	public void addMercy(String name, Triangle2D vec2) {
		Vector3<Long, AtomicInteger, Triangle2D> vec = new Vector3<Long, AtomicInteger, Triangle2D>(
				System.currentTimeMillis(), new AtomicInteger(), vec2);
		mercy.put(name, vec);
	}
	
	private HashSet<Location> mercydrop = new HashSet<Location>();
	
	@EventHandler
	public void mercyTarget(EntityTargetEvent ev) {
		if(ev.isCancelled() || !Upgrade.BeaconOfMercy.isEnabled()) {
			return;
		}
		if(ev.getTarget() instanceof Player) {
			Object obj = null;
			synchronized(mercy) {
				obj = mercy.get(((Player)ev.getTarget()).getName());
			}
			
			if(obj != null) {
//				System.out.println("Butchering."+((Player)ev.getTarget()).getName()+" <- "+ev.getEntity());
				if(ev.getEntity() instanceof LivingEntity && host.contains(ev.getEntityType())) {
					LivingEntity en = (LivingEntity) ev.getEntity();
					en.damage(100);
					ev.setCancelled(true);
					mercydrop.add(en.getLocation().getBlock().getLocation());
				}
			}
		}
	}
	
	@EventHandler
	public void mercyDropRemove(ItemSpawnEvent ev) {
		if(ev.isCancelled() || !Upgrade.BeaconOfMercy.isEnabled()) 
			return;
		
		if(mercydrop.contains(ev.getLocation().getBlock().getLocation())) {
			mercydrop.remove(ev.getLocation().getBlock().getLocation());
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void mercyMove(PlayerMoveEvent ev) {
		if (ev.isCancelled() || !Upgrade.BeaconOfMercy.isEnabled())
			return;
		Vector3<Long, AtomicInteger, Triangle2D> vec;
		synchronized (mercy) {
			vec = mercy.get(ev.getPlayer().getName());
		}
		if (vec == null)
			return;
		if (System.currentTimeMillis() - vec.getT() > TimeUnit.MINUTES.toMillis(30)) {
			ev.getPlayer().sendMessage(Strings.nomoremercy);
			synchronized (mercy) {
				mercy.remove(ev.getPlayer().getName());
			}
		} else {
			if (vec.getV().incrementAndGet() % 100 == 0) {
				Point2D p = new Point2D(ev.getPlayer().getLocation().getX(), ev.getPlayer().getLocation().getZ());
				//Player left mercy area
				if(!vec.getW().contains(p)) {
					ev.getPlayer().sendMessage(Strings.nomoremercy);
					synchronized (mercy) {
						mercy.remove(ev.getPlayer().getName());
					}
				}
				Point2D inter = vec.getW().projectOnAB(p);
				Location loc = new Location(ev.getPlayer().getWorld(), inter.getX(), ev.getPlayer().getLocation()
						.getY(), inter.getY());
				ev.getPlayer().sendBlockChange(loc, Material.REDSTONE_BLOCK, (byte) 0);

				Point2D inter2 = vec.getW().projectOnAC(p);
				Location loc2 = new Location(ev.getPlayer().getWorld(), inter2.getX(), ev.getPlayer().getLocation()
						.getY(), inter2.getY());

				ev.getPlayer().sendBlockChange(loc2, Material.LAPIS_BLOCK, (byte) 0);
			}
		}
	}
}
