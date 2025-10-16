package cardinal.xenoblade.frontiernav.exporter;

import cardinal.xenoblade.frontiernav.FrontierNav;
import cardinal.xenoblade.frontiernav.probe.*;
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

class FrontierNavImporterTest {

	private static final Map<Integer, Site> SITES = IntStream.rangeClosed(101, 121)
			.mapToObj(FrontierNavImporterTest::newSite)
			.collect(Collectors.toUnmodifiableMap(Site::id, Function.identity()));

	@Test
	void should_import_configuration() {
		// Given
		Mira mira = buildMira();

		// When
		FrontierNav frontierNav = FrontierNavImporter.importFrontierNav(mira, "101-1~102-2~103-3~104-4~105-5~106-6~107-7~108-8~109-9~110-10~111-11~112-12~113-13~114-14~115-15~116-16~117-17~118-18~119-19~120-20~121-21");

		// Then
		Map<Site, Probe> probes = frontierNav.getProbes();
		assertThat(probes).isEqualTo(
				Map.ofEntries(
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
				)
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

	private static Site newSite(int id) {
		return new Site(id, MiraniumRank.A, RevenueRank.A);
	}

}
