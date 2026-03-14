package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.List;
import java.util.Set;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class FrontierNav {
	private static final int DEFAULT_MIRANIUM_STORAGE = 6000;

	private final Mira mira;
	private final ProbeLayout probeLayout;

	public FrontierNav(Mira mira, ProbeLayout probeLayout) {
		this.mira = mira;
		this.probeLayout = probeLayout;
	}

	public Mira getMira() {
		return mira;
	}

	public ProbeLayout getProbeLayout() {
		return probeLayout;
	}

	private Probe getRealProbe(Site site) {
		return probeLayout.getProbe(site);
	}

	private List<Probe> getEffectiveProbes(Site site) {
		Probe probe = getRealProbe(site);
		if (probe instanceof DuplicatorProbe) {
			return mira.getConnectedSites(site)
					.stream()
					.map(this::getRealProbe)
					.filter(not(DuplicatorProbe.class::isInstance))
					.toList();
		}
		return List.of(probe);
	}

	public int computeMiranium() {
		return mira.getSites()
				.stream()
				.mapToInt(this::computeMiranium)
				.sum();
	}

	public int computeRevenue() {
		return mira.getSites()
				.stream()
				.mapToInt(this::computeRevenue)
				.sum();
	}

	public int computeStorage() {
		return mira.getSites()
				.stream()
				.mapToInt(this::computeStorage)
				.sum() + DEFAULT_MIRANIUM_STORAGE;
	}

	public int computeMiranium(Site site) {
		return compute(site, FrontierNav::getBaseMiranium, FrontierNav::getMiraniumMultiplier, (_, _) -> 0, MiningProbe.class);
	}

	public int computeRevenue(Site site) {
		return compute(site, FrontierNav::getBaseRevenue, FrontierNav::getRevenueMultiplier, FrontierNav::getRevenueBonus, ResearchProbe.class);
	}

	public int computeStorage(Site site) {
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
		List<Probe> probes = getEffectiveProbes(site);
		return probes.stream()
				.mapToInt(probe -> computeEffective(site, probe, siteValue, probeMultiplierRetriever, bonusValueRetriever, multipliableProbeType))
				.sum();
	}

	private int computeEffective(Site site, Probe probe, int siteValue, ToIntFunction<Probe> probeMultiplierRetriever, ToIntBiFunction<Probe, Site> bonusValueRetriever, Class<? extends Probe> multipliableProbeType) {
		int siteMultiplier = probeMultiplierRetriever.applyAsInt(probe);
		int bonus = bonusValueRetriever.applyAsInt(probe, site);
		int boostMultiplier = computeBoostMultiplier(site, probe, multipliableProbeType);
		int chainMultiplier = computeChainMultiplier(site, probe, multipliableProbeType);
		return applyMultipliers(siteValue, siteMultiplier, chainMultiplier, boostMultiplier)
				+ applyMultipliers(bonus, chainMultiplier, boostMultiplier);
	}

	private int computeChainMultiplier(Site site, Probe probe, Class<? extends Probe> multipliableProbeType) {
		if (canHaveMultiplier(probe, multipliableProbeType)) {
			return computeChainMultiplier(site);
		}
		return 100;
	}

	public int computeChainMultiplier(Site site) {
		int chain = mira.computeChain(site, probeLayout);
		return getChainMultiplier(chain);
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
			return computeIncomingBoostMultiplier(site);
		}
		return 100;
	}

	public int computeIncomingBoostMultiplier(Site site) {
		Set<Site> connectedSites = mira.getConnectedSites(site);
		return connectedSites.stream()
				.mapToInt(this::computeOutgoingBoostMultiplier)
				.reduce(100, FrontierNav::applyMultiplier);
	}

	public int computeOutgoingBoostMultiplier(Site site) {
		List<Probe> probes = getEffectiveProbes(site);
		int boostMultiplier = probes.stream()
				.mapToInt(Probe::getBoostMultiplier)
				.reduce(100, FrontierNav::applyMultiplier);
		if (boostMultiplier > 100) {
			int chainMultiplier = computeChainMultiplier(site);
			return applyMultiplier(chainMultiplier, boostMultiplier);
		}
		return boostMultiplier;
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
