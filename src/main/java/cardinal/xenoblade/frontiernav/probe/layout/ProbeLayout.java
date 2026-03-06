package cardinal.xenoblade.frontiernav.probe.layout;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.site.Site;

import java.util.Map;

public record ProbeLayout(Map<Site, Probe> probes) {

	public ProbeLayout(Map<Site, Probe> probes) {
		this.probes = Map.copyOf(probes);
	}

	public Probe getProbe(Site site) {
		return probes.getOrDefault(site, BasicProbe.DEFAULT);
	}

}
