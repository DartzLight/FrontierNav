package cardinal.xenoblade.frontiernav.serialization;

import cardinal.xenoblade.frontiernav.FrontierNav;
import cardinal.xenoblade.frontiernav.FrontierNavLoader;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FrontierNavExporter {

	private FrontierNavExporter() {
	}

	public static String exportToString(Mira mira, ProbeLayout probeLayout) {
		TreeMap<Integer, Integer> export = new TreeMap<>();
		fillValues(export, probeLayout);
		fillMissingValues(export, mira);

		return export.entrySet()
				.stream()
				.map(entry -> entry.getKey() + ImportExportFormat.PROBE_TO_SITE_DELIMITER + entry.getValue())
				.collect(Collectors.joining(ImportExportFormat.SITES_DELIMITER));
	}

	private static void fillValues(TreeMap<Integer, Integer> export, ProbeLayout probeLayout) {
		probeLayout.probes().forEach((key, value) -> export.put(key.id(), mapToProbeID(value)));
	}

	private static Integer mapToProbeID(Probe value) {
		return ImportExportFormat.PROBE_TO_ID_MAP.get(value);
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
		System.out.println(exportToString(frontierNav.getMira(), frontierNav.getProbeLayout()));
	}

}
