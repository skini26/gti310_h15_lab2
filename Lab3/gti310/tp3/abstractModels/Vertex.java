package gti310.tp3.abstractModels;

public class Vertex {

	private final long id;
	
	public Vertex(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public String toString(){
		return Long.toString(id);
	}
}
