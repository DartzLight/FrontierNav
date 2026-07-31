package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.optimization.Fitness;
import cardinal.xenoblade.frontiernav.optimization.random.ProbeLayoutGenerator;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

	private final Mira mira;
	private final Inventory inventory;
	private final Fitness fitness;
	private final Random random;
	private final Selection selection;
	private final Crossover crossover;
	private final Mutation mutation;
	private final GeneticParameters parameters;
	private final ProbeLayoutGenerator probeLayoutGenerator;

	public GeneticAlgorithm(Mira mira, Inventory inventory, Fitness fitness, Random random, GeneticParameters parameters, Selection selection, Crossover crossover, Mutation mutation, ProbeLayoutGenerator probeLayoutGenerator) {
		this.mira = mira;
		this.inventory = inventory;
		this.fitness = fitness;
		this.random = random;
		this.parameters = parameters;
		this.selection = selection;
		this.crossover = crossover;
		this.mutation = mutation;
		this.probeLayoutGenerator = probeLayoutGenerator;
	}

	public FrontierNavResult optimize() {
		List<FrontierNavResult> population = initialize();
		display(0, extractBest(population));

		for (int generation = 1; generation <= parameters.iterations(); generation++) {
			population = evolve(population);
			display(generation, extractBest(population));
		}

		return extractBest(population);
	}

	private List<FrontierNavResult> initialize() {
		List<ProbeLayout> probeLayouts = new ArrayList<>();
		for (int i = 0; i < parameters.initialPopulationSize(); i++) {
			probeLayouts.add(probeLayoutGenerator.generateRandom());
		}
		return evaluate(probeLayouts);
	}

	private List<FrontierNavResult> evolve(List<FrontierNavResult> previousPopulation) {
		List<FrontierNavResult> selected = new ArrayList<>();
		List<FrontierNavResult> elite = selection.elitism(previousPopulation, parameters.selectionByElitismCount());
		List<FrontierNavResult> tournament = selection.tournament(previousPopulation, parameters.selectionByTournamentCount(), parameters.tournamentSize());
		selected.addAll(elite);
		selected.addAll(tournament);
		List<ProbeLayout> newProbeLayouts = new ArrayList<>();

		for (int i = 0; i < parameters.crossoverOnSelectionCount(); i++) {
			ProbeLayout parent1 = selectRandom(selected);
			ProbeLayout parent2 = selectRandom(selected);
			ProbeLayout offspring = crossover.crossover(mira, parent1, parent2, inventory);
			newProbeLayouts.add(offspring);
		}

		for (int i = 0; i < parameters.crossoverOnRandomCount(); i++) {
			ProbeLayout parent1 = selectRandom(selected);
			ProbeLayout parent2 = probeLayoutGenerator.generateRandom();
			ProbeLayout offspring = crossover.crossover(mira, parent1, parent2, inventory);
			newProbeLayouts.add(offspring);
		}

		for (int i = 0; i < parameters.mutationCount(); i++) {
			ProbeLayout origin = selectRandom(selected);
			ProbeLayout altered = mutation.mutation(mira, origin, inventory);
			newProbeLayouts.add(altered);
		}

		for (int i = 0; i < parameters.randomInjectionCount(); i++) {
			newProbeLayouts.add(probeLayoutGenerator.generateRandom());
		}

		List<FrontierNavResult> nextPopulation = new ArrayList<>(selected);
		nextPopulation.addAll(evaluate(newProbeLayouts));
		return nextPopulation;
	}

	private List<FrontierNavResult> evaluate(List<ProbeLayout> probeLayouts) {
		return probeLayouts.parallelStream()
				.map(probeLayout -> FrontierNavResult.compute(mira, probeLayout))
				.toList();
	}

	private ProbeLayout selectRandom(List<FrontierNavResult> population) {
		int index = random.nextInt(population.size());
		FrontierNavResult selected = population.get(index);
		return selected.getProbeLayout();
	}

	private void display(int generation, FrontierNavResult result) {
		System.out.println(generation + " > " + fitness.evaluate(result) + " (" + result + ")");
	}

	private FrontierNavResult extractBest(List<FrontierNavResult> population) {
		return population.stream()
				.max(fitness.ascendingComparator())
				.orElseThrow();
	}

}
