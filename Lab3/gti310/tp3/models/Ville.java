package gti310.tp3.models;

import gti310.tp3.abstractModels.Vertex;

public class Ville extends Vertex {

	private String nom;
	
	public Ville(long id, String nom) {
		super(id);
		this.nom = nom;
	}

	public String getNom(){
		return this.getNom();
	}
	
	public String toString(){
		return super.toString()+", "+this.getNom();
	}
}
