package gti310.tp3.solver;



import gti310.tp3.algorithms.DjikstraAlgorithm;
import gti310.tp3.models.Chemins;
import gti310.tp3.models.MatriceGraphe;

/**
 * 
 * @author Yanis
 * 
 */
public class MatriceGrapheSolver implements Solver<MatriceGraphe, Chemins> {

	
	@Override
	public Chemins solve(MatriceGraphe input) {
		
		int originId = input.getOrigine();
		int[][] meilleuresChemins = DjikstraAlgorithm.computePaths(input.getGraphe(), originId);
		Chemins chemins = new Chemins(originId, meilleuresChemins);
		
		return chemins;
	}
	
	

}
