package cardinal.xenoblade.frontiernav.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiraLoader {
	private MiraLoader() {
	}

	private record SiteConnection(Site site1, Site site2) {}

	public static Mira loadMira(Path sitesPath, Path networkPath, Path resourcesPath) throws IOException {
		Map<PreciousResource, Map<Integer, Double>> resources = loadResources(resourcesPath);
		Map<Integer, Site> sites = loadSites(sitesPath, resources);
		List<SiteConnection> connections = loadNetwork(networkPath, sites);
		Mira.Builder builder = Mira.builder();
		sites.values().forEach(builder::addSite);
		connections.forEach(connection -> builder.addConnection(connection.site1, connection.site2));
		return builder.build();
	}

	private static Map<PreciousResource, Map<Integer, Double>> loadResources(Path resourcesPath) throws IOException {
		try (var lines = Files.lines(resourcesPath)) {
			return lines.map(line -> line.split("\t"))
					.collect(Collectors.toUnmodifiableMap(cells -> PreciousResource.of(cells[0]).orElseThrow(), cells -> parsePreciousResources(cells[1])));
		}
	}

	private static Map<Integer, Double> parsePreciousResources(String preciousResources) {
		return Arrays.stream(preciousResources.split("\\|"))
				.map(siteAndValue -> siteAndValue.split("="))
				.collect(Collectors.toUnmodifiableMap(siteAndValue -> siteAndValue[0].transform(Integer::parseInt), siteAndValue -> siteAndValue[1].transform(Double::parseDouble)));
	}

	private static Map<Integer, Site> loadSites(Path sitesPath, Map<PreciousResource, Map<Integer, Double>> resources) throws IOException {
		try (var lines = Files.lines(sitesPath)) {
			return lines.map(line -> line.split("\t"))
					.map(cells -> {
						Integer siteID = cells[0].transform(Integer::parseInt);
						return new Site(
								siteID,
								MiraniumRank.of(cells[1]),
								RevenueRank.of(cells[2]),
								cells[3].transform(MiraLoader::parseUnexploredTerritories),
								extractPreciousResources(resources, siteID)
						);
					})
					.collect(Collectors.toUnmodifiableMap(Site::id, Function.identity()));
		}
	}

	private static int parseUnexploredTerritories(String unexploredTerritories) {
		try {
			return Integer.parseInt(unexploredTerritories);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static Map<PreciousResource, Double> extractPreciousResources(Map<PreciousResource, Map<Integer, Double>> resources, Integer siteID) {
		return resources.entrySet()
				.stream()
				.filter(entry -> entry.getValue().containsKey(siteID))
				.collect(Collectors.toUnmodifiableMap(
						Map.Entry::getKey,
						entry -> entry.getValue().get(siteID)
				));
	}

	private static List<SiteConnection> loadNetwork(Path networkPath, Map<Integer, Site> sites) throws IOException {
		try (var lines = Files.lines(networkPath)) {
			return lines.map(line -> line.split("\t"))
					.flatMap(cells -> {
						Site site1 = sites.get(cells[0].transform(Integer::parseInt));
						Site site2 = sites.get(cells[1].transform(Integer::parseInt));
						if (site1 == null || site2 == null) {
							return Stream.empty();
						}
						return Stream.of(new SiteConnection(site1, site2));
					})
					.toList();
		}
	}
}
