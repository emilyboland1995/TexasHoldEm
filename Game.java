/**
 * 
 * The Game class is designed to hold all the information about a game
 * of No Limits Texas Hold 'Em poker that would not otherwise be allocated
 * to the player class. Game also provides the methods for handling the various
 * moves available to players along with a Command-Line Interface for the human 
 * player(s).
 *
 */

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class Game {
	
	private Scanner input = new Scanner(System.in);
	private int pot;							// current chip value in pot
	private int smallBlind; 					// current small blind
	private int bigBlind; 						// big blind value
	private int hands;							// count of hands played
	private int minRaise;						// identifies min raise amount
	private int chipsToCall;					// identifies the total amount in chips a player needs in pot to call, this amount is cumulative
	private Card[] boardCards;					// 5 cards holding for board
	private boolean flopFlag;					// flags whether flop is visible
	private boolean turnFlag;					// flags whether turn is visible
	private boolean riverFlag;					// flags whether river is visible
	private Player player;						// human player
	private Bot bot;							// bot player
	private Player activePlayer;				// identifies player currently acting
	private Player dealer;						// identifies dealer for current hand
	private int playersInHand;					// count of players in current hand
	private boolean botRevealed;				// flags whether bot cards revealed
	private Deck deck = new Deck();				// deck of cards
	private int startingChips = 1000;			//starting chips
	private int roundsAtEachBlind = 10;			//number of rounds at each blind level
	private int blindLevel = 0;					//current blind level
	private lineList myLineList;				//contains list of lines to print
	private GUI gui = new GUI();				//gui
	
	/**
	 * Basic constructor for Game. 
	 */
	public Game() {
		boardCards = new Card[5];
		pot = 0;
		smallBlind = 25;
		bigBlind = 50;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		botRevealed = false;
		
		setup();
		
		bot = new Bot("Bot", startingChips);
		player = new Player("User", startingChips);
		
		dealer = bot;
		
		// Setup circular linked list for player
		bot.setNextPlayer(player);
		player.setNextPlayer(bot);
	}
	/**
	 * Present user with setup menu and prompt
	 * for a response
	 */
	public void setup(){
		char response = '0';
		String dialogResponse;
		String mnuOptions;
		while(response != '1'){
			mnuOptions = printMenuOptions();
			dialogResponse = JOptionPane.showInputDialog(mnuOptions);
			if(dialogResponse != null && dialogResponse.length()>0){
				response = dialogResponse.charAt(0);
	//			response = input.next().charAt(0);
				switch (response){
				case '1':		//start game, no action required
							break;
				case '2':		promptForStartingChips();
							break;
				case '3':		promptForRoundsAtEachBlind();
							break;
				case '4':
							System.exit(0); // Quit game
							break;
				default: 	//System.out.println("Invalid response. Please try again.");
							gui.appendTextAreaLine("Invalid response. Please try again.");
				}
			}
		}
	}
	/**
	 * @return		A String containing the menu options available at the beginning of the gaem
	 */
	private String printMenuOptions(){
		String msg;
		msg = "****Choose option****\n(1) Start game!\n(2) Adjust starting chips. Currently: " + startingChips;
		msg += "\n(3) Adjust number of rounds at each blind level. Currently: " + roundsAtEachBlind;
		msg += "\n(4) Quit game";
		return msg;
	}
	/**
	 * Prompt the user for the blinds used for each round
	 */
	private void promptForRoundsAtEachBlind(){
		int response = 0;
//		System.out.println("Enter rounds at each blind level (1 minimum): ");
		
		while(response < 1){
			try{
				response = Integer.parseInt(JOptionPane.showInputDialog("Enter rounds at each blind level (1 minimum): ", roundsAtEachBlind));
//				if(response < 1){
//					System.out.println("Invalid amount. Try again.");
//				}
			} catch(NumberFormatException ex){
//				System.out.println("Invalid amount. Try again.");
			}
		}
		roundsAtEachBlind = response;
	}
	/**
	 * Prompts user to provide the number of starting chips for the players
	 */
	private void promptForStartingChips(){
		int response = 0;
//		System.out.println("Enter starting chips (500 minimum): ");
		while(response < 500){
			try{
				response = Integer.parseInt(JOptionPane.showInputDialog("Enter starting chips (500 minimum): ", startingChips));
				if(response < 500){
//					System.out.println("Invalid amount. Try again.");
				}
			} catch(NumberFormatException ex){
//				System.out.println("Invalid amount. Try again.");
			}
		}
		startingChips = response;
	}
	/**
	 * Adjust blinds based on blind level
	 */
	private void adjustBlinds(){
		switch(blindLevel) {
		case 0: 
			smallBlind = 25;
			bigBlind = 50;
			break;
		case 1:
			smallBlind = 50;
			bigBlind = 100;
			break;
		case 2:
			smallBlind = 75;
			bigBlind = 150;
			break;
		case 3:
			smallBlind = 100;
			bigBlind = 200;
			break;
		case 4:
			smallBlind = 150;
			bigBlind = 300;
			break;
		case 5:
			smallBlind = 200;
			bigBlind = 400;
			break;
		case 6:
			smallBlind = 250;
			bigBlind = 500;
			break;
		default:
			smallBlind = smallBlind * 2;
			bigBlind = bigBlind * 2;
		}
		JOptionPane.showInputDialog("***** Blinds are now " + smallBlind + " / " + bigBlind + "*****");
//		System.out.println("         ***** Blinds are now " + smallBlind + " / " + bigBlind + "*****");
//		try{
//			Thread.sleep(2500);
//		} catch(Exception ex) {
//			
//		}
	}

	/**
	 * This method continues to play hands of Texas Hold 'Em
	 * until only one player has any chips or the user specifies
	 * that they do not want to pay anymore.
	 */
	public void playGame() {
	    while (hasChips() && (this.hands == 0 || userWantsToContinue())) {
	    	playHand(); // Play hand
	    }
	}
	
	/**
	 * Returns true if both players can play another hand,
	 * false if otherwise.
	 * @return		True if both the player and dealer have 
	 * 				a chip count greater than o, false if 
	 * 				other wise
	 */
	public boolean hasChips(){
		int playersWithChips = 0;
		Player tempPlayer = dealer.getNextPlayer();
		if (dealer.getChips() > 0){
			playersWithChips++;
		}
		while(tempPlayer != dealer){
			if(tempPlayer.getChips()>0){
				playersWithChips++;
			}
			tempPlayer = tempPlayer.getNextPlayer();
		}
	    if (playersWithChips > 1){
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
		int wantsToPlay;
//	    System.out.println("CONTINUE this game? (y/n)");	   		
//	   	wantsToPlay = input.next().charAt(0); 
	    wantsToPlay = JOptionPane.showConfirmDialog(null, "Continue?", "Continue",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
//	    while (wantsToPlay != 'y' && wantsToPlay != 'n') {
//	   		System.out.println("INVALID INPUT!");
//	   		System.out.println("CONTINUE this game?  (y/n)");	   		
//	   		wantsToPlay = input.next().charAt(0); 
//	   		wantsToPlay = Character.toLowerCase(wantsToPlay);
//	   	}
	   	if (wantsToPlay == 0) {
	   		gui.appendTextAreaLine("");
//	   		System.out.println(); 
	   		gui.appendTextAreaLine("---------------------");
//	   		System.out.println("---------------------");
	   		return true;
	   	} else {
	   		return false;
	   	}
	}

	/**
	 * Creates a new hand by setting up board cards and hole cards
	 * for all players and reseting any flags. This method also
	 * sets up the blinds. If the isStacked is true, then createHand
	 * will not reset the deck. !! This capability is for testing only !!
	 * @param isStacked		True if the deck is pre-defined (stacked) and
	 * 						should not be shuffled, false if otherwise.
	 */
	private void createHand(boolean isStacked) {
		if (!isStacked) {
			deck.resetDeck(); // Verify deck is shuffled with the appropriate number of cards
		}
		Card[] holeCards = new Card[2];
		playersInHand = 1;
		// make sure dealer status is set to inHand
		dealer.setInHand(true);
		// reset dealers chips in pot
		dealer.setChipsInPot(0);
		// cycle to first player
		activePlayer = dealer.getNextPlayer();
		// count players and set inHand status
		while(activePlayer != dealer){
			playersInHand++;
			// set inHand boolean to true for player
			activePlayer.setInHand(true);
			// reset players in pot amounts
			activePlayer.setChipsInPot(0);
			activePlayer = activePlayer.getNextPlayer();
		}
		// *************deal cards and set board cards*******************
		activePlayer = dealer.getNextPlayer();
		for (int x = 0; x < playersInHand; x++) {
			// deal player cards
			holeCards[0] = deck.drawCard();
			holeCards[1] = deck.drawCard();
			activePlayer.setHoleCards(holeCards);
			activePlayer = activePlayer.getNextPlayer();
		}
		
		// Set board cards
		boardCards[0] = deck.drawCard();
		boardCards[1] = deck.drawCard();
		boardCards[2] = deck.drawCard();
		boardCards[3] = deck.drawCard();
		boardCards[4] = deck.drawCard();
		
		//increment blinds if necessary
		if((int)(hands/roundsAtEachBlind) > blindLevel){
//			System.out.println("hands: " + hands + "     roundsAtEachBlind: " + roundsAtEachBlind + "     blindLevel: " + blindLevel + "     (int)(hands/roundsAtEachBlind): " + (int)(hands/roundsAtEachBlind));
			blindLevel++;
			adjustBlinds();
		}
		// increment hand counter
		hands++;

		// ******************end deal cards*****************************
		
		processBlinds(); // Handle blinds
		
		// ******************reset initial values for hands**************************
		chipsToCall = bigBlind;
		minRaise = bigBlind;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		botRevealed = false;
	}
	
	/**
	 * Handles the blinds for a game containing two players: the user and a bot.
	 */
	private void processBlinds() {
		// ******************blinds*************************************
		// this is configured for 2 players, if we add players we'll need to adjust logic
		// small blind (for 2 players this is the dealer)
		if(dealer.getChips() > smallBlind){// dealer can cover small blind
			pot = smallBlind;
			dealer.setChipsInPot(smallBlind);
			dealer.setChips(dealer.getChips() - smallBlind);
			myLineList.addLine(dealer.getPlayerName() + " has posted small blind of: " + smallBlind);
		} else {// dealer cannot cover small blind
			pot = dealer.getChips();
			dealer.setChipsInPot(dealer.getChips());
			myLineList.addLine(dealer.getPlayerName() + " goes all in with small blind: " + dealer.getChips());
			dealer.setChips(0);
		}
		// big blind (for 2 players this is the player that is not the dealer)
		activePlayer = dealer.getNextPlayer();
		if(activePlayer.getChips() > bigBlind){// player can cover bigBlind
			pot = pot + bigBlind;
			activePlayer.setChipsInPot(bigBlind);
			activePlayer.setChips(activePlayer.getChips() - bigBlind);
			myLineList.addLine(activePlayer.getPlayerName() + " has posted big blind of: " + bigBlind);
		} else {// Player cannot cover bigBlind
			pot = pot + activePlayer.getChips();
			activePlayer.setChipsInPot(activePlayer.getChips());
			myLineList.addLine(activePlayer.getPlayerName() + " has gone all in with big blind of: " + activePlayer.getChips());
			activePlayer.setChips(0);
		}
		// ******************end blinds***********************************
	}
	
	/**
	 * Processes a Fold.
	 */
    public void fold() {
    	activePlayer.setInHand(false);
    	playersInHand--;
    	if(activePlayer instanceof Bot){
    		revealbot();
    	}
    	
    	myLineList.addLine(activePlayer.toString() + " has folded");
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
    public boolean raise(int raiseAmt) { // returns true if successful
    	if (raiseAmt < 1) {
    		return false; // Cannot raise by zero or a negative number of chips
    	}
    	if(raiseAmt + chipsToCall - activePlayer.getChipsInPot() > 0 
    			&& (raiseAmt >= minRaise || raiseAmt + (chipsToCall - activePlayer.getChipsInPot()) == activePlayer.getChips())){
    		// adjust minRaise
    		if(raiseAmt > minRaise){
    			minRaise = raiseAmt;
    		}
    		// adjust chipsToCall
    		chipsToCall = chipsToCall + raiseAmt;
    		pot = pot + (chipsToCall - activePlayer.getChipsInPot());
    		// adjust players chip count setChips(current stack - (new chips going in pot))
    		activePlayer.setChips(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) );
    		// adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		myLineList.addLine(activePlayer.toString() + " has raised the pot " + raiseAmt);
    		return true;
    	} else {// player either has insufficient funds or the raiseAmt is not of sufficient size
    		return false;
    	}
    }
    
    /**
     * Processes a Check.
     * @return		True if it is possible to check, false if otherwise.
     */
    public boolean check(){ // returns true if check successful
    	if(activePlayer.getChipsInPot() == chipsToCall){ // ok to check
    		myLineList.addLine(activePlayer.toString() + " has checked");
    		return true;
    	} else { // player has not matched the necessary chips in pot
    		return false;
    	}
    }
    
    /**
     * Processes a Call. This method will determine whether
     * the player is capable of calling without going all-in and
     * adjusts the game and player states accordingly.
     */
    public void call() {
    	int amtToCall = chipsToCall - activePlayer.getChipsInPot();
    	if(amtToCall < activePlayer.getChips()){// verify player can cover call
    		// adjust players chip count
    		activePlayer.setChips(activePlayer.getChips() - amtToCall);
    		// adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		pot = pot + amtToCall;
    		myLineList.addLine(activePlayer.toString() + " has called");
    	} else {// player is all in
    		activePlayer.setChipsInPot(activePlayer.getChipsInPot() + activePlayer.getChips());
    		pot = pot + activePlayer.getChips();
    		activePlayer.setChips(0);
    		myLineList.addLine(activePlayer.toString() + " has called and is all in");
    	}
    }
    
    /**
     * Disperses chips based on the relative hand strength of the two
     * players.
     */
    public void disperseChips(){
    	// this really needs a recursive solution due to the fact that we can have multiple pots based off players being all in at different amounts.  
    	// for the time being i'm going to assume 2 players and distribute the chips based off the hand strength.
    	int maxChipsToWin = 0;
    	if(bot.inHand()){
    		if(player.inHand()){
    			myLineList.addLine("Player's best hand: " + player.getHandString(player.getHandValue(this)));
    			myLineList.addLine("Bot's best hand: " + bot.getHandString(bot.getHandValue(this)));
    			if(player.getHandValue(this) > bot.getHandValue(this)){ // player won
    				maxChipsToWin = player.getChipsInPot() * 2;
    				if(maxChipsToWin >= pot){//gets whole pot
    					player.setChips(player.getChips() + pot);
    					myLineList.addLine("You won " + pot + " chips!");
    				} else {//gets partial pot
    					player.setChips(player.getChips() + maxChipsToWin);
    					myLineList.addLine("You won " + maxChipsToWin + " chips!");
    					bot.setChips(bot.getChips() + pot - maxChipsToWin);
    					myLineList.addLine("Bot won " + (pot - maxChipsToWin) + " chips!");
    				}
    			} else if(player.getHandValue(this) == bot.getHandValue(this)){// tied
    				player.setChips(player.getChips() + player.getChipsInPot());
    				bot.setChips(bot.getChips() + bot.getChipsInPot());
    				myLineList.addLine("Split pot! You won " + player.getChipsInPot() + " chips!");
    				myLineList.addLine("Bot won " + bot.getChipsInPot() + " chips!");
    			} else {// player lost
    				maxChipsToWin = bot.getChipsInPot() * 2;
    				if(maxChipsToWin >= pot){//gets whole pot
    					bot.setChips(bot.getChips() + pot);
    					myLineList.addLine("Bot won " + pot + " chips!");
    				} else {//gets partial pot
    					bot.setChips(bot.getChips() + maxChipsToWin);
    					myLineList.addLine("Bot won " + maxChipsToWin + " chips!");
    					player.setChips(player.getChips() + pot - maxChipsToWin);
    					myLineList.addLine("Player won " + (pot - maxChipsToWin) + " chips!");
    				}
    			}
    		} else {// player folded
    			bot.setChips(bot.getChips() + pot);
    			myLineList.addLine("Bot won " + pot + " chips!");
    		}
    		
    	} else {// bot folded
    		player.setChips(player.getChips() + pot);
    		myLineList.addLine("You won " + pot + " chips!");
    	}
    	botRevealed = true;
    	printGameState();
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
     * Plays a single hand with a pre-defined Deck;
     */
    public void playStackedHand(Deck stackedDeck) {
    	playHand(stackedDeck);
    }
    
    /**
     * Plays through one hand of Texas Hold 'Em Poker
     */
    public void playHand() {
    	playHand(null);
    }
    
    /**
     * Plays a single hand of Texas Hold 'Em Poker. If deck is
     * provided for the stackedDeck parameter, the hand will use
     * that deck and not shuffle or reset it. !! For Testing Only !!
     * @param stackedDeck	Takes a reference to a pre-defined
     * 						or "stacked" deck. If the reference is
     * 						null, playHand will play a normal hand
     * 						using the existing deck in game.
     */
    private void playHand(Deck stackedDeck) {
    	boolean onceThrough = false;
    	Player prevPlayer;
    	myLineList = new lineList();
    	
    	if (stackedDeck != null) {
    		this.deck = stackedDeck;
    		createHand(true);
    	} else {
    		createHand(false);
    	}

    	int numRounds = 0;
    	
    	while (numRounds < 4 && this.playersInHand > 1) {
    		if (numRounds == 1) {
    			flop();
    		} else if (numRounds == 2) {
    			turn();
    		} else if (numRounds == 3) {
    			river();
    		}
    		roundOfBetting();
    		numRounds++;
    	}
    	disperseChips(); // Disperse chips
    		
    	// remove players with no chips
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
    	
    	// advance dealer and keep going
    	dealer = dealer.getNextPlayer();
    }
    
    /**
     * This method handles a round of betting, where a move is elicited 
     * from each player in turn.
     */
    public void roundOfBetting(){
    	boolean firstRound = true;
    	minRaise = bigBlind;  //reset minRaise
    	Player lastToAct;
    	// firstRound is just a boolean variable set to make sure the loop below cycles through all players at least one time
    	// if before flop 'lastToAct will be set to big blind before flop and dealer after flop
    	// for 2 players this will be the dealer
    	if(flopFlag){
    		lastToAct = dealer;
    	} else {
    		lastToAct = dealer.getNextPlayer();
    	}
    	activePlayer = lastToAct.getNextPlayer();
    	while((firstRound || !allSquare()) && playersInHand > 1){
    		// update firstRound flag
    		if(activePlayer == lastToAct){//if we have cycled around to lastToAct, everyone has had opportunity to bet/raise
    			firstRound = false;
    		}
    		// Get action from player or Bot class
    		if(activePlayer instanceof Bot && bot.getChips() > 0){// verify bot and bot has chips
    			if(chipsToCall > activePlayer.getChipsInPot() || player.getChips() > 0){//action still required
    				String response;
    				printGameState();
    				//get and process response from bot class
    				response = bot.getAction(this);
    				processBotAction(response);
    			}
    		} else {// this is a human player
    			// verify not all in
    			if(activePlayer.getChips() == 0 || (bot.getChips() == 0 && activePlayer.getChipsInPot() >= bot.getChipsInPot())){// player is all in or bot is all in (and player has covered), no action is required
    				// message here if we so choose to
    			} else {// player is not all in
    				printGameState();
    				getPlayerAction();
    			}
    		}
    		// cycle to next player
    		activePlayer = activePlayer.getNextPlayer();
    	}
    }
    public boolean allSquare(){//This returns true if all players in the hand have the same number of chips in pot or are all in
    	boolean returnVal = true;
    	Player firstPlayer = activePlayer;
    	Player tempPlayer = activePlayer;
    	while(tempPlayer.getNextPlayer() != firstPlayer && returnVal){
    		if(tempPlayer.getChipsInPot() != chipsToCall && tempPlayer.getChips() != 0 && tempPlayer.inHand()){
    			returnVal = false;
    		}
    		tempPlayer = tempPlayer.getNextPlayer();
    	}    	
    	return returnVal;
    }
    public void processBotAction(String response){
    	boolean valid = false;
    	if(response.compareTo("error") == 1){
    		gui.appendTextAreaLine("Bot returned error!");
//    		System.out.println("Bot returned error!");
    		System.exit(0);
    	} else {
    		
    		switch(response.substring(0,1)){
	    	case "A":	processResponse(response);
	    				valid = true;
	    				break;	
	    	case "B": 	if (activePlayer.getChipsInPot() == chipsToCall && activePlayer.getChips() > minRaise) {// player wants to bet, verify they have sufficent funds to make min bet
	    					processResponse(response);
	    					valid = true;
	    				}
	    				break;		
	    	case "C":	//bot wants to check or call  checking and calling is always acceptable for bot
	    				processResponse(response);
/*						if (activePlayer.getChipsInPot() == chipsToCall){//player is checking || activePlayer.getChips() >= (chipsToCall - activePlayer.getChipsInPot())){// player wants to call/check, verify they have funds to do so
	    					processResponse(response);
	    				} else {//bot is calling
	    					if((activePlayer.getChips() + activePlayer.getChipsInPot()) - chipsToCall > 0){//calling not all in
	    						processResponse(response);
	    					} else {//bot is calling and is going all in to do so
	    						processResponse("A");
	    					}
	    				}
*/
	    				valid = true;
	    				break;	
	    	case "R":	if ((activePlayer.getChips() - activePlayer.getChipsInPot()) > (chipsToCall + minRaise)){// player wants to raise, verify they have fund to do so
	    					processResponse(response);
	    					valid = true;
	    				}
	    				break;
	    	case "F":	if (activePlayer.getChipsInPot() < chipsToCall) { // player wants to fold, verify folding is valid option
	    					processResponse(response);
	    					valid = true;
	    				}
	    				break;
	    	default:	valid = false; // Invalid user input
	    	}
    		if(!valid){
    			gui.appendTextAreaLine("Received invalid response for bot action: " + response);
//    			System.out.println("Received invalid response for bot action: " + response);
    		}
    	}
    }
    /**
     * This method prompts the player to select an appropriate action.
     */
    public void getPlayerAction() {
    	String response = "";
    	boolean valid = false;
    	while (!valid){ // make sure we receive a valid response
	    	if (activePlayer.getChipsInPot() == chipsToCall) { // no raise or call is required
	    		if(activePlayer.getChips() > minRaise){// player has 3 options
	    			response = JOptionPane.showInputDialog("You can (B)et, (C)heck, or go (A)ll-in.  Enter 'B', 'C', or 'A' and hit 'Enter'\nMinimum Bet is: " + minRaise);
//	    			System.out.println("You can (B)et, (C)heck, or go (A)ll-in.  Enter 'B', 'C', or 'A' and hit 'Enter'");
//	    			System.out.println("Minimum Bet is: " + minRaise);
//	    			response = getInput();
	    		} else { // player has 2 options
	    			response = JOptionPane.showInputDialog("You can (C)heck or go (A)ll-in.  Enter 'C' or 'A' and hit 'Enter'");
//	    			System.out.println("You can (C)heck or go (A)ll-in.  Enter 'C' or 'A' and hit 'Enter'");
//	    			response = getInput();
	    		}    		
	    	} else { // player needs to call, raise, or fold
	    		if(activePlayer.getChips() > (minRaise + chipsToCall - activePlayer.getChipsInPot())){// player has 4 options
	    			response = JOptionPane.showInputDialog("You can (R)aise, (C)all, (F)old or go (A)ll-in.  Enter 'R', 'C', 'F', or 'A' and hit 'Enter'\nMinimum Bet is: " + minRaise);
//	    			System.out.println("You can (R)aise, (C)all, (F)old, or go (A)ll-in.  Enter 'R', 'C', 'F', or 'A' and hit 'Enter'");
//	    			System.out.println("Minimum Raise is: " + minRaise);
//	    			response = getInput();
	    		} else {
	    			if(activePlayer.getChips() > (chipsToCall - activePlayer.getChipsInPot())){// player can cover a call, but does not have enough for the min raise
	    				response = JOptionPane.showInputDialog("You can (C)all, (F)old, or go (A)ll-in.  Enter 'C', 'F', or 'A' and hit 'Enter'");
//	    				System.out.println("You can (C)all, (F)old, or go (A)ll-in.  Enter 'C', 'F', or 'A' and hit 'Enter'");
//	    				response = getInput();
	    			} else {// player will be going all in if they don't fold
	    				response = JOptionPane.showInputDialog("You can (F)old or go (A)ll-in.  Enter 'F' or 'A' and hit 'Enter'");
//	    				System.out.println("You can (F)old or go (A)ll-in.  Enter 'F' or 'A' and hit 'Enter'");
//	    				response = getInput();
	    			}
	    		}
	    	}
	    	if(response != null){
	    		response = response.toUpperCase();
	    	
		    	switch(response){
		    	case "A":	valid = true;
		    				break;	
		    	case "B": 	if (activePlayer.getChipsInPot() == chipsToCall && activePlayer.getChips() > minRaise) {// player wants to bet, verify they have sufficent funds to make min bet
		    				valid = true;
		    				}
		    				break;		
		    	case "C":	if(activePlayer.getChipsInPot() == chipsToCall || activePlayer.getChips() > (chipsToCall - activePlayer.getChipsInPot())){// player wants to call/check, verify they have funds to do so
		    					valid = true;
		    				}
		    				break;	
		    	case "R":	if((activePlayer.getChips() - activePlayer.getChipsInPot()) > (chipsToCall + minRaise)){// player wants to raise, verify they have fund to do so
		    					valid = true;
		    				}
		    				break;
		    	case "F":	if(activePlayer.getChipsInPot() < chipsToCall){// player wants to fold, verify folding is valid option
		    					valid = true;
		    				}
		    				break;
		    	default:	valid = false; // Invalid user input
		    	}
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
    	if(r.equals("B") || r.equals("R")){// get bet/raise amount and process
    		boolean valid = false;
    		int amt;
    		while(!valid){
    			amt = Integer.parseInt(JOptionPane.showInputDialog("Enter amount to raise (" + minRaise + " - " + (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) + ")"), minRaise));
//    			System.out.println("Enter amount to raise (" + minRaise + " - " + (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) + ")") );
//    			amt = input.nextInt();
    			if(amt >= minRaise && amt <= (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()))){
    				raise(amt);
    				valid = true;
    			}
    		}
    	}
    	if((r.substring(0, 1).equals("B") || r.substring(0, 1).equals("R")) && r.length() > 1){// bot is betting or raising.  first character identifies action, rest of string is the amount
    		int amt;
			amt = Integer.parseInt(r.substring(1, r.length()-1));
			raise(amt);
    	}
    	if(r.equals("A")){// player is going all in
    		if(activePlayer.getChips() > 0){
    			if(chipsToCall - activePlayer.getChipsInPot() > activePlayer.getChips()){//calling an allin
    				processResponse("C");
    			} else {//this is an actual raise
    				raise(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()));
    			}
    		}    		
    	}
    	if(r.equals("C")){// process check/call
    		if(!check()){
    			call();
    		}
    	}
    	if(r.equals("F")){// fold
    		fold();
    	}
    }
    
    /**
     * Mark the bot's hole cards as visible
     */
	public void revealbot(){
		botRevealed = true;
	}
	
	/**
	 * Perform the flop
	 */
	public void flop(){
		myLineList.addLine("  **FLOP : " + boardCards[0].toString() + " | " + boardCards[1].toString() + " | " + boardCards[2].toString() + " **");
		flopFlag = true;
	}
	/**
	 * Perform the turn
	 */
	public void turn(){
		myLineList.addLine("  **TURN : " + boardCards[3].toString() + " **");
		turnFlag = true;
	}
	/**
	 * Perform the river
	 */
	public void river(){
		myLineList.addLine("  **RIVER : " + boardCards[4].toString() + " **");
		riverFlag = true;
	}
	/**
	 * @return		An int representing the number of chips in the pot.
	 */
	public int getPot() {
		return pot;
	}

	/**
	 * 
	 * @param pot		The new value for the pot.
	 */
	public void setPot(int pot) {
		if (pot < 0) {
			throw new IllegalArgumentException("Pot was set to a negative value");
		}
		this.pot = pot;
	}
	
	/**
	 * @return		An int containing the small blind
	 */
	public int getSmallBlind() {
		return smallBlind;
	}

	/**
	 * @return		An int containing the big blind
	 */
	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * @return		An int containing the number of hands played
	 */
	public int getHands() {
		return hands;
	}
	
	/**
	 * @return		A Card[] containing all 5 board cards,
	 * 				regardless of their visibility.
	 */
	public Card[] getBoardCards() {
		return boardCards;
	}
	
	/**
	 * Returns an ArrayList<Card> containing all currently
	 * visible board cards.
	 * @return		A ArrayList<Card> containing all currently
	 * 				visible board cards. If none are visible
	 * 				(i.e. the game is pre-flop), an empty
	 * 				ArrayList<Card> will be returned.
	 */
	public ArrayList<Card> getVisibleBoardCards() {
		ArrayList<Card> boardCards = new ArrayList<Card>();
		int numBoardCards = 0;
		if (this.flopFlag) {
			numBoardCards += 3;
		}
		if (this.riverFlag) {
			numBoardCards++;
		}
		if (this.turnFlag) {
			numBoardCards++;
		}
		for (int i = 0; i < numBoardCards; i++) {
			boardCards.add(this.boardCards[i]);
		}
		return boardCards;
	}
	
	/**
	 * @return		True if the flop has occurred, false if otherwise.
	 */
	public boolean hasFlopOccured() {
		return this.flopFlag;
	}
	
	/**
	 * @return		True if the river has occurred, false if otherwise.
	 */
	public boolean hasRiverOccured() {
		return this.riverFlag;
	}
	
	/**
	 * @return		True if the turn has occurred, false if otherwise.
	 */
	public boolean hasTurnOccured() {
		return this.turnFlag;
	}

	/**
	 * Prints the current game state to the console.
	 */
	public void printGameState(){
		Card[] humanHoleCards;
		Card[] botHoleCards;
		String line1, line2, line3, line4, line5, line6;
		
		humanHoleCards = player.getHoleCards();
		botHoleCards = bot.getHoleCards();
		//print 50 blank lines to create illusion of blank page
//		for(int x = 0; x < 50; x++){
//			System.out.println();
//		}
		gui.clearTextArea();
		//setup line1
		line1 = "*************************Hand: " + getHands() + " *************************";
		line5 = "                     Blinds: " + getSmallBlind() + " / " + getBigBlind();
		line5 = line5 + "       POT: " + pot;
		//setup line2
		line2 = "  user: ";
		if(player.getChips() >= 1000){
			line2 = line2 + "  ";
		} else if(player.getChips() >= 100){
			line2 = line2 + "   ";
		} else {
			line2 = line2 + "   ";
		}
		line2 = line2 + player.getChips() + "                          Bot:  ";
		if(bot.getChips() >= 1000){
			line2 = line2 + "  ";
		} else if(bot.getChips() >= 100){
			line2 = line2 + "   ";
		} else {
			line2 = line2 + "   ";
		}
		line2 = line2 + bot.getChips();
		
		//setup line3
		line3 = " InPot: ";
		if(player.getChipsInPot() >= 1000){
			line3 = line3 + "  ";
		} else if(player.getChipsInPot() >= 100){
			line3 = line3 + "   ";
		} else {
			line3 = line3 + "   ";
		}
		line3 = line3 + player.getChipsInPot() + "                         InPot:  ";
		if(bot.getChipsInPot() >= 1000){
			line3 = line3 + "  ";
		} else if(bot.getChipsInPot() >= 100){
			line3 = line3 + "   ";
		} else {
			line3 = line3 + "   ";
		}
		line3 = line3 + bot.getChipsInPot();
		
		//setup line4. hole cards
		line4 = " ";
		line4 = line4 + humanHoleCards[0].toString() + " | " + humanHoleCards[1].toString();
		for(int counter = line4.length(); counter < 47; counter++){
			line4 = line4 + " ";
		}
		if(botRevealed){
			line4 = line4 + botHoleCards[0].toString() + " | " + botHoleCards[1].toString();
		} else {
			line4 = line4 + "??";
		}
		
		if (this.flopFlag) { // Post flop
			line6 = "Hand Strength: " + HandStrengthCalculator.getHandStrength(player.getHoleCards(), this.getBoardCards());
			line6 += "\nEffective Hand Strength (EHS): " 
					+ HandStrengthCalculator.getEffectiveHandStrength(player.getHoleCards(), this.getBoardCards());
		} else { // Pre-flop
			line6 = "Win rate: " + PreFlopHandRanker.getHoleCardWinRate(player.getHoleCards());
		}
				
		// display pot/blind/hand number
//		System.out.println(line1);
//		System.out.println(line5);
//		System.out.println(line2);
//		System.out.println(line3);
//		System.out.println(line4);
		gui.appendTextAreaLine(line1);
		gui.appendTextAreaLine(line5);
		gui.appendTextAreaLine(line2);
		gui.appendTextAreaLine(line3);
		gui.appendTextAreaLine(line4);
		gui.appendTextAreaLine(line6);

		printBoardCards();
		myLineList.printList();
		
	}
	
	/**
	 * Prints out the currently visible board cards to the console
	 */
	public void printBoardCards() {
//		System.out.print(" ");
		gui.appendTextArea(" ");
		if (this.flopFlag) {
//			System.out.print(boardCards[0].toString() + " | ");
			gui.appendTextArea(boardCards[0].toString() + " | ");
//			System.out.print(boardCards[1].toString() + " | ");
			gui.appendTextArea(boardCards[1].toString() + " | ");
//			System.out.print(boardCards[2].toString());
			gui.appendTextArea(boardCards[2].toString());
		}
		if (this.turnFlag) {
//			System.out.print( " | " + boardCards[3].toString());
			gui.appendTextArea(" | " + boardCards[3].toString());
		}
		if (this.riverFlag) {
//			System.out.print(" | " + boardCards[4].toString());
			gui.appendTextArea(" | " + boardCards[4].toString());
		}
//		System.out.println();
//		System.out.println();
		gui.appendTextAreaLine("");
		gui.appendTextAreaLine("");
	}
	public int getChipsToCall(){
		return chipsToCall;
	}
	/**
	 * 
	 * 
	 *
	 */
	private class lineNode{
		String lineToPrint;
		lineNode nextLine;
		
		lineNode(){
			
		}
		void setLine(String lineToPrint){
			this.lineToPrint = lineToPrint;
		}
		void printLine(){
			gui.appendTextAreaLine(lineToPrint);
//			System.out.println(lineToPrint);
		}
		void setNext(lineNode nextLine){
			this.nextLine = nextLine;
		}
		lineNode getNext(){
			return nextLine;
		}
	}
	/**
	 * 
	 * A linked list designed to hold lines of text
	 * to print out to a CLI.
	 *
	 */
	private class lineList{
		lineNode head = new lineNode();
		lineNode pointer;
		int linesAdded;
		final int LinesForDisplay = 20;
		
		lineList(){
			head.setLine("*********************Hand Detail***************************");
			pointer = head;
			linesAdded = 0;
		}
		void addLine(String lineToAdd){
			lineNode newNode = new lineNode();
			newNode.setLine(lineToAdd);
			pointer.setNext(newNode);
			pointer = pointer.getNext();
			linesAdded++;
		}
		void printList(){
			pointer = head;
			pointer.printLine();
			gui.appendTextAreaLine("");
//			System.out.println();
			while(pointer.getNext() != null){
				pointer = pointer.getNext();
				pointer.printLine();
			}
			//add buffer lines if necessary
			if(linesAdded < LinesForDisplay){
				for(int x = linesAdded; x< LinesForDisplay; x++){
//					System.out.println();
					gui.appendTextAreaLine("");
				}
			}
//			System.out.println("-----------------------------------------------------------");
//			System.out.println();
			gui.appendTextAreaLine("-----------------------------------------------------------");
			gui.appendTextArea("");
		}
		
	}
}

