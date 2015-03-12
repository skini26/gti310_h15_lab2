package gti310.tp3.models;

import gti310.tp3.abstractModels.Edge;

public class Chemin extends Edge {

	public Chemin(Ville origine, Ville destination, int distance) {
		super(origine, destination, distance);
	}
	
	public Ville getOrigine(){
		return (Ville) super.getSource();
	}
	
	public Ville getDestination(){
		return (Ville) super.getDestination();
	}
	
	public int getDistance(){
		return super.getWeight();
	}
	
	public String toString(){
		return ((Ville) super.getSource()).toString() + " --> " + ((Ville) super.getDestination()).toString();
	}

}
