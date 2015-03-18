package gti310.tp3;

import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import gti310.tp3.models.Chemins;
import gti310.tp3.models.MatriceGraphe;
import gti310.tp3.parser.MatriceGraphParser;
import gti310.tp3.parser.Parser;
import gti310.tp3.solver.MatriceGrapheSolver;
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
		
		System.out.println(System.getProperty("user.dir"));
		
		Parser<MatriceGraphe> parser = new MatriceGraphParser();
		MatriceGraphe graphe = null;
		
		graphe = parser.parse(args[0]);
		if(graphe == null){
			System.err.println("invalid file");
			System.exit(-1);
		}

		Solver<MatriceGraphe,Chemins> solver = new MatriceGrapheSolver();
		Chemins chemins = solver.solve(graphe);
		Writer<Chemins> writer = new ConcreteWriter();
		writer.write(args[1], chemins);
		
	}
}
