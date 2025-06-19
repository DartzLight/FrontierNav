package cardinal.xenoblade.frontiernav.site;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

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
				.mapToInt(Site::getMiranium)
				.sum();
	}

	public int getRevenue() {
		return graph.vertexSet()
				.stream()
				.mapToInt(Site::getRevenue)
				.sum();
	}
}
