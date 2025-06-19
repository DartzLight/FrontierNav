package cardinal.xenoblade.frontiernav.site;

public enum RevenueRank {
	S(850),
	A(750),
	B(650),
	C(550),
	D(450),
	E(300),
	F(200);

	private final int baseRevenue;

	RevenueRank(int baseRevenue) {
		this.baseRevenue = baseRevenue;
	}

	public int getBaseRevenue() {
		return baseRevenue;
	}
}
