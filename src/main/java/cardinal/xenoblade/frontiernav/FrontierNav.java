package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FrontierNav {
	private static final int DEFAULT_MIRANIUM_STORAGE = 6000;

	private final Mira mira;
	private final Map<Site, Probe> probes = new HashMap<>();

	public Mira getMira() {
		return mira;
	}

	public FrontierNav(Mira mira) {
		this.mira = mira;
	}

	public void addProbe(Site site, Probe probe) {
		if (probe instanceof BasicProbe) {
			return;
		}
		probes.put(site, probe);
	}

	private Probe getProbe(Site site) {
		return probes.getOrDefault(site, BasicProbe.DEFAULT);
	}

	public Map<Site, Probe> getProbes() {
		return Map.copyOf(probes);
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
			return computeDuplicator(site, connectedSites, _ -> siteValue, FrontierNav::getMiraniumMultiplier, MiningProbe.class);
		}
		int siteMultiplier = getMiraniumMultiplier(probe);
		int boostMultipliers = computeBoostMultiplier(site, probe, MiningProbe.class);
		int chainMultiplier = computeChainMultiplier(site, probe, MiningProbe.class);
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
			return computeDuplicator(site, connectedSites, _ -> siteValue, FrontierNav::getRevenueMultiplier, ResearchProbe.class)
					+ computeDuplicator(site, connectedSites, connected -> getRevenueBonus(getProbe(connected), site), _ -> 100, ResearchProbe.class);
		}
		int siteMultiplier = getRevenueMultiplier(probe);
		int boostMultipliers = computeBoostMultiplier(site, probe, ResearchProbe.class);
		int chainMultiplier = computeChainMultiplier(site, probe, ResearchProbe.class);
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
			return computeDuplicator(site, connectedSites, connected -> getMiraniumStorage(getProbe(connected)), _ -> 100, StorageProbe.class);
		}
		int siteValue = getMiraniumStorage(probe);
		int boostMultipliers = computeBoostMultiplier(site, probe, StorageProbe.class);
		int chainMultiplier = computeChainMultiplier(site, probe, StorageProbe.class);
		return applyMultipliers(siteValue, chainMultiplier, boostMultipliers);
	}

	private static int getMiraniumStorage(Probe probe) {
		return probe.getMiraniumStorage();
	}

	private int computeDuplicator(Site site, Set<Site> connectedSites, Function<Site, Integer> valueRetriever, Function<Probe, Integer> siteMultiplierRetriever, Class<? extends Probe> multipliableProbeType) {
		int total = 0;
		for (Site connected : connectedSites) {
			Probe probe = getProbe(connected);
			if (!(probe instanceof DuplicatorProbe)) {
				int value = valueRetriever.apply(connected);
				int siteMultiplier = siteMultiplierRetriever.apply(probe);
				int boostMultiplier = computeBoostMultiplier(site, probe, multipliableProbeType);
				int chainMultiplier = computeChainMultiplier(site, probe, multipliableProbeType);
				total += applyMultipliers(value, siteMultiplier, boostMultiplier, chainMultiplier);
			}
		}
		return total;
	}

	private int computeChainMultiplier(Site site, Probe probe, Class<? extends Probe> multipliableProbeType) {
		if (canHaveMultiplier(probe, multipliableProbeType)) {
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

	private int computeBoostMultiplier(Site site, Probe probe, Class<? extends Probe> multipliableProbeType) {
		if (canHaveMultiplier(probe, multipliableProbeType)) {
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

	private boolean canHaveMultiplier(Probe probe, Class<? extends Probe> multipliableProbeType) {
		return multipliableProbeType.isInstance(probe);
	}

	private static int applyMultipliers(int siteValue, int... multipliers) {
		return IntStream.of(multipliers)
				.reduce(siteValue, (a, b) -> a * b / 100);
	}

}
