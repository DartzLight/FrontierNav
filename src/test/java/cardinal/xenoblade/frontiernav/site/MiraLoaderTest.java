package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.ResourceHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MiraLoaderTest {

	@Test
	void check_mira_load_from_file_with_simple_network() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("simple/sites.tsv"), ResourceHelper.getResourcePath("simple/network.tsv"));
		check(mira);
	}

	@Test
	void check_mira_load_from_file_with_network_containing_not_unlocked_sites() throws IOException {
		Mira mira = MiraLoader.loadMira(ResourceHelper.getResourcePath("simple/sites.tsv"), ResourceHelper.getResourcePath("complete/network.tsv"));
		check(mira);
	}

	private static void check(Mira mira) {
		Site fn112 = new Site(112, MiraniumRank.A, RevenueRank.F);
		Site fn114 = new Site(114, MiraniumRank.C, RevenueRank.E);
		Site fn116 = new Site(116, MiraniumRank.A, RevenueRank.D);
		Site fn117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
		Mira expected = Mira.builder()
				.addSite(fn112).addSite(fn114).addSite(fn116).addSite(fn117)
				.addConnection(fn112, fn114).addConnection(fn114, fn116).addConnection(fn116, fn117)
				.build();

		assertThat(mira.getSites()).isEqualTo(expected.getSites());
	}
}