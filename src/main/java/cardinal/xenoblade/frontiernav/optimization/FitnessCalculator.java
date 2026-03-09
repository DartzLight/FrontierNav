package cardinal.xenoblade.frontiernav.optimization;

import java.util.function.IntSupplier;

public class FitnessCalculator {

	private FitnessCalculator() {
	}

	public static double compute(IntSupplier miraniumSupplier, double miraniumCoef, IntSupplier revenueSupplier, double revenueCoef) {
		return miraniumCoef * miraniumSupplier.getAsInt() + revenueCoef * revenueSupplier.getAsInt();
	}

}
