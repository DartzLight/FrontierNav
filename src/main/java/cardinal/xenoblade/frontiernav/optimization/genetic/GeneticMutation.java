package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.MutableInventory;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.*;

public class GeneticMutation implements Mutation {

	private final Random random;
	private final double swapRate;
	private final double replaceRate;

	public GeneticMutation(Random random, double swapRate, double replaceRate) {
		this.random = random;
		this.swapRate = swapRate;
		this.replaceRate = replaceRate;
	}

	@Override
	public ProbeLayout mutation(Mira mira, ProbeLayout origin, Inventory inventory) {
		List<Site> sites = new ArrayList<>(mira.getSites());
		Map<Site, Probe> probes = new HashMap<>(origin.probes());
		MutableInventory currentInventory = inventory.retrieveCurrentInventory(origin);
		for (var site : sites) {
			if (shouldSwap()) {
				Site anotherSite = getRandomSite(sites);
				swapProbes(site, anotherSite, probes);
			}
			if (shouldReplace()) {
				replaceProbe(site, currentInventory, probes);
			}
		}
		return new ProbeLayout(probes);
	}

	private boolean shouldSwap() {
		return random.nextDouble() < swapRate;
	}

	private boolean shouldReplace() {
		return random.nextDouble() < replaceRate;
	}

	private Site getRandomSite(List<Site> sites) {
		return sites.get(random.nextInt(sites.size()));
	}

	private static void swapProbes(Site site1, Site site2, Map<Site, Probe> probes) {
		Probe probe1 = probes.get(site1);
		Probe probe2 = probes.get(site2);
		probes.put(site1, probe2);
		probes.put(site2, probe1);
	}

	private void replaceProbe(Site site, MutableInventory currentInventory, Map<Site, Probe> probes) {
		Probe probeBeforeSwap = probes.get(site);
		Probe probeAfterSwap = currentInventory.swapRandomProbe(probeBeforeSwap, random);
		probes.put(site, probeAfterSwap);
	}

}
