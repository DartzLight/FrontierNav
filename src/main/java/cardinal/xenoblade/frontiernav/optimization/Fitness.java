package cardinal.xenoblade.frontiernav.optimization;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.site.PreciousResource;

import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.IntSupplier;
import java.util.stream.DoubleStream;

@FunctionalInterface
public interface Fitness {

	static Fitness of(double miraniumCoef, double revenueCoef) {
		return of(miraniumCoef, revenueCoef, Map.of(), Map.of());
	}

	static Fitness of(double miraniumCoef, double revenueCoef, Map<PreciousResource, Double> thresholds, Map<PreciousResource, Double> ratios) {
		return result -> {
			double base = Fitness.compute(result::getEffectiveMiranium, miraniumCoef, result::getRevenue, revenueCoef);
			DoubleStream malusMultipliers = getMalusMultipliers(result.getPreciousResources(), thresholds, ratios, result.getMira().getMaximumPreciousResources());
			return applyMalusMultipliers(base, malusMultipliers);
		};
	}

	static Fitness cached(Fitness delegate) {
		Map<FrontierNavResult, Double> cache = new WeakHashMap<>();
		return result -> cache.computeIfAbsent(result, delegate::evaluate);
	}

	static double compute(IntSupplier miraniumSupplier, double miraniumCoef, IntSupplier revenueSupplier, double revenueCoef) {
		return miraniumCoef * miraniumSupplier.getAsInt() + revenueCoef * revenueSupplier.getAsInt();
	}

	static DoubleStream getMalusMultipliers(Map<PreciousResource, Double> actual, Map<PreciousResource, Double> thresholds, Map<PreciousResource, Double> ratios, Map<PreciousResource, Double> maximum) {
		DoubleStream thresholdMultipliers = getThresholdMalusMultipliers(actual, thresholds, maximum);
		DoubleStream ratioMultipliers = getRatioMalusMultipliers(actual, ratios, maximum);
		return DoubleStream.concat(thresholdMultipliers, ratioMultipliers);
	}

	static DoubleStream getThresholdMalusMultipliers(Map<PreciousResource, Double> actual, Map<PreciousResource, Double> thresholds, Map<PreciousResource, Double> maximum) {
		return thresholds.entrySet()
				.stream()
				.mapToDouble(entry -> {
					PreciousResource resource = entry.getKey();
					double totalAvailable = maximum.get(resource);
					double threshold = Double.min(entry.getValue(), totalAvailable);
					return computeMalusMultiplier(actual, resource, threshold);
				});
	}

	static DoubleStream getRatioMalusMultipliers(Map<PreciousResource, Double> actual, Map<PreciousResource, Double> ratios, Map<PreciousResource, Double> maximum) {
		return ratios.entrySet()
				.stream()
				.mapToDouble(entry -> {
					PreciousResource resource = entry.getKey();
					double ratio = Double.min(entry.getValue(), 1d);
					double totalAvailable = maximum.getOrDefault(resource, 0d);
					double threshold = ratio * totalAvailable;
					return computeMalusMultiplier(actual, resource, threshold);
				});
	}

	private static double computeMalusMultiplier(Map<PreciousResource, Double> preciousResources, PreciousResource resource, double threshold) {
		double actual = preciousResources.getOrDefault(resource, 0d);
		return Double.min(actual / threshold, 1d);
	}

	private static double applyMalusMultipliers(double base, DoubleStream malusMultipliers) {
		return malusMultipliers.reduce(base, (x, y) -> x * y);
	}

	double evaluate(FrontierNavResult value);

	default Comparator<FrontierNavResult> ascendingComparator() {
		return Comparator.comparingDouble(this::evaluate);
	}

	default Comparator<FrontierNavResult> descendingComparator() {
		return ascendingComparator().reversed();
	}

}
