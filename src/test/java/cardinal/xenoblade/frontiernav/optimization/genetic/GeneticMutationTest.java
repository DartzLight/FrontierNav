package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.probe.DuplicatorProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.StorageProbe;
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

class GeneticMutationTest {

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
	void should_verify_mutation_swap() {
		// Given
		Random random = new Random(7);
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, MiningProbe.G2, MiningProbe.G3, MiningProbe.G4, MiningProbe.G5,
				StorageProbe.DEFAULT));
		ProbeLayout origin = new ProbeLayout(Map.of(
				SITE1, MiningProbe.G1,
				SITE2, MiningProbe.G2,
				SITE3, MiningProbe.G3,
				SITE4, MiningProbe.G4,
				SITE5, MiningProbe.G5
		));
		Mutation geneticMutation = new GeneticMutation(random, 0.25, 0.0);

		// When
		ProbeLayout mutation = geneticMutation.mutation(MIRA, origin, inventory);

		// Then
		assertThat(mutation).isEqualTo(new ProbeLayout(Map.of(
				SITE1, MiningProbe.G4, // swapped
				SITE2, MiningProbe.G2,
				SITE3, MiningProbe.G3,
				SITE4, MiningProbe.G1, // swapped
				SITE5, MiningProbe.G5
		)));
	}

	@Test
	void should_verify_mutation_replace() {
		// Given
		Random random = new Random(25);
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, MiningProbe.G2, MiningProbe.G3, MiningProbe.G4, MiningProbe.G5,
				StorageProbe.DEFAULT, DuplicatorProbe.DEFAULT));
		ProbeLayout origin = new ProbeLayout(Map.of(
				SITE1, MiningProbe.G1,
				SITE2, MiningProbe.G2,
				SITE3, MiningProbe.G3,
				SITE4, MiningProbe.G4,
				SITE5, MiningProbe.G5
		));
		Mutation geneticMutation = new GeneticMutation(random, 0.0, 0.25);

		// When
		ProbeLayout mutation = geneticMutation.mutation(MIRA, origin, inventory);

		// Then
		assertThat(mutation).isEqualTo(new ProbeLayout(Map.of(
				SITE1, DuplicatorProbe.DEFAULT, // replaced
				SITE2, MiningProbe.G2,
				SITE3, MiningProbe.G3,
				SITE4, MiningProbe.G4,
				SITE5, MiningProbe.G5
		)));
	}

	@Test
	void should_verify_mutation_swap_and_replace() {
		// Given
		Random random = new Random(150);
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, MiningProbe.G2, MiningProbe.G3, MiningProbe.G4, MiningProbe.G5,
				StorageProbe.DEFAULT, DuplicatorProbe.DEFAULT));
		ProbeLayout origin = new ProbeLayout(Map.of(
				SITE1, MiningProbe.G1,
				SITE2, MiningProbe.G2,
				SITE3, MiningProbe.G3,
				SITE4, MiningProbe.G4,
				SITE5, MiningProbe.G5
		));
		Mutation geneticMutation = new GeneticMutation(random, 0.25, 0.25);

		// When
		ProbeLayout mutation = geneticMutation.mutation(MIRA, origin, inventory);

		// Then
		assertThat(mutation).isEqualTo(new ProbeLayout(Map.of(
				SITE1, MiningProbe.G2, // swapped
				SITE2, MiningProbe.G1, // swapped
				SITE3, MiningProbe.G4, // swapped
				SITE4, DuplicatorProbe.DEFAULT, // swapped then replaced
				SITE5, MiningProbe.G5
		)));
	}

}