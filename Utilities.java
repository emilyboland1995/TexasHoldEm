import java.util.Set;

public class Utilities {
	
	
	
	/**
	 * 
	 * @param set		The Set<T> the objects in data will be added to
	 * @param data		The T[] containing all the objects to be added
	 * 					to set
	 */
	public static <T> void addToSet(Set<T> set, T[] data) {
		for (T value : data) {
			set.add(value);
		}
	}
}
