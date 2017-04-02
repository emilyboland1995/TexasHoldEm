/**
 * HandRanker defines a single method called getHandRank which produces a numeric representation
 * of the highest possible hand, given a Card[] representing the current board cards combined 
 * with one player's hole cards. This numeric representation of the most powerful hand can be
 * used to quickly compare the strength of two respective hands.
 */

import java.util.*;

public class PostFlopHandRanker {
	
	// Relative hand rankings by type
	private static final long STRAIGHT_FLUSH = 9;
	private static final long FOUR_OF_A_KIND = 8;
	private static final long FULL_HOUSE = 7;
	private static final long FLUSH = 6;
	private static final long STRAIGHT = 5;
	private static final long THREE_OF_A_KIND = 4;
	private static final long TWO_PAIRS = 3;
	private static final long PAIR = 2;
	private static final long HIGH_CARD = 1;
	private static final long majorFactor = Math.multiplyExact((long) 10000, 1000000);
	
	/**
	 * Calculates and returns the relative strength of the best hand
	 * present given one player's hole cards plus the existing board
	 * cards.
	 * @param hand		One player's hole cards plus any board cards
	 * @return			The relative strength of the best hand
	 * 					present.
	 */
	public static double getRelativeHandStrength(Card[] hand) {
		return (double) getAbsoluteHandStrength(hand) / (914 * Math.multiplyExact((long) 10000, 10000));
	}
	
	/**
	 * Calculates and returns the strength of the strongest hand possible
	 * given the player's hole cards plus any board cards.
	 * @param holeCards		A Card[] containing one player's hole cards
	 * @param boardCards	A Card[] containing all visible board cards
	 * @return				A long containing a numeric representation of this
	 * 						hand which representing its relative strength.
	 */
	public static long getAbsoluteHandStrength(Card[] holeCards, Card[] boardCards) {
		return getAbsoluteHandStrength(createHand(holeCards, boardCards));
	}
	
	/**
	 * Combines one player's hole cards and all visible board cards to create
	 * a single Card[].
	 * @param holeCards		A Card[] containing one player's hole cards
	 * @param boardCards	A Card[] containing all visible board cards
	 * @return				A Card[] containing both the hole cards and the
	 * 						visible board cards
	 */
	private static Card[] createHand(Card[] holeCards, Card[] boardCards) {
		if (holeCards.length != 2 || boardCards.length < 3 || boardCards.length > 5) {
			throw new IllegalArgumentException("Invalid number of cards provided.");
		}
		Card[] hand = new Card[holeCards.length + boardCards.length];
		hand[0] = holeCards[0];
		hand[1] = holeCards[1];
		
		for (int i = 2; i < hand.length; i++) {
			hand[i] = boardCards[i - 2];
		}
		
		return hand;
	}
	
	/**
	 * Calculates and returns the strength of the strongest hand possible
	 * given the player's hole cards plus any board cards.
	 * @param hand		One player's hole cards plus any board cards
	 * @return			A long containing a numeric representation of this
	 * 					hand which representing its relative strength.
	 */
	public static long getAbsoluteHandStrength(Card[] hand) {
		long handRank = 0;
		
		int[] suitCounts = new int[4];
		int[] rankCounts = new int[13];
		int containsStraight = -1;
		int containsFlush = -1;
		int highCardRank = -1;
		int threeOfAKind = -1;
		int fourOfAKind = -1;
		int twoOfAKindHigh = -1;
		int twoOfAKindLow = -1;
		
		sortByRank(hand); // Sort the hand by rank
		
		// Count suits and ranks and finds high card
		for (int i = 0; i < hand.length; i++) {
			suitCounts[hand[i].getSuitInt() - 1]++;
			
			// Check if card is a new high card
			if (hand[i].getRankInt() > highCardRank) {
				highCardRank = hand[i].getRankInt();
			}
			
			rankCounts[hand[i].getRankInt() - 1]++;
			int rankCount = rankCounts[hand[i].getRankInt() - 1];
			
			// Check for and track multiples
			if (rankCount == 4){
				fourOfAKind = i;
			} else if (rankCount == 3 && (threeOfAKind == -1 
					|| hand[threeOfAKind].getRankInt() < hand[i].getRankInt()
					|| hand[i].getRankInt() == 1)) {
				threeOfAKind = i;
			} else if (rankCount == 2 && (twoOfAKindHigh == -1 
					|| hand[twoOfAKindHigh].getRankInt() < hand[i].getRankInt() 
					|| hand[i].getRankInt() == 1)) {
				twoOfAKindLow = twoOfAKindHigh;
				twoOfAKindHigh = i;
			}
		}
		
		containsStraight = checkForStraight(hand);
		containsFlush = checkForFlush(suitCounts);
		
		// Check for straight flush only if one might exist
		long straightFlushValue = -1;
		if (containsFlush >= 0 && containsStraight >= 0) {
			straightFlushValue = checkForStraightFlush(hand, containsFlush);
		}

		// Check for Straight Flush
		if (containsFlush >= 0 && containsStraight >= 0 && straightFlushValue > 0) {
			handRank = straightFlushValue; // StraightFlush Found
		} else if (fourOfAKind >= 0) { // Check for four of a kind
			handRank = FOUR_OF_A_KIND * majorFactor;
			if (hand[fourOfAKind].getRankInt() == 1) { // Four Aces
				handRank += 1400000000;
			} else { // Not an Ace
				handRank += hand[fourOfAKind].getRankInt() * 100000000;
			}
		} else if (threeOfAKind >= 0 && twoOfAKindHigh >= 0) { // Check for full house
			handRank += FULL_HOUSE * majorFactor;
			if (hand[threeOfAKind].getRankInt() == 1) { // If three aces...
				handRank += 1400000000;
			} else { // Not all three aces
				handRank += hand[threeOfAKind].getRankInt() * 100000000;
			}
		} else if (containsFlush >= 0) {
			handRank += FLUSH * majorFactor;
			long highestCardsValue = 0;
			for (int i = hand.length - 1; i >= hand.length - 5; i--) {
				highestCardsValue *= 100;
				highestCardsValue += hand[i].getRankInt();
			}
			handRank += highestCardsValue;
		} else if (containsStraight >= 0) {
			handRank += STRAIGHT * majorFactor;
			if (containsStraight == hand.length - 4) {
				handRank += 1400000000; // Ace (14) high straight
			} else {
				handRank += hand[containsStraight + 4].getRankInt() * 100000000; // Different straight flush
			}
			
		} else if (threeOfAKind >= 0) {
			handRank += THREE_OF_A_KIND * majorFactor;
			if (hand[threeOfAKind].getRankInt() == 1) { // If three aces...
				handRank += 1400000000;
			} else { // Not all three aces
				handRank += hand[threeOfAKind].getRankInt() * 100000000;
			}
		} else if (twoOfAKindHigh >= 0 && twoOfAKindLow >= 0) { // Two pair
			handRank += TWO_PAIRS * majorFactor;
			handRank += hand[twoOfAKindHigh].getRankInt() * 100000000;
			handRank += hand[twoOfAKindLow].getRankInt() * 1000000;
			long highestCardsValue = 0;
			int cardsAdded = 0;
			int index = hand.length - 1;
			
			// Handle Ace-high possibility
			if (hand[0].getRankInt() == 1 && hand[0].getRankInt() != hand[twoOfAKindHigh].getRankInt()) {
				highestCardsValue += 140000;
				cardsAdded++;
			}
			while (cardsAdded < 1) {
				int currentCardValue = hand[index].getRankInt();
				if (currentCardValue != hand[twoOfAKindHigh].getRankInt() 
						&& currentCardValue != hand[twoOfAKindLow].getRankInt()) {
					if (currentCardValue == 1) {
						highestCardsValue = 140000;
					} else {
						highestCardsValue = currentCardValue *= 10000;
					}
					cardsAdded++;
				}
				index --;
			}
			handRank += highestCardsValue;
		} else if (twoOfAKindHigh >= 0) { // Pair
			handRank += PAIR * majorFactor;
			handRank += hand[twoOfAKindHigh].getRankInt() * 100000000;
			long highestCardsValue = 0;
			int cardsAdded = 0;
			int index = hand.length - 1;
			// Handle Ace-high possibility
			if (hand[0].getRankInt() == 1 && hand[0].getRankInt() != hand[twoOfAKindHigh].getRankInt()) {
				highestCardsValue += 14;
				cardsAdded++;
			}
			while (cardsAdded < 3) {
				cardsAdded *= 100;
				int currentCardValue = hand[index].getRankInt();
				if (currentCardValue != hand[twoOfAKindHigh].getRankInt()) {
					if (currentCardValue == 1) { // Ace
						highestCardsValue += 14;
					} else {
						highestCardsValue += currentCardValue;
					}
					cardsAdded++;
				}
				index--;
			}
			handRank += highestCardsValue;
		} else { // High Card
			handRank += HIGH_CARD * majorFactor;
			long highestCardsValue = 0;
			int cardsAdded = 0;
			int index = hand.length - 1;
			while (cardsAdded < 5) {
				cardsAdded *= 100;
				int currentCardValue = hand[index].getRankInt();
				if (currentCardValue == 1) {
					highestCardsValue += 14;
				} else {
					highestCardsValue += currentCardValue;
				}
				cardsAdded++;
				index--;
			}
			handRank += highestCardsValue;
		}
		
		return handRank;
	}
	
	/**
	 * 
	 * @param suitCounts		An int[] containing the number of
	 * 							cards of each suit in the hand.
	 * @return					The index of the suit that occurs
	 * 							greater than 5 times, if present.
	 * 							Returns -1 if no suit occurs more
	 * 							than 5 times.
	 */
	public static int checkForFlush(int[] suitCounts) {
		// Check for flush. If present, mark suit
		for (int i = 0; i < suitCounts.length; i++) {
			if (suitCounts[i] >= 5) {
				return i + 1;
			}
		}
		return -1;
	}
	
	/**
	 * Checks for the presence of a straight flush in the
	 * hand provided.
	 * @param hand		A Card[] containing all the cards in
	 * 					the hand being examined
	 * @return			A long containing the value of the straight
	 * 					flush contained in the hand, if it contains
	 * 					one. Returns -1 otherwise.
	 */
	public static long checkForStraightFlush(Card[] hand, int flushSuit) {
		// Get flush cards
		Card[] flushSuitHand = new Card[5];
		for (int i = 0, j = 0; i < hand.length; i++) {
			if (hand[i].getSuitInt() == flushSuit) {
				flushSuitHand[j] = hand[i];
			}
		}
		
		int hasStraight = checkForStraight(flushSuitHand);
		
		if (hasStraight >= 0) { 
			long handRank = STRAIGHT_FLUSH * majorFactor; // Straight flush present
			if (hasStraight == hand.length - 4) {
				handRank += 1400000000; // Royal Flush, Ace (14) high
			} else {
				handRank += hand[hasStraight + 4].getRankInt() * 100000000; // Other straight flush
			}
			return handRank;
		}
		return -1;
	}

	/**
	 * Checks the hand for a straight.
	 * @param hand		The hand being evaluated
	 * @return			The starting index of the straight
	 * 					if the hand contains one, -1 if it
	 * 					doesn't.
	 */
	public static int checkForStraight(Card[] hand) {
		// Check for Straight. Implicitly finds the straight of 
		// the highest value
		for (int i = 0; i <= hand.length - 4; i++) {
			boolean straightFound = true;
			if (i == hand.length - 4) {
				for (int j = i + 1; j < i + 4; j++) {
					// If the straight is broken, break
					if (hand[j].getRankInt() - 1 != hand[j - 1].getRankInt()) {
						straightFound = false;
						break;
					}
				}
				// Check for Ace high straight
				if (hand[hand.length - 1].getRankInt() % 13 + 1 != hand[0].getRankInt()) {
				straightFound = false;
				}
			} else {
				for (int j = i + 1; j < i + 5; j++) {
					// If the straight is broken, break
					if (hand[j].getRankInt() - 1 != hand[j - 1].getRankInt()) {
						straightFound = false;
					}
				}
			}
			// If a straight is found, keep track of its location
			if (straightFound) {
				return i;
			}
		}
		return -1; // Straight not found
	}
	
	/**
	 * Sorts a given hand by the rank of the cards
	 * @param hand		The hole cards plus any board cards
	 */
	private static void sortByRank(Card[] hand) {
		Arrays.sort(hand, new CompareByRank());
	}
	
	/**
	 * 
	 * A basic Comparator that compares cards by rank
	 *
	 */
	private static class CompareByRank implements Comparator<Card> {

		@Override
		public int compare(Card o1, Card o2) {
			if (o1.getRankInt() == 1
					|| o1.getRankInt() > o2.getRankInt()) {
				return 1;
			} else if (o1.getRankInt() == o2.getRankInt()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
