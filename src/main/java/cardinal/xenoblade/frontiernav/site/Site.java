package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.Probe;

public record Site(
		int id,
		MiraniumRank miraniumRank,
		RevenueRank revenueRank,
		Probe probe
) {

	public int getMiranium() {
		return miraniumRank.getBaseMiranium() * probe.getMiraniumMultiplicator() / 100;
	}

	public int getRevenue() {
		return revenueRank.getBaseRevenue() * probe.getRevenueMultiplicator() / 100;
	}
	
}
