package cardinal.xenoblade.frontiernav.optimization.genetic;

public record GeneticParameters(
		int iterations,
		int initialPopulationSize,
		int selectionByElitismCount,
		int selectionByTournamentCount,
		int tournamentSize,
		int crossoverOnSelectionCount,
		int crossoverOnRandomCount,
		int mutationCount,
		double mutationSwapRate,
		double mutationReplaceRate,
		int randomInjectionCount
) {
}
