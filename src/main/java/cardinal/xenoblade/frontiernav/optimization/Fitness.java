package cardinal.xenoblade.frontiernav.optimization;

import cardinal.xenoblade.frontiernav.FrontierNavResult;

import java.util.Comparator;
import java.util.function.IntSupplier;

@FunctionalInterface
public interface Fitness {

	static Fitness of(double miraniumCoef, double revenueCoef) {
		return value -> Fitness.compute(value::getEffectiveMiranium, miraniumCoef, value::getRevenue, revenueCoef);
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
