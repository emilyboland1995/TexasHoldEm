
public class Player {
	String playerName;
	Integer chips;
	Integer chipsInPot;
	Card[] holeCards = new Card[2];
	Player nextPlayer;
	boolean inHand;
	
	Player(){
		this("",0);
	}
	Player(String pName){
		this(pName, 0);		
	}
	Player(String pName,int startingChips){
		playerName = pName;
		chips = startingChips;
		chipsInPot = 0;		
	}
	public Player getNextPlayer(){
		return nextPlayer;
	}
	public void setNextPlayer(Player next){
		this.nextPlayer = next;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Integer getChips() {
		return chips;
	}
	public void setChips(Integer chips) {
		this.chips = chips;
	}
	public Integer getChipsInPot() {
		return chipsInPot;
	}
	public void setChipsInPot(Integer chipsInPot) {
		this.chipsInPot = chipsInPot;
	}
	public Card[] getHoleCards() {
		return holeCards;
	}
	public void setHoleCards(Card[] holeCards) {
		this.holeCards = holeCards;
	}
	public boolean inHand(){
		return inHand;
	}
	public void setInHand(boolean inHand){
		this.inHand = inHand;
	}
	
	public long getHandValue(Game game) {
		return PostFlopHandRanker.getAbsoluteHandStrength(getAllCards(game, this.holeCards));
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

