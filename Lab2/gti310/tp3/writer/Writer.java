package gti310.tp3.writer;

/**
 * The Writer interface defines a method to write user-defined objects to file.
 * The classes implementing the Writer interface should explicitaly define the
 * object they will be wrinting to file.
 * 
 * 		public class MyWriter implements Writer<MyObject> { ... }
 * 
 * The caller will need to know what type of objects it will be writing. This
 * limits the run-time flexibility since two different implementations of the
 * Writer interface will write to file two different kinds of object.
 * 
 * 		Writer<AnObject> writer = new AnObjectWriter();
 * 
 * @author François Caron <francois.caron.7@ens.etsmtl.ca>
 *
 * @param <T> The user-defined object that will be written to file. 
 */
public interface Writer<T> {

	/**
	 * Write the given object's data to file.
	 * @param filename The complete path to the file that will be created.
	 * @param output The data which will be written to the file.
	 */
	void write(String filename, T output);
}
