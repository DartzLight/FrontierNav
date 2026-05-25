package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.InventoryLoader;
import cardinal.xenoblade.frontiernav.optimization.Fitness;
import cardinal.xenoblade.frontiernav.optimization.random.ProbeLayoutGenerator;
import cardinal.xenoblade.frontiernav.serialization.FrontierNavExporter;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class FrontierNavGeneticOptimizer {

	public static FrontierNavResult searchBestFrontierNav(Mira mira, Inventory inventory, Random random, GeneticParameters parameters) {
		Fitness fitness = Fitness.of(parameters.miraniumCoef(), parameters.revenueCoef());
		GeneticSelection selection = new GeneticSelection(random, fitness);
		GeneticCrossover crossover = new GeneticCrossover(random);
		GeneticMutation mutation = new GeneticMutation(random, parameters.mutationSwapRate(), parameters.mutationReplaceRate());
		ProbeLayoutGenerator probeLayoutGenerator = new ProbeLayoutGenerator(mira, inventory, random);
		GeneticAlgorithm genetic = new GeneticAlgorithm(mira, inventory, fitness, random, parameters, selection, crossover, mutation, probeLayoutGenerator);
		return genetic.optimize();
	}

	static void main() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		Inventory inventory = InventoryLoader.loadInventory(Path.of("input/inventory.tsv"));
		Random random = new Random();
		GeneticParameters parameters = GeneticParametersLoader.load(Path.of("input/genetic.properties"));

		FrontierNavResult best = searchBestFrontierNav(mira, inventory, random, parameters);
		System.out.println(best);
		System.out.println(FrontierNavExporter.exportToString(mira, best.getProbeLayout()));
	}

}
