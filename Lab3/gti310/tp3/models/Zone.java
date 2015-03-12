package gti310.tp3.models;

import java.util.ArrayList;
import java.util.List;

import gti310.tp3.abstractModels.Edge;
import gti310.tp3.abstractModels.Graph;
import gti310.tp3.abstractModels.Vertex;

public class Zone extends Graph {

	private Ville villeDepart;
	
	public Zone(ArrayList<? extends Vertex> villes, ArrayList<? extends Edge> chemins, Ville villeDepart) {
		super(villes, chemins);
		this.villeDepart = villeDepart;
	}
	
	public List<Vertex> getVilles(){
		return super.getVertices();
	}
	
	public List<Edge> getChemins(){
		return super.getEdges();
	}
	
	public Ville getVilleDepart(){
		return villeDepart;
	}

}
