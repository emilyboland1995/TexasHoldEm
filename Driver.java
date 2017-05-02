/**
 * This class defines a simple driver that continues to play games 
 * of No Limits Texas Hold 'Em until the player opts to stop.
 */

public class Driver {
	public static void main(String[] args) {
		Game newGame = new Game();
		newGame.playGames(); // Start game
	}
}
