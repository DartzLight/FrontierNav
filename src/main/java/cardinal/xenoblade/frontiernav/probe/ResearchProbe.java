package cardinal.xenoblade.frontiernav.probe;

public enum ResearchProbe implements Probe {
	G1(200),
	G2(250),
	G3(300),
	G4(350),
	G5(400),
	G6(450);

	private final int revenueMultiplicator;

	ResearchProbe(int revenueMultiplicator) {
		this.revenueMultiplicator = revenueMultiplicator;
	}

	@Override
	public int getMiraniumMultiplicator() {
		return 30;
	}

	@Override
	public int getRevenueMultiplicator() {
		return revenueMultiplicator;
	}
}
