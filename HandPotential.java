import java.util.ArrayList;
import java.util.Set;

/**
 * 
 * A simple class designed to calculate and hold the positive and negative 
 * potential of a particular players hand, given currently available board 
 * cards.
 *
 * Requirement: 2.2.1
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
	 * 
	 * Requirement: 2.2.1
	 */
	public double getNegativePotential() {
		return negativePotential;
	}
	
	/**
	 * 
	 * @return		The positive potential calculated during
	 * 				this instance's construction
	 * 
	 * Requirement: 2.2.1
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
	 * 
	 * Requirement: 2.2.1
	 */
	private void calculateHandPotential(Card[] holeCards, Card[] boardCards, Deck d) {
		if (boardCards.length == 5) { // No further potential to grow
			negativePotential = 0.0;
			positivePotential = 0.0;
			return;
		}
		Set<Card> possibleCards = d.getDeckAsSet();
		
		// Add visible cards to set
		Utilities.removeFromSet(possibleCards, holeCards);
		Utilities.removeFromSet(possibleCards, boardCards);
		
		int[][] HP = new int[3][3];
		int[] HPTotal = new int[3];
	
		// Assemble two Card arrays to hold all cards available
		// to each player.
		ArrayList<Card> allPlayerCards = new ArrayList<Card>(7);
		ArrayList<Card> allOppCards = new ArrayList<Card>(7);
		for (int i = 0; i < 7; i++) {
			allPlayerCards.add(null);
			allOppCards.add(null);
		}
		allPlayerCards.set(0, holeCards[0]);
		allPlayerCards.set(1, holeCards[1]);
		
		
		for (int i = 0; i < boardCards.length; i++) {
			allPlayerCards.set(i + 2, boardCards[i]);
			allOppCards.set(i + 2, boardCards[i]);
		}
	
		long playerRank = PostFlopHandRanker.getAbsoluteHandStrength(holeCards, boardCards);
		
		Card[] possibleCardsArr = possibleCards.toArray(new Card[47]);
	
		// Check all possible two-card combinations of cards remaining for the opponent
		for (int i = 0; i < possibleCardsArr.length; i++) {
			for (int j = i + 1; j < possibleCardsArr.length; j++) {
				Card first = possibleCardsArr[i];
				Card second = possibleCardsArr[j];
				possibleCardsArr[i] = null;
				possibleCardsArr[j] = null;
				allOppCards.set(0, first);
				allOppCards.set(1, second);
				simulateOutcomes(playerRank, HP, HPTotal, allOppCards, boardCards,
						allPlayerCards, possibleCardsArr);
				possibleCardsArr[i] = first;
				possibleCardsArr[j] = second;
			}
		}

		positivePotential = (double) (HP[BEHIND][AHEAD] + HP[BEHIND][TIED] / 2.0 + HP[TIED][AHEAD] / 2.0) / (HPTotal[BEHIND] * 990 + (HPTotal[TIED] * 990) / 2.0);
		negativePotential = (double) (HP[AHEAD][BEHIND] + HP[TIED][BEHIND] / 2.0 + HP[AHEAD][TIED] / 2.0) / (HPTotal[AHEAD] * 990 + (HPTotal[TIED] * 990) / 2.0);
	}
	/**
	 * 
	 * @param playerRank
	 * @param HP
	 * @param HPTotal
	 * @param allOppCards
	 * @param boardCards
	 * @param allPlayerCards
	 * @param possibleCards
	 */
	private void simulateOutcomes(long playerRank, int[][] HP, int[] HPTotal, ArrayList<Card> allOppCards, 
			Card[] boardCards, ArrayList<Card> allPlayerCards, Card[] possibleCards) {
		long oppRank = PostFlopHandRanker.getAbsoluteHandStrength(allOppCards);
		
		int index;
		if (playerRank > oppRank) {
			index = AHEAD;
		} else if (playerRank == oppRank) {
			index = TIED;
		} else {
			index = BEHIND;
		}
		HPTotal[index]++;
		
		// Fill HP table by examining all possible future board cards
		// and tracking the number of times the player's final hand wins,
		// loses, or ties the opponant's hand.
		if (boardCards.length == 3) { // Check all turn/river possibilities
			fillTableForTurnAndRiver(allOppCards, allPlayerCards, possibleCards, HP, index);
		} else { // Check all river possibilities
			fillTableForRiver(allOppCards, allPlayerCards, possibleCards, HP, index);
		}
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
	private void fillTableForRiver(ArrayList<Card> allOppCards, ArrayList<Card> allPlayerCards, 
			Card[] possibleCards, int[][] HP, int index) {
		
		// Examine all possible board cards to come
		// and the expected outcome
		for (Card next : possibleCards) {
			if (next != null) {
				allOppCards.set(6, next);
				allPlayerCards.set(6, next);
				
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
	private void fillTableForTurnAndRiver(ArrayList<Card> allOppCards, ArrayList<Card> allPlayerCards, 
			Card[] possibleCards, int[][] HP, int index) {
		
		// Examine all possible board cards to come
		// and the expected outcome
		for (int i = 0; i < possibleCards.length; i++) {
			if (possibleCards[i] != null) {
				for (int j = i + 1; j < possibleCards.length; j++) {
					if (possibleCards[j] != null) {
						allOppCards.set(5, possibleCards[i]);
						allOppCards.set(6, possibleCards[j]);
						allPlayerCards.set(5, possibleCards[i]);
						allPlayerCards.set(6, possibleCards[j]);
						
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
	}
}