package cardinal.xenoblade.frontiernav.optimization;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.site.PreciousResource;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

@FunctionalInterface
public interface Fitness {

	static Fitness of(double miraniumCoef, double revenueCoef) {
		return of(miraniumCoef, revenueCoef, Map.of());
	}

	static Fitness of(double miraniumCoef, double revenueCoef, Map<PreciousResource, Double> thresholds) {
		return value -> {
			Map<PreciousResource, Double> preciousResources = value.getPreciousResources();
			double base = Fitness.compute(value::getEffectiveMiranium, miraniumCoef, value::getRevenue, revenueCoef);
			List<Double> multipliers = thresholds.entrySet()
					.stream()
					.map(entry -> {
						PreciousResource resource = entry.getKey();
						double threshold = entry.getValue();
						double actual = preciousResources.getOrDefault(resource, 0d);
						return Double.min(actual / threshold, 1.0d);
					}).toList();
			return multipliers.stream()
					.mapToDouble(x -> x)
					.reduce(base, (x, y) -> x * y);
		};
	}

	static double compute(IntSupplier miraniumSupplier, double miraniumCoef, IntSupplier revenueSupplier, double revenueCoef) {
		return miraniumCoef * miraniumSupplier.getAsInt() + revenueCoef * revenueSupplier.getAsInt();
	}

	double evaluate(FrontierNavResult value);

	default Comparator<FrontierNavResult> ascendingComparator() {
		return Comparator.comparingDouble(this::evaluate);
	}

	default Comparator<FrontierNavResult> descendingComparator() {
		return ascendingComparator().reversed();
	}

}
