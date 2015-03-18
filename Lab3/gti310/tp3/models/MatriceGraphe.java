package gti310.tp3.models;

/**
 * Graphe implemente avec une matrice
 * @author Yanis
 *
 */
public class MatriceGraphe {

	public final static int INFINI = (int) Double.POSITIVE_INFINITY; 
	
	private int origine;
	private int graphe[][];
	
	public MatriceGraphe(int nbDeVilles, int villeOrigine){
		this.origine = villeOrigine;
		this.graphe = new int[nbDeVilles][nbDeVilles];
	}

	public int[][] getGraphe(){
		return graphe;
	}
	
	public int getOrigine(){
		return origine;
	}
	
	//Pas besoin de mutateurs, le graph etant un tableau de primitive,
	//il est passe par reference et non pas par copie de valeur
	
	
}
