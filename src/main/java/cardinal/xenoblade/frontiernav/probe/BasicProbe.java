package cardinal.xenoblade.frontiernav.probe;

public enum BasicProbe implements Probe {
	DEFAULT;

	@Override
	public int getMiraniumMultiplicator() {
		return 50;
	}

	@Override
	public int getRevenueMultiplicator() {
		return 50;
	}

	@Override
	public int bonus() {
		return 0;
	}
}
