/**
 * This class contains two methods, getEffeciveHandStregth and getHandStrength, which
 * are described below.
 */

import java.util.*;

public class HandStrengthCalculator {
	/**
	 * Calculates the effective hand strength (EHS) for a single
	 * player, given their hole cards and the visible board cards.
	 * EHS is calculated using the following formula:
	 * EHS = HS + (1 - HS) * PPOT - HS * NPOT
	 * EHS: Effective Hand Strength
	 * HS: Hand Strength (explained further for getHandStrength)
	 * PPOT: Positive Potential
	 * NPOT: Negative Potential
	 * @param holeCards		A Card[] containing one player's hole cards
	 * @param boardCards	A Card[] containing all visible board cards	
	 * @return				The effective hand strength of the player's
	 * 						best hand
	 */
	public static double getEffectiveHandStrength(Card[] holeCards, Card[] boardCards) {
		// Calculate and return effective hand strength
		double handStrength = getHandStrength(holeCards, boardCards);
		HandPotential p = new HandPotential(holeCards, boardCards);
		return (double) handStrength + (1 - handStrength) * p.getPositivePotential() - handStrength * p.getNegativePotential();
	}
	/**
	 * Calculates the optimistic effective hand strength (EHS) for 
	 * a single player, given their hole cards and the visible board cards.
	 * Optimistic EHS is calculated using the following formula:
	 * EHS = HS + (1 - HS) * PPOT
	 * EHS: Effective Hand Strength
	 * HS: Hand Strength (explained further for getHandStrength)
	 * PPOT: Positive Potential
	 * Note: The difference between the standard EHS formula and the
	 * optimistic version is the absence of the HS * NPOT term in the
	 * optimistic version.
	 * @param holeCards		A Card[] containing one player's hole cards
	 * @param boardCards	A Card[] containing all visible board cards	
	 * @return				The effective hand strength of the player's
	 * 						best hand
	 */
	public static double getEffectiveHandStrengthOptimistic(Card[] holeCards, Card[] boardCards) {
		// Calculate and return effective hand strength
				double handStrength = getHandStrength(holeCards, boardCards);
				HandPotential p = new HandPotential(holeCards, boardCards);
				return (double) handStrength + (1 - handStrength) * p.getPositivePotential();
	}
	
	/**
	 * This method calculates the hand strength for a given hand, which is
	 * composed of one player's hole cards plus 3 or more board cards. The
	 * hand strength calculation used here is based on how many times the
	 * player's current best hand beats, ties, loses to each possible hand
	 * the opponent might have.
	 * @param holeCards		A Card[] containing one player's hole cards
	 * @param boardCards	A Card[] containing all visible board cards	
	 * @return				The strength of the given hand
	 */
	public static double getHandStrength(Card[] holeCards, Card[] boardCards) {
		Deck d = new Deck(); // Test deck
		Set<Card> usedCards = new HashSet<Card>();
		
		// Add visible cards to set
		Utilities.addToSet(usedCards, holeCards);
		Utilities.addToSet(usedCards, boardCards);
		
		int ahead = 0, tied = 0, behind = 0;
		long playerRank = PostFlopHandRanker.getAbsoluteHandStrength(holeCards, boardCards);
		
		// Examine all possible oppCards and compare to player's current hand, tracking outcomes
		Iterator<Pair> oppCards = d.getPossiblePairsIterator();
		while (oppCards.hasNext()) {
			Pair oppHoleCards = oppCards.next();
			if (!usedCards.contains(oppHoleCards.getFirst()) && !usedCards.contains(oppHoleCards.getSecond())) {
				Card[] oppHoleCardsArr = {oppHoleCards.getFirst(), oppHoleCards.getSecond()};
				
				long oppRank = PostFlopHandRanker.getAbsoluteHandStrength(oppHoleCardsArr, boardCards);
				
				if (playerRank > oppRank) {
					ahead++;
				} else if (playerRank == oppRank) {
					tied++;
				} else {
					behind++;
				}
			}
		}
		return (double) (ahead + tied / 2) / (ahead + tied + behind);
	}
}
