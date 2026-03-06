package cardinal.xenoblade.frontiernav.exporter;

import cardinal.xenoblade.frontiernav.FrontierNav;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import cardinal.xenoblade.frontiernav.site.Mira;
import cardinal.xenoblade.frontiernav.site.MiraLoader;
import cardinal.xenoblade.frontiernav.site.Site;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class FrontierNavImporter {

	private FrontierNavImporter() {
	}

	public static Map<Site, Probe> importProbes(Mira mira, String encoded) {
		return Arrays.stream(encoded.split(ImportExportFormat.SITES_DELIMITER))
				.map(token -> token.split(ImportExportFormat.PROBE_TO_SITE_DELIMITER))
				.collect(Collectors.toUnmodifiableMap(tokens -> {
					int siteID = tokens[0].transform(Integer::parseInt);
					return mira.getSite(siteID);
				}, tokens -> {
					int probeID = tokens[1].transform(Integer::parseInt);
					if (probeID == 0) {
						throw new IllegalArgumentException("Missing probe for site: " + tokens[0]);
					}
					return ImportExportFormat.ID_TO_PROBE_MAP.get(probeID);
				}));
	}

	public static FrontierNav importFrontierNav(Mira mira, String encoded) {
		Map<Site, Probe> probes = importProbes(mira, encoded);
		return new FrontierNav(mira, new ProbeLayout(probes));
	}

	static void main() throws IOException {
		Mira mira = MiraLoader.loadMira(Path.of("input/sites.tsv"), Path.of("input/network.tsv"));
		String string = "101-2~102-21~103-13~104-15~105-4~106-21~107-8~108-4~109-4~110-5~111-9~112-5~113-9~114-5~115-5~116-5~117-12~118-3~119-21~120-6~121-3~201-18~202-5~203-18~204-20~205-11~206-20~207-8~208-5~209-21~210-21~211-11~212-11~213-15~214-16~215-5~216-14~217-6~218-5~219-3~220-6~221-16~222-6~223-3~224-21~225-4~301-3~302-3~303-21~304-21~305-6~306-21~307-16~308-21~309-6~310-21~311-21~312-19~313-20~314-17~315-20~316-17~317-17~318-17~319-17~320-21~321-21~322-21~401-7~402-11~403-9~404-7~405-9~406-10~407-7~408-20~409-9~410-12~411-9~412-7~413-10~414-15~415-21~416-15~417-18~418-9~419-20~420-15~501-6~502-8~503-3~504-4~505-9~506-12~507-13~508-10~509-9~510-3~511-11~512-21~513-15~514-14~515-2~516-3";
		Map<Site, Probe> probes = importProbes(mira, string);
		FrontierNav frontierNav = new FrontierNav(mira, new ProbeLayout(probes));
		System.out.println("Miranium: " + frontierNav.getMiranium());
		System.out.println("Revenue: " + frontierNav.getRevenue());
		System.out.println("Storage: " + frontierNav.getStorage());
	}

}
