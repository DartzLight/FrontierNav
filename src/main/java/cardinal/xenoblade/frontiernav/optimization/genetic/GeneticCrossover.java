package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.MutableInventory;
import cardinal.xenoblade.frontiernav.inventory.ProbeLayoutValidator;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.*;

public class GeneticCrossover implements Crossover {

	private final Random random;

	public GeneticCrossover(Random random) {
		this.random = random;
	}

	@Override
	public ProbeLayout crossover(Mira mira, ProbeLayout parent1, ProbeLayout parent2, Inventory inventory) {
		var crossoverProbes = new HashMap<Site, Probe>();
		for (var site : mira.getSites()) {
			Probe probe = takeRandomProbeFromOneParent(site, parent1, parent2);
			crossoverProbes.put(site, probe);
		}
		repairProbeLayout(mira, inventory, crossoverProbes);
		return new ProbeLayout(crossoverProbes);
	}

	private Probe takeRandomProbeFromOneParent(Site site, ProbeLayout parent1, ProbeLayout parent2) {
		if (random.nextBoolean()) {
			return parent1.getProbe(site);
		} else {
			return parent2.getProbe(site);
		}
	}

	private void repairProbeLayout(Mira mira, Inventory inventory, Map<Site, Probe> crossoverProbes) {
		removeInvalidProbes(inventory, crossoverProbes);
		replaceMissingProbes(mira, inventory, crossoverProbes);
	}

	private void removeInvalidProbes(Inventory inventory, Map<Site, Probe> crossoverProbes) {
		List<Probe> invalidProbes = ProbeLayoutValidator.searchForInvalidProbes(new ProbeLayout(crossoverProbes), inventory);
		for (var invalidProbe : invalidProbes) {
			List<Site> invalidCandidates = crossoverProbes.entrySet()
					.stream()
					.filter(entry -> entry.getValue().equals(invalidProbe))
					.map(Map.Entry::getKey)
					.sorted(Comparator.comparing(Site::id))
					.toList();
			Site invalidSite = invalidCandidates.get(random.nextInt(invalidCandidates.size()));
			crossoverProbes.remove(invalidSite);
		}
	}

	private void replaceMissingProbes(Mira mira, Inventory inventory, Map<Site, Probe> crossoverProbes) {
		MutableInventory currentInventory = inventory.retrieveCurrentInventory(new ProbeLayout(crossoverProbes));
		for (var site : mira.getSites()) {
			if (!crossoverProbes.containsKey(site)) {
				Probe replacement = currentInventory.takeRandomProbe(random);
				crossoverProbes.put(site, replacement);
			}
		}
	}

}
