package cardinal.xenoblade.frontiernav.optimization;

import cardinal.xenoblade.frontiernav.site.PreciousResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;
import java.util.function.IntSupplier;

import static org.assertj.core.api.Assertions.assertThat;

class FitnessTest {

	@ParameterizedTest
	@CsvSource({
			"75000, 0.3, 350000, 0.7, 267500.0",
			"55000, 0.3, 360000, 0.7, 268500.0",
			"75000, 0.75, 350000, 0.25, 143750.0",
	})
	void should_compute_fitness(int miraniumValue, double miraniumCoef, int revenueValue, double revenueCoef, double expected) {
		IntSupplier miraniumSupplier = () -> miraniumValue;
		IntSupplier revenueSupplier = () -> revenueValue;
		double result = Fitness.compute(miraniumSupplier, miraniumCoef, revenueSupplier, revenueCoef);
		assertThat(result).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({
			"10, 10, 10, 1", // current = threshold = max
			"10, 10, 15, 1", // curent = threshold < max
			"12, 10, 15, 1", // curent > threshold & current < max
			"7, 10, 10, 0.7", // current < threshold = max
			"7, 10, 15, 0.7", // current < threshold < max
			"0, 5, 10, 0", // current = 0
			"10, 15, 10, 1", // threshold too high
			"5, 0, 10, 1" // no threshold at all
	})
	void should_compute_malus_with_threshold(double current, double threshold, double max, double result) {
		assertThat(Fitness.getThresholdMalusMultipliers(
				Map.of(PreciousResource.BONJELIUM, current),
				Map.of(PreciousResource.BONJELIUM, threshold),
				Map.of(PreciousResource.BONJELIUM, max)
		)).containsExactly(result);
	}

	@ParameterizedTest
	@CsvSource({
			"10, 1.0, 10, 1", // current = ratio = max
			"5, 0.5, 10, 1", // curent = ratio < max
			"8, 0.5, 10, 1", // curent > ratio & current < max
			"2, 0.4, 10, 0.5", // current < ratio
			"0, 0.5, 10, 0", // current = 0
			"10, 2.0, 10, 1", // ratio too high
			"5, 0.0, 10, 1" // no ratio at all
	})
	void should_compute_malus_with_ratio(double current, double ratio, double max, double result) {
		assertThat(Fitness.getRatioMalusMultipliers(
				Map.of(PreciousResource.BONJELIUM, current),
				Map.of(PreciousResource.BONJELIUM, ratio),
				Map.of(PreciousResource.BONJELIUM, max)
		)).containsExactly(result);
	}

}