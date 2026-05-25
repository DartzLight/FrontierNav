package cardinal.xenoblade.frontiernav.optimization.genetic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

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

}
