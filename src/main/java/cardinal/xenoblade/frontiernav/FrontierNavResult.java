package cardinal.xenoblade.frontiernav;

public class FrontierNavResult {
	private final int miranium;
	private final int revenue;
	private final int storage;

	private FrontierNavResult(int miranium, int revenue, int storage) {
		this.miranium = miranium;
		this.revenue = revenue;
		this.storage = storage;
	}

	public int getMiraniumProduction() {
		return miranium;
	}

	public int getEffectiveMiranium() {
		return Math.min(miranium, storage);
	}

	public int getRevenue() {
		return revenue;
	}

	public int getStorage() {
		return storage;
	}

	@Override
	public String toString() {
		return "Miranium: " + miranium + "/" + storage + " | Revenue: " + revenue;
	}

	public static FrontierNavResult compute(FrontierNav frontierNav) {
		int miranium = frontierNav.getMiranium();
		int revenue = frontierNav.getRevenue();
		int storage = frontierNav.getStorage();
		return new FrontierNavResult(miranium, revenue, storage);
	}
}
