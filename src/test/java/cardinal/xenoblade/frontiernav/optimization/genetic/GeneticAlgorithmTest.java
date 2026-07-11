package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.optimization.Fitness;
import cardinal.xenoblade.frontiernav.optimization.random.ProbeLayoutGenerator;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.PreciousResource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GeneticAlgorithmTest {

	@Test
	void should_validate_algorithm() {
		Mira mira = Mockito.mock(Mira.class);
		Inventory inventory = Mockito.mock(Inventory.class);
		Random random = new Random(1);
		Selection selection = Mockito.spy(new DummySelection());
		Crossover crossover = Mockito.spy(new DummyCrossover());
		Mutation mutation = Mockito.spy(new DummyMutation());
		ProbeLayoutGenerator probeLayoutGenerator = Mockito.mock(ProbeLayoutGenerator.class);

		when(probeLayoutGenerator.generateRandom()).thenReturn(Mockito.mock(ProbeLayout.class));

		double miraniumCoef = 0.0;
		double revenueCoef = 0.0;
		Map<PreciousResource, Double> preciousResourcesThresholds = Map.of();
		Map<PreciousResource, Double> preciousResourcesRatios = Map.of();
		Fitness fitness = Fitness.of(miraniumCoef, revenueCoef);
		int iterations = 150;
		int initialPopulationSize = 100;
		int selectionByElitismCount = 1;
		int selectedByTournamentCount = 7;
		int tournamentSize = 3;
		int crossoverOnSelectionCount = 12;
		int crossoverOnRandomCount = 18;
		int mutationCount = 25;
		double mutationSwapRate = 0.1;
		double mutationReplaceRate = 0.1;
		int randomInjectionCount = 30;
		GeneticParameters parameters = new GeneticParameters(miraniumCoef, revenueCoef, preciousResourcesThresholds, preciousResourcesRatios, iterations, initialPopulationSize, selectionByElitismCount, selectedByTournamentCount, tournamentSize, crossoverOnSelectionCount, crossoverOnRandomCount, mutationCount, mutationSwapRate, mutationReplaceRate, randomInjectionCount);

		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(mira, inventory, fitness, random, parameters, selection, crossover, mutation, probeLayoutGenerator);

		geneticAlgorithm.optimize();

		verify(probeLayoutGenerator, times(initialPopulationSize + (iterations * (crossoverOnRandomCount + randomInjectionCount))))
				.generateRandom();
		verify(selection, times(iterations))
				.elitism(any(), eq(selectionByElitismCount));
		verify(selection, times(iterations))
				.tournament(any(), eq(selectedByTournamentCount), eq(tournamentSize));
		verify(crossover, times(iterations * (crossoverOnSelectionCount + crossoverOnRandomCount)))
				.crossover(eq(mira), any(), any(), eq(inventory));
		verify(mutation, times(iterations * mutationCount))
				.mutation(eq(mira), any(), eq(inventory));
	}

}