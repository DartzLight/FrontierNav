package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.MiningProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.ResearchProbe;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class Map {
	private final SimpleGraph<Site, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

	public void addSite(Site site) {
		graph.addVertex(site);
	}

	public void addConnection(Site site1, Site site2) {
		graph.addEdge(site1, site2);
	}

	public int getMiranium() {
		return graph.vertexSet()
				.stream()
				.mapToInt(site -> compute(site, site::getMiranium, MiningProbe.class))
				.sum();
	}

	public int getRevenue() {
		return graph.vertexSet()
				.stream()
				.mapToInt(site -> compute(site, site::getRevenue, ResearchProbe.class))
				.sum();
	}

	private int compute(Site site, IntSupplier value, Class<? extends Probe> comboProbeType) {
		int comboMultiplier;
		if (comboProbeType.isInstance(site.probe())) {
			int chain = computeChain(site);
			comboMultiplier = getComboMultiplier(chain);
		} else {
			comboMultiplier = 100;
		}
		return value.getAsInt() * comboMultiplier / 100;
	}

	private int computeChain(Site site) {
		Probe probe = site.probe();
		if (probe == BasicProbe.DEFAULT) {
			return 1;
		}

		Set<Site> sameProbe = graph.vertexSet()
				.stream()
				.filter(s -> s.probe() == probe)
				.collect(Collectors.toSet());
		Graph<Site, DefaultEdge> subgraph = new AsSubgraph<>(graph, sameProbe);

		ConnectivityInspector<Site, DefaultEdge> connectivity = new ConnectivityInspector<>(subgraph);
		return connectivity.connectedSetOf(site).size();
	}

	private static int getComboMultiplier(int chain) {
		if (chain >= 8) {
			return 180;
		}
		if (chain >= 5) {
			return 150;
		}
		if (chain >= 3) {
			return 130;
		}
		return 100;
	}

}
