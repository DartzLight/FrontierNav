package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FrontierNavLoader {

	private record SiteProbe(Site site, Probe probe) {}

	public static FrontierNav loadFrontierNav(Mira mira, Path probesPath) throws IOException {
		Map<Integer, Site> sitesById = mira.getSitesByID();
		List<SiteProbe> probes = loadProbes(probesPath, sitesById);
		FrontierNav frontierNav = new FrontierNav(mira);
		probes.forEach(x -> frontierNav.addProbe(x.site, x.probe));
		return frontierNav;
	}

	private static List<SiteProbe> loadProbes(Path probesPath, Map<Integer, Site> siteById) throws IOException {
		try (var lines = Files.lines(probesPath)) {
			return lines.map(line -> line.split("\t"))
					.map(cells -> new SiteProbe(
							siteById.get(cells[0].transform(Integer::parseInt)),
							cells[1].transform(FrontierNavLoader::parseProbe)
					))
					.toList();
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
			default -> throw new UncheckedIOException(new IOException("Unsupported probe: " + string));
		};
	}
}
