package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class GeneticCrossoverTest {

	private static final Site SITE1 = new Site(1, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE2 = new Site(2, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE3 = new Site(3, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE4 = new Site(4, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE5 = new Site(5, MiraniumRank.A, RevenueRank.A);
	private static final Mira MIRA = Mira.builder()
			.addSite(SITE1)
			.addSite(SITE2)
			.addSite(SITE3)
			.addSite(SITE4)
			.addSite(SITE5)
			.addConnection(SITE1, SITE2)
			.addConnection(SITE2, SITE3)
			.addConnection(SITE3, SITE4)
			.addConnection(SITE4, SITE5)
			.build();

	@Test
	void should_verify_crossover_without_repair() {
		// Given
		Random random = new Random(777);
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, MiningProbe.G2, MiningProbe.G3, MiningProbe.G4, MiningProbe.G5,
				ResearchProbe.G1, ResearchProbe.G2, ResearchProbe.G3, ResearchProbe.G4, ResearchProbe.G5));
		ProbeLayout parent1 = new ProbeLayout(Map.of(
				SITE1, MiningProbe.G1,
				SITE2, MiningProbe.G2,
				SITE3, MiningProbe.G3,
				SITE4, MiningProbe.G4,
				SITE5, MiningProbe.G5
		));
		ProbeLayout parent2 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G1,
				SITE2, ResearchProbe.G2,
				SITE3, ResearchProbe.G3,
				SITE4, ResearchProbe.G4,
				SITE5, ResearchProbe.G5
		));
		Crossover geneticCrossover = new GeneticCrossover(random);

		// When
		ProbeLayout crossover = geneticCrossover.crossover(MIRA, parent1, parent2, inventory);

		// Then
		assertThat(crossover).isEqualTo(new ProbeLayout(Map.of(
				SITE1, MiningProbe.G1, // parent 1
				SITE2, ResearchProbe.G2, // parent 2
				SITE3, ResearchProbe.G3, // parent 2
				SITE4, MiningProbe.G4, // parent 1
				SITE5, MiningProbe.G5 // parent 1
		)));
	}

	@Test
	void should_verify_crossover_with_repair() {
		// Given
		Random random = new Random(777);
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, MiningProbe.G2, MiningProbe.G3, MiningProbe.G4, MiningProbe.G5,
				ResearchProbe.G1, ResearchProbe.G2, ResearchProbe.G3, ResearchProbe.G4, ResearchProbe.G5,
				StorageProbe.DEFAULT, StorageProbe.DEFAULT, BoosterProbe.G1, BoosterProbe.G2, DuplicatorProbe.DEFAULT));
		ProbeLayout parent1 = new ProbeLayout(Map.of(
				SITE1, DuplicatorProbe.DEFAULT,
				SITE2, BoosterProbe.G2,
				SITE3, BoosterProbe.G1,
				SITE4, StorageProbe.DEFAULT,
				SITE5, StorageProbe.DEFAULT
		));
		ProbeLayout parent2 = new ProbeLayout(Map.of(
				SITE1, StorageProbe.DEFAULT,
				SITE2, StorageProbe.DEFAULT,
				SITE3, DuplicatorProbe.DEFAULT,
				SITE4, BoosterProbe.G2,
				SITE5, BoosterProbe.G1
		));
		Crossover geneticCrossover = new GeneticCrossover(random);

		// When
		ProbeLayout crossover = geneticCrossover.crossover(MIRA, parent1, parent2, inventory);

		// Then
		assertThat(crossover).isEqualTo(new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G4, // repaired
				SITE2, StorageProbe.DEFAULT, // parent 2
				SITE3, DuplicatorProbe.DEFAULT, // parent 2
				SITE4, ResearchProbe.G1, // repaired
				SITE5, StorageProbe.DEFAULT // parent 1
		)));
	}

}