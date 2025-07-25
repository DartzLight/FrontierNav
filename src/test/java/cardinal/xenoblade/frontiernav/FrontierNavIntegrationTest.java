package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavIntegrationTest {

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
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		FrontierNav frontierNav = new FrontierNav(mira);
		frontierNav.addProbe(sitesByID.get(112), MiningProbe.G1);
		frontierNav.addProbe(sitesByID.get(114), MiningProbe.G1);
		frontierNav.addProbe(sitesByID.get(116), MiningProbe.G1);
		frontierNav.addProbe(sitesByID.get(117), ResearchProbe.G1);
		assertThat(frontierNav.getMiranium()).isEqualTo(1775);
		assertThat(frontierNav.getRevenue()).isEqualTo(3185);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

	@Test
	void check_all_basic_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("complete/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		assertThat(frontierNav.getMiranium()).isEqualTo(16550);
		assertThat(frontierNav.getRevenue()).isEqualTo(27675);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

	@Test
	void check_all_custom_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("complete/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(mira, ResourceHelper.getResourcePath("complete/probes.tsv"));
		assertThat(frontierNav.getMiranium()).isEqualTo(61166);
		assertThat(frontierNav.getRevenue()).isEqualTo(213025);
		assertThat(frontierNav.getStorage()).isEqualTo(72300);
	}

	@Test
	void check_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("complete/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		frontierNav.addProbe(sitesByID.get(112), BoosterProbe.G2);
		frontierNav.addProbe(sitesByID.get(114), MiningProbe.G1);
		frontierNav.addProbe(sitesByID.get(116), BoosterProbe.G1);
		frontierNav.addProbe(sitesByID.get(117), ResearchProbe.G1);
		frontierNav.addProbe(sitesByID.get(118), BoosterProbe.G1);
		assertThat(frontierNav.getMiranium()).isEqualTo(16575);
		assertThat(frontierNav.getRevenue()).isEqualTo(33535);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

	@Test
	void check_duplicator_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("complete/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		frontierNav.addProbe(sitesByID.get(204), DuplicatorProbe.DEFAULT);
		frontierNav.addProbe(sitesByID.get(203), ResearchProbe.G6);
		frontierNav.addProbe(sitesByID.get(205), MiningProbe.G10);
		frontierNav.addProbe(sitesByID.get(211), StorageProbe.DEFAULT);
		assertThat(frontierNav.getMiranium()).isEqualTo(19250);
		assertThat(frontierNav.getRevenue()).isEqualTo(33150);
		assertThat(frontierNav.getStorage()).isEqualTo(12000);
	}

	@Test
	void check_duplicator_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("complete/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		frontierNav.addProbe(sitesByID.get(204), DuplicatorProbe.DEFAULT);
		frontierNav.addProbe(sitesByID.get(203), ResearchProbe.G6);
		frontierNav.addProbe(sitesByID.get(205), MiningProbe.G10);
		frontierNav.addProbe(sitesByID.get(211), StorageProbe.DEFAULT);
		frontierNav.addProbe(sitesByID.get(212), BoosterProbe.G1);
		assertThat(frontierNav.getMiranium()).isEqualTo(20410);
		assertThat(frontierNav.getRevenue()).isEqualTo(35734);
		assertThat(frontierNav.getStorage()).isEqualTo(15000);
	}

	@Test
	void check_multiple_duplicator_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("complete/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		frontierNav.addProbe(sitesByID.get(204), DuplicatorProbe.DEFAULT);
		frontierNav.addProbe(sitesByID.get(203), ResearchProbe.G6);
		frontierNav.addProbe(sitesByID.get(205), MiningProbe.G10);
		frontierNav.addProbe(sitesByID.get(211), DuplicatorProbe.DEFAULT);
		frontierNav.addProbe(sitesByID.get(212), BoosterProbe.G1);
		assertThat(frontierNav.getMiranium()).isEqualTo(20560);
		assertThat(frontierNav.getRevenue()).isEqualTo(35859);
		assertThat(frontierNav.getStorage()).isEqualTo(6000);
	}

}