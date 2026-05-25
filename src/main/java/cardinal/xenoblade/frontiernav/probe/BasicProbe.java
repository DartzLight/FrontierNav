package cardinal.xenoblade.frontiernav.probe;

public enum BasicProbe implements Probe {
	DEFAULT;

	@Override
	public int getMiraniumMultiplier() {
		return 50;
	}

	@Override
	public int getMiraniumStorage() {
		return 0;
	}

	@Override
	public int getRevenueMultiplier() {
		return 50;
	}

	@Override
	public int getRevenueBonus() {
		return 0;
	}

	@Override
	public int getBoostMultiplier() {
		return 100;
	}

	@Override
	public String toString() {
		return "Basic";
	}
}
