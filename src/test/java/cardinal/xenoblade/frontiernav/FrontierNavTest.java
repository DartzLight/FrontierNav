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

	public static final Site FN_112 = new Site(112, MiraniumRank.A, RevenueRank.F);
	public static final Site FN_114 = new Site(114, MiraniumRank.C, RevenueRank.E);
	public static final Site FN_116 = new Site(116, MiraniumRank.A, RevenueRank.D);
	public static final Site FN_117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
	public static final Mira MIRA = Mira.builder()
			.addSite(FN_112).addSite(FN_114).addSite(FN_116).addSite(FN_117)
			.addConnection(FN_112, FN_114).addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
			.build();

	private static Stream<Arguments> args() {
		return Stream.of(
				Arguments.of( // All basic
						MIRA, Map.of(
								FN_112, BasicProbe.DEFAULT,
								FN_114, BasicProbe.DEFAULT,
								FN_116, BasicProbe.DEFAULT,
								FN_117, BasicProbe.DEFAULT
						),
						875, 700, 6000
				),
				Arguments.of( // Mining & Research
						MIRA, Map.of(
								FN_112, MiningProbe.G1,
								FN_114, ResearchProbe.G1,
								FN_116, MiningProbe.G1,
								FN_117, MiningProbe.G1
						),
						1575, 930, 6000
				),
				Arguments.of( // Mining 4x chain +30%
						MIRA, Map.of(
								FN_112, MiningProbe.G1,
								FN_114, MiningProbe.G1,
								FN_116, MiningProbe.G1,
								FN_117, MiningProbe.G1
						),
						2275, 420, 6000
				),
				Arguments.of(  // Unexplored with research
						MIRA, Map.of(
								FN_112, BasicProbe.DEFAULT,
								FN_114, BasicProbe.DEFAULT,
								FN_116, ResearchProbe.G1,
								FN_117, ResearchProbe.G1
						),
						675, 4050, 6000
				),
				Arguments.of( // Mining chain + Research unexplored
						MIRA, Map.of(
								FN_112, MiningProbe.G1,
								FN_114, MiningProbe.G1,
								FN_116, MiningProbe.G1,
								FN_117, ResearchProbe.G1
						),
						1775, 3185, 6000
				),
				Arguments.of(  // Storage chain +30%
						MIRA, Map.of(
								FN_112, StorageProbe.DEFAULT,
								FN_114, StorageProbe.DEFAULT,
								FN_116, StorageProbe.DEFAULT,
								FN_117, StorageProbe.DEFAULT
						),
						175, 140, 21600
				)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void check_map_exploitation_for_4_sites_in_line(Mira mira, Map<Site, Probe> probes, int expectedMiranium, int expectedRevenue, int expectedStorage) {
		FrontierNav frontierNav = new FrontierNav(mira);
		probes.forEach(frontierNav::addProbe);
		assertThat(frontierNav.getMiranium()).isEqualTo(expectedMiranium);
		assertThat(frontierNav.getRevenue()).isEqualTo(expectedRevenue);
		assertThat(frontierNav.getStorage()).isEqualTo(expectedStorage);
	}

}