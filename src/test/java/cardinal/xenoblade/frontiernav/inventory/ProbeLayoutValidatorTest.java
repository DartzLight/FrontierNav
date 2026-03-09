package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProbeLayoutValidatorTest {

	private static final Site SITE_1 = new Site(1, MiraniumRank.A, RevenueRank.A);
	private static final Site SITE_2 = new Site(2, MiraniumRank.A, RevenueRank.A);

	@Test
	void check_valid_layout_when_all_probes_in_inventory() {
		ProbeLayout probeLayout = new ProbeLayout(Map.of(SITE_1, MiningProbe.G1, SITE_2, ResearchProbe.G1));
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, ResearchProbe.G1, StorageProbe.DEFAULT));

		assertThat(ProbeLayoutValidator.searchForInvalidProbes(probeLayout, inventory))
				.isEmpty();
	}

	@Test
	void check_valid_layout_when_basic_probe() {
		ProbeLayout probeLayout = new ProbeLayout(Map.of(SITE_1, MiningProbe.G1, SITE_2, BasicProbe.DEFAULT));
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, ResearchProbe.G1, StorageProbe.DEFAULT));

		assertThat(ProbeLayoutValidator.searchForInvalidProbes(probeLayout, inventory))
				.isEmpty();
	}

	@Test
	void check_invalid_layout_when_too_much_probes() {
		ProbeLayout probeLayout = new ProbeLayout(Map.of(SITE_1, MiningProbe.G1, SITE_2, MiningProbe.G1));
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, ResearchProbe.G1, StorageProbe.DEFAULT));

		assertThat(ProbeLayoutValidator.searchForInvalidProbes(probeLayout, inventory))
				.containsExactlyInAnyOrder(MiningProbe.G1);
	}

	@Test
	void check_invalid_layout_when_missing_probes() {
		ProbeLayout probeLayout = new ProbeLayout(Map.of(SITE_1, BoosterProbe.G1, SITE_2, BoosterProbe.G2));
		Inventory inventory = new Inventory(List.of(MiningProbe.G1, ResearchProbe.G1, StorageProbe.DEFAULT));

		assertThat(ProbeLayoutValidator.searchForInvalidProbes(probeLayout, inventory))
				.containsExactlyInAnyOrder(BoosterProbe.G1, BoosterProbe.G2);
	}

}