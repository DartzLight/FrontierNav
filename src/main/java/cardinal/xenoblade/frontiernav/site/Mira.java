package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mira {
	private final SimpleGraph<Site, DefaultEdge> graph;

	private Mira(SimpleGraph<Site, DefaultEdge> graph) {
		this.graph = graph;
	}

	public Set<Site> getSites() {
		return graph.vertexSet();
	}

	public Map<Integer, Site> getSitesByID() {
		return getSites()
				.stream()
				.collect(Collectors.toUnmodifiableMap(Site::id, Function.identity()));
	}

	public static Builder builder() {
		return new Builder();
	}

	public int computeChain(Site site, Map<Site, Probe> probes) {
		Probe probe = probes.get(site);
		if (probe == BasicProbe.DEFAULT) {
			return 1;
		}

		Set<Site> sameProbe = getSites()
				.stream()
				.filter(s -> probes.get(s) == probe)
				.collect(Collectors.toSet());
		Graph<Site, DefaultEdge> subgraph = new AsSubgraph<>(graph, sameProbe);

		ConnectivityInspector<Site, DefaultEdge> connectivity = new ConnectivityInspector<>(subgraph);
		return connectivity.connectedSetOf(site).size();
	}

	public static class Builder {
		private final SimpleGraph<Site, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		private Builder() {
		}

		public Builder addSite(Site site) {
			graph.addVertex(site);
			return this;
		}

		public Builder addConnection(Site site1, Site site2) {
			graph.addEdge(site1, site2);
			return this;
		}

		public Mira build() {
			return new Mira(graph);
		}
	}
}
