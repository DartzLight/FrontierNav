package cardinal.xenoblade.frontiernav.probe.layout;

import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ProbeParser;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class ProbeLayoutLoader {

	public static ProbeLayout loadProbes(Path probesPath, Mira mira) throws IOException {
		record SiteProbe(Site site, Probe probe) {}
		try (var lines = Files.lines(probesPath)) {
			Map<Site, Probe> probes = lines.map(line -> line.split("\t"))
					.map(cells -> new SiteProbe(
							mira.getSite(cells[0].transform(Integer::parseInt)),
							cells[1].transform(ProbeParser::parseProbe)
					))
					.collect(Collectors.toUnmodifiableMap(SiteProbe::site, SiteProbe::probe));
			return new ProbeLayout(probes);
		}
	}
}
