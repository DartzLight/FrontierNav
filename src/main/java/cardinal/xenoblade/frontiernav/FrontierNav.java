package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.HashMap;
import java.util.function.IntSupplier;

public class FrontierNav {
	private static final int DEFAULT_MIRANIUM_STORAGE = 6000;
	
	private final Mira mira;
	private final HashMap<Site, Probe> probes = new HashMap<>();

	public FrontierNav(Mira mira) {
		this.mira = mira;
	}

	public void addProbe(Site site, Probe probe) {
		probes.put(site, probe);
	}

	private Probe getProbe(Site site) {
		return probes.getOrDefault(site, BasicProbe.DEFAULT);
	}

	public int getMiranium() {
		return mira.getSites()
				.stream()
				.mapToInt(site -> compute(site, () -> site.getMiranium(getProbe(site)), MiningProbe.class))
				.sum();
	}

	public int getRevenue() {
		return mira.getSites()
				.stream()
				.mapToInt(site -> compute(site, () -> site.getRevenue(getProbe(site)), ResearchProbe.class))
				.sum();
	}

	public int getMiraniumStorage() {
		return mira.getSites()
				.stream()
				.mapToInt(site -> compute(site, () -> getProbe(site).getMiraniumStorage(), StorageProbe.class))
				.sum() + DEFAULT_MIRANIUM_STORAGE;
	}

	private int compute(Site site, IntSupplier value, Class<? extends Probe> comboProbeType) {
		int comboMultiplier;
		if (comboProbeType.isInstance(getProbe(site))) {
			int chain = mira.computeChain(site, probes);
			comboMultiplier = getComboMultiplier(chain);
		} else {
			comboMultiplier = 100;
		}
		return value.getAsInt() * comboMultiplier / 100;
	}

	private static int getComboMultiplier(int chain) {
		if (chain >= 8) {
			return 180;
		}
		if (chain >= 5) {
			return 150;
		}
		if (chain >= 3) {
			return 130;
		}
		return 100;
	}

}
