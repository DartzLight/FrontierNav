package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

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
				.mapToInt(this::computeMiranium)
				.sum();
	}

	public int getRevenue() {
		return mira.getSites()
				.stream()
				.mapToInt(this::computeRevenue)
				.sum();
	}

	public int getStorage() {
		return mira.getSites()
				.stream()
				.mapToInt(this::computeStorage)
				.sum() + DEFAULT_MIRANIUM_STORAGE;
	}

	private int computeMiranium(Site site) {
		int siteValue = site.miraniumRank().getBaseMiranium();
		Probe probe = getProbe(site);
		if (probe instanceof DuplicatorProbe) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return computeDuplicator(connectedSites, __ -> siteValue, FrontierNav::getMiraniumMultiplier, MiningProbe.class);
		}
		int siteMultiplier = getMiraniumMultiplier(probe);
		int boostMultipliers = computeBoostMultiplier(site, MiningProbe.class);
		int chainMultiplier = computeChainMultiplier(site, MiningProbe.class);
		return applyMultipliers(siteValue, siteMultiplier, chainMultiplier, boostMultipliers);
	}

	private static int getMiraniumMultiplier(Probe probe) {
		return probe.getMiraniumMultiplier();
	}

	private int computeRevenue(Site site) {
		int siteValue = site.revenueRank().getBaseRevenue();
		Probe probe = getProbe(site);
		if (probe instanceof DuplicatorProbe) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return computeDuplicator(connectedSites, __ -> siteValue, FrontierNav::getRevenueMultiplier, ResearchProbe.class)
					+ computeDuplicator(connectedSites, connected -> getRevenueBonus(getProbe(connected), site), __ -> 100, ResearchProbe.class);
		}
		int siteMultiplier = getRevenueMultiplier(probe);
		int boostMultipliers = computeBoostMultiplier(site, ResearchProbe.class);
		int chainMultiplier = computeChainMultiplier(site, ResearchProbe.class);
		int bonus = getRevenueBonus(probe, site);
		return applyMultipliers(siteValue, siteMultiplier, chainMultiplier, boostMultipliers)
				+ applyMultipliers(bonus, chainMultiplier, boostMultipliers);
	}

	private static int getRevenueMultiplier(Probe probe) {
		return probe.getRevenueMultiplier();
	}

	private static int getRevenueBonus(Probe probe, Site site) {
		return probe.getRevenueBonus() * site.unexploredTerritories();
	}

	private int computeStorage(Site site) {
		Probe probe = getProbe(site);
		if (probe instanceof DuplicatorProbe) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return computeDuplicator(connectedSites, connected -> getMiraniumStorage(getProbe(connected)), __ -> 100, StorageProbe.class);
		}
		int siteValue = getMiraniumStorage(probe);
		int boostMultipliers = computeBoostMultiplier(site, StorageProbe.class);
		int chainMultiplier = computeChainMultiplier(site, StorageProbe.class);
		return applyMultipliers(siteValue, chainMultiplier, boostMultipliers);
	}

	private static int getMiraniumStorage(Probe probe) {
		return probe.getMiraniumStorage();
	}

	private int computeDuplicator(Set<Site> connectedSites, Function<Site, Integer> valueRetriever, Function<Probe, Integer> siteMultiplierRetriever, Class<? extends Probe> multipliableProbeType) {
		int total = 0;
		for (Site connected : connectedSites) {
			Probe probe = getProbe(connected);
			if (!(probe instanceof DuplicatorProbe)) {
				int value = valueRetriever.apply(connected);
				int siteMultiplier = siteMultiplierRetriever.apply(probe);
				int boostMultiplier = computeBoostMultiplier(connected, multipliableProbeType);
				int chainMultiplier = computeChainMultiplier(connected, multipliableProbeType);
				total += applyMultipliers(value, siteMultiplier, boostMultiplier, chainMultiplier);
			}
		}
		return total;
	}

	private int computeChainMultiplier(Site site, Class<? extends Probe> multipliableProbeType) {
		if (canHaveMultiplier(site, multipliableProbeType)) {
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

	private int computeBoostMultiplier(Site site, Class<? extends Probe> multipliableProbeType) {
		if (canHaveMultiplier(site, multipliableProbeType)) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return connectedSites.stream()
					.mapToInt(this::computeBoostMultiplier)
					.reduce(100, (a, b) -> a * b / 100);
		}
		return 100;
	}

	private int computeBoostMultiplier(Site site) {
		Probe probe = getProbe(site);
		if (probe instanceof DuplicatorProbe) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return connectedSites.stream()
					.filter(connected -> !(getProbe(connected) instanceof DuplicatorProbe))
					.mapToInt(this::computeBoostMultiplier)
					.map(x -> x - 100)
					.sum() + 100;
		}
		return probe.getBoostMultiplier();
	}

	private boolean canHaveMultiplier(Site site, Class<? extends Probe> multipliableProbeType) {
		Probe probe = getProbe(site);
		if (probe instanceof DuplicatorProbe) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return connectedSites.stream()
					.filter(connected -> !(getProbe(connected) instanceof DuplicatorProbe))
					.anyMatch(connected -> canHaveMultiplier(connected, multipliableProbeType));
		}
		return multipliableProbeType.isInstance(probe);
	}

	private static int applyMultipliers(int siteValue, int... multipliers) {
		return IntStream.of(multipliers)
				.reduce(siteValue, (a, b) -> a * b / 100);
	}

}
