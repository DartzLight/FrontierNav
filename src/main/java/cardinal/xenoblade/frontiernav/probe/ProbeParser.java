package cardinal.xenoblade.frontiernav.probe;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ProbeParser {
	private ProbeParser() {
	}

	public static Optional<Probe> tryParseProbe(String string) {
		return Optional.ofNullable(
				switch (string) {
					case "M1" -> MiningProbe.G1;
					case "M2" -> MiningProbe.G2;
					case "M3" -> MiningProbe.G3;
					case "M4" -> MiningProbe.G4;
					case "M5" -> MiningProbe.G5;
					case "M6" -> MiningProbe.G6;
					case "M7" -> MiningProbe.G7;
					case "M8" -> MiningProbe.G8;
					case "M9" -> MiningProbe.G9;
					case "M10" -> MiningProbe.G10;
					case "R1" -> ResearchProbe.G1;
					case "R2" -> ResearchProbe.G2;
					case "R3" -> ResearchProbe.G3;
					case "R4" -> ResearchProbe.G4;
					case "R5" -> ResearchProbe.G5;
					case "R6" -> ResearchProbe.G6;
					case "S" -> StorageProbe.DEFAULT;
					case "B1" -> BoosterProbe.G1;
					case "B2" -> BoosterProbe.G2;
					case "D" -> DuplicatorProbe.DEFAULT;
					case "-" -> BasicProbe.DEFAULT;
					default -> null;
				}
		);
	}

	public static Probe parseProbe(String string) {
		return tryParseProbe(string)
				.orElseThrow(() -> new NoSuchElementException("Invalid probe: " + string));
	}
}
