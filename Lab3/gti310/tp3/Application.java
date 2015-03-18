package gti310.tp3;


import gti310.tp3.models.Chemins;
import gti310.tp3.models.MatriceGraphe;
import gti310.tp3.parser.ConcreteParser;
import gti310.tp3.parser.Parser;
import gti310.tp3.solver.ConcreteSolver;
import gti310.tp3.solver.Solver;
import gti310.tp3.writer.ConcreteWriter;
import gti310.tp3.writer.Writer;

/**
 * The Application class defines a template method to call the elements to
 * solve the problem Unreal-Networks is façing.
 * 
 * @author François Caron <francois.caron.7@ens.etsmtl.ca>
 */
public class Application {

	/**
	 * The Application's entry point.
	 * 
	 * The main method makes a series of calls to find a solution to the
	 * problem. The program awaits two arguments, the complete path to the
	 * input file, and the complete path to the output file.
	 * 
	 * @param args The array containing the arguments to the files.
	 */
	public static void main(String args[]) {
		System.out.println("Unreal Networks Solver !");
		
		//System.out.println(System.getProperty("user.dir"));
		
		//Creer le parser qui decodera le fichier texte
		Parser<MatriceGraphe> parser = new ConcreteParser();
		//Parser le fichier texte pour obtenir un graphe en matrice
		MatriceGraphe graphe = parser.parse(args[0]);
		//Si le fichier n'est pas conforme, quitter le programme
		if(graphe == null){
			System.err.println("invalid file");
			System.exit(-1);
		}
		//Creer le solver qui resoudra le probleme
		Solver<MatriceGraphe,Chemins> solver = new ConcreteSolver();
		//Executer le solver afin d'obtenir les meilleures chemins
		//dans le graphe grace 'a l'algorithme de Dijkstra
		Chemins chemins = solver.solve(graphe);
		
		//Ecrire le fichier de sortie
		Writer<Chemins> writer = new ConcreteWriter();
		writer.write(args[1], chemins);
		
	}
}
