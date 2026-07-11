package cardinal.xenoblade.frontiernav.optimization;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.PreciousResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

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

	@Test
	void should_validate_fitness() {
		FrontierNavResult result = Mockito.mock(FrontierNavResult.class);
		Mockito.when(result.getEffectiveMiranium()).thenReturn(100_000);
		Mockito.when(result.getRevenue()).thenReturn(300_000);
		Mockito.when(result.getPreciousResources()).thenReturn(Map.of());
		Fitness fitness = Fitness.of(2d, 1d);

		double evaluate = fitness.evaluate(result);

		assertThat(evaluate).isEqualTo(500000.0);
	}

	@Test
	void should_validate_fitness_with_resources_thresholds() {
		FrontierNavResult result = Mockito.mock(FrontierNavResult.class);
		Mockito.when(result.getEffectiveMiranium()).thenReturn(100_000);
		Mockito.when(result.getRevenue()).thenReturn(300_000);
		Map<PreciousResource, Double> preciousResources = Map.of(
				PreciousResource.DAWNSTONE, 1d,
				PreciousResource.CIMMERIAN_CINNABAR, 1d,
				PreciousResource.OUROBOROS_CRYSTAL, 3d,
				PreciousResource.PARHELION_PLATINUM, 5d,
				PreciousResource.BONJELIUM, 4d,
				PreciousResource.AURORITE, 10d
		);
		Mockito.when(result.getPreciousResources()).thenReturn(preciousResources);
		Map<PreciousResource, Double> thresholds = Map.of(
				PreciousResource.DAWNSTONE, 1d,
				PreciousResource.CIMMERIAN_CINNABAR, 2d,
				PreciousResource.OUROBOROS_CRYSTAL, 3d,
				PreciousResource.PARHELION_PLATINUM, 4d,
				PreciousResource.BONJELIUM, 5d
		);
		Fitness fitness = Fitness.of(2d, 1d, thresholds, Map.of());

		double evaluate = fitness.evaluate(result);

		assertThat(evaluate).isEqualTo(200000.0);
	}

	@Test
	void should_validate_fitness_with_resources_ratios() {
		Mira mira = Mockito.mock(Mira.class);
		FrontierNavResult result = Mockito.mock(FrontierNavResult.class);
		Mockito.when(result.getMira()).thenReturn(mira);
		Map<PreciousResource, Double> maximumPreciousResources = Map.of(
				PreciousResource.CIMMERIAN_CINNABAR, 2d,
				PreciousResource.OUROBOROS_CRYSTAL, 5d,
				PreciousResource.PARHELION_PLATINUM, 20d,
				PreciousResource.BONJELIUM, 4d,
				PreciousResource.AURORITE, 10d
		);
		Mockito.when(mira.getMaximumPreciousResources()).thenReturn(maximumPreciousResources);
		Mockito.when(result.getEffectiveMiranium()).thenReturn(100_000);
		Mockito.when(result.getRevenue()).thenReturn(300_000);
		Map<PreciousResource, Double> preciousResources = Map.of(
				PreciousResource.DAWNSTONE, 1d,
				PreciousResource.CIMMERIAN_CINNABAR, 1d,
				PreciousResource.OUROBOROS_CRYSTAL, 3d,
				PreciousResource.PARHELION_PLATINUM, 5d,
				PreciousResource.BONJELIUM, 4d,
				PreciousResource.AURORITE, 10d
		);
		Mockito.when(result.getPreciousResources()).thenReturn(preciousResources);
		Map<PreciousResource, Double> ratios = Map.of(
				PreciousResource.CIMMERIAN_CINNABAR, 0.5d,
				PreciousResource.OUROBOROS_CRYSTAL, 0.5d,
				PreciousResource.PARHELION_PLATINUM, 0.5d,
				PreciousResource.BONJELIUM, 1d
		);
		Fitness fitness = Fitness.of(2d, 1d, Map.of(), ratios);

		double evaluate = fitness.evaluate(result);

		assertThat(evaluate).isEqualTo(250000.0);
	}

}