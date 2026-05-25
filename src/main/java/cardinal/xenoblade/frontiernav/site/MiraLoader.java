package cardinal.xenoblade.frontiernav.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiraLoader {
	private MiraLoader() {
	}

	private record SiteConnection(Site site1, Site site2) {}

	public static Mira loadMira(Path sitesPath, Path networkPath) throws IOException {
		Map<Integer, Site> sites = loadSites(sitesPath);
		List<SiteConnection> connections = loadNetwork(networkPath, sites);
		Mira.Builder builder = Mira.builder();
		sites.values().forEach(builder::addSite);
		connections.forEach(connection -> builder.addConnection(connection.site1, connection.site2));
		return builder.build();
	}

	private static Map<Integer, Site> loadSites(Path sitesPath) throws IOException {
		try (var lines = Files.lines(sitesPath)) {
			return lines.map(line -> line.split("\t"))
					.map(cells -> new Site(
							cells[0].transform(Integer::parseInt),
							MiraniumRank.of(cells[1]),
							RevenueRank.of(cells[2]),
							cells[3].transform(MiraLoader::parseUnexploredTerritories)
					))
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
