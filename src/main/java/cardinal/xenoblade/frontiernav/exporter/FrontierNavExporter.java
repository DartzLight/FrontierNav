package cardinal.xenoblade.frontiernav.exporter;

import cardinal.xenoblade.frontiernav.FrontierNav;
import cardinal.xenoblade.frontiernav.FrontierNavLoader;
import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FrontierNavExporter {

	private static final Map<? extends Probe, Integer> PROBES_IDS = Map.ofEntries(
			Map.entry(BasicProbe.DEFAULT, 1),
			Map.entry(MiningProbe.G1, 2),
			Map.entry(MiningProbe.G2, 3),
			Map.entry(MiningProbe.G3, 4),
			Map.entry(MiningProbe.G4, 5),
			Map.entry(MiningProbe.G5, 6),
			Map.entry(MiningProbe.G6, 7),
			Map.entry(MiningProbe.G7, 8),
			Map.entry(MiningProbe.G8, 9),
			Map.entry(MiningProbe.G9, 10),
			Map.entry(MiningProbe.G10, 11),
			Map.entry(ResearchProbe.G1, 12),
			Map.entry(ResearchProbe.G2, 13),
			Map.entry(ResearchProbe.G3, 14),
			Map.entry(ResearchProbe.G4, 15),
			Map.entry(ResearchProbe.G5, 16),
			Map.entry(ResearchProbe.G6, 17),
			Map.entry(BoosterProbe.G1, 18),
			Map.entry(BoosterProbe.G2, 19),
			Map.entry(DuplicatorProbe.DEFAULT, 20),
			Map.entry(StorageProbe.DEFAULT, 21)
	);

	private FrontierNavExporter() {
	}

	public static String exportToString(FrontierNav frontierNav) {
		return exportToString(frontierNav.getMira(), frontierNav.getProbes());
	}

	public static String exportToString(Mira mira, Map<Site, Probe> probes) {
		TreeMap<Integer, Integer> export = new TreeMap<>();
		fillValues(export, probes);
		fillMissingValues(export, mira);

		return export.entrySet()
				.stream()
				.map(entry -> entry.getKey() + "-" + entry.getValue())
				.collect(Collectors.joining("~"));
	}

	private static void fillValues(TreeMap<Integer, Integer> export, Map<Site, Probe> probes) {
		probes.forEach((key, value) -> export.put(key.id(), mapToProbeID(value)));
	}

	private static Integer mapToProbeID(Probe value) {
		return PROBES_IDS.get(value);
	}

	private static void fillMissingValues(TreeMap<Integer, Integer> export, Mira mira) {
		Set<Integer> sites = mira.getSites()
				.stream()
				.map(Site::id)
				.collect(Collectors.toSet());
		Stream.of(IntStream.rangeClosed(101, 121),
						IntStream.rangeClosed(201, 225),
						IntStream.rangeClosed(301, 322),
						IntStream.rangeClosed(401, 420),
						IntStream.rangeClosed(501, 516))
				.flatMapToInt(Function.identity())
				.filter(siteID -> !export.containsKey(siteID))
				.forEach(siteID -> {
					if (sites.contains(siteID)) {
						export.put(siteID, 1);
					} else {
						export.put(siteID, 0);
					}
				});
	}

	static void main() throws IOException {
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(Path.of("input/sites.tsv"), Path.of("input/network.tsv"), Path.of("input/probes.tsv"));
		System.out.println(exportToString(frontierNav));
	}

}
