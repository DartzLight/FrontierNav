package cardinal.xenoblade.frontiernav.optimization.random;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.inventory.InventoryLoader;
import cardinal.xenoblade.frontiernav.optimization.FitnessCalculator;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.serialization.FrontierNavExporter;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;
import java.util.function.ToDoubleFunction;

public class FrontierNavRandomOptimizer {

	public static FrontierNavResult searchBestFrontierNav(Mira mira, Inventory inventory, Random random, double miraniumCoef, double revenueCoef, int iterations) {
		RandomProbeLayoutGenerator generator = new RandomProbeLayoutGenerator(mira, inventory, random);
		ToDoubleFunction<FrontierNavResult> fitness = getFitness(miraniumCoef, revenueCoef);

		FrontierNavResult best = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (int i = 1; i <= iterations; i++) {
			ProbeLayout probeLayout = generator.generateRandom();
			FrontierNavResult result = FrontierNavResult.compute(mira, probeLayout);
			double score = fitness.applyAsDouble(result);
			if (score > bestScore) {
				bestScore = score;
				best = result;
				System.out.println(i + " > " + score + " (" + result + ")");
			}
		}

		return best;
	}

	private static ToDoubleFunction<FrontierNavResult> getFitness(double miraniumCoef, double revenueCoef) {
		return frontierNavResult -> FitnessCalculator.compute(frontierNavResult::getEffectiveMiranium, miraniumCoef, frontierNavResult::getRevenue, revenueCoef);
	}

	static void main() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		Inventory inventory = InventoryLoader.loadInventory(Path.of("input/inventory.tsv"));
		Random random = new Random();
		double miraniumCoef = 6d;
		double revenueCoef = 1d;
		int iterations = 100_000;

		FrontierNavResult best = searchBestFrontierNav(mira, inventory, random, miraniumCoef, revenueCoef, iterations);
		System.out.println(best);
		System.out.println(FrontierNavExporter.exportToString(mira, best.getProbeLayout()));
	}

}
