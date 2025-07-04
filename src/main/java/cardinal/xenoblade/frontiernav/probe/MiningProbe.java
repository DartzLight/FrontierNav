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

	private final int miraniumMultiplier;

	MiningProbe(int miraniumMultiplier) {
		this.miraniumMultiplier = miraniumMultiplier;
	}

	@Override
	public int getMiraniumMultiplier() {
		return miraniumMultiplier;
	}

	@Override
	public int getMiraniumStorage() {
		return 0;
	}

	@Override
	public int getRevenueMultiplier() {
		return 30;
	}

	@Override
	public int getRevenueBonus() {
		return 0;
	}

	@Override
	public int getBoostMultiplier() {
		return 100;
	}
}
