package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.optimization.Fitness;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import cardinal.xenoblade.frontiernav.probe.StorageProbe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class GeneticSelectionTest {

	private static final Fitness FITNESS = Fitness.of(0d, 1d);

	private static final Site SITE1 = new Site(1, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE2 = new Site(2, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE3 = new Site(3, MiraniumRank.A, RevenueRank.A);
	private static final Mira MIRA = Mira.builder()
			.addSite(SITE1)
			.addSite(SITE2)
			.addSite(SITE3)
			.addConnection(SITE1, SITE2)
			.addConnection(SITE2, SITE3)
			.build();

	@Test
	void should_verify_elite_selection() {
		Random random = new Random(7);

		ProbeLayout probeLayout555 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G5,
				SITE2, ResearchProbe.G5,
				SITE3, ResearchProbe.G5
		));
		ProbeLayout probeLayout551 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G5,
				SITE2, ResearchProbe.G5,
				SITE3, ResearchProbe.G1
		));
		ProbeLayout probeLayout511 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G5,
				SITE2, ResearchProbe.G1,
				SITE3, ResearchProbe.G1
		));
		ProbeLayout probeLayout111 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G1,
				SITE2, ResearchProbe.G1,
				SITE3, ResearchProbe.G1
		));

		List<FrontierNavResult> population = new ArrayList<>();
		population.add(FrontierNavResult.compute(MIRA, probeLayout111));
		population.add(FrontierNavResult.compute(MIRA, probeLayout511));
		population.add(FrontierNavResult.compute(MIRA, probeLayout551));
		population.add(FrontierNavResult.compute(MIRA, probeLayout555));

		Selection selection = new GeneticSelection(random, FITNESS);

		List<FrontierNavResult> selected = selection.elitism(population, 2);

		assertThat(selected).hasSize(2);
		assertThat(selected.get(0).getProbeLayout()).isEqualTo(probeLayout555);
		assertThat(selected.get(1).getProbeLayout()).isEqualTo(probeLayout551);
	}

	@Test
	void should_verify_tournament_selection() {
		Random random = new Random(15);

		ProbeLayout probeLayout555 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G5,
				SITE2, ResearchProbe.G5,
				SITE3, ResearchProbe.G5
		));
		ProbeLayout probeLayout551 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G5,
				SITE2, ResearchProbe.G5,
				SITE3, ResearchProbe.G1
		));
		ProbeLayout probeLayout511 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G5,
				SITE2, ResearchProbe.G1,
				SITE3, ResearchProbe.G1
		));
		ProbeLayout probeLayout111 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G1,
				SITE2, ResearchProbe.G1,
				SITE3, ResearchProbe.G1
		));
		ProbeLayout probeLayout110 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G1,
				SITE2, ResearchProbe.G1,
				SITE3, StorageProbe.DEFAULT
		));
		ProbeLayout probeLayout100 = new ProbeLayout(Map.of(
				SITE1, ResearchProbe.G1,
				SITE2, StorageProbe.DEFAULT,
				SITE3, StorageProbe.DEFAULT
		));
		ProbeLayout probeLayout000 = new ProbeLayout(Map.of(
				SITE1, StorageProbe.DEFAULT,
				SITE2, StorageProbe.DEFAULT,
				SITE3, StorageProbe.DEFAULT
		));

		List<FrontierNavResult> population = new ArrayList<>();
		population.add(FrontierNavResult.compute(MIRA, probeLayout000));
		population.add(FrontierNavResult.compute(MIRA, probeLayout100));
		population.add(FrontierNavResult.compute(MIRA, probeLayout110));
		population.add(FrontierNavResult.compute(MIRA, probeLayout111));
		population.add(FrontierNavResult.compute(MIRA, probeLayout511));
		population.add(FrontierNavResult.compute(MIRA, probeLayout551));
		population.add(FrontierNavResult.compute(MIRA, probeLayout555));

		Selection selection = new GeneticSelection(random, FITNESS);

		List<FrontierNavResult> selected = selection.tournament(population, 2, 3);

		assertThat(selected).hasSize(2);
		assertThat(selected.get(0).getProbeLayout()).isEqualTo(probeLayout551);
		assertThat(selected.get(1).getProbeLayout()).isEqualTo(probeLayout111);
	}

}