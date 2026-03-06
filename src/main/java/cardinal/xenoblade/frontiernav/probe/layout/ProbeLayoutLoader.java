package cardinal.xenoblade.frontiernav.probe.layout;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.io.UncheckedIOException;
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
							cells[1].transform(ProbeLayoutLoader::parseProbe)
					))
					.collect(Collectors.toUnmodifiableMap(SiteProbe::site, SiteProbe::probe));
			return new ProbeLayout(probes);
		}
	}

	private static Probe parseProbe(String string) {
		return switch (string) {
			case "M1" -> MiningProbe.G1;
			case "M2" -> MiningProbe.G2;
			case "M3" -> MiningProbe.G3;
			case "M4" -> MiningProbe.G4;
			case "M5" -> MiningProbe.G5;
			case "M6" -> MiningProbe.G6;
			case "M7" -> MiningProbe.G7;
			case "M8" -> MiningProbe.G8;
			case "M9" -> MiningProbe.G9;
			case "M10" -> MiningProbe.G10;
			case "R1" -> ResearchProbe.G1;
			case "R2" -> ResearchProbe.G2;
			case "R3" -> ResearchProbe.G3;
			case "R4" -> ResearchProbe.G4;
			case "R5" -> ResearchProbe.G5;
			case "R6" -> ResearchProbe.G6;
			case "S" -> StorageProbe.DEFAULT;
			case "B1" -> BoosterProbe.G1;
			case "B2" -> BoosterProbe.G2;
			case "D" -> DuplicatorProbe.DEFAULT;
			case "-" -> BasicProbe.DEFAULT;
			default -> throw new UncheckedIOException(new IOException("Unsupported probe: " + string));
		};
	}
}
