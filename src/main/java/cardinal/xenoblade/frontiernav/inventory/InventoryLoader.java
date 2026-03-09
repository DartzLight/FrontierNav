package cardinal.xenoblade.frontiernav.inventory;

import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ProbeParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryLoader {

	private InventoryLoader() {
	}

	public static Inventory loadInventory(Path inventoryPath) throws IOException {
		try (var lines = Files.lines(inventoryPath)) {
			Map<Probe, Integer> countByProbes = lines.map(line -> line.split("\t"))
					.map(cells -> Map.entry(
							cells[0].transform(ProbeParser::parseProbe),
							cells[1].transform(Integer::parseInt)
					))
					.collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
			Collection<Probe> probes = new ArrayList<>();
			countByProbes.forEach((probe, count) -> {
				for (int i = 0; i < count; i++) {
					probes.add(probe);
				}
			});
			return new Inventory(probes);
		}
	}
}
