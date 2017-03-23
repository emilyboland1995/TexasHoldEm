import java.util.Random;

/**
 * This class defines a computer opponent or bot for the game.
 * The main addition the Bot class makes to the Player class
 * is the addition of the getAction method, which prompts the
 * bot to make a move based on current game conditions.
 */


public class Bot extends Player {
	private Random rand;
	
	/**
	 * Basic constructor for instances of Bot
	 * @param botName			A String containing the name of this bot.
	 * @param startingChips		The number of starting chips for the bot.
	 */
	public Bot(String botName,int startingChips) {
		super(botName, startingChips);
		rand = new Random();
	}
	
	/**
	 * Prompts the Bot to make a move.
	 * @param game		The current Game object containing all
	 * 					the current game info.
	 * @return			Returns a string containing the bot's move
	 */
	public String getAction(Game game) {
		if (!game.hasFlopOccured()) { // Pre-flop
			double relativeStrength = PreFlopHandRanker.getRelativeHandStrengthWeak(this.getHoleCards());
			if (relativeStrength > 0.5) {
				if (!game.check()) {
					return "C";
				}
			} else {
				double choice = rand.nextDouble();
				if (choice > 0.2) {
					if (!game.check()) {
						return "C";
					}
				} else {
					return "F";
				}
			}
		} else {// Post-flop
			double relativeStrength = PostFlopHandRanker.getRelativeHandStrength(super.getAllCards(game, this.getHoleCards()));
			if (relativeStrength < 0.3) {
				return "F";
			} else if (relativeStrength < 0.8) {
				if (!game.check()) {
					return "C";
				}
			} else { // relativeStrength >= 0.8
				if (this.getChips() - this.getChipsInPot() > 0) {
					return ("R" + (this.getChips() - this.getChipsInPot()) / 2);
				} else {
					if (!game.check()) {
						return "C";
					} else {
						return "C";
					}
				}
			}
		}
		return "error";
	}
}
