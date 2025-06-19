package cardinal.xenoblade.frontiernav.probe;

public enum StorageProbe implements Probe {
	DEFAULT;

	@Override
	public int getMiraniumMultiplicator() {
		return 10;
	}

	@Override
	public int getMiraniumStorage() {
		return 3000;
	}

	@Override
	public int getRevenueMultiplicator() {
		return 10;
	}

	@Override
	public int getRevenueBonus() {
		return 0;
	}

}
