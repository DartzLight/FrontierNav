package cardinal.xenoblade.frontiernav.probe;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProbeParserTest {
	private static Stream<Arguments> probeTestCases() {
		return Stream.of(
				Arguments.of("M1", Optional.of(MiningProbe.G1)),
				Arguments.of("M2", Optional.of(MiningProbe.G2)),
				Arguments.of("M3", Optional.of(MiningProbe.G3)),
				Arguments.of("M4", Optional.of(MiningProbe.G4)),
				Arguments.of("M5", Optional.of(MiningProbe.G5)),
				Arguments.of("M6", Optional.of(MiningProbe.G6)),
				Arguments.of("M7", Optional.of(MiningProbe.G7)),
				Arguments.of("M8", Optional.of(MiningProbe.G8)),
				Arguments.of("M9", Optional.of(MiningProbe.G9)),
				Arguments.of("M10", Optional.of(MiningProbe.G10)),
				Arguments.of("R1", Optional.of(ResearchProbe.G1)),
				Arguments.of("R2", Optional.of(ResearchProbe.G2)),
				Arguments.of("R3", Optional.of(ResearchProbe.G3)),
				Arguments.of("R4", Optional.of(ResearchProbe.G4)),
				Arguments.of("R5", Optional.of(ResearchProbe.G5)),
				Arguments.of("R6", Optional.of(ResearchProbe.G6)),
				Arguments.of("S", Optional.of(StorageProbe.DEFAULT)),
				Arguments.of("B1", Optional.of(BoosterProbe.G1)),
				Arguments.of("B2", Optional.of(BoosterProbe.G2)),
				Arguments.of("D", Optional.of(DuplicatorProbe.DEFAULT)),
				Arguments.of("-", Optional.of(BasicProbe.DEFAULT)),
				Arguments.of("invalid", Optional.empty())
		);
	}

	@ParameterizedTest
	@MethodSource("probeTestCases")
	void check_try_parse_probe(String input, Optional<Probe> expected) {
		assertThat(ProbeParser.tryParseProbe(input)).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("probeTestCases")
	void check_parse_probe(String input, Optional<Probe> expected) {
		expected.ifPresentOrElse(
				probe -> assertThat(ProbeParser.parseProbe(input)).isEqualTo(probe),
				() -> assertThatThrownBy(() -> ProbeParser.parseProbe(input)).isInstanceOf(NoSuchElementException.class)
		);
	}
}
