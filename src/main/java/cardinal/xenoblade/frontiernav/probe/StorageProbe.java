package cardinal.xenoblade.frontiernav.probe;

public enum StorageProbe implements Probe {
	DEFAULT;

	@Override
	public int getMiraniumMultiplier() {
		return 10;
	}

	@Override
	public int getMiraniumStorage() {
		return 3000;
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
		return 100;
	}

}
