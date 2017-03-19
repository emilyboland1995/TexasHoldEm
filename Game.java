// Current Game class (possibly replaces gameState)

import java.util.Scanner;

public class Game {
	private static final int STARTING_CHIPS = 1000;
	
	private Scanner input = new Scanner(System.in);
	private int pot;							//current chip value in pot
	private int smallBlind; 					//current small blind
	private int bigBlind; 						//big blind value
	private int hands;							//count of hands played
	private int minRaise;						//identifies min raise amount
	private int chipsToCall;					//identifies the total amount in chips a player needs in pot to call, this amount is cumulative
	private Card[] boardCards;					//5 cards holding for board
	private boolean flopFlag;					//flags whether flop is visible
	private boolean turnFlag;					//flags whether turn is visible
	private boolean riverFlag;					//flags whether river is visible
	private Player player;						//human player
	private Bot bot;							//bot player
	private Player activePlayer;				//identifies player currently acting
	private Player dealer;						//identifies dealer for current hand
	private int playersInHand;					//count of players in current hand
	private boolean botRevealed;				//flags whether bot cards revealed
	private Deck deck = new Deck();				//deck of cards
	
	// Basic Constructor for Game.
	public Game() {
		boardCards = new Card[5];
		pot = 0;
		smallBlind = 25;
		bigBlind = 50;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		botRevealed = false;
		
		
		bot = new Bot("Bot", STARTING_CHIPS);
		player = new Player("User", STARTING_CHIPS);
		
		dealer = bot;
		
		// Setup circular linked list for player
		bot.setNextPlayer(player);
		player.setNextPlayer(bot);
	}
	
	/**
	 * This method continues to play hands of Texas Hold 'Em
	 * until only one player has any chips or the user specifies
	 * that they do not want to pay anymore.
	 */
	public void playGame() {
	    if (hasChips() && userWantsToContinue()) {
	        playHand(); // Play hand
	    } else {
	        System.exit(0); // Exit game
	    }
	}
	
	/**
	 * 
	 * @return		True if both the player and dealer have 
	 * 				a chip count greater than o.
	 */
	public boolean hasChips(){
	    if (player.getChips() > 0 && dealer.getChips() > 0){
	        return true;
	    } else {
	        return false;
	    }
	}
	
	/**
	 * This method prompts the user to specify whether or not they want to
	 * play another hand.
	 * @return		True if the user wants to continue, false if otherwise
	 */
	public boolean userWantsToContinue() {
		char wantsToPlay = 'a';
	    System.out.println("Would you like to play again? (y/n)");	   		
	   	wantsToPlay = input.next().charAt(0); 
	    wantsToPlay = Character.toLowerCase(wantsToPlay);
	   		  			
	    while (wantsToPlay != 'y' && wantsToPlay != 'n') {
	   		System.out.println("INVALID CHARACTER!");
	   		System.out.println("Would you like to play again? (y/n)");	   		
	   		wantsToPlay = input.next().charAt(0); 
	   		wantsToPlay = Character.toLowerCase(wantsToPlay);
	   	}
	   	if (wantsToPlay=='y') {
	   		System.out.println(); 
	   		System.out.println("---------------------");
	   		return true;
	   	}
	    return false;
	}

	/**
	 * Creates a new hand by setting up board cards and hole cards
	 * for all players and reseting any flags. This method also
	 * sets up the blinds.
	 */
	public void createHand(){
	    deck.shuffleDeck();
		Card[] holeCards = new Card[2];
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
			//reset players in pot amounts
			activePlayer.setChipsInPot(0);
			activePlayer = activePlayer.getNextPlayer();
		}
		//*************deal cards and set board cards*******************
		activePlayer = dealer.getNextPlayer();
		for (int x = 0; x < playersInHand; x++) {
			//deal player cards
			holeCards[0] = deck.drawCard();
			holeCards[1] = deck.drawCard();
			activePlayer.setHoleCards(holeCards);
			activePlayer = activePlayer.getNextPlayer();
		}
		
		//set board cards
		boardCards[0] = deck.drawCard();
		boardCards[1] = deck.drawCard();
		boardCards[2] = deck.drawCard();
		boardCards[3] = deck.drawCard();
		boardCards[4] = deck.drawCard();

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
		botRevealed = false;
		//increment hand counter
		hands++;
	}
	
	/**
	 * Processes a Fold.
	 */
    public void fold(){
    	activePlayer.setInHand(false);
    	playersInHand--;
    	
    	System.out.println(activePlayer.toString() + " has folded");
    }
    
    /**
     * Processes a Raise. Attempts to handle a raise request by the
     * active player. Verifies that it is possible for the player to
     * raise by the specified amount and takes the appropriate action.
     * @param raiseAmt		The amount of chips to raise.
     * @return				True if the raise was successful, false
     * 						if otherwise (for instance, if the active
     * 						player has insufficient chips to cover the
     * 						raise amount specified).
     */
    public boolean raise(int raiseAmt) { //returns true if successful
    	if (raiseAmt < 1) {
    		return false; // Cannot raise by zero or a negative number of chips
    	}
    	if(raiseAmt + chipsToCall - activePlayer.getChipsInPot() > 0 
    			&& (raiseAmt >= minRaise 
    			|| raiseAmt + (chipsToCall - activePlayer.getChipsInPot()) == activePlayer.getChips())){
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
    		System.out.println(activePlayer.toString() + " has raised the pot " + raiseAmt);
    		System.out.println();
    		return true;
    	} else {//player either has insufficient funds or the raiseAmt is not of sufficient size
    		return false;
    	}
    }
    
    /**
     * Processes a Check.
     * @return		True if it is possible to check, false if otherwise.
     */
    public boolean check(){ // returns true if check successful
    	if(activePlayer.getChipsInPot() == chipsToCall){//ok to check
    		System.out.println(activePlayer.toString() + " has checked");
    		System.out.println();
    		return true;
    	} else {//player has not matched the necessary chips in pot
    		return false;
    	}
    }
    
    /**
     * Processes a Call. This method will determine whether
     * the player is capable of calling without going all-in and
     * adjusts the game and player states accordingly.
     */
    public void call(){
    	int amtToCall = chipsToCall - activePlayer.getChipsInPot();
    	if(amtToCall < activePlayer.getChips()){//verify player can cover call
    		//adjust players chip count
    		activePlayer.setChips(activePlayer.getChips() - amtToCall);
    		//adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		System.out.println(activePlayer.toString() + " has called");
    	} else {//player is all in
    		activePlayer.setChipsInPot(activePlayer.getChipsInPot() + activePlayer.getChips());
    		activePlayer.setChips(0);
    		System.out.println(activePlayer.toString() + " has called and is all in");
    	}
    }
    
    /**
     * Disperses chips based on the relative hand strength of the two
     * players.
     */
    public void disperseChips(){
    	//this really needs a recursive solution due to the fact that we can have multiple pots based off players being all in at different amounts.  
    	//for the time being i'm going to assume 2 players and distribute the chips based off the hand strength.
    	if(bot.inHand()){
    		if(player.inHand()){
    			if(player.getHandValue(this) > bot.getHandValue(this)){ //player won
    				player.setChips(player.getChips() + pot);
    			} else if(player.getHandValue(this) == bot.getHandValue(this)){//tied
    				player.setChips(player.getChips() + pot - pot / 2);
    				bot.setChips(bot.getChips() + pot / 2);
    			} else {//player lost
    				bot.setChips(bot.getChips() + pot);
    			}
    		} else {//player folded
    			bot.setChips(bot.getChips() + pot);
    		}
    		
    	} else {//bot folded
    		player.setChips(player.getChips() + pot);
    	}
    }
    
    /**
     * Checks whether there are still players active in
     * the current hand.
     * @return		True if at least one player remains
     * 				in the hand, false if otherwise.
     */
    public boolean checkForRemainingPlayers() {
    	return this.playersInHand > 0;
    }
    /**
     * Plays through one hand of Texas Hold 'Em Poker
     */
    public void playHand(){
    	boolean onceThrough = false;
    	Player prevPlayer;
    	
    	createHand();
    	
    	int numRounds = 0;
    	
    	while (numRounds < 4) {
    		roundOfBetting();
    		if (numRounds == 1) {
    			flop();
    		} else if (numRounds == 2) {
    			turn();
    		} else if (numRounds == 3) {
    			river();
    		}
    		
    		// Verify there are players still in the hand
    		// If only one player remains, disperse chips
    		if (this.playersInHand == 1) {
    			disperseChips();
    		}
    		numRounds++;
    	}
    	
    	// Disperse chips if at least 2 players remain in play after
    	// completing all rounds of betting
    	if (this.playersInHand > 1) {
    		disperseChips(); //
    	}
    	
    	//remove players with no chips
    	activePlayer = dealer.getNextPlayer();
    	prevPlayer = dealer;
    	while(activePlayer != dealer.getNextPlayer() || !onceThrough){
    		onceThrough = true;
    		if(activePlayer.getChips() <= 0){
    			prevPlayer.setNextPlayer(activePlayer.getNextPlayer());
    		} else {
    			prevPlayer = activePlayer;
    		}
    		activePlayer = activePlayer.getNextPlayer();
    	}
    	
    	//advance dealer and keep going
    	dealer = dealer.getNextPlayer();
    	deck.resetDeck();
    }
    
    /**
     * This method handles a round of betting, where a move is elicited 
     * from each player in turn.
     */
    public void roundOfBetting(){
    	boolean firstItem = false;
    	Player lastToRaise;
    	//first item is just a boolean variable set to make sure the loop below cycles through all players at least one time
    	//if before flop 'lastToRaise will initially be set to player after bigBlind
    	//for 2 players this will be the dealer
    	if(flopFlag){
    		lastToRaise = dealer.getNextPlayer();
    	} else {
    		lastToRaise = dealer;
    	}
    	activePlayer = lastToRaise;
    	while(!firstItem || activePlayer != lastToRaise){
    		//update firstItem flag
    		firstItem = true;
    		//Get action from player or Bot class
    		if(activePlayer instanceof Bot){//send info to bot class and get return of action to act on
    			bot.getAction(this);
    		} else {//this is a human player
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
    
    /**
     * This method prompts the player to select an appropriate action.
     */
    public void getPlayerAction() {
    	String response = "";
    	boolean valid = false;
    	printGameState();
    	while (!valid){ //make sure we receive a valid response
	    	if (activePlayer.getChipsInPot() == chipsToCall) { // no raise or call is required
	    		if(activePlayer.getChips() > minRaise){//player has 3 options
	    			System.out.println("You can (B)et, (C)heck, or go (A)ll-in.  Enter 'B', 'C', or 'A' and hit 'Enter'");
	    			System.out.println("Minimum Bet is: " + minRaise);
	    			response = getInput();
	    		} else {//player has 2 options
	    			System.out.println("You can (C)heck or go (A)ll-in.  Enter 'C' or 'A' and hit 'Enter'");
	    			response = getInput();
	    		}    		
	    	} else {// player needs to call, raise, or fold
	    		if(activePlayer.getChips() > (minRaise + chipsToCall - activePlayer.getChipsInPot())){//player has 4 options
	    			System.out.println("You can (R)aise, (C)all, (F)old, or go (A)ll-in.  Enter 'R', 'C', 'F', or 'A' and hit 'Enter'");
	    			System.out.println("Minimum Raise is: " + minRaise);
	    			response = getInput();
	    		} else {
	    			if(activePlayer.getChips() > (chipsToCall - activePlayer.getChipsInPot())){//player can cover a call, but does not have enough for the min raise
	    				System.out.println("You can (C)all, (F)old, or go (A)ll-in.  Enter 'C', 'F', or 'A' and hit 'Enter'");
	    				response = getInput();
	    			} else {//player will be going all in if they don't fold
	    				System.out.println("You can (F)old or go (A)ll-in.  Enter 'F' or 'A' and hit 'Enter'");
	    				response = getInput();
	    			}
	    		}
	    	}
	    	response = response.toUpperCase();
	    	switch(response){
	    	case "A":	valid = true;
	    				break;	
	    	case "B": 	if (activePlayer.getChipsInPot() == chipsToCall && activePlayer.getChips() > minRaise) {//player wants to bet, verify they have sufficent funds to make min bet
	    				valid = true;
	    				}
	    				break;		
	    	case "C":	if(activePlayer.getChipsInPot() == chipsToCall || activePlayer.getChips() > (chipsToCall - activePlayer.getChipsInPot())){//player wants to call/check, verify they have funds to do so
	    					valid = true;
	    				}
	    				break;	
	    	case "R":	if((activePlayer.getChips() - activePlayer.getChipsInPot()) > (chipsToCall + minRaise)){//player wants to raise, verify they have fund to do so
	    					valid = true;
	    				}
	    				break;
	    	case "F":	if(activePlayer.getChipsInPot() < chipsToCall){//player wants to fold, verify folding is valid option
	    					valid = true;
	    				}
	    				break;
	    	default:	valid = false; // Invalid user input
	    	}
    	}
    	processResponse(response);
    }
    
    /**
     * @return	A String representing the next token of input
     */
    public String getInput(){
    	return input.next();
    }
    
    /**
     * This method handles user responses by making the
     * move the player requested. Assumes r contains a
     * value selection.
     * @param r		The response provided by the user
     */
    public void processResponse(String r){
    	if(r.equals("B") || r.equals("R")){//get bet/raise amount and process
    		boolean valid = false;
    		int amt;
    		while(!valid){
    			System.out.println("Enter amount to raise (" + minRaise + " - " + (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) + ")") );
    			amt = input.nextInt();
    			if(amt >= minRaise && amt <= (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()))){
    				raise(amt);
    				valid = true;
    			}
    		}
    	}
    	if(r.equals("A")){//player is going all in
    		if(activePlayer.getChips() > 0){
    			raise(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()));
    		}    		
    	}
    	if(r.equals("C")){//process check/call
    		if(!check()){
    			call();
    		}
    	}
    	if(r.equals("F")){//fold
    		fold();
    	}
    }
	public void revealbot(){
		botRevealed = true;
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

	public Card[] getBoardCards() {
		return boardCards;
	}
	
	public boolean hasFlopOccured() {
		return this.flopFlag;
	}
	
	public boolean hasRiverOccured() {
		return this.riverFlag;
	}

	public boolean hasturnOccured() {
		return this.turnFlag;
	}
	
	/**
	 * Prints the current game state to the console.
	 */
	public void printGameState(){
		Card[] humanHoleCards;
		Card[] botHoleCards;
		
		//display pot/blind/hand number
		System.out.println("Hand #: " + getHands());
		System.out.println("Blinds: " + getSmallBlind() + " / " + getBigBlind());
		System.out.println("Pot: " + getPot());
		
		//display player info
		humanHoleCards = player.getHoleCards();
		botHoleCards = bot.getHoleCards();
		System.out.println();
		System.out.println(player.toString());
		System.out.println("Chips: " + player.getChips());
		System.out.println("Hole Cards: " + humanHoleCards[0].toString() + ", "  + humanHoleCards[1].toString());
		System.out.println();
		System.out.println(bot.toString());
		System.out.println("Chips: " + bot.getChips());
		//make sure the bots cards are visible
		if(botRevealed){
			System.out.println("Hole Cards: " + botHoleCards[0].toString() + ", "  + botHoleCards[1].toString());
		} else {
			System.out.println("Hole Cards: ??");
		}
		System.out.println(flopFlag + " " + turnFlag + " " + riverFlag);
		printBoardCards();
		
		//print stats... unsure what to put here
		
	}
	
	public void printBoardCards() {
		System.out.print("Board cards: ");
		if (this.flopFlag) {
			System.out.print(boardCards[0].toString() + ", ");
			System.out.print(boardCards[1].toString() + ", ");
			System.out.print(boardCards[2].toString());
		}
		if (this.riverFlag) {
			System.out.print( ", " + boardCards[3].toString());
		}
		if (this.turnFlag) {
			System.out.print(", " + boardCards[4].toString());
		}
		System.out.println();
	}
}

