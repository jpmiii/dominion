package com.psygate.civdominion;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import com.psygate.civdominion.commands.ACommand;
import com.psygate.civdominion.commands.error.CommandException;
import com.psygate.civdominion.commands.error.NotAPlayerException;
import com.psygate.civdominion.commands.information.DominionSetNameCommand;
import com.psygate.civdominion.commands.information.DominionShowNameCommand;
import com.psygate.civdominion.commands.information.DrawBoundariesCommand;
import com.psygate.civdominion.commands.op.ClearCommand;
import com.psygate.civdominion.commands.op.DominionForceGlobalGrowthCommand;
import com.psygate.civdominion.commands.op.DominionSetGrowIntervalCommand;
import com.psygate.civdominion.commands.op.DominionSetRadiusCommand;
import com.psygate.civdominion.commands.op.ForceAddAllUpgrades;
import com.psygate.civdominion.commands.op.ForceCompleteCommand;
import com.psygate.civdominion.commands.op.ForceDominionGrowth;
import com.psygate.civdominion.commands.op.GenerateDominionsCommand;
import com.psygate.civdominion.commands.op.GeneratePreDominionsCommand;
import com.psygate.civdominion.commands.op.StripUpgradesCommand;
import com.psygate.civdominion.commands.op.ToggleDebugCommand;
import com.psygate.civdominion.commands.uac.AddCitadelGroupCommand;
import com.psygate.civdominion.commands.uac.AddUserCommand;
import com.psygate.civdominion.commands.uac.ListUserCommand;
import com.psygate.civdominion.commands.uac.ModifyUserCommand;
import com.psygate.civdominion.commands.uac.RemoveUserCommand;
import com.psygate.civdominion.commands.upgrades.AddUpgradeCommand;
import com.psygate.civdominion.commands.upgrades.DominionSetPathCommand;
import com.psygate.civdominion.commands.upgrades.ListUpgradesCommand;
import com.psygate.civdominion.commands.upgrades.RemoveUpgradeCommand;
import com.psygate.civdominion.commands.upgrades.tesseractdisplacement.DisplaceCommand;
import com.psygate.civdominion.commands.upgrades.tesseractdisplacement.LinkCommand;
import com.psygate.civdominion.commands.upgrades.tesseractdisplacement.UnlinkCommand;
import com.psygate.civdominion.commands.upgrades.xpleech.XPInfoCommand;
import com.psygate.civdominion.configuration.Configuration;
import com.psygate.civdominion.configuration.Parser;
import com.psygate.civdominion.configuration.Strings;
import com.psygate.civdominion.listeners.ChatListener;
import com.psygate.civdominion.listeners.DominionListener;
import com.psygate.civdominion.listeners.NoCitadelListener;
import com.psygate.civdominion.listeners.UpgradeListener;
import com.psygate.civdominion.persistence.XMLPersister;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.GrowthManager;
import com.psygate.civdominion.types.MapStructure;
import com.psygate.civdominion.types.PlayerMap;
import com.psygate.civdominion.upgrades.UpgradeTimer;
import com.psygate.civdominion.upgrades.Vector;

public class CivDominion extends JavaPlugin {
	private static CivDominion dom = null;
	private static Logger log = Logger.getLogger("CivDominion");
	private Configuration conf;
	private MapStructure struc;
	private HashMap<String, ACommand> coms = new HashMap<String, ACommand>();
	private HashSet<String> ops = new HashSet<String>();
	private UpgradeTimer timer = new UpgradeTimer();
	private GrowthManager mgr = new GrowthManager();
	private PlayerMap map = new PlayerMap();
	private HashMap<Coordinates, Long> xp = new HashMap<Coordinates, Long>();
	public static boolean DEBUG = false;
	public static boolean CITADEL = false;

	public CivDominion() {
		dom = this;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		log.info("Bootstrapping...");
		dom = this;
		Parser.prepare();
		conf = Parser.parse();
		struc = new MapStructure();

		XMLPersister xmlp = new XMLPersister();
		xmlp.load();

		DominionListener doml = new DominionListener(conf, struc);
		ChatListener cl = new ChatListener();
		UpgradeListener ul = new UpgradeListener();
		if (super.getServer().getPluginManager().getPlugin("Citadel") != null) {
			CITADEL = true;
			NoCitadelListener list = new NoCitadelListener();
			super.getServer().getPluginManager().registerEvents(list, this);
		}
		super.getServer().getPluginManager().registerEvents(doml, this);
		super.getServer().getPluginManager().registerEvents(cl, this);
		super.getServer().getPluginManager().registerEvents(ul, this);
		super.getServer().getScheduler().scheduleSyncDelayedTask(this, mgr, 1000);
		log.info("Registering commands.");
		registerCommands();
		log.info("Bootstrapping Completed.");
		mgr.grow(false);
		super.getServer().getPluginManager().registerEvents(map.getListener(), this);
	}

	private void registerCommands() {
		addCommand(new ForceCompleteCommand());
		addCommand(new DrawBoundariesCommand());
		addCommand(new AddUserCommand());
		addCommand(new RemoveUserCommand());
		addCommand(new ModifyUserCommand());
		addCommand(new ListUpgradesCommand());
		addCommand(new RemoveUpgradeCommand());
		addCommand(new ForceAddAllUpgrades());
		addCommand(new AddUpgradeCommand());
		addCommand(new ForceDominionGrowth());
		addCommand(new GenerateDominionsCommand());
		addCommand(new GeneratePreDominionsCommand());
		addCommand(new ClearCommand());
		addCommand(new DominionSetGrowIntervalCommand());
		addCommand(new DominionForceGlobalGrowthCommand());
		addCommand(new StripUpgradesCommand());
		addCommand(new ToggleDebugCommand());
		addCommand(new AddCitadelGroupCommand());
		addCommand(new DominionSetRadiusCommand());
		addCommand(new XPInfoCommand());
		addCommand(new DominionSetPathCommand());
		addCommand(new ListUserCommand());
		addCommand(new DominionShowNameCommand());
		addCommand(new DominionSetNameCommand());
		addCommand(new DisplaceCommand());
		addCommand(new LinkCommand());
		addCommand(new UnlinkCommand());
	}

	private void addCommand(ACommand com) {
		coms.put(com.commandString, com);
	}

	@Override
	public void onDisable() {
		XMLPersister xmlp = new XMLPersister();
		xmlp.save();
		super.onDisable();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ACommand com = coms.get(command.getName());
		if (com != null) {
			if (com.requiresOP() && !ops.contains(sender.getName())) {
				sender.sendMessage(Strings.opOnlyCommand);
			} else {
				try {
					com.onCommand(sender, command, label, args);
				} catch (NotAPlayerException e) {
					sender.sendMessage(Strings.onlyPlayerCommand);
				} catch (CommandException e) {
					sender.sendMessage(Strings.generalCommandException);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static CivDominion getInstance() {
		return dom;
	}

	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}

	public Configuration getConfiguration() {
		return conf;
	}

	public MapStructure getMap() {
		return struc;
	}

	public void emitEvent(Event ev) {
		super.getServer().getPluginManager().callEvent(ev);
	}

	public void addDominionOperator(String name) {
		ops.add(name);
	}

	public boolean isDominionOp(String name) {
		return ops.contains(name);
	}

	public int scheduleTaskOnce(Runnable finishUpgradeTask, long delay) {
		return getServer().getScheduler().scheduleSyncDelayedTask(this, finishUpgradeTask, delay * 20);
	}

	public UpgradeTimer getUpgradeTimer() {
		return timer;
	}

	public GrowthManager getGrowthManager() {
		return mgr;
	}

	public PlayerMap getPlayerMap() {
		return map;
	}

	public void setPlayerMap(PlayerMap obj) {
		map = obj;
	}

	public void addXP(Coordinates c, long xp) {
		if (this.xp.containsKey(c)) {
			this.xp.put(c, this.xp.get(c) + xp);
		} else {
			this.xp.put(c, xp);
		}
	}

	public void removeXP(Coordinates c, long xp) {
		if (this.xp.containsKey(c)) {
			this.xp.put(c, this.xp.get(c) - xp);
		}
	}

	public Long getXP(Coordinates c) {
		return (xp.get(c) == null) ? null : (xp.get(c) < 0) ? 0 : xp.get(c);
	}

	public void setUpgradeTimer(UpgradeTimer obj) {
		HashMap<Coordinates, Vector> ups = obj.getMap();
		if (ups == null)
			return;
		for (Entry<Coordinates, Vector> en : ups.entrySet()) {
			if (en.getValue().getFinishTime() <= System.currentTimeMillis()) {
				struc.getDominion(en.getKey()).addUpgrade(en.getValue().getUp());
			} else {
				// Date fini = new Date(en.getValue().getFinishTime());
				// long dur = en.getValue().getFinishTime() -
				// System.currentTimeMillis();
				timer.addVector(en.getKey(), en.getValue());
			}
		}
	}

	public void cancelTask(int finishjob) {
		getServer().getScheduler().cancelTask(finishjob);
	}
}
