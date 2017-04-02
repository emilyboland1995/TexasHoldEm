import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * A simple class designed to calculate and hold the positive and negative 
 * potential of a particular players hand, given currently available board 
 * cards.
 *
 */
public class HandPotential {
	private static final int AHEAD = 0;
	private static final int TIED = 1;
	private static final int BEHIND = 2;
	private double negativePotential;
	private double positivePotential;
	
	public HandPotential(Card[] holeCards, Card[] boardCards) {
		Deck deck = new Deck(); // Test deck
		calculateHandPotential(holeCards, boardCards, deck); // Calculate potentials
	}
	
	/**
	 * @return		The negative potential calculated during
	 * 				this instance's construction
	 */
	public double getNegativePotential() {
		return negativePotential;
	}
	
	/**
	 * 
	 * @return		The positive potential calculated during
	 * 				this instance's construction
	 */
	public double getPositivePotential() {
		return positivePotential;
	}
		
	/**
	 * This method calculates the positive and negative potential for a 
	 * given player's hand. Calculation is based on a two-player game.
	 * @param holeCards		A Card[] containing one player's hole cards
	 * @param boardCards	A Card[] containing one player's board cards
	 * @param d				A Deck used to enumerate all possible future
	 * 						hands for the player and one opponent.
	 */
	private void calculateHandPotential(Card[] holeCards, Card[] boardCards, Deck d) {
		if (boardCards.length == 5) { // No further potential to grow
			negativePotential = 0.0;
			positivePotential = 0.0;
			return;
		}
		Set<Card> usedCards = new HashSet<Card>();
		
		// Add visible cards to set
		Utilities.addToSet(usedCards, holeCards);
		Utilities.addToSet(usedCards, boardCards);
		
		int[][] HP = new int[3][3];
		int[] HPTotal = new int[3];
	
		// Assemble two Card arrays to hold all cards available
		// to each player.
		Card[] allPlayerCards = new Card[7];
		allPlayerCards[0] = holeCards[0];
		allPlayerCards[1] = holeCards[1];
		Card[] allOppCards = new Card[7];
			
			for (int i = 0; i < boardCards.length; i++) {
			allPlayerCards[i + 2] = boardCards[i];
			allOppCards[i + 2] = boardCards[i];
		}
	
		long playerRank = PostFlopHandRanker.getAbsoluteHandStrength(holeCards, boardCards);
	
		// Check all possible two-card combinations of cards remaining for the opponent
		Iterator<Pair> oppCards = d.getPossiblePairsIterator();
		while (oppCards.hasNext()) {
			Pair oppHoleCards = oppCards.next();
			if (!usedCards.contains(oppHoleCards.getFirst()) && !usedCards.contains(oppHoleCards.getSecond())) {
				usedCards.add(oppHoleCards.getFirst());
				usedCards.add(oppHoleCards.getSecond());
				Card[] oppHoleCardsArr = new Card[2];
				oppHoleCardsArr[0] = oppHoleCards.getFirst();
				oppHoleCardsArr[1] = oppHoleCards.getSecond();
				
				long oppRank = PostFlopHandRanker.getAbsoluteHandStrength(oppHoleCardsArr, boardCards);
				
				int index;
				if (playerRank > oppRank) {
					index = AHEAD;
				} else if (playerRank == oppRank) {
					index = TIED;
				} else {
					index = BEHIND;
				}
				HPTotal[index]++;
				
				allOppCards[0] = oppHoleCardsArr[0];
				allOppCards[1] = oppHoleCardsArr[1];
				
				// Fill HP table by examining all possible future board cards
				// and tracking the number of times the player's final hand wins,
				// loses, or ties the opponant's hand.
				if (boardCards.length == 3) { // Check all turn/river possibilities
					fillTableForTurnAndRiver(d, allOppCards, allPlayerCards, usedCards, HP, index);
				} else { // Check all river possibilities
					fillTableForRiver(d, allOppCards, allPlayerCards, usedCards, HP, index);
				}
				
				// Cleanup: Remove oppHoleCards from the set of visible cards
				usedCards.remove(oppHoleCards.getFirst());
				usedCards.remove(oppHoleCards.getSecond());
			}
		}
		positivePotential = (double) (HP[BEHIND][AHEAD] + HP[BEHIND][TIED] / 2.0 + HP[TIED][AHEAD] / 2.0) / (HPTotal[BEHIND] + HPTotal[TIED] / 2.0);
		negativePotential = (double) (HP[AHEAD][BEHIND] + HP[TIED][BEHIND] / 2.0 + HP[AHEAD][TIED] / 2.0) / (HPTotal[AHEAD] + HPTotal[TIED] / 2.0);
	}
	/**
	 * This method enumerates all possible outcomes for the river and checks
	 * whether the player will win or lose, given the opponant's cards and the
	 * players cards, storing the results in the table HP at the row indicated
	 * by index.
	 * @param d					A Deck for enumerating possible future cards
	 * @param allOppCards		A Card[] containing all the opponant's cards
	 * @param allPlayerCards	A Card[] containing all the player's cards
	 * @param usedCards			A Set<Card> containing all cards currently assigned
	 * 							to a player or the board.
	 * @param HP				An int[][] containing ahead, behind, and tied counts
	 * @param index				An int containing the win/lose/tie status for the player's
	 * 							hand and the opponant's, given the currently visible board cards.
	 */
	private void fillTableForRiver(Deck d, Card[] allOppCards, Card[] allPlayerCards, 
			Set<Card> usedCards, int[][] HP, int index) {
		// Examine all possible board cards to come
		// and the expected outcome
		Iterator<Card> nextBoardCards = d.getPossibleCardsIterator();
		while (nextBoardCards.hasNext()) {
			Card next = nextBoardCards.next();
			if (!usedCards.contains(next)) {
				allOppCards[6] = next;
				allPlayerCards[6] = next;
				
				long possiblePlayerRank = PostFlopHandRanker.getAbsoluteHandStrength(allPlayerCards);
				long possibleOppRank = PostFlopHandRanker.getAbsoluteHandStrength(allOppCards);
				
				if (possiblePlayerRank > possibleOppRank) {
					HP[index][AHEAD]++;
				} else if (possiblePlayerRank == possibleOppRank) {
					HP[index][TIED]++;
				} else {
					HP[index][BEHIND]++;
				}
			}
		}
	}
	/**
	 * This method enumerates all possible outcomes for the turn and river and 
	 * checks whether the player will win or lose, given the opponant's cards 
	 * and the players cards, storing the results in the table HP at the row 
	 * indicated by index.
	 * @param d					A Deck for enumerating possible future cards
	 * @param allOppCards		A Card[] containing all the opponant's cards
	 * @param allPlayerCards	A Card[] containing all the player's cards
	 * @param usedCards			A Set<Card> containing all cards currently assigned
	 * 							to a player or the board.
	 * @param HP				An int[][] containing ahead, behind, and tied counts
	 * @param index				An int containing the win/lose/tie status for the player's
	 * 							hand and the opponant's, given the currently visible board cards.
	 */
	private void fillTableForTurnAndRiver(Deck d, Card[] allOppCards, Card[]allPlayerCards, 
			Set<Card> usedCards, int[][] HP, int index) {
		// Examine all possible board cards to come
		// and the expected outcome
		Iterator<Pair> nextBoardCards = d.getPossiblePairsIterator();
		while (nextBoardCards.hasNext()) {
			Pair turnAndRiver = nextBoardCards.next();
			if (!usedCards.contains(turnAndRiver.getFirst()) && !usedCards.contains(turnAndRiver.getSecond())) {
				allOppCards[5] = turnAndRiver.getFirst();
				allOppCards[6] = turnAndRiver.getSecond();
				allPlayerCards[5] = turnAndRiver.getFirst();
				allPlayerCards[6] = turnAndRiver.getSecond();
				
				long possiblePlayerRank = PostFlopHandRanker.getAbsoluteHandStrength(allPlayerCards);
				long possibleOppRank = PostFlopHandRanker.getAbsoluteHandStrength(allOppCards);
				
				if (possiblePlayerRank > possibleOppRank) {
					HP[index][AHEAD]++;
				} else if (possiblePlayerRank == possibleOppRank) {
					HP[index][TIED]++;
				} else {
					HP[index][BEHIND]++;
				}
			}
		}
	}
}