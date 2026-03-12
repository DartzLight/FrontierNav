package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayoutLoader;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;

import java.io.IOException;
import java.nio.file.Path;

public class FrontierNavLoader {

	public static FrontierNav loadFrontierNav(Path sitesPath, Path networkPath, Path probesPath) throws IOException {
		Mira mira = MiraLoader.loadMira(sitesPath, networkPath);
		ProbeLayout probeLayout = ProbeLayoutLoader.loadProbes(probesPath, mira);
		return new FrontierNav(mira, probeLayout);
	}

	static void main() throws IOException {
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(Path.of("input/sites.tsv"), Path.of("input/network.tsv"), Path.of("input/probes.tsv"));
		FrontierNavResult result = FrontierNavResult.compute(frontierNav.getMira(), frontierNav.getProbeLayout());
		System.out.println(result);
	}
}
