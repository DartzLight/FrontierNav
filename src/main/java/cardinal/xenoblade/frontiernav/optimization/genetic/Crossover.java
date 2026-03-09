package cardinal.xenoblade.frontiernav.optimization.genetic;

import cardinal.xenoblade.frontiernav.inventory.Inventory;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;

public interface Crossover {

	ProbeLayout crossover(Mira mira, ProbeLayout parent1, ProbeLayout parent2, Inventory inventory);
	
}
