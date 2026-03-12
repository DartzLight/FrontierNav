package cardinal.xenoblade.frontiernav.serialization;

import cardinal.xenoblade.frontiernav.probe.*;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraniumRank;
import cardinal.xenoblade.frontiernav.site.RevenueRank;
import cardinal.xenoblade.frontiernav.site.Site;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class FrontierNavExporterTest {

	private static final Map<Integer, Site> SITES = IntStream.rangeClosed(101, 121)
			.mapToObj(FrontierNavExporterTest::newSite)
			.collect(Collectors.toUnmodifiableMap(Site::id, Function.identity()));

	@Test
	void should_export_configuration() {
		// Given
		Mira mira = buildMira();
		Map<Site, Probe> probes = buildProbes();
		ProbeLayout probeLayout = new ProbeLayout(probes);

		// When
		String exported = FrontierNavExporter.exportToString(mira, probeLayout);

		// Then
		assertThat(exported).isEqualTo(
				"101-1~102-2~103-3~104-4~105-5~106-6~107-7~108-8~109-9~110-10~111-11~112-12~113-13~114-14~115-15~116-16~117-17~118-18~119-19~120-20~121-21" +
						"~201-0~202-0~203-0~204-0~205-0~206-0~207-0~208-0~209-0~210-0~211-0~212-0~213-0~214-0~215-0~216-0~217-0~218-0~219-0~220-0~221-0~222-0~223-0~224-0~225-0" +
						"~301-0~302-0~303-0~304-0~305-0~306-0~307-0~308-0~309-0~310-0~311-0~312-0~313-0~314-0~315-0~316-0~317-0~318-0~319-0~320-0~321-0~322-0" +
						"~401-0~402-0~403-0~404-0~405-0~406-0~407-0~408-0~409-0~410-0~411-0~412-0~413-0~414-0~415-0~416-0~417-0~418-0~419-0~420-0" +
						"~501-0~502-0~503-0~504-0~505-0~506-0~507-0~508-0~509-0~510-0~511-0~512-0~513-0~514-0~515-0~516-0"
		);
	}

	private static Mira buildMira() {
		return Mira.builder()
				.addSite(SITES.get(101))
				.addSite(SITES.get(102))
				.addSite(SITES.get(103))
				.addSite(SITES.get(104))
				.addSite(SITES.get(105))
				.addSite(SITES.get(106))
				.addSite(SITES.get(107))
				.addSite(SITES.get(108))
				.addSite(SITES.get(109))
				.addSite(SITES.get(110))
				.addSite(SITES.get(111))
				.addSite(SITES.get(112))
				.addSite(SITES.get(113))
				.addSite(SITES.get(114))
				.addSite(SITES.get(115))
				.addSite(SITES.get(116))
				.addSite(SITES.get(117))
				.addSite(SITES.get(118))
				.addSite(SITES.get(119))
				.addSite(SITES.get(120))
				.addSite(SITES.get(121))
				.build();
	}

	private static Map<Site, Probe> buildProbes() {
		return Map.ofEntries(
				Map.entry(SITES.get(101), BasicProbe.DEFAULT),
				Map.entry(SITES.get(102), MiningProbe.G1),
				Map.entry(SITES.get(103), MiningProbe.G2),
				Map.entry(SITES.get(104), MiningProbe.G3),
				Map.entry(SITES.get(105), MiningProbe.G4),
				Map.entry(SITES.get(106), MiningProbe.G5),
				Map.entry(SITES.get(107), MiningProbe.G6),
				Map.entry(SITES.get(108), MiningProbe.G7),
				Map.entry(SITES.get(109), MiningProbe.G8),
				Map.entry(SITES.get(110), MiningProbe.G9),
				Map.entry(SITES.get(111), MiningProbe.G10),
				Map.entry(SITES.get(112), ResearchProbe.G1),
				Map.entry(SITES.get(113), ResearchProbe.G2),
				Map.entry(SITES.get(114), ResearchProbe.G3),
				Map.entry(SITES.get(115), ResearchProbe.G4),
				Map.entry(SITES.get(116), ResearchProbe.G5),
				Map.entry(SITES.get(117), ResearchProbe.G6),
				Map.entry(SITES.get(118), BoosterProbe.G1),
				Map.entry(SITES.get(119), BoosterProbe.G2),
				Map.entry(SITES.get(120), DuplicatorProbe.DEFAULT),
				Map.entry(SITES.get(121), StorageProbe.DEFAULT)
		);
	}

	private static Site newSite(int id) {
		return new Site(id, MiraniumRank.A, RevenueRank.A);
	}

}