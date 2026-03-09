package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MutableInventoryTest {

	@Test
	void should_remove_one_random_probe() {
		Inventory initialInventory = new Inventory(List.of(MiningProbe.G1, ResearchProbe.G1));
		MutableInventory mutableInventory = initialInventory.getMutableInventory();
		Random random = new Random(777);

		assertThat(initialInventory.getProbes()).hasSize(2);
		assertThat(mutableInventory.getAvailableProbes()).hasSize(2);

		Probe probe = mutableInventory.takeRandomProbe(random);

		assertThat(probe).isEqualTo(ResearchProbe.G1);
		assertThat(mutableInventory.getAvailableProbes()).hasSize(1);
	}

	@Test
	void should_take_basic_probe_if_inventory_is_empty() {
		Inventory initialInventory = new Inventory(List.of());
		MutableInventory mutableInventory = initialInventory.getMutableInventory();
		Random random = new Random(777);

		assertThat(initialInventory.getProbes()).isEmpty();
		assertThat(mutableInventory.getAvailableProbes()).isEmpty();

		Probe probe = mutableInventory.takeRandomProbe(random);

		assertThat(probe).isEqualTo(BasicProbe.DEFAULT);
		assertThat(mutableInventory.getAvailableProbes()).isEmpty();
	}

}