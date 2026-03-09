package cardinal.xenoblade.frontiernav.optimization.random;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.MutableInventory;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProbeLayoutGenerator {
	private final Mira mira;
	private final Inventory inventory;
	private final Random random;

	public ProbeLayoutGenerator(Mira mira, Inventory inventory, Random random) {
		this.mira = mira;
		this.inventory = inventory;
		this.random = random;
	}

	public ProbeLayout generateRandom() {
		MutableInventory mutableInventory = inventory.getMutableInventory();
		Map<Site, Probe> probes = new HashMap<>();
		mira.getSites()
				.stream()
				.sorted(Comparator.comparing(Site::id))
				.forEach(site -> probes.put(site, mutableInventory.takeRandomProbe(random)));
		return new ProbeLayout(probes);
	}
}
