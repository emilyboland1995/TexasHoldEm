
public class gameState {
	int pot;							//current chip value in pot
	int smallBlind; 					//current small blind
	int bigBlind; 						//big blind value
	int hands;							//count of hands played
	int minRaise;						//identifies min raise amount
	int chipsToCall;					//identifies the total amount in chips a player needs in pot to call, this amount is cumulative
	Cards[] boardCards;					//5 cards holding for board
	boolean flopFlag;					//flags whether flop is visible
	boolean turnFlag;					//flags whether turn is visible
	boolean riverFlag;					//flags whether river is visible
	Player player1;						//human player
	Bot bot1;							//bot player
	Player activePlayer;				//identifies player currently acting
	Player dealer;						//identifies dealer for current hand
	int playersInHand;					//count of players in current hand
	boolean bot1Revealed;				//flags whether bot cards revealed
	
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

	public void createHand(Player d, Deck deck){
		this.dealer = d;
		Cards[] holeCards = new Cards[2];
	
		playersInHand = 1;
		//make sure dealer status is set to inHand
		dealer.setInHand(true);
		//reset dealers chips in pot
		dealer.setChipsInPot(0);
		//cycle to first player
		activePlayer = dealer.getNextPlayer();
		//count players and set inHand status
		while(activePlayer != dealer){
			playersInHand++;
			//set inHand boolean to true for player
			activePlayer.setInHand(true);
			//reset players inpot amounts
			activePlayer.setChipsInPot(0);
			activePlayer = activePlayer.getNextPlayer();
		}
		//*************deal cards and set board cards*******************
		for (int x = 0; x < playersInHand; x++) {
			//deal player cards
			activePlayer = dealer.getNextPlayer();
			holeCards[0] = deck.getCard(x);
			holeCards[1] = deck.getCard(x + playersInHand);
			activePlayer.setHoleCards(holeCards);			
		}
		//set boardcards
		//skip 1 card for burn
		boardCards[0] = deck.getCard(2*playersInHand + 1);
		boardCards[1] = deck.getCard(2*playersInHand + 2);
		boardCards[2] = deck.getCard(2*playersInHand + 3);
		//skip 1 card for burn
		boardCards[3] = deck.getCard(2*playersInHand + 5);
		//skip 1 card for burn
		boardCards[4] = deck.getCard(2*playersInHand + 7);
		//******************end deal cards*****************************
		
		//******************blinds*************************************
		//this is configured for 2 players, if we add players we'll need to adjust logic
		//small blind (for 2 players this is the dealer)
		if(dealer.getChips() > smallBlind){//dealer can cover small blind
			pot = smallBlind;
			dealer.setChipsInPot(smallBlind);
			dealer.setChips(dealer.getChips() - smallBlind);
		} else {//dealer cannot cover small blind
			pot = dealer.getChips();
			dealer.setChipsInPot(dealer.getChips());
			dealer.setChips(0);
		}
		//big blind (for 2 players this is the player that is not the dealer)
		activePlayer = dealer.getNextPlayer();
		if(activePlayer.getChips() > bigBlind){//player can cover bigBlind
			pot = pot + bigBlind;
			activePlayer.setChipsInPot(bigBlind);
			activePlayer.setChips(activePlayer.getChips() - bigBlind);
		} else {//Player cannot cover bigBlind
			pot = pot + activePlayer.getChips();
			activePlayer.setChipsInPot(activePlayer.getChips());
			activePlayer.setChips(0);
		}
		//******************end blinds***********************************
		
		//******************reset initial values for hands**************************
		chipsToCall = bigBlind;
		minRaise = bigBlind;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		bot1Revealed = false;
		//increment hand counter
		hands++;
	}

    public void fold(Player p){
    	p.setInHand(false);
    	playersInHand--;
    }
    public boolean raise(int raiseAmt){//returns true if successful
    	if(raiseAmt + chipsToCall - activePlayer.getChipsInPot() > 0 && (raiseAmt >= minRaise || raiseAmt + (chipsToCall - activePlayer.getChipsInPot()) == activePlayer.getChips())){
    		//adjust minRaise
    		if(raiseAmt > minRaise){
    			minRaise = raiseAmt;
    		}
    		//adjust chipsToCall
    		chipsToCall = chipsToCall + raiseAmt;
    		//adjust players chip count setChips(current stack - (new chips going in pot))
    		activePlayer.setChips(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) );
    		//adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		System.out.println(activePlayer.getPlayerName() + " has raised the pot " + raiseAmt);
    		return true;
    	} else {//player either has insufficient funds or the raiseAmt is not of sufficient size
    		return false;
    	}
    }

    public boolean check(){//returns true if check successful
    	if(activePlayer.getChipsInPot() == chipsToCall){//ok to check
    		System.out.println(activePlayer.getPlayerName() + " has checked ");
    		return true;
    	} else {//player has not matched the necessary chips in pot
    		return false;
    	}
    }
    public void call(){
    	int amtToCall = chipsToCall - activePlayer.getChipsInPot();
    	if(amtToCall < activePlayer.getChips()){//verify player can cover call
    		//adjust players chip count
    		activePlayer.setChips(activePlayer.getChips() - amtToCall);
    		//adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		System.out.println(activePlayer.getPlayerName() + " has called");
    	} else {//player is all in
    		activePlayer.setChipsInPot(activePlayer.getChipsInPot() + activePlayer.getChips());
    		activePlayer.setChips(0);
    		System.out.println(activePlayer.getPlayerName() + " has called and is all in");
    	}
    }

    public void playHand(){
    	
    }
    public void roundOfBetting(){
    	boolean firstItem = false;
    	//first item is just a boolean variable set to make sure the loop below cycles through all players at least one time
    	Player lastToRaise = dealer.getNextPlayer();
    	//if before flop 'lastToRaise will initially be set to player after bigBlind
    	//for 2 players this will be the dealer
    	lastToRaise = dealer;
    	while(!firstItem || activePlayer != lastToRaise){
    		//update firstItem flag
    		firstItem = true;
    		//Get action from player or Bot class
    		if(activePlayer instanceof Bot){//send info to bot class and get return of action to act on
    			//to be completed once we have bot class set up
    		} else {//this is a human player
    			//display gameState
    			printGameState();
    			//give options for action
    			//verify not all in
    			if(activePlayer.getChips() == 0){//player is all in, no action is required
    				//message here if we so choose to
    			} else {//player is not all in
    				getPlayerAction();
    			}
    		}
    		//cycle to next player
    		activePlayer = activePlayer.getNextPlayer();
    	}
    }
    public void getPlayerAction(){
    	
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
		System.out.println("Hole Cards: " + humanHoleCards[0].getRank() + humanHoleCards[0].getSuit() + " "  + humanHoleCards[1].getRank() + humanHoleCards[1].getSuit());
		System.out.println("Bot");
		System.out.println("Chips: " + bot1.getChips());
		//make sure the bots cards are visible
		if(bot1Revealed){
			System.out.println("Hole Cards: " + botHoleCards[0].getRank() + botHoleCards[0].getSuit() + " "  + botHoleCards[1].getRank() + botHoleCards[1].getSuit());
		} else {
			System.out.println("Hole Cards: ??");
		}
		
		//print stats... unsure what to put here
		
	}
	
}
