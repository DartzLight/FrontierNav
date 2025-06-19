package cardinal.xenoblade.frontiernav;

import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavIntegrationTest {

	@Test
	void check_default() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		FrontierNav frontierNav = new FrontierNav(mira);
		assertThat(frontierNav.getMiranium()).isEqualTo(13400);
		assertThat(frontierNav.getRevenue()).isEqualTo(21475);
		assertThat(frontierNav.getMiraniumStorage()).isEqualTo(6000);
	}

}