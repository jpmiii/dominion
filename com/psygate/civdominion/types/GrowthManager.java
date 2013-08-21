package com.psygate.civdominion.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.psygate.civdominion.CivDominion;
import com.psygate.civdominion.events.DominionGrowthEvent;

public class GrowthManager implements Runnable {
	public synchronized void grow(boolean forced) {
		boolean grow = false;
		long last = 0;
		File f = new File(CivDominion.getInstance().getDataFolder(), "lastgrowth");
		FileInputStream in = null;
		if (!f.exists()) {
			grow = true;
		} else {
			try {
				StringBuffer read = new StringBuffer();
				int r;
				in = new FileInputStream(f);
				while ((r = in.read()) != -1) {
					read.append((char) r);
				}
				last = Long.parseLong(read.toString());
				if (System.currentTimeMillis() - last > CivDominion.getInstance().getConfiguration()
						.getGrowthInterval()) {
					grow = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			} finally {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		}

		if (grow || forced) {
			CivDominion.getInstance().emitEvent(new DominionGrowthEvent());
			FileOutputStream fo = null;
			try {
				f.delete();
				String lastg = Long.toString(System.currentTimeMillis());
				fo = new FileOutputStream(f);
				fo.write(lastg.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fo.close();
				} catch (Exception e) {

				}
			}
		}

	}

	@Override
	public void run() {
		grow(false);
	}
}
