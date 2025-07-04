package cardinal.xenoblade.frontiernav.site;

public record Site(
		int id,
		MiraniumRank miraniumRank,
		RevenueRank revenueRank,
		int unexploredTerritories
) {

	public Site(int id, MiraniumRank miraniumRank, RevenueRank revenueRank) {
		this(id, miraniumRank, revenueRank, 0);
	}

}
