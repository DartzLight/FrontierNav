package cardinal.xenoblade.frontiernav.site;

public enum MiraniumRank {
	A(500),
	B(350),
	C(250);

	private final int baseMiranium;

	MiraniumRank(int baseMiranium) {
		this.baseMiranium = baseMiranium;
	}

	public int getBaseMiranium() {
		return baseMiranium;
	}
}
