package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryTest {

	@Test
	void should_contains_no_basic_probes() {
		Inventory inventory = new Inventory(List.of(BasicProbe.DEFAULT));

		assertThat(inventory.getProbes()).doesNotContain(BasicProbe.DEFAULT);
		assertThat(inventory.getProbes()).isEmpty();
	}

}