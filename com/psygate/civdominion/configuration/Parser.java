package com.psygate.civdominion.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.types.Coordinates;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.PreDominion;
import com.psygate.civdominion.types.uac.Rank;
import com.psygate.civdominion.upgrades.Upgrade;

public class Parser {
	public static void prepare() {
		CivDominion.getInstance().reloadConfig();
		CivDominion.getInstance().getConfig().options().copyDefaults(true);
		CivDominion.getInstance().saveConfig();
	}

	public static Configuration parse() {
		Logger log = Logger.getLogger("CivDominion");
		FileConfiguration config = CivDominion.getInstance().getConfig();
		for (Object obj : (Collection<?>) config.get("upgrades")) {
			Map<?, ?> map = (Map<?, ?>) obj;
			Upgrade up = Upgrade.valueOf((String) map.get("name"));
			up.setEnabled((Boolean) map.get("enabled"));
			up.setBuildtime(((Number) map.get("buildtime")).longValue());
			String matstring = ((String) map.get("paymat")).toUpperCase();
			int amount = ((Number) map.get("payamount")).intValue();
			up.setCosts(new ItemStack(Material.getMaterial(matstring), amount));
			log.info("Upgrade: " + up + "(" + up.getId() + ")\t\tEnabled: " + up.isEnabled() + "\tBuildtime: "
					+ up.getBuildTime());
		}

		MemorySection sc = (MemorySection) config.get("dominion");

		Material mat = Material.getMaterial(sc.getString("material").toUpperCase());
		float initrad = (float) sc.getDouble("initial_radius");
		int maxrad = sc.getInt("max_radius");
		int exclz = sc.getInt("exclusion_zone_size");
		long growthinterval = sc.getLong("growthinterval");
		float gwpu = (float) sc.getDouble("growthperuser");
		Dominion dom = new Dominion(new Coordinates(0, 0, 0, ""), initrad, new HashMap<String, Rank>(), maxrad, exclz,
				gwpu);
		log.info("Prototype dominion bootstrapped.");
		log.info(dom.toString());
		int votes = sc.getInt("votes");
		Material appr = Material.getMaterial(sc.getString("approval_material").toUpperCase());
		int amount = sc.getInt("approval_amount");
		ItemStack stack = new ItemStack(appr, amount);

		PreDominion pred = new PreDominion(new Coordinates(0, 0, 0, ""), mat, votes, new HashMap<String, Rank>(), stack);
		log.info("Prototype PreDominion bootstrapped.");
		log.info(pred.toString());

		Configuration conf = new Configuration();

		conf.setPrototypeDominion(dom);
		conf.setPrototypePreDominion(pred);

		for (Object name : (Collection<?>) config.get("operators")) {
			if (name instanceof String) {
				CivDominion.getInstance().addDominionOperator((String) name);
				log.info("Dominion Operator added. (" + (String) name + ")");
			}
		}
		conf.setGrowthInterval(growthinterval);

		return conf;
	}
}
