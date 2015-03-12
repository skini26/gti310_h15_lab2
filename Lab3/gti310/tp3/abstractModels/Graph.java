package gti310.tp3.abstractModels;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	  private final List<Vertex> vertices;
	  private final List<Edge> edges;

	  /*
	  public Graph(List<Vertex> vertices, List<Edge> edges) {
	    this.vertices = vertices;
	    this.edges = edges;
	  }
	  */

	  public Graph(List<? extends Vertex> vertices,
			List<? extends Edge> edges) {
		  this.vertices = (List<Vertex>) vertices;
		    this.edges = (List<Edge>) edges;
	}

	public List<Vertex> getVertices() {
	    return vertices;
	  }

	  public List<Edge> getEdges() {
	    return edges;
	  }
}
