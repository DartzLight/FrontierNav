package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavTest {

	private static Stream<Arguments> args() {
		return Stream.of(
				Arguments.of( // All basic
						new Site(1, MiraniumRank.C, RevenueRank.E, BasicProbe.DEFAULT),
						new Site(2, MiraniumRank.A, RevenueRank.D, BasicProbe.DEFAULT),
						new Site(3, MiraniumRank.A, RevenueRank.D, BasicProbe.DEFAULT),
						625, 600
				),
				Arguments.of( // Mining & Research
						new Site(1, MiraniumRank.C, RevenueRank.E, MiningProbe.G1),
						new Site(2, MiraniumRank.A, RevenueRank.D, ResearchProbe.G1),
						new Site(3, MiraniumRank.A, RevenueRank.D, MiningProbe.G1),
						900, 1125
				),
				Arguments.of( // Mining combo +30%
						new Site(1, MiraniumRank.C, RevenueRank.E, MiningProbe.G1),
						new Site(2, MiraniumRank.A, RevenueRank.D, MiningProbe.G1),
						new Site(3, MiraniumRank.A, RevenueRank.D, MiningProbe.G1),
						1625, 360
				),
				Arguments.of( // Unexplored with research
						new Site(1, MiraniumRank.C, RevenueRank.E, BasicProbe.DEFAULT),
						new Site(2, MiraniumRank.A, RevenueRank.D, BasicProbe.DEFAULT),
						new Site(3, MiraniumRank.A, RevenueRank.D, 1, ResearchProbe.G1),
						525, 3275
				),
				Arguments.of( // Unexplored without research
						new Site(1, MiraniumRank.C, RevenueRank.E, BasicProbe.DEFAULT),
						new Site(2, MiraniumRank.A, RevenueRank.D, BasicProbe.DEFAULT),
						new Site(3, MiraniumRank.A, RevenueRank.D, 1, MiningProbe.G1),
						875, 510
				)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void check_map_exploitation_for_3_sites_in_line(Site site1, Site site2, Site site3, int expectedMiranium, int expectedRevenue) {
		FrontierNav map = new FrontierNav();
		map.addSite(site1);
		map.addSite(site2);
		map.addSite(site3);
		map.addConnection(site1, site2);
		map.addConnection(site2, site3);
		assertThat(map.getMiranium()).isEqualTo(expectedMiranium);
		assertThat(map.getRevenue()).isEqualTo(expectedRevenue);
	}

}