package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ProbeLoader;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class FrontierNavLoader {

	public static FrontierNav loadFrontierNav(Path sitesPath, Path networkPath, Path probesPath) throws IOException {
		Mira mira = MiraLoader.loadMira(sitesPath, networkPath);
		Map<Site, Probe> probes = ProbeLoader.loadProbes(probesPath, mira);
		return new FrontierNav(mira, probes);
	}

	static void main() throws IOException {
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(Path.of("input/sites.tsv"), Path.of("input/network.tsv"), Path.of("input/probes.tsv"));
		FrontierNavResult result = FrontierNavResult.compute(frontierNav);
		System.out.println(result);
	}
}
