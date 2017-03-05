
public class gameState {
	int pot;			//current chip value in pot
	int smallBlind; 	//current small blind
	int bigBlind; 		//big blind value
	int hands; 			//count of hands played
	Cards[] boardCards;	//5 cards holding for board
	boolean flopFlag;	//flags whether flop is visible
	boolean turnFlag;	//flags whether turn is visible
	boolean riverFlag;	//flags whether river is visible
	Player player1;		//human player
	Bot bot1;			//bot player
	boolean bot1Revealed;//flags whether bot cards revealed
	
	gameState(){
		boardCards = new Cards[5];
		pot = 0;
		smallBlind = 0;
		bigBlind = 0;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		bot1Revealed = false;
	}
	public void newHand(Cards[] boardCards,int bigBlind, int smallBlind) {  //resets gamestate values for individual hand and increments handcounter
		this.boardCards = boardCards;
		this.pot = bigBlind + smallBlind;
		this.bigBlind = bigBlind;
		this.smallBlind = smallBlind;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		bot1Revealed = false;
		hands++;
	}
	public void revealBot1(){
		bot1Revealed = true;
	}
	public void flop(){
		flopFlag = true;
	}
	public void turn(){
		turnFlag = true;
	}
	public void river(){
		riverFlag = true;
	}
	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public int getHands() {
		return hands;
	}

	public Cards[] getBoardCards() {
		return boardCards;
	}
	public void printGameState(){
		Cards[] humanHoleCards;
		Cards[] botHoleCards;
		
		//display pot/blind/hand number
		System.out.println("Hand #: " + getHands());
		System.out.println("Blinds: " + getSmallBlind() + " / " + getBigBlind());
		System.out.println("Pot: " + getPot());
		
		//display player info
		humanHoleCards = player1.getHoleCards();
		botHoleCards = bot1.getHoleCards();
		System.out.println(player1.getPlayerName());
		System.out.println("Chips: " + player1.getChips());
		System.out.println("Hole Cards: " + humanHoleCards[0].getRank() + humanHoleCards[1].getSuit() + " "  + humanHoleCards[1].getRank() + humanHoleCards[2].getSuit());
		System.out.println("Bot");
		System.out.println("Chips: " + bot1.getChips());
		//make sure the bots cards are visible
		if(bot1Revealed){
			System.out.println("Hole Cards: " + botHoleCards[0].getRank() + botHoleCards[1].getSuit() + " "  + botHoleCards[1].getRank() + botHoleCards[2].getSuit());
		} else {
			System.out.println("Hole Cards: ??");
		}
		
		//print stats... unsure what to put here
		
	}
	
}
