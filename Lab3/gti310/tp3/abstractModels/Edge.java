package gti310.tp3.abstractModels;

public class Edge {

	private final Vertex source;
	private final Vertex destination;
	private final int weight;
	
	public Edge(Vertex source, Vertex destination, int weight){
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public Vertex getSource() {
		return source;
	}

	public Vertex getDestination() {
		return destination;
	}

	public int getWeight() {
		return weight;
	}
	
	public String toString(){
		return source.toString() + " " + destination.toString();
		
	}
	
}
