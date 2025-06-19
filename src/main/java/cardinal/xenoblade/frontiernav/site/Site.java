package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.Probe;

public record Site(
		int id,
		MiraniumRank miraniumRank,
		RevenueRank revenueRank,
		int unexploredTerritories,
		Probe probe
) {

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank, Probe probe) {
		this(id, miraniumRank, revenueRank, 0, probe);
	}

	public int getMiranium() {
		return miraniumRank.getBaseMiranium() * probe.getMiraniumMultiplicator() / 100;
	}

	public int getRevenue() {
		int bonus = probe.bonus() * unexploredTerritories;
		return revenueRank.getBaseRevenue() * probe.getRevenueMultiplicator() / 100 + bonus;
	}

}
