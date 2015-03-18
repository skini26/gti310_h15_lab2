package gti310.tp3.solver;



import gti310.tp3.algorithms.DjikstraAlgorithm;
import gti310.tp3.models.Chemins;
import gti310.tp3.models.MatriceGraphe;

/**
 * 
 * @author Yanis
 * 
 */
public class ConcreteSolver implements Solver<MatriceGraphe, Chemins> {

	
	@Override
	public Chemins solve(MatriceGraphe input) {
		
		//-1 parceque l'index de la matrice commence 'a 0
		int originId = input.getOrigine() - 1;
		int[][] meilleuresChemins = DjikstraAlgorithm.computePaths(input.getGraphe(), originId);
		Chemins chemins = new Chemins(originId, meilleuresChemins);
		
		return chemins;
	}
	
	

}
