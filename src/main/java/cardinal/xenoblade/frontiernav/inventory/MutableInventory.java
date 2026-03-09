package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;

import java.util.*;

public class MutableInventory {
	private final List<Probe> probes;

	public MutableInventory(Collection<Probe> probes) {
		this.probes = new ArrayList<>(probes);
	}

	public List<Probe> getAvailableProbes() {
		return Collections.unmodifiableList(probes);
	}

	public Probe takeRandomProbe(Random random) {
		if (probes.isEmpty()) {
			return BasicProbe.DEFAULT;
		}
		return probes.remove(random.nextInt(probes.size()));
	}
}
