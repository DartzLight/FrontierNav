package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.site.PreciousResource;

import java.util.Map;

public record GeneticParameters(
		double miraniumCoef,
		double revenueCoef,
		Map<PreciousResource, Double> preciousResourcesThresholds,
		Map<PreciousResource, Double> preciousResourcesRatios,
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
