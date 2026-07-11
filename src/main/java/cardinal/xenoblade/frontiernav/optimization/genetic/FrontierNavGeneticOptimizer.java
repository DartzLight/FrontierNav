package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.InventoryLoader;
import cardinal.xenoblade.frontiernav.optimization.Fitness;
import cardinal.xenoblade.frontiernav.optimization.random.ProbeLayoutGenerator;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.serialization.FrontierNavExporter;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;
import cardinal.xenoblade.frontiernav.site.Site;
import cardinal.xenoblade.frontiernav.utils.FileHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public class FrontierNavGeneticOptimizer {

	public static FrontierNavResult searchBestFrontierNav(Mira mira, Inventory inventory, Random random, GeneticParameters parameters) {
		Fitness fitness = Fitness.of(parameters.miraniumCoef(), parameters.revenueCoef(), parameters.preciousResourcesThresholds(), parameters.preciousResourcesRatios());
		GeneticSelection selection = new GeneticSelection(random, fitness);
		GeneticCrossover crossover = new GeneticCrossover(random);
		GeneticMutation mutation = new GeneticMutation(random, parameters.mutationSwapRate(), parameters.mutationReplaceRate());
		ProbeLayoutGenerator probeLayoutGenerator = new ProbeLayoutGenerator(mira, inventory, random);
		GeneticAlgorithm genetic = new GeneticAlgorithm(mira, inventory, fitness, random, parameters, selection, crossover, mutation, probeLayoutGenerator);
		return genetic.optimize();
	}

	static void main() throws IOException {
		Path sitesPath = FileHelper.findOrDefault(Path.of("input/sites.tsv"), Path.of("input/default/sites.tsv"));
		Path networkPath = FileHelper.findOrDefault(Path.of("input/network.tsv"), Path.of("input/default/network.tsv"));
		Path resourcesPath = FileHelper.findOrDefault(Path.of("input/resources.tsv"), Path.of("input/default/resources.tsv"));
		Path inventoryPath = FileHelper.findOrDefault(Path.of("input/inventory.tsv"), Path.of("input/default/inventory.tsv"));
		Path geneticParametersPath = FileHelper.findOrDefault(Path.of("input/genetic.properties"), Path.of("input/default/genetic.properties"));

		Mira mira = MiraLoader.loadMira(sitesPath, networkPath, resourcesPath);
		Inventory inventory = InventoryLoader.loadInventory(inventoryPath);
		Random random = new Random();
		GeneticParameters parameters = GeneticParametersLoader.load(geneticParametersPath);

		FrontierNavResult best = searchBestFrontierNav(mira, inventory, random, parameters);

		ProbeLayout layout = best.getProbeLayout();
		layout.probes()
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparingInt(Site::id)))
				.forEachOrdered(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
		System.out.println(best);
		System.out.println(FrontierNavExporter.exportToString(mira, layout));
	}

}
