package cardinal.xenoblade.frontiernav.probe;

public enum BoosterProbe implements Probe {
	G1(150),
	G2(200);

	private final int boostMultiplier;

	BoosterProbe(int boostMultiplier) {
		this.boostMultiplier = boostMultiplier;
	}

	@Override
	public int getMiraniumMultiplier() {
		return 10;
	}

	@Override
	public int getMiraniumStorage() {
		return 0;
	}

	@Override
	public int getRevenueMultiplier() {
		return 10;
	}

	@Override
	public int getRevenueBonus() {
		return 0;
	}

	@Override
	public int getBoostMultiplier() {
		return boostMultiplier;
	}
}
