/**
 * 
 * A Simple class for encapsulating a single move
 * by instances of the class Bot. Allows the move
 * type and, if applicable, the amount of chips
 * associated with the move to be accessed
 */

public class BotMove {
	private Game.Move move;	// The move type
	private int amount;		// The amount of chips, if applicable
	
	/**
	 * Basic constructor. Allows the value of move to
	 * be set while assigning amount the default value
	 * of 0.
	 * @param move		The Game.Move representing
	 * 					the desired move
	 */
	public BotMove(Game.Move move) {
		this.move = move;
		this.amount = 0;
	}
	/**
	 * 
	 * @param move		The Game.move representing
	 * 					the desired move
	 * @param amount	The number of chips associated
	 * 					with the move (for bet's and raises)
	 */
	public BotMove(Game.Move move, int amount) {
		this.move = move;
		this.amount = amount;
	}
	/**
	 * @return		Returns the Game.Move 
	 *				representing the move
	 *				made
	 */
	public Game.Move getMove() {
		return this.move;
	}
	/**
	 * @return		The number of chips
	 * 				associated with the 
	 * 				move
	 */
	public int getAmount() {
		return this.amount;
	}
}