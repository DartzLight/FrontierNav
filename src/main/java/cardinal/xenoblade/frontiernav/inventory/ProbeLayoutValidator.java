package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.function.Predicate.not;

public class ProbeLayoutValidator {

	public static List<Probe> searchForInvalidProbes(ProbeLayout probeLayout, Inventory inventory) {
		List<Probe> availableProbes = new ArrayList<>(inventory.getProbes());
		return probeLayout.probes()
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(Site::id)))
				.map(Map.Entry::getValue)
				.filter(not(BasicProbe.class::isInstance))
				.filter(not(availableProbes::remove))
				.toList();
	}

}
