package gti310.tp3.models;

public class Chemins {

	private int origine;
	private int chemins[][];
	
	public Chemins(int origine, int[][] chemins){
		this.origine = origine;
		this.chemins = chemins;
	}

	public int[][] getChemins(){
		return chemins;
	}
	
	public int getOrigine(){
		return origine;
	}
	
}
