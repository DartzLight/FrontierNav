package cardinal.xenoblade.frontiernav.probe;

public enum DuplicatorProbe implements Probe {
	DEFAULT;

	@Override
	public int getMiraniumMultiplier() {
		throw new UnsupportedOperationException("Duplicator probe does not have its own miranium multiplier");
	}

	@Override
	public int getMiraniumStorage() {
		throw new UnsupportedOperationException("Duplicator probe does not have its own miranium storage");
	}

	@Override
	public int getRevenueMultiplier() {
		throw new UnsupportedOperationException("Duplicator probe does not have its own revenue multiplier");
	}

	@Override
	public int getRevenueBonus() {
		throw new UnsupportedOperationException("Duplicator probe does not have its own revenue bonus");
	}

	@Override
	public int getBoostMultiplier() {
		throw new UnsupportedOperationException("Duplicator probe does not have its own boost multiplier");
	}

	@Override
	public String toString() {
		return "Duplicator";
	}
}