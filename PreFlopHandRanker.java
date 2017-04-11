
public class PreFlopHandRanker {
	//REQUIREMENTS TRACING: 1.2.2 Provide hand evaluation for pre-flop hands
	public static double getRelativeHandStrengthWeak(Card[] holeCards) {
		return (double) getAbsoluteHandStrengthWeak(holeCards) / 140000;
	}
	
	/**
	 * A very naive approach to calculating pre-flop 
	 * hand strengths;
	 * @return		An integer roughly representing the
	 * 				the strength of the pre-flop hand
	 */
	public static long getAbsoluteHandStrengthWeak(Card[] holeCards) {
		if (holeCards.length != 2) {
			throw new IllegalArgumentException("holeCards must have a length equal to 2.");
		}
		long strength = 0;
		if (holeCards[0].getRankInt() == holeCards[1].getRankInt()) { // Pair
			if (holeCards[0].getRankInt() == 1) {
				strength += 14;
			} else {
				strength += holeCards[0].getRankInt();
			}
			strength *= 10000;
		}
		if (holeCards[0].getSuitInt() == holeCards[1].getSuitInt()) { // Same suit
			int card1 = holeCards[0].getRankInt();
			int card2 = holeCards[1].getRankInt();
			if (card1 == 1) {
				card1 = 14;
			}
			if (card2 == 1) {
				card2 = 14;
			}
			strength += (card1 + card2) * 1000;
		}
		strength += holeCards[0].getRankInt() + holeCards[1].getRankInt();
		return strength;
	}
}
