import java.util.Arrays;

public class HandRanker {
	
	// Relative hand rankings by type
	private static final int STRAIGHT_FLUSH = 900000;
	private static final int FOUR_OF_A_KIND = 800000;
	private static final int FULL_HOUSE = 700000;
	private static final int FLUSH = 600000;
	private static final int STAIGHT = 500000;
	private static final int THREE_OF_A_KIND = 400000;
	private static final int TWO_PAIRS = 300000;
	private static final int PAIR = 200000;
	private static final int HIGH_CARD = 100000;
	
	/**
	 * 
	 * @param hand	Hand sorted by rank
	 * @return		True if hand contains four
	 * 				of a kind, false if otherwise
	 */
	private static boolean isFourOfAKind(Card[] hand) {
		boolean test1 = hand[0].rank() == h[1].rank() 
				&& hand[1].rank() == h[2].rank() 
				&& hand[2].rank() == h[3].rank(); 
		
		boolean test2 = hand[1].rank() == h[2].rank() 
				&& hand[2].rank() == h[3].rank() 
				&& hand[3].rank() == h[4].rank(); 
		return test1 || test2;
	}
	/**
	 * 
	 * @param hand	Hand sorted by rank
	 * @return		True if hand contains
	 * 				three of a kindk, false
	 * 				if otherwise
	 */
	private static boolean isThreeOfAKind(Card[] hand) {
		boolean test1 = hand[0].rank() == hand[1].rank()
					&& hand[1].rank() == hand[2].rank();
		boolean test2 = hand[1].rank() == hand[2].rank()
				&& hand[2].rank() == hand[3].rank();
		boolean test2 = hand[2].rank() == hand[3].rank()
				&& hand[3].rank() == hand[4].rank();
		return test1 || test2 || test3;
	}
	
	private static boolean
	
	private static void sortByRank(Card[] hand) {
		Arrays.sort(hand, (a, b) -> a.rank < b. rank ? -1 : a.rank == b.rank ? 0 : 1);
	}
	
	/**
	 * 
	 * @param allCards		Card[] sorted by card type then by suit
	 * @return				900 if Card[] contains a royal flush,0 if
	 * 						otherwise 
	 */
	public static boolean royalFlush(Set<Cards> hand) {
		if (allCards.length < 5) return false; // Insufficient number of cards
		return 
	}
	
	public int postFlopHandRank(Card[] playerCards, Card[] boardCards) {
		HashMap<Suit, Integer> suits = new HashMap<Suit>
	}
}
