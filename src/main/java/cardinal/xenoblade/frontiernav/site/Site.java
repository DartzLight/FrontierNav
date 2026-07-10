package cardinal.xenoblade.frontiernav.site;

import java.util.Map;

public record Site(
		int id,
		MiraniumRank miraniumRank,
		RevenueRank revenueRank,
		int unexploredTerritories,
		Map<PreciousResource, Double> preciousResources
) {

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank) {
		this(id, miraniumRank, revenueRank, 0);
	}

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank, int unexploredTerritories) {
		this(id, miraniumRank, revenueRank, unexploredTerritories, Map.of());
	}

}
