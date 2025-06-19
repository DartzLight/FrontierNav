package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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

	private int compute(Site site, IntSupplier baseValue, Class<? extends Probe> probeType) {
		int chainMultiplier = computeChainMultiplier(site, probeType);
		int valueAfterChain = baseValue.getAsInt() * chainMultiplier / 100;
		List<Integer> boostMultipliers = computeBonusMultiplier(site, probeType);
		int valueAfterBoost = valueAfterChain;
		for (int boostMultiplier : boostMultipliers) {
			valueAfterBoost = valueAfterBoost * boostMultiplier / 100;
		}
		return valueAfterBoost;
	}

	private int computeChainMultiplier(Site site, Class<? extends Probe> siteProbeType) {
		if (siteProbeType.isInstance(getProbe(site))) {
			int chain = mira.computeChain(site, Collections.unmodifiableMap(probes));
			return getChainMultiplier(chain);
		}
		return 100;
	}

	private static int getChainMultiplier(int chain) {
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

	private List<Integer> computeBonusMultiplier(Site site, Class<? extends Probe> siteProbeType) {
		if (siteProbeType.isInstance(getProbe(site))) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return connectedSites.stream()
					.map(this::getProbe)
					.filter(BoosterProbe.class::isInstance)
					.map(Probe::getBoostMultiplier)
					.toList();
		}
		return List.of();
	}

}
