package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.Map;
import java.util.Set;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

public class FrontierNav {
	private static final int DEFAULT_MIRANIUM_STORAGE = 6000;

	private final Mira mira;
	private final Map<Site, Probe> probes;

	public FrontierNav(Mira mira, Map<Site, Probe> probes) {
		this.mira = mira;
		this.probes = Map.copyOf(probes);
	}

	public Mira getMira() {
		return mira;
	}

	public Map<Site, Probe> getProbes() {
		return probes;
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
		return compute(site, FrontierNav::getBaseMiranium, FrontierNav::getMiraniumMultiplier, (_, _) -> 0, MiningProbe.class);
	}

	private int computeRevenue(Site site) {
		return compute(site, FrontierNav::getBaseRevenue, FrontierNav::getRevenueMultiplier, FrontierNav::getRevenueBonus, ResearchProbe.class);
	}

	private int computeStorage(Site site) {
		return compute(site, _ -> 0, _ -> 0, (probe, _) -> getMiraniumStorage(probe), StorageProbe.class);
	}

	private static int getBaseMiranium(Site site) {
		return site.miraniumRank().getBaseMiranium();
	}

	private static int getBaseRevenue(Site site) {
		return site.revenueRank().getBaseRevenue();
	}

	private static int getMiraniumMultiplier(Probe probe) {
		return probe.getMiraniumMultiplier();
	}

	private static int getRevenueMultiplier(Probe probe) {
		return probe.getRevenueMultiplier();
	}

	private static int getRevenueBonus(Probe probe, Site site) {
		return probe.getRevenueBonus() * site.unexploredTerritories();
	}

	private static int getMiraniumStorage(Probe probe) {
		return probe.getMiraniumStorage();
	}

	private int compute(Site site, ToIntFunction<Site> siteValueRetriever, ToIntFunction<Probe> probeMultiplierRetriever, ToIntBiFunction<Probe, Site> bonusValueRetriever, Class<? extends Probe> multipliableProbeType) {
		int siteValue = siteValueRetriever.applyAsInt(site);
		Probe probe = getProbe(site);
		if (probe instanceof DuplicatorProbe) {
			Set<Site> connectedSites = mira.getConnectedSites(site);
			return computeDuplicator(site, connectedSites, _ -> siteValue, probeMultiplierRetriever, multipliableProbeType)
					+ computeDuplicator(site, connectedSites, connected -> bonusValueRetriever.applyAsInt(getProbe(connected), site), _ -> 100, multipliableProbeType);
		}
		int siteMultiplier = probeMultiplierRetriever.applyAsInt(probe);
		int bonus = bonusValueRetriever.applyAsInt(probe, site);
		int boostMultiplier = computeBoostMultiplier(site, probe, multipliableProbeType);
		int chainMultiplier = computeChainMultiplier(site, probe, multipliableProbeType);
		return applyMultipliers(siteValue, siteMultiplier, chainMultiplier, boostMultiplier)
				+ applyMultipliers(bonus, chainMultiplier, boostMultiplier);
	}

	private int computeDuplicator(Site site, Set<Site> connectedSites, ToIntFunction<Site> valueRetriever, ToIntFunction<Probe> siteMultiplierRetriever, Class<? extends Probe> multipliableProbeType) {
		return connectedSites.stream()
				.mapToInt(connected -> computeDuplicator(site, connected, valueRetriever, siteMultiplierRetriever, multipliableProbeType))
				.sum();
	}

	private int computeDuplicator(Site originSite, Site connectedSite, ToIntFunction<Site> valueRetriever, ToIntFunction<Probe> siteMultiplierRetriever, Class<? extends Probe> multipliableProbeType) {
		Probe probe = getProbe(connectedSite);
		if (probe instanceof DuplicatorProbe) {
			return 0;
		}
		int value = valueRetriever.applyAsInt(connectedSite);
		int siteMultiplier = siteMultiplierRetriever.applyAsInt(probe);
		int boostMultiplier = computeBoostMultiplier(originSite, probe, multipliableProbeType);
		int chainMultiplier = computeChainMultiplier(originSite, probe, multipliableProbeType);
		return applyMultipliers(value, siteMultiplier, boostMultiplier, chainMultiplier);
	}

	private int computeChainMultiplier(Site site, Probe probe, Class<? extends Probe> multipliableProbeType) {
		if (canHaveMultiplier(probe, multipliableProbeType)) {
			int chain = mira.computeChain(site, probes);
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
					.reduce(100, FrontierNav::applyMultiplier);
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
					.reduce(100, FrontierNav::applyMultiplier);
		}
		return probe.getBoostMultiplier();
	}

	private boolean canHaveMultiplier(Probe probe, Class<? extends Probe> multipliableProbeType) {
		return multipliableProbeType.isInstance(probe);
	}

	private static int applyMultipliers(int value, int... multipliers) {
		return IntStream.of(multipliers)
				.reduce(value, FrontierNav::applyMultiplier);
	}

	private static int applyMultiplier(int value, int multiplier) {
		return value * multiplier / 100;
	}

}
