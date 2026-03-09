package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;

public class DummyMutation implements Mutation {

	@Override
	public ProbeLayout mutation(Mira mira, ProbeLayout origin, Inventory inventory) {
		return origin;
	}

}
