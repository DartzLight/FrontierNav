package cardinal.xenoblade.frontiernav.optimization.genetic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class GeneticParametersLoader {

	public static GeneticParameters load(Path path) throws IOException {
		try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
			Properties properties = new Properties();
			properties.load(is);
			int iterations = Integer.parseInt(properties.getProperty("iterations"));
			int initialPopulationSize = Integer.parseInt(properties.getProperty("initialPopulationSize"));
			int selectionByElitismCount = Integer.parseInt(properties.getProperty("selectionByElitismCount"));
			int selectionByTournamentCount = Integer.parseInt(properties.getProperty("selectionByTournamentCount"));
			int tournamentSize = Integer.parseInt(properties.getProperty("tournamentSize"));
			int crossoverOnSelectionCount = Integer.parseInt(properties.getProperty("crossoverOnSelectionCount"));
			int crossoverOnRandomCount = Integer.parseInt(properties.getProperty("crossoverOnRandomCount"));
			int mutationCount = Integer.parseInt(properties.getProperty("mutationCount"));
			double mutationSwapRate = Double.parseDouble(properties.getProperty("mutationSwapRate"));
			double mutationReplaceRate = Double.parseDouble(properties.getProperty("mutationReplaceRate"));
			int randomInjectionCount = Integer.parseInt(properties.getProperty("randomInjectionCount"));
			return new GeneticParameters(
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

}
