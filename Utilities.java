/**
 * This class defines a few convenience tools for use in other
 * classes.
 */

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Scanner;
import java.util.Set;

public class Utilities {
	private static String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
	private static String[] ranks = {
			"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"
	  };
	
   /** builds a Card[] containing the cards specified in S. Card format
	* used is as follows: first digit indicates suit (1-4) second two digits
	* indicates the card type (1-14), where 1 is an Ace and 14 is a King.
	*/
	public static Card[] setupHand(String s) {
		@SuppressWarnings("resource")
		Scanner cards = new Scanner(s);
		if (!cards.hasNext()) {
			throw new IllegalArgumentException("Invalid hand string");
		}
		
		Card[] hand = new Card[Integer.parseInt(cards.next())];
		int index = 0;
		while (cards.hasNext()) {
			if (index > hand.length) {
				throw new IllegalArgumentException("Invalid hand string");
			}
			int card = Integer.parseInt(cards.next());
			String suit = "";
			String rank = "";
			
			if (card > 100) { // 10 or face card
				suit = suits[card / 100 - 1];
				rank = ranks[card % 100 - 1];
			} else { // All other cards
				suit = suits[card / 10 - 1];
				rank = ranks[card % 10 - 1];
			}
			hand[index] = new Card(suit, rank);
			index++;
		}
		cards.close(); // Close resource
		return hand;
	}
	
	/**
	 * Adds all the elements in data to the set passed.
	 * @param set		The Set<T> the objects in data will be added to
	 * @param data		The T[] containing all the objects to be added
	 * 					to set
	 */
	public static <T> void addToSet(Set<T> set, T[] data) {
		for (T value : data) {
			set.add(value);
		}
	}
	/**
	 * Removes all the objects in data from the set passed.
	 * @param set		The Set<T> the objects in data will be added to
	 * @param data		The T[] containing all the objects to be added
	 * 					to set
	 */
	public static <T> void removeFromSet(Set<T> set, T[] data) {
		for (T value : data) {
			set.remove(value);
		}
	}
	
	/**
	 * Takes a single Card and returns the file path to the image
	 * representing that card
	 * @param c		The Card whose image path will be searched for
	 * @return		A Image containing the image representing the
	 * 				Card passed.
	 */
	public static Image getCardFilePath(Card c) {
		if (c == null) { // Assume back of card
			return Toolkit.getDefaultToolkit().getImage((Utilities.class.getResource("CardPics/custom_back.jpg")));
		}
		String s = c.toString().toLowerCase() + ".png";
		s.replaceAll("\\w", "_");
		return Toolkit.getDefaultToolkit().getImage((Utilities.class.getResource("CardPics/" + s.replaceAll("[ ]", "_"))));
	}
}
