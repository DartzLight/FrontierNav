package cardinal.xenoblade.frontiernav.optimization.random;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.InventoryLoader;
import cardinal.xenoblade.frontiernav.optimization.Fitness;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.serialization.FrontierNavExporter;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;
import cardinal.xenoblade.frontiernav.utils.FileHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class FrontierNavRandomOptimizer {

	public static FrontierNavResult searchBestFrontierNav(Mira mira, Inventory inventory, Random random, double miraniumCoef, double revenueCoef, int iterations) {
		ProbeLayoutGenerator generator = new ProbeLayoutGenerator(mira, inventory, random);
		Fitness fitness = Fitness.of(miraniumCoef, revenueCoef);

		FrontierNavResult best = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (int i = 1; i <= iterations; i++) {
			ProbeLayout probeLayout = generator.generateRandom();
			FrontierNavResult result = FrontierNavResult.compute(mira, probeLayout);
			double score = fitness.evaluate(result);
			if (score > bestScore) {
				bestScore = score;
				best = result;
				System.out.println(i + " > " + score + " (" + result + ")");
			}
		}

		return best;
	}

	static void main() throws IOException {
		Path sitesPath = FileHelper.findOrDefault(Path.of("input/sites.tsv"), Path.of("input/default/sites.tsv"));
		Path networkPath = FileHelper.findOrDefault(Path.of("input/network.tsv"), Path.of("input/default/network.tsv"));
		Path inventoryPath = FileHelper.findOrDefault(Path.of("input/inventory.tsv"), Path.of("input/default/inventory.tsv"));

		Mira mira = MiraLoader.loadMira(sitesPath, networkPath);
		Inventory inventory = InventoryLoader.loadInventory(inventoryPath);
		Random random = new Random();
		double miraniumCoef = 6d;
		double revenueCoef = 1d;
		int iterations = 100_000;

		FrontierNavResult best = searchBestFrontierNav(mira, inventory, random, miraniumCoef, revenueCoef, iterations);
		System.out.println(best);
		System.out.println(FrontierNavExporter.exportToString(mira, best.getProbeLayout()));
	}

}
