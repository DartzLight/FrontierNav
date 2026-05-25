package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.ResourceHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GeneticParametersLoaderTest {

	@Test
	void should_load_genetic_parameters() throws IOException {
		GeneticParameters parameters = GeneticParametersLoader.load(ResourceHelper.getResourcePath("genetic/genetic.properties"));
		assertThat(parameters.miraniumCoef()).isEqualTo(0.5d);
		assertThat(parameters.revenueCoef()).isEqualTo(3d);
		assertThat(parameters.iterations()).isEqualTo(1_000_000);
		assertThat(parameters.initialPopulationSize()).isEqualTo(100);
		assertThat(parameters.selectionByElitismCount()).isEqualTo(1);
		assertThat(parameters.selectionByTournamentCount()).isEqualTo(17);
		assertThat(parameters.tournamentSize()).isEqualTo(3);
		assertThat(parameters.crossoverOnSelectionCount()).isEqualTo(10);
		assertThat(parameters.crossoverOnRandomCount()).isEqualTo(15);
		assertThat(parameters.mutationCount()).isEqualTo(30);
		assertThat(parameters.mutationSwapRate()).isEqualTo(0.05d);
		assertThat(parameters.mutationReplaceRate()).isEqualTo(0.15d);
		assertThat(parameters.randomInjectionCount()).isEqualTo(20);
	}

}