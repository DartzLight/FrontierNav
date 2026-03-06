package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
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
		Path sitesPath = ResourceHelper.getResourcePath("simple/sites.tsv");
		Path networkPath = ResourceHelper.getResourcePath("simple/network.tsv");
		Path preciousResourcesPath = ResourceHelper.getResourcePath("complete/resources.tsv");
		Path probesPath = ResourceHelper.getResourcePath("simple/probes.tsv");

		// When
		FrontierNav frontierNav = FrontierNavLoader.loadFrontierNav(sitesPath, networkPath, preciousResourcesPath, probesPath);

		// Then
		Mira mira = buildMira();
		Map<Site, Probe> probes = buildProbes(mira);
		FrontierNav expected = new FrontierNav(mira, new ProbeLayout(probes));
		assertThat(frontierNav.computeMiranium()).isEqualTo(expected.computeMiranium());
		assertThat(frontierNav.computeRevenue()).isEqualTo(expected.computeRevenue());
		assertThat(frontierNav.computeStorage()).isEqualTo(expected.computeStorage());
	}

	private static Mira buildMira() {
		Site fn112 = new Site(112, MiraniumRank.A, RevenueRank.F);
		Site fn114 = new Site(114, MiraniumRank.C, RevenueRank.E);
		Site fn116 = new Site(116, MiraniumRank.A, RevenueRank.D);
		Site fn117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
		return Mira.builder()
				.addSite(fn112).addSite(fn114).addSite(fn116).addSite(fn117)
				.addConnection(fn112, fn114).addConnection(fn114, fn116).addConnection(fn116, fn117)
				.build();
	}

	private static Map<Site, Probe> buildProbes(Mira mira) {
		return Map.of(
				mira.getSite(112), MiningProbe.G1,
				mira.getSite(114), MiningProbe.G1,
				mira.getSite(116), MiningProbe.G1,
				mira.getSite(117), ResearchProbe.G1
		);
	}

}