import java.util.Random;

import javax.swing.JOptionPane;

/**
 * This class defines a computer opponent or bot for the game.
 * The main addition the Bot class makes to the Player class
 * is the addition of the getAction method, which prompts the
 * bot to make a move based on current game conditions.
 */


public class Bot extends Player {
	private int strategy; // An indicator of the bot's confidence
	
	// Indicates bot's confidence in its starting hand
	private static final int STRONG = 0;
	private static final int MID = 1;
	private static final int LOW = 2;
	private static final int WEAK = 3;
	
	private static double[] preFlopThresholds = {0.3, 0.5, 0.8};
	private Random rand;
	
	/**
	 * Basic constructor for instances of Bot
	 * @param botName			A String containing the name of this bot.
	 * @param startingChips		The number of starting chips for the bot.
	 */
	public Bot(String botName,int startingChips) {
		super(botName, startingChips);
		rand = new Random();
		//semiBluff = false;
	}
	
	/**
	 * Examines Bot's current hole cards and the state of the
	 * game and selects an appropriate strategy.
	 * @param state		The Game object currently in use
	 * @return			A String containing the move selected by
	 * 					this instance of Bot
	 */
	public BotMove getAction(Game state) {
		double chipVariation = rand.nextDouble() - .5;
		double aggressiveVariation = rand.nextDouble()/5 - .1;
		int foldEntries = 300 - ((int)(290 * aggressiveVariation));
		int checkEntries = 300 - ((int)(290 * aggressiveVariation));
		int betEntries = 300 + ((int)(290 * aggressiveVariation));
		int raiseEntries = 300 + ((int)(290 * aggressiveVariation));
		int callEntries = 400;
		int chipsToCall;
		double handStrength;
		int standardRaise = ((int)(state.getBigBlind() * (4 + chipVariation)));
		if(state.getBotChipsInPot() < state.getChipsToCall()){//bot needs to fold, call, or raise
			chipsToCall = state.getChipsToCall() - state.getBotChipsInPot();
			if(state.getPot()/chipsToCall >=3){//consider pot odds
				for(int x = 0; x < state.getPot()/chipsToCall - 2; x++){
					foldEntries *= .9;
					callEntries *= 1.1;
				}
			}
		} else {//bot needs to check or bet
			chipsToCall = 0;
		}
		if(state.hasFlopOccured()){
			handStrength = HandStrengthCalculator.getEffectiveHandStrengthOptimistic(state.getBotHoleCards(), state.getVisibleBoardCards());
		} else {//flop has not occured
			handStrength = PreFlopHandRanker.getHoleCardWinRate(state.getBotHoleCards());
		}
		if(Double.isNaN(handStrength)){
			handStrength = 0;
		}
		foldEntries *= (1.5 - handStrength);
		checkEntries *= (1.5 - handStrength);
		callEntries *= (.5 + handStrength);
		raiseEntries *= (handStrength * 2);
		betEntries *= (handStrength * 2);
		if(!state.hasFlopOccured() && handStrength > .75){
			foldEntries *= 0;
		} else if(handStrength > .9){
			foldEntries *= 0;
		}
		
		if(chipsToCall>standardRaise * 1.5){//significant call amount
			foldEntries *= 1.5;
			callEntries *= .75;
		} else if(chipsToCall<standardRaise * .65) {//insignificant call amount
			foldEntries *= .75;
			callEntries *= 1.5;
		}
		if(state.minRaise > standardRaise * 1.5){//significant amount to raise
			raiseEntries *= .75;
			callEntries *= 1.5;
		} else if(state.minRaise < standardRaise * .65){//insignificant raise
			raiseEntries *= 1.25;
			callEntries *= 1.25;
			foldEntries *= .5;
		}
		if(standardRaise%25 >= 13){
			standardRaise += 25 - standardRaise%25;
		} else {
			standardRaise -= standardRaise%25;
		}
		if(chipsToCall == 0){//bot needs to check or bet
			if(rand.nextInt(checkEntries + betEntries) <= checkEntries){
				return new BotMove(Game.Move.CHECK);
			} else {
				return new BotMove(Game.Move.BET,standardRaise);
			}
		} else {//bot needs to fold, call, or raise
			int value = rand.nextInt(foldEntries + callEntries + raiseEntries);
			if(value < foldEntries){
				return new BotMove(Game.Move.FOLD);				
			} else if(value < foldEntries + callEntries){
				return new BotMove(Game.Move.CALL);
			} else {
				return new BotMove(Game.Move.RAISE,standardRaise);
			}
		}
	}
}