package gti310.tp3.models;

/**
 * Classe representant les meilleures chemins dans un tableau
 * par rapport 'a un point d'origine
 * @author Yanis
 *
 */
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
