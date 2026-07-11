package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.ResourceHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MiraLoaderTest {

	private static final Path SIMPLE_SITES_PATH = ResourceHelper.getResourcePath("simple/sites.tsv");
	private static final Path SIMPLE_NETWORK_PATH = ResourceHelper.getResourcePath("simple/network.tsv");
	private static final Path SIMPLE_RESOURCES_PATH = ResourceHelper.getResourcePath("simple/resources.tsv");
	private static final Path COMPLETE_NETWORK_PATH = ResourceHelper.getResourcePath("complete/network.tsv");
	private static final Path COMPLETE_RESOURCES_PATH = ResourceHelper.getResourcePath("complete/resources.tsv");

	@Test
	void check_mira_load_from_file_with_simple_network() throws IOException {
		Mira mira = MiraLoader.loadMira(SIMPLE_SITES_PATH, SIMPLE_NETWORK_PATH, SIMPLE_RESOURCES_PATH);
		check(mira);
	}

	@Test
	void check_mira_load_from_file_with_network_containing_not_unlocked_sites() throws IOException {
		Mira mira = MiraLoader.loadMira(SIMPLE_SITES_PATH, COMPLETE_NETWORK_PATH, SIMPLE_RESOURCES_PATH);
		check(mira);
	}

	@Test
	void check_mira_load_from_file_with_precious_resources_containing_not_unlocked_sites() throws IOException {
		Mira mira = MiraLoader.loadMira(SIMPLE_SITES_PATH, SIMPLE_NETWORK_PATH, COMPLETE_RESOURCES_PATH);
		check(mira);
	}

	private static void check(Mira mira) {
		Site fn112 = new Site(112, MiraniumRank.A, RevenueRank.F);
		Site fn114 = new Site(114, MiraniumRank.C, RevenueRank.E);
		Site fn115 = new Site(115, MiraniumRank.C, RevenueRank.D, 0, Map.of(PreciousResource.ARC_SAND_ORE, 0.48d, PreciousResource.LIONBONE_BORT, 0.72d, PreciousResource.WHITE_COMETITE, 0.84d));
		Site fn116 = new Site(116, MiraniumRank.A, RevenueRank.D);
		Site fn117 = new Site(117, MiraniumRank.A, RevenueRank.D, 1);
		Mira expected = Mira.builder()
				.addSite(fn112).addSite(fn114).addSite(fn115).addSite(fn116).addSite(fn117)
				.addConnection(fn115, fn112).addConnection(fn112, fn114).addConnection(fn114, fn116).addConnection(fn116, fn117)
				.build();

		assertThat(mira.getSites()).isEqualTo(expected.getSites());
	}
}