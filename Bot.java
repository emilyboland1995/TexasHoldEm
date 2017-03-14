/**
 * This class defines a computer opponent or bot for the game.
 * The main addition the Bot class makes to the Player class
 * is the addition of the getAction method, which prompts the
 * bot to make a move based on current game conditions.
 */


public class Bot extends Player {
	
	/**
	 * Basic constructor for instances of Bot
	 * @param botName			A String containing the name of this bot.
	 * @param startingChips		The number of starting chips for the bot.
	 */
	public Bot(String botName,int startingChips) {
		super(botName, startingChips);
	}
	
	/**
	 * Prompts the Bot to make a move.
	 * @param game		The current Game object containing all
	 * 					the current game info.
	 */
	public void getAction(Game game) {
		if (!game.flopFlag) { // Pre-flop
			long handStrength = PreFlopHandRanker.getPreFlopStrengthWeak(this.holeCards);
		} else {// Post-flop
			long handStrength = PostFlopHandRanker.getHandRank(getAllCards(game, this.holeCards));
		}
	}
	
	/**
	 * Combines one player's hole Cards with any board cards to
	 * form a signle Card[]
	 * @param game			The current Game object containing
	 * 						all the game info.
	 * @param holeCards		One player's hole cards
	 * @return				A Card[] containing all the cards
	 * 						currently visible to the player
	 * 						whose hole cards are provided.
	 */
	private Card[] getAllCards(Game game, Card[] holeCards) {
		int size = 2;
		if (game.flopFlag) {
			size += 3;
		} 
		if (game.riverFlag) {
			size++;
		}
		if (game.turnFlag) {
			size++;
		}
		Card[] allCards = new Card[size];
		allCards[0] = holeCards[0];
		allCards[1] = holeCards[1];
		int index = 2;
		while (index < size) {
			allCards[index] = game.boardCards[index - 2];
			index++;
		}
		return allCards;
	}
	
}
