package gti310.tp3.solver;

/**
 * The Solver interface presents a method definition to complete a given task.
 * The Solver uses the output produced by a Parser object and returns an
 * object. The object is not defined to allow greater flexibility. On the
 * downside, the added flexibility also bring disadvantages. The caster must
 * know the type of the object that will be returned by the Solver beforehand.
 * 
 * 		public class ConcreteSolver implements Solver<InObj, OutObj> { ... }
 * 
 * 		Solver<InObj,OutObj> solver = new ConcreteSolver();
 * 
 *  The input object must match the one produced by the Parser object that is
 *  handing in the data.
 *  @see Parser
 *  
 * @author François Caron <francois.caron.7@ens.etsmtl.ca>
 *
 * @param <E> The template defining the awaited input object.
 * @param <T> The template defining the output object to return.
 */
public interface Solver<E,T> {

	/**
	 * Complete the required task based on the input E and return an output
	 * object T. The method should return null if the task cannot be completed
	 * properly.
	 * 
	 * @param input The object required to complete the task.
	 * @return A user defined object, or null if something went wrong.
	 */
	T solve(E input);
}
