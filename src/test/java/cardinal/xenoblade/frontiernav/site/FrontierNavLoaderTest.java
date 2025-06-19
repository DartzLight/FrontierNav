package cardinal.xenoblade.frontiernav.site;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavLoaderTest {

	@Test
	void check_default() throws IOException {
		FrontierNav map = FrontierNavLoader.loadMap(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		assertThat(map.getMiranium()).isEqualTo(13400);
		assertThat(map.getRevenue()).isEqualTo(21475);
	}
	
}