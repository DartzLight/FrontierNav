package cardinal.xenoblade.frontiernav.probe;

public enum ResearchProbe implements Probe {
	G1(200, 2000),
	G2(250, 2500),
	G3(300, 3000),
	G4(350, 3500),
	G5(400, 4000),
	G6(450, 4500);

	private final int revenueMultiplier;
	private final int bonus;

	ResearchProbe(int revenueMultiplier, int bonus) {
		this.revenueMultiplier = revenueMultiplier;
		this.bonus = bonus;
	}

	@Override
	public int getMiraniumMultiplier() {
		return 30;
	}

	@Override
	public int getMiraniumStorage() {
		return 0;
	}

	@Override
	public int getRevenueMultiplier() {
		return revenueMultiplier;
	}

	@Override
	public int getRevenueBonus() {
		return bonus;
	}

	@Override
	public int getBoostMultiplier() {
		return 100;
	}
}
