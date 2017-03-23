/**
 * This class defines a simple driver that continues to play games of No Limits Texas Hold 'Em
 * until the player opts to stop.
 */

import java.util.Scanner;

public class Driver {
	public static void main(String[] args) {
		Game newGame = null;
		Scanner input = new Scanner(System.in);
		while (userWantsToPlay(input)) {
			newGame = new Game(); // Create game
			newGame.playGame(); // Play game
		}
		input.close();
	}
	/**
	 * This method prompts the user to play a new game of Texas Hold 'Em Poker
	 * and returns a boolean indicating whether or not the user wants to play
	 * a game.
	 * @param input		A Scanner through which to read user input
	 * @return			True if the user wants to play a game, false
	 * 					if the user indicated otherwise.
	 */
	public static boolean userWantsToPlay(Scanner input) {
		String userInput = "";
		while (true) {
			System.out.print("Welcome! Do you want to play a game of No Limits Texas Hold 'Em (y/n)? ");
			userInput = input.next();
			char userChoice = userInput.toLowerCase().charAt(0);
			if (userChoice == 'y') {
				return true;
			} else if (userChoice == 'n') {
				return false;
			} else {
				System.out.println("Invalid input!");
			}
		}
	}
}
