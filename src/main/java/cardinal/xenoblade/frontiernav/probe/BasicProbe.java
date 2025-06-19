package cardinal.xenoblade.frontiernav.probe;

public enum BasicProbe implements Probe {
	DEFAULT;

	@Override
	public int getMiraniumMultiplicator() {
		return 50;
	}

	@Override
	public int getMiraniumStorage() {
		return 0;
	}

	@Override
	public int getRevenueMultiplicator() {
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
}
