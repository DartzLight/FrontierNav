package cardinal.xenoblade.frontiernav.probe;

public enum MiningProbe implements Probe {
	G1(100),
	G2(120),
	G3(140),
	G4(160),
	G5(180),
	G6(200),
	G7(220),
	G8(240),
	G9(270),
	G10(300);

	private final int miraniumMultiplicator;

	MiningProbe(int miraniumMultiplicator) {
		this.miraniumMultiplicator = miraniumMultiplicator;
	}

	@Override
	public int getMiraniumMultiplicator() {
		return miraniumMultiplicator;
	}

	@Override
	public int getRevenueMultiplicator() {
		return 30;
	}
}
