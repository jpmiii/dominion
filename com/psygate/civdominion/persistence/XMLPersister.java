package com.psygate.civdominion.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.types.Dominion;
import com.psygate.civdominion.types.MapStructure;
import com.psygate.civdominion.types.PlayerMap;
import com.psygate.civdominion.types.PreDominion;
import com.psygate.civdominion.upgrades.UpgradeTimer;
import com.thoughtworks.xstream.XStream;

public class XMLPersister {
	private final static Pattern pat = Pattern
			.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,2}");
	private final static String prefix = "xmlpersistence";
	private final static String playermap = "playermap.xml";
	private final static String upgrademap = "updatemap.xml";

	public static synchronized void load() {
		File f = new File(CivDominion.getInstance().getDataFolder(), prefix);
		if (f.isFile())
			f.delete();
		if (!f.exists())
			f.mkdirs();

		// yyyy-mm-dd-hh-mm-ss ->
		// [0-9]{4}-[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}-[0-9]{2}

		Date now = new Date();
		File recent = null;
		long delta = Long.MAX_VALUE;
		Date d = null;
		for (File fi : f.listFiles()) {
			String name = fi.getName();
			if (name.matches(pat.pattern())) {
				String[] parts = name.split("-");
				int year = Integer.parseInt(parts[0]);
				int month = Integer.parseInt(parts[1]);
				int day = Integer.parseInt(parts[2]);
				int hour = Integer.parseInt(parts[3]);
				int minute = Integer.parseInt(parts[4]);
				int second = Integer.parseInt(parts[5]);
				Calendar cal = GregorianCalendar.getInstance();
				cal.set(year, month, day, hour, minute, second);
				d = cal.getTime();
				if (now.getTime() - d.getTime() < delta) {
					delta = now.getTime() - d.getTime();
					recent = fi;
				}
			}
		}

		if (recent == null || delta == Long.MAX_VALUE) {
			Logger.getLogger("CivDominion-XMLPersister").info("No recent saves found. Bailing out.");
			return;
		} else {
			Logger.getLogger("CivDominion-XMLPersister").info(
					"Most recent save: " + recent + " Age: " + delta + " Now: " + now + " Save: " + d);
		}

		XStream stream = new XStream();
		int doms = 0;
		int predoms = 0;
		for (File dom : recent.listFiles()) {
			// Logger.getLogger("CivDominion-XMLPersister").info("Loading: "+dom);
			ObjectInputStream obji = null;
			try {
				obji = stream.createObjectInputStream(new FileInputStream(dom));
				Object obj = obji.readObject();
				if (obj instanceof Dominion) {
					Dominion dominion = (Dominion) obj;
					CivDominion.getInstance().getMap().addDominion(dominion);
					for (String name : dominion.getMemberMap().keySet()) {
						CivDominion.getInstance().getMap().addPlayerDominion(name, dominion);
					}
					// Logger.getLogger("CivDominion-XMLPersister").info("Dominion loaded: "
					// + dominion);
					doms++;
				} else if (obj instanceof PreDominion) {
					PreDominion predom = (PreDominion) obj;
					CivDominion.getInstance().getMap().addPreDominion(predom);
					// Logger.getLogger("CivDominion-XMLPersister").info("PreDominion loaded: "
					// + predom);
					predoms++;
				} else {
					if (!(obj instanceof PlayerMap) && !(obj instanceof UpgradeTimer)) {
						Logger.getLogger("CivDominion-XMLPersister").warning(
								"Unknown class: " + obj.getClass() + " @" + dom);
					}
				}
			} catch (Exception e) {
				Logger.getLogger("CivDominion-XMLPersister").log(Level.SEVERE, "Unable to load dominion " + dom, e);
			} finally {
				try {
					obji.close();
				} catch (Exception e) {

				}
			}
		}

		File playf = new File(recent, playermap);
		if (playf.exists() && playf.isFile()) {
			ObjectInputStream in = null;
			try {
				in = stream.createObjectInputStream(new FileInputStream(playf));
				Object obj = in.readObject();
				if (obj instanceof PlayerMap) {
					Logger.getLogger("CivDominion-XMLPersister").info("Playermap loaded. (" + playf + ")");
					CivDominion.getInstance().setPlayerMap((PlayerMap) obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		} else {
			Logger.getLogger("CivDominion-XMLPersister").info("Couldn't locate player map. (" + playf + ")");
		}

		File upf = new File(recent, upgrademap);
		if (upf.exists() && upf.isFile()) {
			ObjectInputStream in = null;
			try {
				in = stream.createObjectInputStream(new FileInputStream(upf));
				Object obj = in.readObject();
				if (obj instanceof UpgradeTimer) {

					Logger.getLogger("CivDominion-XMLPersister").info("Upgrademap loaded. (" + upf + ")");

					CivDominion.getInstance().setUpgradeTimer((UpgradeTimer) obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		} else {
			Logger.getLogger("CivDominion-XMLPersister").info("Couldn't locate updatemap map. (" + upf + ")");
		}

		Logger.getLogger("CivDominion-XMLPersister").info(
				"Loaded " + doms + " Dominions and " + predoms + " PreDominions.");
	}

	public static synchronized void save() {
		MapStructure struc = CivDominion.getInstance().getMap();
		String format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		File storage = new File(new File(CivDominion.getInstance().getDataFolder(), prefix), format);
		storage.mkdirs();
		XStream stream = new XStream();
		int stored = 0;
		int prestored = 0;
		Logger.getLogger("CivDominion-XMLPersister").info(
				"Storing: " + struc.getDominionNumber() + " Dominions, " + struc.getPreDominionNumber()
						+ " PreDominions.");
		for (Dominion dom : struc.getDominionSet()) {
			int x = dom.getCoordinates().getX();
			int y = dom.getCoordinates().getY();
			int z = dom.getCoordinates().getZ();
			String name = "dom " + x + "," + y + "," + z + ".xml";
			File out = new File(storage, name);
			ObjectOutputStream oo = null;
			try {
				oo = stream.createObjectOutputStream(new FileOutputStream(out));
				oo.writeObject(dom);
				stored++;
				// Logger.getLogger("CivDominion-XMLPersister").info("Stored "+dom.getCoordinates());
			} catch (Exception e) {
				Logger.getLogger("CivDominion-XMLPersister").log(Level.SEVERE,
						"Unable to store " + dom.getCoordinates(), e);
			} finally {
				try {
					oo.close();
				} catch (Exception e) {

				}
			}
		}

		for (PreDominion pdom : struc.getPreDominionSet()) {
			int x = pdom.getCoordinates().getX();
			int y = pdom.getCoordinates().getY();
			int z = pdom.getCoordinates().getZ();
			String name = "predom " + x + "," + y + "," + z + ".xml";
			File out = new File(storage, name);
			if (out.exists())
				throw new RuntimeException("Exists: " + out);
			ObjectOutputStream oo = null;
			try {
				oo = stream.createObjectOutputStream(new FileOutputStream(out));
				oo.writeObject(pdom);
				prestored++;
				// Logger.getLogger("CivDominion-XMLPersister").info("Stored "+dom.getCoordinates());
			} catch (Exception e) {
				Logger.getLogger("CivDominion-XMLPersister").log(Level.SEVERE,
						"Unable to store " + pdom.getCoordinates(), e);
			} finally {
				try {
					oo.close();
				} catch (Exception e) {

				}
			}
		}

		Logger.getLogger("CivDominion-XMLPersister").info(
				"Stored " + stored + " Dominions and " + prestored + " PreDominions.");
		File playf = new File(storage, playermap);

		ObjectOutputStream in = null;
		try {
			in = stream.createObjectOutputStream(new FileOutputStream(playf));
			in.writeObject(CivDominion.getInstance().getPlayerMap().clone());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {

			}
		}

		File upf = new File(storage, upgrademap);

		ObjectOutputStream uin = null;
		try {
			uin = stream.createObjectOutputStream(new FileOutputStream(upf));
			uin.writeObject(CivDominion.getInstance().getUpgradeTimer().clone());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				uin.close();
			} catch (Exception e) {

			}
		}
	}
}
