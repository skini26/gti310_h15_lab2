package gti310.tp3.solver;



import gti310.tp3.algorithms.DjikstraAlgorithm;
import gti310.tp3.models.MatriceGraphe;
import gti310.tp3.models.Zone;

/**
 * 
 * @author Yanis
 * 
 */
public class MatriceGrapheSolver implements Solver<MatriceGraphe, MatriceGraphe> {

	private DjikstraAlgorithm djikstra;
	
	@Override
	public MatriceGraphe solve(MatriceGraphe input) {
		
		int nbOfVertice = input.getGraphe().length;
		int[][] outputGraph = new int[nbOfVertice][2];
		int originId = input.getOrigine();
		
		
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
