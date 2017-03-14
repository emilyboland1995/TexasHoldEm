
public class PreFlopHandRanker {
	
	/**
	 * A very naive approach to calculating pre-flop 
	 * hand strengths;
	 * @return		An integer roughly representing the
	 * 				the strength of the pre-flop hand
	 */
	public static long getPreFlopStrengthWeak(Card[] holeCards) {
		if (holeCards.length != 2) {
			throw new IllegalArgumentException("holdCards must have a length equal to 2.");
		}
		long strength = 0;
		if (holeCards[0].getRankInt() == holeCards[1].getRankInt()) { // Pair
			strength += holeCards[0].getRankInt() * 10000;
		}
		if (holeCards[0].getSuitInt() == holeCards[1].getSuitInt()) { // Same suit
			strength += (holeCards[0].getRankInt() + holeCards[1].getRankInt()) * 1000;
		}
		strength += holeCards[0].getRankInt() + holeCards[1].getRankInt();
		return strength;
	}
}
