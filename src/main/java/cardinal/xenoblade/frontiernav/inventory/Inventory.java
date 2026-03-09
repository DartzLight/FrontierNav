package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;

import java.util.Collection;
import java.util.List;

public class Inventory {

	private final List<Probe> probes;

	public Inventory(Collection<Probe> probes) {
		this.probes = probes.stream()
				.filter(probe -> !(probe instanceof BasicProbe))
				.toList();
	}

	public List<Probe> getProbes() {
		return probes;
	}

	public MutableInventory getMutableInventory() {
		return new MutableInventory(probes);
	}

	public MutableInventory retrieveCurrentInventory(ProbeLayout probeLayout) {
		MutableInventory mutableInventory = getMutableInventory();
		probeLayout.probes().values().forEach(mutableInventory::removeProbe);
		return mutableInventory;
	}

}
