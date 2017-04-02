/**
 * 
 * The class Pair defines a simple structure
 * for holding and access two instances of Card.
 * Appropriate constructor and getters are provided.
 *
 */

public class Pair {
	private Card first;
	private Card second;
	
	public Pair(Card first, Card second) {
		if (first == null || second == null) {
			throw new NullPointerException("Cannot create an instance of Pair"
					+ " with a null value.");
			
		}
		this.first = first;
		this.second = second;
	}
	
	public Card getFirst() {
		return this.first;
	}
	
	public Card getSecond() {
		return this.second;
	}
}