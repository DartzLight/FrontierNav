package cardinal.xenoblade.frontiernav.serialization;

import cardinal.xenoblade.frontiernav.probe.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ImportExportFormat {
	static final String PROBE_TO_SITE_DELIMITER = "-";
	static final String SITES_DELIMITER = "~";

	private record ProbeEntry(Probe probe, int identifier) {}

	private static final List<ProbeEntry> PROBES = List.of(
			new ProbeEntry(BasicProbe.DEFAULT, 1),
			new ProbeEntry(MiningProbe.G1, 2),
			new ProbeEntry(MiningProbe.G2, 3),
			new ProbeEntry(MiningProbe.G3, 4),
			new ProbeEntry(MiningProbe.G4, 5),
			new ProbeEntry(MiningProbe.G5, 6),
			new ProbeEntry(MiningProbe.G6, 7),
			new ProbeEntry(MiningProbe.G7, 8),
			new ProbeEntry(MiningProbe.G8, 9),
			new ProbeEntry(MiningProbe.G9, 10),
			new ProbeEntry(MiningProbe.G10, 11),
			new ProbeEntry(ResearchProbe.G1, 12),
			new ProbeEntry(ResearchProbe.G2, 13),
			new ProbeEntry(ResearchProbe.G3, 14),
			new ProbeEntry(ResearchProbe.G4, 15),
			new ProbeEntry(ResearchProbe.G5, 16),
			new ProbeEntry(ResearchProbe.G6, 17),
			new ProbeEntry(BoosterProbe.G1, 18),
			new ProbeEntry(BoosterProbe.G2, 19),
			new ProbeEntry(DuplicatorProbe.DEFAULT, 20),
			new ProbeEntry(StorageProbe.DEFAULT, 21)
	);
	static final Map<Probe, Integer> PROBE_TO_ID_MAP = PROBES.stream().collect(Collectors.toUnmodifiableMap(ProbeEntry::probe, ProbeEntry::identifier));
	static final Map<Integer, Probe> ID_TO_PROBE_MAP = PROBES.stream().collect(Collectors.toUnmodifiableMap(ProbeEntry::identifier, ProbeEntry::probe));

	private ImportExportFormat() {
	}
}
