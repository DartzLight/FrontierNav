package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;

public class FrontierNavResult {
	private final Mira mira;
	private final ProbeLayout probeLayout;
	private final int miranium;
	private final int revenue;
	private final int storage;

	private FrontierNavResult(Mira mira, ProbeLayout probeLayout, int miranium, int revenue, int storage) {
		this.mira = mira;
		this.probeLayout = probeLayout;
		this.miranium = miranium;
		this.revenue = revenue;
		this.storage = storage;
	}

	public Mira getMira() {
		return mira;
	}

	public ProbeLayout getProbeLayout() {
		return probeLayout;
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

	public static FrontierNavResult compute(Mira mira, ProbeLayout probeLayout) {
		FrontierNav frontierNav = new FrontierNav(mira, probeLayout);
		int miranium = frontierNav.getMiranium();
		int revenue = frontierNav.getRevenue();
		int storage = frontierNav.getStorage();
		return new FrontierNavResult(mira, probeLayout, miranium, revenue, storage);
	}

}
