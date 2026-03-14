package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavTest {

	private static final Site FN_112 = new Site(112, MiraniumRank.A, RevenueRank.F);
	private static final Site FN_114 = new Site(114, MiraniumRank.C, RevenueRank.E);
	private static final Site FN_116 = new Site(116, MiraniumRank.A, RevenueRank.D);
	private static final Site FN_117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
	private static final Mira MIRA = Mira.builder()
			.addSite(FN_112).addSite(FN_114).addSite(FN_116).addSite(FN_117)
			.addConnection(FN_112, FN_114).addConnection(FN_114, FN_116).addConnection(FN_116, FN_117)
			.build();

	private static Stream<Arguments> simpleSetups() {
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
	@MethodSource("simpleSetups")
	void check_map_exploitation_for_4_sites_in_line(Mira mira, Map<Site, Probe> probes, int expectedMiranium, int expectedRevenue, int expectedStorage) {
		FrontierNav frontierNav = new FrontierNav(mira, new ProbeLayout(probes));
		assertThat(frontierNav.computeMiranium()).isEqualTo(expectedMiranium);
		assertThat(frontierNav.computeRevenue()).isEqualTo(expectedRevenue);
		assertThat(frontierNav.computeStorage()).isEqualTo(expectedStorage);
	}

	@Test
	void check_duplicator_with_multiple_boosters() {
		// Given
		Site fn402 = new Site(402, MiraniumRank.A, RevenueRank.B, 0);
		Site fn405 = new Site(405, MiraniumRank.A, RevenueRank.E, 0);
		Site fn406 = new Site(406, MiraniumRank.C, RevenueRank.B, 0);
		Site fn408 = new Site(408, MiraniumRank.B, RevenueRank.D, 1);
		Site fn413 = new Site(413, MiraniumRank.C, RevenueRank.A, 1);
		Mira mira = Mira.builder()
				.addSite(fn402)
				.addSite(fn405)
				.addSite(fn406)
				.addSite(fn408)
				.addSite(fn413)
				.addConnection(fn408, fn402)
				.addConnection(fn408, fn405)
				.addConnection(fn408, fn406)
				.addConnection(fn408, fn413)
				.build();
		ProbeLayout probeLayout = new ProbeLayout(Map.of(
				fn402, BoosterProbe.G2,
				fn405, BoosterProbe.G2,
				fn406, BoosterProbe.G2,
				fn408, DuplicatorProbe.DEFAULT,
				fn413, MiningProbe.G10
		));
		FrontierNav frontierNav = new FrontierNav(mira, probeLayout);

		// When Then
		assertThat(frontierNav.computeOutgoingBoostMultiplier(fn402))
				.isEqualTo(frontierNav.computeOutgoingBoostMultiplier(fn405))
				.isEqualTo(frontierNav.computeOutgoingBoostMultiplier(fn406))
				.isEqualTo(200);

		assertThat(frontierNav.computeOutgoingBoostMultiplier(fn408))
				.isEqualTo(800);

		assertThat(frontierNav.computeIncomingBoostMultiplier(fn408))
				.isEqualTo(frontierNav.computeIncomingBoostMultiplier(fn402))
				.isEqualTo(frontierNav.computeIncomingBoostMultiplier(fn405))
				.isEqualTo(frontierNav.computeIncomingBoostMultiplier(fn406))
				.isEqualTo(frontierNav.computeIncomingBoostMultiplier(fn413))
				.isEqualTo(800);

		assertThat(frontierNav.computeMiranium()).isEqualTo(14630);
		assertThat(frontierNav.computeRevenue()).isEqualTo(655);
		assertThat(frontierNav.computeStorage()).isEqualTo(6000);
	}

	@Test
	void check_duplicator_with_chained_booster() {
		// Given
		Site fn315 = new Site(315, MiraniumRank.A, RevenueRank.S, 2);
		Site fn316 = new Site(316, MiraniumRank.C, RevenueRank.D, 0);
		Site fn320 = new Site(320, MiraniumRank.C, RevenueRank.B, 0);
		Site fn321 = new Site(321, MiraniumRank.A, RevenueRank.D, 0);
		Site fn322 = new Site(322, MiraniumRank.A, RevenueRank.A, 0);
		Mira mira = Mira.builder()
				.addSite(fn315)
				.addSite(fn316)
				.addSite(fn320)
				.addSite(fn321)
				.addSite(fn322)
				.addConnection(fn315, fn316)
				.addConnection(fn315, fn321)
				.addConnection(fn321, fn320)
				.addConnection(fn321, fn322)
				.build();
		ProbeLayout probeLayout = new ProbeLayout(Map.of(
				fn315, DuplicatorProbe.DEFAULT,
				fn316, ResearchProbe.G6,
				fn320, BoosterProbe.G2,
				fn321, BoosterProbe.G2,
				fn322, BoosterProbe.G2
		));
		FrontierNav frontierNav = new FrontierNav(mira, probeLayout);

		// When Then
		assertThat(frontierNav.computeChainMultiplier(fn320))
				.isEqualTo(frontierNav.computeChainMultiplier(fn321))
				.isEqualTo(frontierNav.computeChainMultiplier(fn322))
				.isEqualTo(130);

		assertThat(frontierNav.computeIncomingBoostMultiplier(fn315))
				.isEqualTo(frontierNav.computeOutgoingBoostMultiplier(fn320))
				.isEqualTo(frontierNav.computeOutgoingBoostMultiplier(fn321))
				.isEqualTo(frontierNav.computeOutgoingBoostMultiplier(fn322))
				.isEqualTo(260);

		assertThat(frontierNav.computeMiranium()).isEqualTo(400);
		assertThat(frontierNav.computeRevenue()).isEqualTo(37665);
		assertThat(frontierNav.computeStorage()).isEqualTo(6000);
	}

	@Test
	void check_chain_of_duplicator_with_boosters() {
		// Given
		Site fn504 = new Site(504, MiraniumRank.C, RevenueRank.C, 0);
		Site fn505 = new Site(505, MiraniumRank.C, RevenueRank.B, 2);
		Site fn507 = new Site(507, MiraniumRank.C, RevenueRank.A, 1);
		Site fn508 = new Site(508, MiraniumRank.A, RevenueRank.B, 1);
		Site fn509 = new Site(509, MiraniumRank.A, RevenueRank.A, 0);
		Site fn510 = new Site(510, MiraniumRank.C, RevenueRank.B, 0);
		Site fn511 = new Site(511, MiraniumRank.A, RevenueRank.C, 0);
		Site fn512 = new Site(512, MiraniumRank.C, RevenueRank.A, 0);
		Site fn513 = new Site(513, MiraniumRank.C, RevenueRank.A, 2);
		Site fn514 = new Site(514, MiraniumRank.C, RevenueRank.A, 1);
		Mira mira = Mira.builder()
				.addSite(fn504)
				.addSite(fn505)
				.addSite(fn507)
				.addSite(fn508)
				.addSite(fn509)
				.addSite(fn510)
				.addSite(fn511)
				.addSite(fn512)
				.addSite(fn513)
				.addSite(fn514)
				.addConnection(fn508, fn504)
				.addConnection(fn508, fn507)
				.addConnection(fn508, fn511)
				.addConnection(fn511, fn512)
				.addConnection(fn511, fn514)
				.addConnection(fn508, fn509)
				.addConnection(fn509, fn505)
				.addConnection(fn509, fn510)
				.addConnection(fn509, fn513)
				.build();
		ProbeLayout probeLayout = new ProbeLayout(Map.of(
				fn504, MiningProbe.G10,
				fn505, BoosterProbe.G2,
				fn507, ResearchProbe.G6,
				fn508, DuplicatorProbe.DEFAULT,
				fn509, DuplicatorProbe.DEFAULT,
				fn510, BoosterProbe.G2,
				fn511, DuplicatorProbe.DEFAULT,
				fn512, BoosterProbe.G2,
				fn513, BoosterProbe.G2,
				fn514, BoosterProbe.G2
		));
		FrontierNav frontierNav = new FrontierNav(mira, probeLayout);

		// When Then
		assertThat(frontierNav.computeChainMultiplier(fn508))
				.isEqualTo(frontierNav.computeChainMultiplier(fn509))
				.isEqualTo(frontierNav.computeChainMultiplier(fn511))
				.isEqualTo(130);

		assertThat(frontierNav.computeIncomingBoostMultiplier(fn509))
				.isEqualTo(800);
		assertThat(frontierNav.computeOutgoingBoostMultiplier(fn509))
				.isEqualTo(1040);

		assertThat(frontierNav.computeIncomingBoostMultiplier(fn511))
				.isEqualTo(400);
		assertThat(frontierNav.computeOutgoingBoostMultiplier(fn511))
				.isEqualTo(520);

		assertThat(frontierNav.computeIncomingBoostMultiplier(fn508))
				.isEqualTo(5408);
		assertThat(frontierNav.computeOutgoingBoostMultiplier(fn508))
				.isEqualTo(100);

		assertThat(frontierNav.computeMiranium()).isEqualTo(106806);
		assertThat(frontierNav.computeRevenue()).isEqualTo(530905);
		assertThat(frontierNav.computeStorage()).isEqualTo(6000);
	}

}