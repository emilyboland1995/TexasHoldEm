import java.util.Random;

/**
 * This class defines a computer opponent or bot for the game.
 * The main addition the Bot class makes to the Player class
 * is the addition of the getAction method, which prompts the
 * bot to make a move based on current game conditions.
 * 
 * Requirement Sets: 1.3.0, 2.3.0
 */

public class Bot extends Player {
	private int strategy; // An indicator of the bot's confidence
	
	// Indicates bot's confidence in its starting hand
	private static final int STRONG = 0;
	private static final int MID = 1;
	private static final int LOW = 2;
	private static final int WEAK = 3;
	
	// Pre-flop action thresholds, Requirement 1.3.1
	private static double[] preFlopThresholds = {0.3, 0.5, 0.8};
	
	// Used to randomize the bot's behavior
	private Random rand;
	
	/**
	 * Basic constructor for instances of Bot
	 * @param botName			A String containing the name of this bot.
	 * @param startingChips		The number of starting chips for the bot.
	 */
	public Bot(String botName,int startingChips) {
		super(botName, startingChips);
		this.rand = new Random();
	}
	
	/**
	 * Examines Bot's current hole cards and the state of the
	 * game and selects an appropriate strategy.
	 * @param state		The Game object currently in use
	 * @return			A String containing the move selected by
	 * 					this instance of Bot
	 * 
	 * Requirements: 1.3.1, 1.3.2, 1.3.3, 2.3.1, 2.3.2
	 */
	public BotMove getAction(Game state) {
		double chipVariation = rand.nextDouble() - .5;
		double aggressiveVariation = rand.nextDouble() / 5 - .1;
		int foldEntries = 300 - ((int)(290 * aggressiveVariation));
		int checkEntries = 300 - ((int)(290 * aggressiveVariation));
		int betEntries = 300 + ((int)(290 * aggressiveVariation));
		int raiseEntries = 300 + ((int)(290 * aggressiveVariation));
		int callEntries = 400;
		int chipsToCall;
		double handStrength;
		int standardRaise = ((int)(state.getBigBlind() * (4 + chipVariation)));
		if (state.getBotChipsInPot() < state.getChipsToCall()) { //bot needs to fold, call, or raise
			chipsToCall = state.getChipsToCall() - state.getBotChipsInPot();
			if (state.getPot()/chipsToCall >=3) { //consider pot odds
				for(int x = 0; x < state.getPot()/chipsToCall - 2; x++) {
					foldEntries *= .9;
					callEntries *= 1.1;
				}
			}
		} else { // Bot needs to check or bet
			chipsToCall = 0;
		}
		if (state.hasFlopOccured()) {
			handStrength = HandStrengthCalculator.getEffectiveHandStrengthOptimistic(state.getBotHoleCards(), state.getVisibleBoardCards());
		} else { // flop has not occurred
			handStrength = PreFlopHandRanker.getHoleCardWinRate(state.getBotHoleCards());
		}
		if (Double.isNaN(handStrength)) { // Ensure handStrength is a valid double
			handStrength = 0;
		}
		foldEntries *= (1.5 - handStrength);
		checkEntries *= (1.5 - handStrength);
		callEntries *= (.5 + handStrength);
		raiseEntries *= (handStrength * 2);
		betEntries *= (handStrength * 2);
		if (!state.hasFlopOccured() && handStrength > .75) {
			foldEntries *= 0;
		} else if (handStrength > .9) {
			foldEntries *= 0;
		}
		
		if (chipsToCall>standardRaise * 1.5) { // significant call amount
			foldEntries *= 1.5;
			callEntries *= .75;
		} else if (chipsToCall<standardRaise * .65) { // insignificant call amount
			foldEntries *= .75;
			callEntries *= 1.5;
		}
		if (state.getMinRaise() > standardRaise * 1.5) { // significant amount to raise
			raiseEntries *= .75;
			callEntries *= 1.5;
		} else if (state.getMinRaise() < standardRaise * .65) { // insignificant raise
			raiseEntries *= 1.25;
			callEntries *= 1.25;
			foldEntries *= .5;
		}
		if (standardRaise % 25 >= 13) {
			standardRaise += 25 - standardRaise % 25;
		} else {
			standardRaise -= standardRaise % 25;
		}
		if (chipsToCall == 0) { // bot needs to check or bet
			if (rand.nextInt(checkEntries + betEntries) <= checkEntries) {
				return new BotMove(Game.Move.CHECK);
			} else {
				return new BotMove(Game.Move.BET,standardRaise);
			}
		} else { // bot needs to fold, call, or raise
			int value = rand.nextInt(foldEntries + callEntries + raiseEntries);
			if (value < foldEntries) {
				return new BotMove(Game.Move.FOLD);				
			} else if (value < foldEntries + callEntries) {
				return new BotMove(Game.Move.CALL);
			} else {
				return new BotMove(Game.Move.RAISE,standardRaise);
			}
		}
	}
	
	/**
	 * Examines Bot's current hole cards and the state of the
	 * game and selects an appropriate strategy.
	 * 
	 * Note: This method represents an obsolete betting strategy
	 * and the new version of getAction should be used instead
	 * 
	 * @param state		The Game object currently in use
	 * @return			A String containing the move selected by
	 * 					this instance of Bot
	 * 
	 * Requirements: 1.3.1, 1.3.2, 2.3.0
	 */
	public BotMove getActionOLD(Game state) {
		double choice = rand.nextDouble();
		if (state.hasFlopOccured()) {
			if (state.hasRiverOccured()) { // Final round of betting
				double handStrength = HandStrengthCalculator.getHandStrength(this.getHoleCards(), state.getVisibleBoardCards());
				double potOdds = state.getChipsToCall() / (state.getPot() + state.getChipsToCall());
				if (handStrength >= potOdds) { // We are more likely to make a profit than not
					if (choice <= 0.50) {
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * handStrength));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else {
						return new BotMove(Game.Move.CALL);
					}
				} else { // Pot odds do not favor the bot
					if (choice <= 0.25) { // Bluff occasionally, if possible
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * handStrength));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else { // Call 
						return new BotMove(Game.Move.CALL);
					}
				}
			} else if (state.hasTurnOccured()) { // On the turn
				HandPotential potentials = new HandPotential(this.getHoleCards(), state.getVisibleBoardCards());
				double potOdds = state.getChipsToCall() / (state.getPot() + state.getChipsToCall());
				if (potentials.getPositivePotential() >= potOdds) { // We are more likely to make a profit than not
					if (choice <= 0.40) {
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * potentials.getPositivePotential()));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else {
						return new BotMove(Game.Move.CALL);
					}
				} else { // Pot odds do not favor the bot
					if (choice <= 0.15) { // Bluff occasionally, if possible
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * potentials.getPositivePotential()));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else { // Call 
						return new BotMove(Game.Move.CALL);
					}
				}
			} else { // On the flop
				HandPotential potentials = new HandPotential(this.getHoleCards(), state.getVisibleBoardCards());
				double EHSOptimistic = HandStrengthCalculator.getEffectiveHandStrengthOptimistic(this.getHoleCards(), 
						state.getVisibleBoardCards(), potentials);
				if (EHSOptimistic >= 0.75) {
					if (choice <= 0.75) {
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * potentials.getPositivePotential()));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else {
						return new BotMove(Game.Move.CALL);
					}
				} else if (EHSOptimistic >= 0.5) {
					if (choice <= 0.5) {
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * potentials.getPositivePotential()));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else {
						return new BotMove(Game.Move.CALL);
					}
				} else {
					if (choice <= 0.3) {
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * potentials.getPositivePotential()));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else if (choice <= 0.7) {
						return new BotMove(Game.Move.CALL);
					} else {
						return new BotMove(Game.Move.FOLD);
					}
				}
			}
		} else { // Pre-flop strategy. Never fold pre-flop
			evaluatePreFlopStrength();
			switch (this.strategy) {
				case STRONG: // Strong hand, bet often
					if (choice <= 0.85) { // Bet
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * (choice % 0.20)));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else { 
						return new BotMove(Game.Move.CALL);
					}
				case MID: // Moderately strong hand, bet less often, call otherwise
					if (choice <= 0.65) { // Bet
						// Bet more conservatively than with STRONG hand
						if (this.getChips() - this.getChipsInPot() > 0) {
							return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * (choice % 0.05)));
						} else {
							return new BotMove(Game.Move.CALL);
						}
					} else {
						return new BotMove(Game.Move.CALL);
					}
				case LOW: // Call moderately often, occasionally bluff
					if (choice <= 0.90) {
						return new BotMove(Game.Move.CALL);
					} else {
						return new BotMove(Game.Move.RAISE, (int) ((this.getChips() - this.getChipsInPot()) * (choice % 0.02)));
					}
				default: // Fold often, hand is weak
					return new BotMove(Game.Move.CALL); // Call/Check
			}
		}
	}
	/**
	 * Determines the strategy the bot will pursue based on the strength
	 * of the bot's hole cards
	 * 
	 * Requirement: 1.3.3, 2.3.2
	 */
	private void evaluatePreFlopStrength() {
		double preFlopStrength = PreFlopHandRanker.getHoleCardWinRate(this.getHoleCards());
		if (preFlopStrength >= preFlopThresholds[2]) { // Strong pair, bet strong
			this.strategy = STRONG;
		} else if (preFlopStrength >= preFlopThresholds[1]) { // Average pair, call/check, possibly bet
			this.strategy = MID;
		} else if (preFlopStrength >= preFlopThresholds[0]) { // Weaker pair, call/check sometimes
			this.strategy = LOW;
		} else { // Very weak pair, play rarely
			this.strategy = WEAK;
		}
	}
}