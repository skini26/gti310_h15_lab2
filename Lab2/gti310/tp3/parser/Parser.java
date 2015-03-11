package gti310.tp3.parser;

/**
 * The Parser interface defines the method through which an input file will be
 * interpreted and an output object will be returned. For greater flexibility,
 * the output object produced from the method is not defined. The inconvenience
 * is that two specialized classes can produce different outputs. The owner
 * will have to know in advance what type of object will be produced by the
 * method when declaring the object : 
 * 
 *      public class ConcreteParser implements Parser<MyObject> { ... }
 *      
 * 		Parser<MyObject> parser = new ConcreteParse();
 * 
 * @author François Caron <francois.caron.7@ens.etsmtl.ca>
 *
 * @param <E> A template that must be redifined by the implementations of the
 *            Parser interface.
 */
public interface Parser<E> {

	/**
	 * The parse method is designed to scan an input file and produce an output
	 * object defined by the <E> templace. The method should return null if the
	 * file cannot be read, or if the file structure is different than what is
	 * awaited.
	 * 
	 * @param filename The complete path to the file to parse.
	 * @return An object produced from the input file, or null if something
	 * 		   went wrong.
	 */
	E parse(String filename);
}
