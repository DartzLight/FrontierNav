package cardinal.xenoblade.frontiernav.probe.layout;

import cardinal.xenoblade.frontiernav.ResourceHelper;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProbeLayoutLoaderTest {

	@Test
	void check_probes_load_from_file() throws IOException {
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
		ProbeLayout probeLayout = ProbeLayoutLoader.loadProbes(ResourceHelper.getResourcePath("simple/probes.tsv"), mira);

		// Then
		ProbeLayout expected = new ProbeLayout(Map.of(
				mira.getSite(112), MiningProbe.G1,
				mira.getSite(114), MiningProbe.G1,
				mira.getSite(116), MiningProbe.G1,
				mira.getSite(117), ResearchProbe.G1
		));
		assertThat(probeLayout).isEqualTo(expected);
	}

}