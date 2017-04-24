public class BotMove {
	private Game.Move move;
	private int amount;
	
	public BotMove(Game.Move move) {
		this.move = move;
		this.amount = 0;
	}
	
	public BotMove(Game.Move move, int amount) {
		this.move = move;
		this.amount = amount;
	}
	
	public Game.Move getMove() {
		return this.move;
	}
	
	public int getAmount() {
		return this.amount;
	}
}