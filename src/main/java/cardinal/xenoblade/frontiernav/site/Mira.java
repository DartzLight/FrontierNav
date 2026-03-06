package cardinal.xenoblade.frontiernav.site;

import cardinal.xenoblade.frontiernav.probe.BasicProbe;
import cardinal.xenoblade.frontiernav.probe.Probe;
import cardinal.xenoblade.frontiernav.probe.layout.ProbeLayout;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
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
	private final Set<Site> sites;
	private final Map<Integer, Site> siteByID;

	private Mira(SimpleGraph<Site, DefaultEdge> graph) {
		this.graph = graph;
		this.sites = Set.copyOf(graph.vertexSet());
		this.siteByID = sites.stream()
				.collect(Collectors.toUnmodifiableMap(Site::id, Function.identity()));
	}

	public static Builder builder() {
		return new Builder();
	}

	public Set<Site> getSites() {
		return sites;
	}

	public Site getSite(int siteID) {
		return siteByID.get(siteID);
	}

	public int computeChain(Site site, ProbeLayout probeLayout) {
		Probe probe = probeLayout.getProbe(site);
		if (probe == BasicProbe.DEFAULT) {
			return 1;
		}

		Set<Site> sameProbe = sites.stream()
				.filter(s -> probe.equals(probeLayout.getProbe(s)))
				.collect(Collectors.toSet());
		Graph<Site, DefaultEdge> subgraph = new AsSubgraph<>(graph, sameProbe);

		ConnectivityInspector<Site, DefaultEdge> connectivity = new ConnectivityInspector<>(subgraph);
		return connectivity.connectedSetOf(site).size();
	}

	public Set<Site> getConnectedSites(Site site) {
		return Graphs.neighborSetOf(graph, site);
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
