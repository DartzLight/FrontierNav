package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.site.PreciousResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class GeneticParametersLoader {

	private GeneticParametersLoader() {
	}

	public static GeneticParameters load(Path path) throws IOException {
		try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
			Properties properties = new Properties();
			properties.load(is);
			double miraniumCoef = readProperty(properties, "miraniumCoef").transform(Double::parseDouble);
			double revenueCoef = readProperty(properties, "revenueCoef").transform(Double::parseDouble);
			Map<PreciousResource, Double> preciousResourcesThresholds = readPreciousResourcesThresholds(properties, "threshold.");
			Map<PreciousResource, Double> preciousResourcesRatios = readPreciousResourcesRatios(properties, "ratio.");
			int iterations = readProperty(properties, "iterations").transform(Integer::parseInt);
			int initialPopulationSize = readProperty(properties, "initialPopulationSize").transform(Integer::parseInt);
			int selectionByElitismCount = readProperty(properties, "selectionByElitismCount").transform(Integer::parseInt);
			int selectionByTournamentCount = readProperty(properties, "selectionByTournamentCount").transform(Integer::parseInt);
			int tournamentSize = readProperty(properties, "tournamentSize").transform(Integer::parseInt);
			int crossoverOnSelectionCount = readProperty(properties, "crossoverOnSelectionCount").transform(Integer::parseInt);
			int crossoverOnRandomCount = readProperty(properties, "crossoverOnRandomCount").transform(Integer::parseInt);
			int mutationCount = readProperty(properties, "mutationCount").transform(Integer::parseInt);
			double mutationSwapRate = readProperty(properties, "mutationSwapRate").transform(Double::parseDouble);
			double mutationReplaceRate = readProperty(properties, "mutationReplaceRate").transform(Double::parseDouble);
			int randomInjectionCount = readProperty(properties, "randomInjectionCount").transform(Integer::parseInt);
			return new GeneticParameters(
					miraniumCoef,
					revenueCoef,
					preciousResourcesThresholds,
					preciousResourcesRatios,
					iterations,
					initialPopulationSize,
					selectionByElitismCount,
					selectionByTournamentCount,
					tournamentSize,
					crossoverOnSelectionCount,
					crossoverOnRandomCount,
					mutationCount,
					mutationSwapRate,
					mutationReplaceRate,
					randomInjectionCount
			);
		}
	}

	private static String readProperty(Properties properties, String propertyName) {
		return requireNonNull(properties.getProperty(propertyName), "missing property: " + propertyName);
	}

	private static Map<PreciousResource, Double> readPreciousResourcesThresholds(Properties properties, String propertyPrefix) {
		return Arrays.stream(PreciousResource.values())
				.flatMap(resource ->
						Optional.ofNullable(properties.getProperty(propertyPrefix + resource))
								.map(Double::parseDouble)
								.filter(value -> value > 0.0d)
								.map(threshold -> Map.entry(resource, threshold))
								.stream()
				)
				.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static Map<PreciousResource, Double> readPreciousResourcesRatios(Properties properties, String propertyPrefix) {
		return Arrays.stream(PreciousResource.values())
				.flatMap(resource ->
						Optional.ofNullable(properties.getProperty(propertyPrefix + resource))
								.map(Double::parseDouble)
								.filter(value -> value > 0.0d)
								.filter(value -> value <= 1.0d)
								.map(ratio -> Map.entry(resource, ratio))
								.stream()
				)
				.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
