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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mira {
	private final SimpleGraph<Site, DefaultEdge> graph;
	private final SequencedSet<Site> sites;
	private final Map<Integer, Site> siteByID;
	private final Map<PreciousResource, Double> maxPreciousResources;

	private Mira(SimpleGraph<Site, DefaultEdge> graph) {
		LinkedHashSet<Site> sortedSites = graph.vertexSet()
				.stream()
				.sorted(Comparator.comparing(Site::id))
				.collect(Collectors.toCollection(LinkedHashSet::new));
		this.graph = graph;
		this.sites = Collections.unmodifiableSequencedSet(sortedSites);
		this.siteByID = sites.stream()
				.collect(Collectors.toUnmodifiableMap(Site::id, Function.identity()));
		this.maxPreciousResources = getSites()
				.stream()
				.flatMap(site -> site.preciousResources().entrySet().stream())
				.collect(Collectors.groupingBy(
						Map.Entry::getKey,
						Collectors.summingDouble(Map.Entry::getValue)));
	}

	public static Builder builder() {
		return new Builder();
	}

	public SequencedSet<Site> getSites() {
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

	public Map<PreciousResource, Double> getMaximumPreciousResources() {
		return maxPreciousResources;
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
