package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavTest {

	public static final Site FN_114 = new Site(114, MiraniumRank.C, RevenueRank.E);
	public static final Site FN_116 = new Site(116, MiraniumRank.A, RevenueRank.D);
	public static final Site FN_117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);

	private static Stream<Arguments> args() {
		return Stream.of(
				Arguments.of( // All basic
						Mira.builder()
								.addSite(FN_114).addSite(FN_116).addSite(FN_117)
								.addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
								.build(),
						Map.of(
								FN_114, BasicProbe.DEFAULT,
								FN_116, BasicProbe.DEFAULT,
								FN_117, BasicProbe.DEFAULT
						),
						625, 600, 6000
				),
				Arguments.of( // Mining & Research
						Mira.builder()
								.addSite(FN_114).addSite(FN_116).addSite(FN_117)
								.addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
								.build(),
						Map.of(
								FN_114, MiningProbe.G1,
								FN_116, ResearchProbe.G1,
								FN_117, MiningProbe.G1
						),
						900, 1125, 6000
				),
				Arguments.of( // Mining combo +30%
						Mira.builder()
								.addSite(FN_114).addSite(FN_116).addSite(FN_117)
								.addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
								.build(),
						Map.of(
								FN_114, MiningProbe.G1,
								FN_116, MiningProbe.G1,
								FN_117, MiningProbe.G1
						),
						1625, 360, 6000
				),
				Arguments.of(  // Unexplored with research
						Mira.builder()
								.addSite(FN_114).addSite(FN_116).addSite(FN_117)
								.addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
								.build(),
						Map.of(
								FN_114, BasicProbe.DEFAULT,
								FN_116, BasicProbe.DEFAULT,
								FN_117, ResearchProbe.G1
						),
						525, 3275, 6000
				),
				Arguments.of(  // Unexplored with research
						Mira.builder()
								.addSite(FN_114).addSite(FN_116).addSite(FN_117)
								.addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
								.build(),
						Map.of(
								FN_114, BasicProbe.DEFAULT,
								FN_116, BasicProbe.DEFAULT,
								FN_117, MiningProbe.G1
						),
						875, 510, 6000
				),
				Arguments.of(  // Storage combo +30%
						Mira.builder()
								.addSite(FN_114).addSite(FN_116).addSite(FN_117)
								.addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
								.build(),
						Map.of(
								FN_114, StorageProbe.DEFAULT,
								FN_116, StorageProbe.DEFAULT,
								FN_117, StorageProbe.DEFAULT
						),
						125, 120, 17700
				)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void check_map_exploitation_for_3_sites_in_line(Mira mira, Map<Site, Probe> probes, int expectedMiranium, int expectedRevenue, int expectedStorage) {
		FrontierNav frontierNav = new FrontierNav(mira);
		probes.forEach(frontierNav::addProbe);
		assertThat(frontierNav.getMiranium()).isEqualTo(expectedMiranium);
		assertThat(frontierNav.getRevenue()).isEqualTo(expectedRevenue);
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(expectedStorage);
	}

}