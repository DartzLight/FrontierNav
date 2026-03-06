package cardinal.xenoblade.frontiernav.site;

import java.util.Set;

public record Site(
		int id,
		MiraniumRank miraniumRank,
		RevenueRank revenueRank,
		int unexploredTerritories,
		Set<PreciousResource> preciousResources
) {

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank) {
		this(id, miraniumRank, revenueRank, 0);
	}

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank, int unexploredTerritories) {
		this(id, miraniumRank, revenueRank, unexploredTerritories, Set.of());
	}

}
