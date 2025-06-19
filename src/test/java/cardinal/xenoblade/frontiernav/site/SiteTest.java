package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SiteTest {

	private static Stream<Arguments> args() {
		return Stream.of(
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, BasicProbe.DEFAULT), 250, 225),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, MiningProbe.G1), 500, 135),
				Arguments.of(new Site(1, MiraniumRank.A, RevenueRank.D, ResearchProbe.G1), 150, 900)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void check_site_exploitation(Site site, int expectedMiranium, int expectedRevenue) {
		assertThat(site.getMiranium()).isEqualTo(expectedMiranium);
		assertThat(site.getRevenue()).isEqualTo(expectedRevenue);
	}

}