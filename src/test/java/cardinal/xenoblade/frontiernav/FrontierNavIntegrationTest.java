package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.BoosterProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import cardinal.xenoblade.frontiernav.site.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
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
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(6000);
	}

	@Test
	void check_all_basic_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		assertThat(frontierNav.getMiranium()).isEqualTo(13400);
		assertThat(frontierNav.getRevenue()).isEqualTo(21475);
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(6000);
	}

	@Test
	void check_all_custom_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(mira, Path.of("input/probes.tsv"));
		assertThat(frontierNav.getMiranium()).isEqualTo(38177);
		assertThat(frontierNav.getRevenue()).isEqualTo(75596);
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(39000);
	}

	@Test
	void check_booster_probes() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		frontierNav.addProbe(sitesByID.get(112), BoosterProbe.G2);
		frontierNav.addProbe(sitesByID.get(114), MiningProbe.G1);
		frontierNav.addProbe(sitesByID.get(116), BoosterProbe.G1);
		frontierNav.addProbe(sitesByID.get(117), ResearchProbe.G1);
		frontierNav.addProbe(sitesByID.get(118), BoosterProbe.G1);
		assertThat(frontierNav.getMiranium()).isEqualTo(13425);
		assertThat(frontierNav.getRevenue()).isEqualTo(27335);
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(6000);
	}

}