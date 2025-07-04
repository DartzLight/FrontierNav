package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SiteTest {

	private static Stream<Arguments> args() {
		return Stream.of(
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D), BasicProbe.DEFAULT, 250, 225),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D), MiningProbe.G1, 500, 135),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D), ResearchProbe.G1, 150, 900),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, 1), BasicProbe.DEFAULT, 250, 225),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, 1), MiningProbe.G1, 500, 135),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, 1), ResearchProbe.G1, 150, 2900),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, 2), ResearchProbe.G1, 150, 4900)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void check_site_exploitation(Site site, Probe probe, int expectedMiranium, int expectedRevenue) {
		assertThat(computeLocalSiteMiranium(site, probe)).isEqualTo(expectedMiranium);
		assertThat(computeLocalSiteRevenue(site, probe)).isEqualTo(expectedRevenue);
	}

	private static int computeLocalSiteMiranium(Site site, Probe probe) {
		return site.miraniumRank().getBaseMiranium() * probe.getMiraniumMultiplier() / 100;
	}

	private static int computeLocalSiteRevenue(Site site, Probe probe) {
		int bonus = probe.getRevenueBonus() * site.unexploredTerritories();
		return site.revenueRank().getBaseRevenue() * probe.getRevenueMultiplier() / 100 + bonus;
	}

}