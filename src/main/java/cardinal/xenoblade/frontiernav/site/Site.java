package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.Probe;

public record Site(
		int id,
		MiraniumRank miraniumRank,
		RevenueRank revenueRank,
		int unexploredTerritories
) {

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank) {
		this(id, miraniumRank, revenueRank, 0);
	}

	public int getMiranium(Probe probe) {
		return miraniumRank.getBaseMiranium() * probe.getMiraniumMultiplicator() / 100;
	}

	public int getRevenue(Probe probe) {
		int bonus = probe.getRevenueBonus() * unexploredTerritories;
		return revenueRank.getBaseRevenue() * probe.getRevenueMultiplicator() / 100 + bonus;
	}
	
}
