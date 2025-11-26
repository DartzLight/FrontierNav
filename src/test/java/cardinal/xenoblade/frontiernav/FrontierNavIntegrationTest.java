package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavIntegrationTest {
	private static final Path SITES_PATH = ResourceHelper.getResourcePath("complete/sites.tsv");
	private static final Path NETWORK_PATH = ResourceHelper.getResourcePath("complete/network.tsv");
	private static final Path PROBES_PATH = ResourceHelper.getResourcePath("complete/probes.tsv");

	@Test
	void check_simple_probes() {
		Site fn112 = new Site(112, MiraniumRank.A, RevenueRank.F);
		Site fn114 = new Site(114, MiraniumRank.C, RevenueRank.E);
		Site fn116 = new Site(116, MiraniumRank.A, RevenueRank.D);
		Site fn117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
		Mira mira = Mira.builder()
				.addSite(fn112).addSite(fn114).addSite(fn116).addSite(fn117)
				.addConnection(fn112, fn114).addConnection(fn114, fn116).addConnection(fn116, fn117)
				.build();
		Map<Site, Probe> probes = new HashMap<>();
		probes.put(mira.getSite(112), MiningProbe.G1);
		probes.put(mira.getSite(114), MiningProbe.G1);
		probes.put(mira.getSite(116), MiningProbe.G1);
		probes.put(mira.getSite(117), ResearchProbe.G1);
		FrontierNav frontierNav = new FrontierNav(mira, probes);
		assertThat(frontierNav.getMiranium()).isEqualTo(1775);
		assertThat(frontierNav.getRevenue()).isEqualTo(3185);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

	@Test
	void check_all_basic_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(SITES_PATH, NETWORK_PATH);
		FrontierNav frontierNav = new FrontierNav(mira, Map.of());
		assertThat(frontierNav.getMiranium()).isEqualTo(16550);
		assertThat(frontierNav.getRevenue()).isEqualTo(27675);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

	@Test
	void check_all_custom_probes() throws IOException {
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(SITES_PATH, NETWORK_PATH, PROBES_PATH);
		assertThat(frontierNav.getMiranium()).isEqualTo(61166);
		assertThat(frontierNav.getRevenue()).isEqualTo(213025);
		assertThat(frontierNav.getStorage()).isEqualTo(72300);
	}

	@Test
	void check_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(SITES_PATH, NETWORK_PATH);
		Map<Site, Probe> probes = Map.of(
				mira.getSite(112), BoosterProbe.G2,
				mira.getSite(114), MiningProbe.G1,
				mira.getSite(116), BoosterProbe.G1,
				mira.getSite(117), ResearchProbe.G1,
				mira.getSite(118), BoosterProbe.G1
		);
		FrontierNav frontierNav = new FrontierNav(mira, probes);
		assertThat(frontierNav.getMiranium()).isEqualTo(16575);
		assertThat(frontierNav.getRevenue()).isEqualTo(33535);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

	@Test
	void check_duplicator_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(SITES_PATH, NETWORK_PATH);
		Map<Site, Probe> probes = Map.of(
				mira.getSite(204), DuplicatorProbe.DEFAULT,
				mira.getSite(203), ResearchProbe.G6,
				mira.getSite(205), MiningProbe.G10,
				mira.getSite(211), StorageProbe.DEFAULT
		);
		FrontierNav frontierNav = new FrontierNav(mira, probes);
		assertThat(frontierNav.getMiranium()).isEqualTo(19250);
		assertThat(frontierNav.getRevenue()).isEqualTo(33150);
		assertThat(frontierNav.getStorage()).isEqualTo(12000);
	}

	@Test
	void check_duplicator_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(SITES_PATH, NETWORK_PATH);
		Map<Site, Probe> probes = Map.of(
				mira.getSite(204), DuplicatorProbe.DEFAULT,
				mira.getSite(203), ResearchProbe.G6,
				mira.getSite(205), MiningProbe.G10,
				mira.getSite(211), StorageProbe.DEFAULT,
				mira.getSite(212), BoosterProbe.G1
		);
		FrontierNav frontierNav = new FrontierNav(mira, probes);
		assertThat(frontierNav.getMiranium()).isEqualTo(20410);
		assertThat(frontierNav.getRevenue()).isEqualTo(35734);
		assertThat(frontierNav.getStorage()).isEqualTo(15000);
	}

	@Test
	void check_multiple_duplicator_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(SITES_PATH, NETWORK_PATH);
		Map<Site, Probe> probes = Map.of(
				mira.getSite(204), DuplicatorProbe.DEFAULT,
				mira.getSite(203), BoosterProbe.G2,
				mira.getSite(205), MiningProbe.G10,
				mira.getSite(211), DuplicatorProbe.DEFAULT,
				mira.getSite(212), BoosterProbe.G1
		);
		FrontierNav frontierNav = new FrontierNav(mira, probes);
		assertThat(frontierNav.getMiranium()).isEqualTo(24910);
		assertThat(frontierNav.getRevenue()).isEqualTo(27215);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

}