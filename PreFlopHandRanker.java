/**
 * 
 *  This class defines a method for retrieving the approximate rate at which a given set of hole cards
 *  results in a winning hand at showdown. This is meant to provide a simple scale for which to gauge 
 *  the strength of a pre-flop hand, where only one's hole cards are known. The win-rate is obtained
 *  by playing 1,000,000 simplified games for each of 169 distinct possible starting hands and tracking
 *  the results, assuming both players play to showdown.
 *  
 *  Requirement Sets: 2.2.0, 2.5.1
 */
public class PreFlopHandRanker {
	private static final double[][] preFlopWinRateTable 
		= {{0.849211, 0.661544, 0.653311, 0.644652, 0.635012, 0.615509, 0.604746, 0.593015, 0.582247, 0.580123, 0.571108, 0.562823, 0.554478},
			{0.644171, 0.820989, 0.623798, 0.614302, 0.606323, 0.586482, 0.567798, 0.557932, 0.54851, 0.538475, 0.528988, 0.520728, 0.512239},
			{0.635216, 0.604335, 0.795927, 0.590645, 0.581039, 0.561803, 0.544017, 0.525953, 0.516466, 0.508062, 0.498055, 0.489494, 0.480925},
			{0.625203, 0.594438, 0.569701, 0.77091, 0.561248, 0.541377, 0.524251, 0.505183, 0.485084, 0.478573, 0.468273, 0.460722, 0.451716},
			{0.615493, 0.585208, 0.559493, 0.538385, 0.746321, 0.524949, 0.506139, 0.487057, 0.468235, 0.449446, 0.44207, 0.433401, 0.425741},
			{0.594289, 0.56293, 0.538621, 0.516202, 0.498045, 0.717094, 0.487998, 0.470492, 0.450751, 0.433123, 0.414216, 0.407336, 0.399383},
			{0.583238, 0.543927, 0.518707, 0.496964, 0.478194, 0.460497, 0.687718, 0.456686, 0.437743, 0.420135, 0.401333, 0.383042, 0.377125},
			{0.570717, 0.534826, 0.497827, 0.477044, 0.458793, 0.440842, 0.427435, 0.657262, 0.428688, 0.410318, 0.391288, 0.373308, 0.355032},
			{0.559248, 0.52328, 0.490533, 0.45696, 0.438354, 0.420467, 0.4069, 0.396004, 0.626767, 0.403207, 0.385169, 0.366686, 0.348583},
			{0.557304, 0.511601, 0.479044, 0.44883, 0.418581, 0.401026, 0.386972, 0.376998, 0.370032, 0.597102, 0.384514, 0.36817, 0.349518},
			{0.546785, 0.502907, 0.469513, 0.437663, 0.409868, 0.381203, 0.367153, 0.356281, 0.350351, 0.351937, 0.562752, 0.357792, 0.339354},
			{0.537781, 0.493084, 0.45969, 0.429746, 0.401833, 0.374485, 0.34783, 0.336432, 0.330131, 0.331963, 0.320079, 0.528409, 0.330964},
			{0.528994, 0.484103, 0.450197, 0.419995, 0.393228, 0.365038, 0.340832, 0.31675, 0.310385, 0.311723, 0.302119, 0.292509, 0.49403},
		};
	/**
	 * This method takes a set of hole cards and returns a double representing 
	 * the approximate rate at which the set results in a winning hand if played
	 * to showdown.
	 * @param holeCards		A Card[] containing two cards (one set of hole cards)
	 * @return				A double containing the approximate rate at this the 
	 * 						set of hole cards passed wins
	 * 
	 * Requirement: 2.2.0, 2.5.1
	 */
	public static double getHoleCardWinRate(Card[] holeCards) {
		if (holeCards.length != 2) {
			throw new IllegalArgumentException("holeCards must have a length equal to 2.");
		}
		int rank1 = holeCards[0].getRankInt();
		int rank2 = holeCards[1].getRankInt();
		
		// Adjust rank for aces
		if (rank1 == 1) {
			rank1 = 14;
		}
		if (rank2 == 1) {
			rank2 = 14;
		}
		
		// Ensure highest rank is in rank2
		if (rank1 > rank2) {
			int temp = rank1;
			rank1 = rank2;
			rank2 = temp;
		}
		// Convert ranks to table indices
		rank1 = 14 - rank1;
		rank2 = 14 - rank2;
		
		// Locate appropriate value
		if (holeCards[0].getSuitInt() != holeCards[1].getSuitInt()) { // Not suited...
			return preFlopWinRateTable[rank1][rank2];
		} else { // Suited
			return preFlopWinRateTable[rank2][rank1];
		}
	}
}
