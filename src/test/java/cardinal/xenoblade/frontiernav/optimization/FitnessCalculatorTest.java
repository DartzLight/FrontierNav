package cardinal.xenoblade.frontiernav.optimization;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FitnessCalculatorTest {

	@ParameterizedTest
	@CsvSource({
			"75000, 0.3, 350000, 0.7, 267500.0",
			"55000, 0.3, 360000, 0.7, 268500.0",
			"75000, 0.75, 350000, 0.25, 143750.0",
	})
	void testCompute(int miraniumValue, double miraniumCoef, int revenueValue, double revenueCoef, double expected) {
		IntSupplier miraniumSupplier = () -> miraniumValue;
		IntSupplier revenueSupplier = () -> revenueValue;
		double result = FitnessCalculator.compute(miraniumSupplier, miraniumCoef, revenueSupplier, revenueCoef);
		assertEquals(expected, result);
	}

}