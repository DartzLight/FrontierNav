package cardinal.xenoblade.frontiernav.optimization.random;

import cardinal.xenoblade.frontiernav.FrontierNavResult;
import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class RandomProbeLayoutGeneratorTest {

	@Test
	void should_generate_random_frontier_nav() {
		Mira mira = buildMira();
		Inventory inventory = new Inventory(List.of(
				MiningProbe.G1, MiningProbe.G2, MiningProbe.G3, MiningProbe.G4, MiningProbe.G5, MiningProbe.G6, MiningProbe.G7, MiningProbe.G8, MiningProbe.G9, MiningProbe.G10,
				ResearchProbe.G1, ResearchProbe.G2, ResearchProbe.G3, ResearchProbe.G4, ResearchProbe.G5, ResearchProbe.G6,
				StorageProbe.DEFAULT,
				BoosterProbe.G1, BoosterProbe.G2,
				DuplicatorProbe.DEFAULT));
		Random random = new Random(777);
		RandomProbeLayoutGenerator generator = new RandomProbeLayoutGenerator(mira, inventory, random);

		ProbeLayout probeLayout = generator.generateRandom();
		FrontierNavResult result = FrontierNavResult.compute(mira, probeLayout);

		assertThat(result.getMiraniumProduction()).isEqualTo(1420);
		assertThat(result.getRevenue()).isEqualTo(6780);
		assertThat(result.getStorage()).isEqualTo(9000);
	}

	private static Mira buildMira() {
		Site site1 = new Site(1, MiraniumRank.A, RevenueRank.S);
		Site site2 = new Site(2, MiraniumRank.A, RevenueRank.A);
		Site site3 = new Site(3, MiraniumRank.B, RevenueRank.B);
		Site site4 = new Site(4, MiraniumRank.B, RevenueRank.C);
		Site site5 = new Site(5, MiraniumRank.C, RevenueRank.D);
		Site site6 = new Site(6, MiraniumRank.C, RevenueRank.E);
		return Mira.builder()
				.addSite(site1)
				.addSite(site2)
				.addSite(site3)
				.addSite(site4)
				.addSite(site5)
				.addSite(site6)
				.addConnection(site1, site2)
				.addConnection(site2, site3)
				.addConnection(site3, site4)
				.addConnection(site4, site5)
				.addConnection(site5, site6)
				.build();
	}

}