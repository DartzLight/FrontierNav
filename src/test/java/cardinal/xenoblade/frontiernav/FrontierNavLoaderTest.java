package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavLoaderTest {
	@Test
	void check_frontier_nav_load_from_file() throws IOException {
		// Given
		Site fn112 = new Site(112, MiraniumRank.A, RevenueRank.F);
		Site fn114 = new Site(114, MiraniumRank.C, RevenueRank.E);
		Site fn116 = new Site(116, MiraniumRank.A, RevenueRank.D);
		Site fn117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
		Mira mira = Mira.builder()
				.addSite(fn112).addSite(fn114).addSite(fn116).addSite(fn117)
				.addConnection(fn112, fn114).addConnection(fn114, fn116).addConnection(fn116, fn117)
				.build();

		// When
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(mira, Path.of("input/simple/probes.tsv"));

		// Then
		FrontierNav expected = new FrontierNav(mira);
		Map<Integer, Site> sitesByID = mira.getSitesByID();
		expected.addProbe(sitesByID.get(112), MiningProbe.G1);
		expected.addProbe(sitesByID.get(114), MiningProbe.G1);
		expected.addProbe(sitesByID.get(116), MiningProbe.G1);
		expected.addProbe(sitesByID.get(117), ResearchProbe.G1);
		assertThat(frontierNav.getMiranium()).isEqualTo(expected.getMiranium());
		assertThat(frontierNav.getRevenue()).isEqualTo(expected.getRevenue());
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(expected.getMiraniumStorage());
	}
}