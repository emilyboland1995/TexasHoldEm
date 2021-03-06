/**
 * 
 * The Player class is built to represent a single player in
 * the game. It allows a name to be specified during construction,
 * along with the number of chips available to the player, if applicable.
 * Instances of this class can also store the current hole cards for a player.
 * Each instance can be assigned a link to another instance of Player. This allows
 * a sort of linked list of players to be constructed.
 *
 * Requirement Sets: 1.7.0
 */

public class Player {
	private String playerName;					// The name of the player
	private int chips;							// The number of chips available to the player
	private int chipsInPot;						// The number of chips the player has put into the current pot
	private Card[] holeCards = new Card[2];		// The player's hole cards
	private Player nextPlayer;					// A reference to another instance of Player (allows for linked data structures)
	private boolean inHand;						// A flag to indicate whether the player is active in the current hand
	
	/**
	 * Default constructor for instances of Player.
	 */
	public Player() {
		this("",0);
	}
	/**
	 * Addition constructor for instances of Player. 
	 * Allows the caller to provide a String to serve
	 * as the name of the player.
	 * @param pName		A String to serve as the name of the
	 * 					new instance of Player.
	 */
	public Player(String pName){
		this(pName, 0);		
	}
	/**
	 * A constructor for new instances of Player. Allows
	 * the caller to specify the playerName, and startingShips
	 * values.
	 * @param pName			A String to serve as the name of the
	 * 						new instance of Player.
	 * @param startingChips	The number of starting chips available to
	 * 						the new instance of Player.
	 */
	public Player(String playerName,int startingChips){
		if (startingChips < 0) {
			throw new IllegalArgumentException("startinChips cannot be negative.");
		}
		if (playerName == null) {
			throw new NullPointerException("playerName cannot be null.");
		}
		this.playerName = playerName;
		this.chips = startingChips;
		this.chipsInPot = 0;		
	}
	/**
	 * This method is only applicable when the instance of player
	 * it is called on is part of a linked data structure.
	 * @return		The reference to the next player.
	 */
	public Player getNextPlayer(){
		return nextPlayer;
	}
	/**
	 * Allows the caller to specify the next player.
	 * @param next		A reference to another instance of Player
	 */
	public void setNextPlayer(Player next){
		this.nextPlayer = next;
	}
	/**
	 * A simple implementation of toString that utilizes that
	 * returns the players name.
	 */
	public String toString() {
		return playerName;
	}
	/**
	 * Sets the player's name equal to playerName.
	 * @param playerName		A String that will serve
	 * 							as the player's name.
	 * 
	 * Requirement: 1.7.1
	 */
	public void setPlayerName(String playerName) {
		if (playerName == null) {
			throw new NullPointerException("playerName cannot be null.");
		}
		this.playerName = playerName;
	}
	/**
	 * 
	 * @return		Returns a String
	 * 				containing this
	 * 				player's name
	 * 
	 * Requirement: 1.7.1
	 */
	public String getPlayerName(){
		return this.playerName;
	}
	/**
	 * @return		The number of chips
	 * 				currently available
	 * 				to the player
	 * 
	 * Requirement: 1.7.5
	 */
	public int getChips() {
		return chips;
	}
	/**
	 * Set's the number of chips available to the player
	 * @param chips
	 * 
	 * Requirement: 1.7.3
	 */
	public void setChips(int chips) {
		if (chips < 0) {
			throw new IllegalArgumentException("The value of chips must be non-negative.");
		}
		this.chips = chips;
	}
	/**
	 * @return		Returns the number of chips
	 * 				this player has currently
	 * 				invested in the pot
	 * 
	 * Requirement: 1.7.5
	 */
	public int getChipsInPot() {
		return chipsInPot;
	}
	/**
	 * 
	 * @param chipsInPot	The number of chips
	 * 						this player currently
	 * 						has invested in the pot
	 * 
	 * Requirement: 1.7.4
	 */
	public void setChipsInPot(int chipsInPot) {
		if (chipsInPot < 0) {
			throw new IllegalArgumentException("The value of chips must be non-negative.");
		}
		this.chipsInPot = chipsInPot;
	}
	/**
	 * @return		A Card[] containing the hole
	 * 				cards for this player
	 * 
	 * Requirement: 1.7.7
	 */
	public Card[] getHoleCards() {
		return holeCards;
	}
	/**
	 * Sets the player's current hole cards
	 * @param holeCards
	 * 
	 * Requirement: 1.7.7
	 */
	public void setHoleCards(Card first, Card second) {
		if (first == null || second == null) {
			throw new NullPointerException("Cards cannot be set to null");
		}
		this.holeCards[0] = first;
		this.holeCards[1] = second;
	}
	/**
	 * @return		A boolean indicating whether
	 * 				this instance of Player is 
	 * 				still active in the current
	 * 				hand.
	 */
	public boolean inHand(){
		return inHand;
	}
	/**
	 * 
	 * @param inHand	A boolean indicating whether
	 * 					or not this instance of Player
	 * 					is still active in the current
	 * 					hand.
	 */
	public void setInHand(boolean inHand){
		this.inHand = inHand;
	}
	
	/**
	 * Returns the absolute hand strength of the best hand available
	 * to the player, given the players hole cards and the currently
	 * visible board cards.
	 * @param game		A reference to the relevant instance of Game
	 * @return			A long value containing the best absolute hand
	 * 					strength available to the player.
	 */
	public long getHandValue(Game game) {
		return PostFlopHandRanker.getAbsoluteHandStrength(getAllCards(game, this.holeCards));
	}
	/**
	 * Takes a hand ranking and uses it to generate a String
	 * representing the player's best hand.
	 * @param handValue		A long containing the rank of the best
	 * 						hand currently available to the player
	 * 						as generated by the getAbsoluteHandStrength
	 * 						method in the class PostFlopHandRanker
	 * @return				A String representing the best hand currently
	 * 						available to the player
	 */
	public String getHandString(long handValue){
		return PostFlopHandRanker.getHandStringFromRank(handValue);
	}
	
	/**
	 * Combines one player's hole Cards with any board cards to
	 * form a single Card[]
	 * @param game			The current Game object containing
	 * 						all the game info.
	 * @param holeCards		One player's hole cards
	 * @return				A Card[] containing all the cards
	 * 						currently visible to the player
	 * 						whose hole cards are provided.
	 */
	public Card[] getAllCards(Game game, Card[] holeCards) {
		Card[] boardCards = game.getVisibleBoardCards();
		Card[] allCards = new Card[2 + boardCards.length];
		allCards[0] = holeCards[0];
		allCards[1] = holeCards[1];
		int index = 2;
		while (index < 2 + boardCards.length) {
			allCards[index] = boardCards[index - 2];
			index++;
		}
		return allCards;
	}
}

