/**
 * 
 * The Game class is designed to hold all the information about a game
 * of No Limits Texas Hold 'Em poker that would not otherwise be allocated
 * to another class. Game provides all the methods necessary to manage the
 * playing of multiple hands of Texas Hold 'Em. Game is also designed to
 * work in tandem with an instance of the GUI class. This relationship allows
 * a user to play the game through the GUI provided by the GUI class.
 * 
 * Requirement Sets: 1.0.0, 2.0.0, 3.0.0
 *
 */

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Game {
	
	public static enum Move {FOLD, CHECK, CALL, BET, RAISE, NOMOVE, ALLIN};
	private final int defaultStartingChips = 2000;
	private final int MAX_STARTING_CHIPS = ((Integer.MAX_VALUE) - 1) / 2;
	
	// Requirement: 1.5.0
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
	private int startingChips;					//starting chips
	private int roundsAtEachBlind = 10;			//number of rounds at each blind level
	private int blindLevel = 0;					//current blind level
	private GUI gui = new GUI(this);			//gui
	
	/**
	 * Basic constructor for Game. Creates a new
	 * instance of game using default values.
	 */
	public Game() {
		this.setInitialGameState();
	}
	/**
	 * Resets this instance of Game to default values
	 * that would be otherwise displayed initially.
	 * Prompts the user with a start menu
	 * 
	 * 1.1.4
	 */
	public void resetGame() {
		this.setInitialGameState();
	}
	/**
	 * Sets initial values and prompts the
	 * user with a start menu
	 */
	private void setInitialGameState() {
		hands = 0;
		boardCards = new Card[5];
		pot = 0;
		smallBlind = 25;
		bigBlind = 50;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		botRevealed = false;
		startingChips = defaultStartingChips;
		
		displayStartMenu(); // Requirement 3.4.0
		
		bot = new Bot("Bot", startingChips);
		player = new Player("User", startingChips);
		
		dealer = bot;
		
		// Setup circular linked list for player
		bot.setNextPlayer(player);
		player.setNextPlayer(bot);
		
		// Setup initial board cards
		gui.updateView();
	}
	
	/**
	 * Displays a start menu for the user which prompts the user to
	 * select from one of four options: Start game, set starting chips,
	 * set rounds for each blind, and quit. If the user simply closes
	 * the menu without selecting an option, the start menu will be
	 * displayed again, forcing the user to either fully exit the game
	 * or make a valid selection.
	 * 
	 * Requirement: 3.4.0
	 */
	private void displayStartMenu() {
		boolean gameStarted = false;
		int selectedOption = 0;
		while (!gameStarted) {
			selectedOption = gui.displayStartMenu(); // Display start menu and store user selection
			switch (selectedOption) {
				case 0:		// Start game
							gameStarted = true;
							break;
				case 1:		// Set starting chips
							this.promptForStartingChips();
							break;
				case 2:		// Set rounds for each blind level
							promptForRoundsAtEachBlind();
							break;
				case 3:		// Quit game
							if (gui.confirmQuit()) {
								System.exit(0); // Quit game
							}
							break;
				default: 	
							gui.appendTextAreaLine("Invalid response. Please try again.");
			}
			gui.updateView();
		}
	}
	
	/**
	 * Prompt the user for the blinds used for each round
	 * @return True if successful, false if user cancelled
	 * 
	 * Requirement: 2.1.2
	 */
	private boolean promptForRoundsAtEachBlind() {
		int responseInt = 0;	
		while(responseInt < 1) {
			try{
				String response = JOptionPane.showInputDialog("Enter rounds at each blind level (1 minimum): ", roundsAtEachBlind);
				if (response == null) {
				    return false;
				} else {
				    responseInt = Integer.parseInt(response);
				}
			} catch(NumberFormatException ex) {
			}
		}
		this.roundsAtEachBlind = responseInt;
		return true;
	}
	/**
	 * Prompts user to provide the number of starting chips for the players
	 * @return		True if the change was successful, false if the cancelled
	 * 
	 * Requirement: 2.1.1
	 */
	
	private boolean promptForStartingChips() {
		int responseInt = 0;
		while(responseInt < 500 || responseInt > this.MAX_STARTING_CHIPS) {
			try{
				String response = JOptionPane.showInputDialog("Enter starting chips (500 - " + this.MAX_STARTING_CHIPS + "): ", startingChips);
				if (response == null) {
					return false;
				} else {
					responseInt = Integer.parseInt(response);
				}
			} catch(NumberFormatException ex) {
				
			}
		}
		this.startingChips = responseInt;
		return true;
	}
	/**
	 * Adjust blinds based on blind level
	 * 
	 * Requirement: 2.4.1
	 */
	private void adjustBlinds() {
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
		JOptionPane.showMessageDialog(null,"***** Blinds are now " + smallBlind + " / " + bigBlind + "*****");
	}
	/**
	 * Continues to play whole games of No LImits
	 * Texas Hold 'Em until the player requests to
	 * quit. Assumes initial values were set during
	 * construction of Game and start menu has already
	 * been displayed once
	 * 
	 * Requirement: 1.1.4
	 */
	public void playGames() {
		playGame();
		while (JOptionPane.showConfirmDialog(null, "Do you want to play another game?", "New Game", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
			resetGame();
			playGame();
		}
		JOptionPane.showMessageDialog(null, "Thanks for playing ETCetera's No Limits Texas Hold 'Em!");
		System.exit(0); // Quit game
	}

	/**
	 * This method continues to play hands of Texas Hold 'Em
	 * until only one player has any chips or the user specifies
	 * that they do not want to pay anymore.
	 * 
	 * Requirement: 1.1.1
	 */
	public void playGame() {
	    while (hasChips() && (this.hands == 0 || userWantsToContinue())) {
	    	playHand(); // Play hand
	    }
	    if(bot.getChips() == 0) {
	    	JOptionPane.showMessageDialog(null, "You Win!!");
	    } else if (player.getChips() == 0) {
	    	JOptionPane.showMessageDialog(null, "You Lose!!");
	    } else {
	    	JOptionPane.showMessageDialog(null, "Thanks for playing!");
	    }
	}
	
	/**
	 * Returns true if both players can play another hand,
	 * false if otherwise.
	 * @return		True if both the player and dealer have 
	 * 				a chip count greater than o, false if 
	 * 				other wise
	 */
	private boolean hasChips() {
		int playersWithChips = 0;
		Player tempPlayer = dealer.getNextPlayer();
		if (dealer.getChips() > 0) {
			playersWithChips++;
		}
		while(tempPlayer != dealer) {
			if (tempPlayer.getChips() > 0) {
				playersWithChips++;
			}
			tempPlayer = tempPlayer.getNextPlayer();
		}
	    if (playersWithChips > 1) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	/**
	 * This method prompts the user to specify whether or not they want to
	 * play another hand.
	 * @return		True if the user wants to continue, false if otherwise
	 * 
	 * Requirement 1.1.1
	 */
	 private boolean userWantsToContinue() {
		int wantsToPlay;
	    wantsToPlay = JOptionPane.showConfirmDialog(null, "Continue?", "Continue",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
	   	if (wantsToPlay == 0) {
	   		gui.appendTextAreaLine("");
	   		gui.appendTextAreaLine("---------------------");
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
	 * 
	 * Requirement: 1.1.2
	 */
	private void createHand(boolean isStacked) {
		if (!isStacked) {
			deck.resetDeck(); // Verify deck is shuffled with the appropriate number of cards
		}
		playersInHand = 1;
		// make sure dealer status is set to inHand
		dealer.setInHand(true);
		// reset dealers chips in pot
		dealer.setChipsInPot(0);
		// cycle to first player
		activePlayer = dealer.getNextPlayer();
		// count players and set inHand status
		while(activePlayer != dealer) {
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
			activePlayer.setHoleCards(deck.drawCard(), deck.drawCard());
			activePlayer = activePlayer.getNextPlayer();
		}
		
		// Set board cards
		boardCards[0] = deck.drawCard();
		boardCards[1] = deck.drawCard();
		boardCards[2] = deck.drawCard();
		boardCards[3] = deck.drawCard();
		boardCards[4] = deck.drawCard();
		
		//increment blinds if necessary
		if ((int) (hands / roundsAtEachBlind) > blindLevel) {
			blindLevel++;
			adjustBlinds();
		}

		// ******************end deal cards*****************************
		
		processBlinds(); // Handle blinds
		
		// ******************reset initial values for hands**************************
		//chipsToCall = bigBlind;
		minRaise = bigBlind;
		flopFlag = false;
		turnFlag = false;
		riverFlag = false;
		botRevealed = false;
		
		gui.updateView(); // Update view to reflect new cards
	}
	
	/**
	 * Handles the blinds for a game containing two players: the user and one bot.
	 * 
	 * Requirement: 2.4.1
	 */
	private void processBlinds() {
		// ******************blinds*************************************
		// this is configured for 2 players, if we add players we'll need to adjust logic
		// small blind (for 2 players this is the dealer)
		if (dealer.getChips() >= smallBlind) { //  dealer can cover small blind
			pot = smallBlind;
			dealer.setChipsInPot(smallBlind);
			dealer.setChips(dealer.getChips() - smallBlind);
			chipsToCall = smallBlind;
			gui.appendTextAreaLine(dealer.getPlayerName() + " has posted small blind of: " + smallBlind);
		} else { //  dealer cannot cover small blind
			pot = dealer.getChips();
			dealer.setChipsInPot(dealer.getChips());
			gui.appendTextAreaLine(dealer.getPlayerName() + " goes all in with small blind: " + dealer.getChips());
			chipsToCall = dealer.getChips();
			dealer.setChips(0);
		}
		// big blind (for 2 players this is the player that is not the dealer)
		activePlayer = dealer.getNextPlayer();
		if (activePlayer.getChips() >= bigBlind) { //  player can cover bigBlind
			pot = pot + bigBlind;
			activePlayer.setChipsInPot(bigBlind);
			activePlayer.setChips(activePlayer.getChips() - bigBlind);
			chipsToCall = bigBlind;
			gui.appendTextAreaLine(activePlayer.getPlayerName() + " has posted big blind of: " + bigBlind);
		} else { //  Player cannot cover bigBlind
			pot = pot + activePlayer.getChips();
			activePlayer.setChipsInPot(activePlayer.getChips());
			gui.appendTextAreaLine(activePlayer.getPlayerName() + " has gone all in with big blind of: " + activePlayer.getChips());
			if (activePlayer.getChips() > chipsToCall) {
				chipsToCall = activePlayer.getChips();
			}
			activePlayer.setChips(0);
		}
		// ******************end blinds***********************************
	}
	
	/**
	 * Processes a Fold.
	 * 
	 * Requirement: 1.2.1
	 */
    public void fold() {
    	activePlayer.setInHand(false);
    	playersInHand--;
    	if (activePlayer instanceof Bot) {
    		revealbot();
    	}
    	
    	gui.appendTextAreaLine(activePlayer.toString() + " has folded");
    }
    
    /**
     * Processes a Raise. Attempts to handle a raise request by the
     * active player. Verifies that it is possible for the player to
     * raise by the specified amount and takes the appropriate action.
     * @param raiseOrBetAmt	The amount of chips to raise.
     * @return				True if the raise was successful, false
     * 						if otherwise (for instance, if the active
     * 						player has insufficient chips to cover the
     * 						raise amount specified).
     * 
     * Requirement: 1.2.2, 1.2.3, 1.2.6
     */
    public boolean raiseOrBet(int raiseOrBetAmt) { // returns true if successful
    	if (raiseOrBetAmt < 1) {
    		return false; // Cannot raise by zero or a negative number of chips
    	}
    	if (raiseOrBetAmt + chipsToCall - activePlayer.getChipsInPot() >= activePlayer.getChips()){
    		// player was trying to raise more than they had, resetting to make them all in
    		raiseOrBetAmt = activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot());  
    	}
    	if (raiseOrBetAmt >= minRaise || raiseOrBetAmt + (chipsToCall - activePlayer.getChipsInPot()) == activePlayer.getChips()) {
    		// adjust minRaise
    		if (raiseOrBetAmt > minRaise) {
    			minRaise = raiseOrBetAmt;
    		}
    		// adjust chipsToCall
    		chipsToCall = chipsToCall + raiseOrBetAmt;
    		pot = pot + (chipsToCall - activePlayer.getChipsInPot());
    		// adjust players chip count setChips(current stack - (new chips going in pot))
    		activePlayer.setChips(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) );
    		// adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		gui.appendTextAreaLine(activePlayer.toString() + " has raised the pot " + raiseOrBetAmt);
    		return true;
    	} else { //  player either has insufficient funds or the raiseOrBetAmt is not of sufficient size
    		return false;
    	}
    }
    
    /**
     * Processes a Check.
     * @return		True if it is possible to check, false if otherwise.
     * 
     * Requirement: 1.2.4
     */
   	public boolean check() { // returns true if check successful
    	if (activePlayer.getChipsInPot() == chipsToCall) { // ok to check
    		gui.appendTextAreaLine(activePlayer.toString() + " has checked");
    		return true;
    	} else { // player has not matched the necessary chips in pot
    		return false;
    	}
    }
    
    /**
     * Processes a Call. This method will determine whether
     * the player is capable of calling without going all-in and
     * adjusts the game and player states accordingly.
     * 
     * Requirement: 1.2.3
     */
    private void call() {
    	int amtToCall = chipsToCall - activePlayer.getChipsInPot();
    	if (amtToCall < activePlayer.getChips()) { //  verify player can cover call
    		// adjust players chip count
    		activePlayer.setChips(activePlayer.getChips() - amtToCall);
    		// adjust players chip in pot count
    		activePlayer.setChipsInPot(chipsToCall);
    		pot = pot + amtToCall;
    		gui.appendTextAreaLine(activePlayer.toString() + " has called");
    	} else { //  player is all in
    		activePlayer.setChipsInPot(activePlayer.getChipsInPot() + activePlayer.getChips());
    		pot = pot + activePlayer.getChips();
    		activePlayer.setChips(0);
    		gui.appendTextAreaLine(activePlayer.toString() + " has called and is all in");
    	}
    }
    
    /**
     * Disperses chips based on which players remain in
     * the hand and their respective hand strengths. The
     * pot goes to the player with the strongest hand, or
     * is split in the event that both hands are exactly
     * equal.
     * 
     * Requirement: 1.1.3
     */
    private void disperseChips() {
    	int maxChipsToWin = 0;
    	if (bot.inHand()) {
    		if (player.inHand()) {
    			gui.appendTextAreaLine("Player's best hand: " + player.getHandString(player.getHandValue(this)));
    			gui.appendTextAreaLine("Bot's best hand: " + bot.getHandString(bot.getHandValue(this)));
    			if (player.getHandValue(this) > bot.getHandValue(this)) { // player won
    				maxChipsToWin = player.getChipsInPot() * 2;
    				if (maxChipsToWin >= pot) { // gets whole pot
    					player.setChips(player.getChips() + pot);
    					gui.appendTextAreaLine("You won " + pot + " chips!");
    				} else { // gets partial pot
    					player.setChips(player.getChips() + maxChipsToWin);
    					gui.appendTextAreaLine("You won " + maxChipsToWin + " chips!");
    					bot.setChips(bot.getChips() + pot - maxChipsToWin);
    					gui.appendTextAreaLine("Bot won " + (pot - maxChipsToWin) + " chips!");
    				}
    			} else if (player.getHandValue(this) == bot.getHandValue(this)) { //  tied
    				player.setChips(player.getChips() + player.getChipsInPot());
    				bot.setChips(bot.getChips() + bot.getChipsInPot());
    				gui.appendTextAreaLine("Split pot! You won " + player.getChipsInPot() + " chips!");
    				gui.appendTextAreaLine("Bot won " + bot.getChipsInPot() + " chips!");
    			} else { //  player lost
    				maxChipsToWin = bot.getChipsInPot() * 2;
    				if (maxChipsToWin >= pot) { // gets whole pot
    					bot.setChips(bot.getChips() + pot);
    					gui.appendTextAreaLine("Bot won " + pot + " chips!");
    				} else { // gets partial pot
    					bot.setChips(bot.getChips() + maxChipsToWin);
    					gui.appendTextAreaLine("Bot won " + maxChipsToWin + " chips!");
    					player.setChips(player.getChips() + pot - maxChipsToWin);
    					gui.appendTextAreaLine("Player won " + (pot - maxChipsToWin) + " chips!");
    				}
    			}
    		} else { //  player folded
    			bot.setChips(bot.getChips() + pot);
    			gui.appendTextAreaLine("Bot won " + pot + " chips!");
    		}
    		
    	} else { //  bot folded
    		player.setChips(player.getChips() + pot);
    		gui.appendTextAreaLine("You won " + pot + " chips!");
    	}
    	botRevealed = true;
    	gui.showBotCards();
    }
    /**
     * @return		True if the bot's cards
     * 				should be visible to 
     * 				other players, false if
     * 				otherwise
     */
    public boolean botRevealed() {
    	return this.botRevealed;
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
     * 
     * Requirement: 1.1.2
     */
    private void playHand() {
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
     * 
     * Requirement: 1.1.2
     */
    private void playHand(Deck stackedDeck) {
    	boolean onceThrough = false;
    	Player prevPlayer;
    	
    	gui.setDealerLabel(dealer);
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
    		gui.updateBoardCards(); // Update board cards displayed
    		showUserStatistics();
    		roundOfBetting();
    		gui.updateView();
    		numRounds++;
    	}
    	disperseChips(); // Disperse chips
    	gui.updateView();
    		
    	// remove players with no chips
    	activePlayer = dealer.getNextPlayer();
    	prevPlayer = dealer;
    	while(activePlayer != dealer.getNextPlayer() || !onceThrough) {
    		onceThrough = true;
    		if (activePlayer.getChips() <= 0) {
    			prevPlayer.setNextPlayer(activePlayer.getNextPlayer());
    		} else {
    			prevPlayer = activePlayer;
    		}
    		activePlayer = activePlayer.getNextPlayer();
    	}
    	
    	// advance dealer and keep going
    	dealer = dealer.getNextPlayer();
    	// increment hand counter
    	this.hands++;
    }
    /**
     * Displays information about the player's current 
     * hand, given the currently known board cards. 
     * More information about the information provided
     * can be found be referencing the individual methods
     * called from the HandStrenthCalculator and
     * PreFlopHandRanker classes. The statistics displayed
     * are as follows:
     * 
     * Pre-flop: Displays Win rate
     * 
     * Post-flop, pre-river: Displays HS, EHS, and EHS'
     * 
     * Post-flop, river: Displays HS
     * 
     * Requirement Set: 2.5.0
     */
    private void showUserStatistics() {
		StringBuilder userStats = new StringBuilder();
		userStats.append("**** Stats for your current hand ****\n");
		if (this.hasFlopOccured()) {
			if (this.hasRiverOccured()) { // River has occurred
			// Requirement: 2.5.2
				userStats.append(String.format("Hand Strength: %.2f", 
						100 * HandStrengthCalculator.getHandStrength(player.getHoleCards(), this.getVisibleBoardCards())) + "%\n");
			} else { // River has not occurred
				// Requirements: 2.5.2, 2.5.3, 2.5.4
				userStats.append(String.format("EHS: %.2f", 
						100 * HandStrengthCalculator.getEffectiveHandStrength(player.getHoleCards(), this.getVisibleBoardCards())) + "%\n");
				userStats.append(String.format("EHS': %.2f", 
						100 * HandStrengthCalculator.getEffectiveHandStrengthOptimistic(player.getHoleCards(), this.getVisibleBoardCards())) + "%\n");
				userStats.append(String.format("Hand Strength: %.2f", 
						100 * HandStrengthCalculator.getHandStrength(player.getHoleCards(), this.getVisibleBoardCards())) + "%\n");
			}
		} else { // Flop has not occurred
			// Requirement: 2.5.1
			userStats.append(String.format("Estimated Win Rate For Your Hole Cards: %.2f",
					100 * PreFlopHandRanker.getHoleCardWinRate(player.getHoleCards())) + "%\n");
		}
		gui.appendTextAreaLine(userStats.toString());
	}
	/**
     * This method handles a round of betting, where a move is elicited 
     * from each player in turn.
     * 
     * Requirement: 1.1.2
     */
    private void roundOfBetting() {
    	boolean firstRound = true;
    	minRaise = bigBlind;  //reset minRaise
    	Player lastToAct;
    	// firstRound is just a boolean variable set to make sure the loop below cycles through all players at least one time
    	// if before flop 'lastToAct will be set to big blind before flop and dealer after flop
    	// for 2 players this will be the dealer
    	if (flopFlag) {
    		lastToAct = dealer;
    	} else {
    		lastToAct = dealer.getNextPlayer();
    	}
    	activePlayer = lastToAct.getNextPlayer();
    	while((firstRound || !allSquare()) && playersInHand > 1) {
    		// update firstRound flag
    		if (activePlayer == lastToAct) { //if we have cycled around to lastToAct, everyone has had opportunity to bet/raise
    			firstRound = false;
    		}
    		// Get action from player or Bot class
    		if (activePlayer instanceof Bot) { // Current player is the bot
    			if(bot.getChips() > 0 && (player.getChips() > 0 || activePlayer.getChipsInPot() < chipsToCall)) { //  verify bot has chips
    				if (chipsToCall > activePlayer.getChipsInPot() || activePlayer.getChips() > 0) { // action still required
    					//get and process response from bot class
    			 	    processBotAction(bot.getAction(this));
    				}
    			 }
    		} else { //  this is a human player
    			// verify not all in
    			gui.updateView();
    			if (activePlayer.getChips() == 0 || (bot.getChips() == 0 && activePlayer.getChipsInPot() >= bot.getChipsInPot())) { // player is all in or bot is all in (and player has covered), no action is required
    				// message here if we so choose to
    			} else { //  player is not all in
    				getPlayerAction();
    			}
    		}
    		// cycle to next player
    		activePlayer = activePlayer.getNextPlayer();
    	}
    }
    /**
     * Verifies whether or not all players in the hand
     * have the same number of chips in the pot
     * @return		Returns true if all players in the 
     * 				hand have the same number of chips in pot 
     * 				or are all in
     * 
     * Requirement: 1.1.2
     */
    private boolean allSquare() { 
    	boolean returnVal = true;
    	Player firstPlayer = activePlayer;
    	Player tempPlayer = activePlayer;
    	boolean firstFlag = true;
    	while((tempPlayer != firstPlayer && returnVal) || firstFlag) {
    		firstFlag = false;
    		if (tempPlayer.getChipsInPot() != chipsToCall && tempPlayer.getChips() != 0 && tempPlayer.inHand()) {
    			returnVal = false;
    		}
    		tempPlayer = tempPlayer.getNextPlayer();
    	}    	
    	return returnVal;
    }
    
    /**
     * Processes the bot's action.
     * @param move		The BotMove object representing
     * 					the bot's move for this round
     * 					of betting
     * 
     * Requirement 1.3.3
     */
    private void processBotAction(BotMove move) {
    	boolean valid = false;
    	switch(move.getMove()) {
	    	case ALLIN:	if (activePlayer.getChips() > 0) {
			    			if (chipsToCall - activePlayer.getChipsInPot() >= activePlayer.getChips()) { // calling an allin
			    				processResponse(Move.CALL);
			    				valid = true;
			    			} else { // this is an actual raise
			    				raiseOrBet(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()));
			    				valid = true;
			    			}
			    		}    
	    				break;	
	    	case BET: 	int amt = move.getAmount();
	    				raiseOrBet(amt);
	    				valid = true;
	    				break;	
	    	case CHECK:	if (!check()) {
	    					call(); // Unable to check, must call
	    				}
	    				valid = true;
	    				break;
	    	case CALL:	call();
	    				valid = true;
	    				break;	
	    	case RAISE:	amt = move.getAmount();
	    				if (amt > minRaise){
	    					raiseOrBet(amt);
	    				} else {
	    					call();
	    				}
						valid = true;
	    				break;
	    	case FOLD:	fold();
	    				valid = true;
	    				break;
	    	default:	valid = false; // Invalid user input
	    }
    	if (!valid) {
    		gui.appendTextAreaLine("Received invalid response for bot action." + move.getMove());
    	}
    }
    
    /**
     * This method prompts the player to select an appropriate action.
     * 
     * Requirement: 1.2.0
     */
    private void getPlayerAction() {
    	gui.promptUser();
    	boolean moveMade = false;
    	boolean valid = false;
    	
    	gui.enableMoveButtons();
    	gui.updateView();
    	if (activePlayer.getChipsInPot() == chipsToCall) { // no raise or call is required
	    	if(activePlayer.getChips() > minRaise) { // player has 3 options
	    	     gui.disableRaise();
	    	     gui.disableCall();
	    	     gui.disableFold();
	    	} else { // player has 2 options
	    	     gui.disableRaise();
	    	     gui.disableCall();
	    	     gui.disableFold();
	    	     gui.disableCheck();
	    	}    		
    	} else { // player needs to call, raise, or fold
    	    if(activePlayer.getChips() > (minRaise + chipsToCall - activePlayer.getChipsInPot())) { // player has 4 options
    	     	 gui.disableCheck();
    	     	 gui.disableBet();
    	     } else {
    	     	 if(activePlayer.getChips() > (chipsToCall - activePlayer.getChipsInPot())) { // player can cover a call, but does not have enough for the min raise
    	     		 gui.disableCheck();
    	     		 gui.disableBet();
    	     		 gui.disableRaise();
    	     	 } else {  // player will be going all in if they don't fold
    	     		 gui.disableBet();
    	     		 gui.disableCall();
    	     		 gui.disableCheck();
    	     		 gui.disableRaise();
    	     	 }
    	     }
    	}
    	while(!moveMade) {
    		Move userMove = gui.getUserMove();
    			if (userMove.equals(Move.BET)) {
    				if (activePlayer.getChipsInPot() == chipsToCall && activePlayer.getChips() > minRaise) { // player wants to bet, verify they have sufficient funds to make min bet
	    				valid = true;
	    			}
    			} else if (userMove.equals(Move.CHECK) || userMove.equals(Move.CALL)) {
    				if (activePlayer.getChipsInPot() == chipsToCall || activePlayer.getChips() > (chipsToCall - activePlayer.getChipsInPot())) { // player wants to call/check, verify they have funds to do so
	    				valid = true;
	    			}
    			} else if (userMove.equals(Move.RAISE)) {
    				if ((activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot())) >= minRaise) { // player wants to raise, verify they have funds to do so
	    				valid = true;
	    			}
    			} else if (userMove.equals(Move.FOLD)) {
    				if (activePlayer.getChipsInPot() < chipsToCall) { // player wants to fold, verify folding is valid option
	    				valid = true;
	    			}
    			} else if (userMove.equals(Move.ALLIN)) {
    				valid = true;
    			}
    		if (valid) {
    			moveMade = processResponse(userMove);
    		} else {
    			JOptionPane.showMessageDialog(null, "Invalid move!");
    		}
    	}
    	gui.disableMoveButtons();
    	gui.removeUserPrompt();
    }
    
    /**
     * This method handles user responses by making the
     * move the player requested.
     * @param r		The response provided by the user
     * @return		True if a move is made, false if
     * 				otherwise
     * 
     * Requirements: 1.2.0, 1.3.3
     */
    private boolean processResponse(Move move) {
    	if (move.equals(Move.BET) || move.equals(Move.RAISE) ) { // get bet/raise amount and process
    		boolean valid = false;
    		int amt = 0;
    		String strAmt;
    		while(!valid) {
    			String promptType = "bet"; // Show bet prompt by default
    			if (move.equals(Move.RAISE)) { // Show raise prompt if the move type is a raise
    				promptType = "raise";
    			}
    			strAmt = JOptionPane.showInputDialog("Enter amount to " + promptType + " (" + minRaise + " - " 
    					    					+ (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()) + ")"), minRaise);
    			if (strAmt == null) {
    				return false; // User cancelled
    			} else {
        			amt = Integer.parseInt(strAmt);
        			
        			if (amt >= minRaise && amt <= (activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()))) {
        				raiseOrBet(amt);
        				valid = true;
        			}
    			}
    		}
    	}
    	if (move.equals(Move.ALLIN)) { // player is going all in
    		if (activePlayer.getChips() > 0) {
    			if (chipsToCall - activePlayer.getChipsInPot() >= activePlayer.getChips()) { // calling an allin
    				processResponse(Move.CALL);
    			} else { // this is an actual raise
    				raiseOrBet(activePlayer.getChips() - (chipsToCall - activePlayer.getChipsInPot()));
    			}
    		}    		
    	}
    	if (move.equals(Move.CALL) || move.equals(Move.CHECK)) { // process check/call
    		if (!check()) {
    			call();
    		}
    	}
    	if (move.equals(Move.FOLD)) { // fold
    		fold();
    	}
    	return true;
    }
    
    /**
     * Mark the bot's hole cards as visible
     */
	public void revealbot() {
		botRevealed = true;
	}
	
	/**
	 * Perform the flop
	 * 
	 * Requirement: 1.1.2
	 */
	private void flop() {
		flopFlag = true;
		gui.updateBoardCards();
	}
	/**
	 * Perform the turn
	 * 
	 * Requirement: 1.1.2
	 */
	private void turn() {
		turnFlag = true;
		gui.updateBoardCards();
	}
	/**
	 * Perform the river
	 * 
	 * Requirement: 1.1.2
	 */
	private void river() {
		riverFlag = true;
		gui.updateBoardCards();
	}
	/**
	 * @return		An int representing the number of chips in the pot.
	 */
	public int getPot() {
		return pot;
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
	 * Returns an ArrayList<Card> containing all currently
	 * visible board cards.
	 * @return		A ArrayList<Card> containing all currently
	 * 				visible board cards. If none are visible
	 * 				(i.e. the game is pre-flop), an empty
	 * 				ArrayList<Card> will be returned.
	 */
	public ArrayList<Card> getVisibleBoardCardsInArrayList() {
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
	 * @return		A Card[] containing all visible board
	 * 				cards, or null if there are no visible
	 * 				board cards at the time.
	 */
	public Card[] getVisibleBoardCards() {
		Card[] boardCards;
		if (this.turnFlag) { // If turn occurred...
			boardCards = new Card[5];
		} else if (this.riverFlag) { // If river occurred...
			boardCards = new Card[4];
		} else if (this.flopFlag) { // If flop occurred...
			boardCards = new Card[3];
		} else {
			return null; // No board cards visible
		}
		
		for (int i = 0; i < boardCards.length; i++) {
			boardCards[i] = this.boardCards[i];
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
	 * @return		The number of chips required
	 * 				to call and remain in the hand
	 */
	public int getChipsToCall() {
		return chipsToCall;
	}
	
	/**
	 * 
	 * @return		A Card[] containing
	 * 				the player's hole cards
	 * 				or null if they have not
	 * 				been set
	 */
	public Card[] getPlayerHoleCards() {
		if (player == null) {
			return null;
		} else {
			return this.player.getHoleCards();
		}
	}
	
	/**
	 * 
	 * @return		A Card[] containing
	 * 				the bot's hole cards
	 * 				or null if they have not
	 * 				been set
	 */
	public Card[] getBotHoleCards() {
		if (bot == null) {
			return null;
		} else {
			return this.bot.getHoleCards();
		}
	}
	/**
	 * 
	 * @return		Return player's current
	 * 				chip count
	 */
	public int getPlayerChips() {
		if (player == null) {
			return 0;
		} else {
			return this.player.getChips();
		}
	}
	/**
	 * 
	 * @return		Return Bot's current
	 * 				chip count
	 */
	public int getBotChips() {
		if (bot == null) {
			return 0;
		} else {
			return this.bot.getChips();
		}
	}
	/**
	 * 
	 * @return		Return the number of
	 * 				chips Bot has put in
	 * 				the pot
	 */
	public int getBotChipsInPot() {
		if (bot == null) {
			return 0;
		} else {
			return this.bot.getChipsInPot();
		}
	}
	/**
	 * 
	 * @return		Return the number of chips
	 * 				the player has put in the 
	 * 				pot
	 */
	public int getPlayerChipsInPot() {
		if (player == null) {
			return 0;
		} else {
			return this.player.getChipsInPot();
		}
	}
	/**
	 * @return		The minimum raiseOrBet amount
	 */
	public int getMinRaise() {
		return this.minRaise;
	}
}