package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavResultTest {
	private static final Site FN_1 = new Site(1, MiraniumRank.A, RevenueRank.A);
	private static final Site FN_2 = new Site(2, MiraniumRank.A, RevenueRank.A);
	private static final Site FN_3 = new Site(3, MiraniumRank.A, RevenueRank.A);
	private static final Site FN_4 = new Site(4, MiraniumRank.A, RevenueRank.A);
	private static final Site FN_5 = new Site(5, MiraniumRank.A, RevenueRank.A);
	private static final Mira MIRA = Mira.builder()
			.addSite(FN_1).addSite(FN_2).addSite(FN_3).addSite(FN_4).addSite(FN_5)
			.addConnection(FN_1, FN_2).addConnection(FN_2, FN_3).addConnection(FN_3, FN_4).addConnection(FN_4, FN_5)
			.build();

	@Test
	void should_compute_miranium() {
		Map<Site, Probe> probes = Map.of(
				FN_1, BoosterProbe.G2,
				FN_2, MiningProbe.G10,
				FN_3, ResearchProbe.G6,
				FN_4, BoosterProbe.G2,
				FN_5, StorageProbe.DEFAULT
		);

		FrontierNavResult result = FrontierNavResult.compute(new FrontierNav(MIRA, new ProbeLayout(probes)));

		assertThat(result.getMiraniumProduction()).isEqualTo(3300);
	}

	@Test
	void should_compute_revenue() {
		Map<Site, Probe> probes = Map.of(
				FN_1, BoosterProbe.G2,
				FN_2, MiningProbe.G10,
				FN_3, ResearchProbe.G6,
				FN_4, BoosterProbe.G2,
				FN_5, StorageProbe.DEFAULT
		);

		FrontierNavResult result = FrontierNavResult.compute(new FrontierNav(MIRA, new ProbeLayout(probes)));

		assertThat(result.getRevenue()).isEqualTo(7200);
	}

	@Test
	void should_compute_storage() {
		Map<Site, Probe> probes = Map.of(
				FN_1, BoosterProbe.G2,
				FN_2, MiningProbe.G10,
				FN_3, ResearchProbe.G6,
				FN_4, BoosterProbe.G2,
				FN_5, StorageProbe.DEFAULT
		);

		FrontierNavResult result = FrontierNavResult.compute(new FrontierNav(MIRA, new ProbeLayout(probes)));

		assertThat(result.getStorage()).isEqualTo(12000);
	}

	@Test
	void should_compute_effective_miranium_when_storage_is_not_full() {
		Map<Site, Probe> probes = Map.of(
				FN_1, BoosterProbe.G2,
				FN_2, MiningProbe.G10,
				FN_3, MiningProbe.G10,
				FN_4, StorageProbe.DEFAULT,
				FN_5, BoosterProbe.G2
		);

		FrontierNavResult result = FrontierNavResult.compute(new FrontierNav(MIRA, new ProbeLayout(probes)));

		assertThat(result.getMiraniumProduction()).isEqualTo(4650);
		assertThat(result.getStorage()).isEqualTo(12000);
		assertThat(result.getEffectiveMiranium()).isEqualTo(result.getMiraniumProduction());
	}

	@Test
	void should_compute_effective_miranium_when_storage_is_full() {
		Map<Site, Probe> probes = Map.of(
				FN_1, BoosterProbe.G2,
				FN_2, MiningProbe.G10,
				FN_3, MiningProbe.G10,
				FN_4, MiningProbe.G10,
				FN_5, BoosterProbe.G2
		);

		FrontierNavResult result = FrontierNavResult.compute(new FrontierNav(MIRA, new ProbeLayout(probes)));

		assertThat(result.getMiraniumProduction()).isEqualTo(9850);
		assertThat(result.getStorage()).isEqualTo(6000);
		assertThat(result.getEffectiveMiranium()).isEqualTo(result.getStorage());
	}
}