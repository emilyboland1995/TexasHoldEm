/**
 * 
 * This class defines the Card object, which is capable of representing all
 * 52 possible playing cards. Getters are provided to access various 
 * characteristics of a given instance of Card, along with methods to compare
 * any two cards, generate a hash code, and provide a String representation 
 * of the card.
 *
 * Requirement Sets: 1.9.0
 */

public class Card {
	private String cardRank; // A String representation of the card's rank
	private String cardSuit; // A String representation of the card's suit
	private int cardRankInt; // Ace=1, Jack=11, Queen=12, King=13
	private int cardSuitInt; // Spades=1, Clubs=2, Hearts=3, Diamonds=4 

	/**
	 * A simple constructor that crates a new instance of Card of
	 * the given rank and suit
	 * @param suit		A String representing the suit of the card
	 * 					to be created
	 * @param value		A String representing the rank of the card
	 * 					to be created
	 * 
	 * Requirement: 1.9.3
	 */
	public Card(String suit, String rank) {
		cardSuit = suit;
	    cardRank = rank;
	    cardRankInt = setRankInt();
	    cardSuitInt = setSuitInt();
	}
	/**
	 * @return		A String representing the Card's rank
	 * 
	 * Requirement: 1.9.3
	 */
	public String getRankString() {
	    return cardRank;
	}
	/**
	 * @return		A String representing the Card's suit
	 * 
	 * Requirement: 1.9.3
	 */
	public String getSuitString() {
	    return cardSuit;
	}
	/**
	 * @return			The int associated with the Card's rank
	 * 
	 * Requirement: 1.9.2
	 */
	public int getRankInt() {
		return cardRankInt;
	}
	/**
	 * @return			The int associated with the Card's suit
	 * 
	 * Requirement: 1.9.2
	 */
	public int getSuitInt() {
		return cardSuitInt;
	}
	/**
	 * A simple hascode function
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardRank == null) ? 0 : cardRank.hashCode());
		result = prime * result + cardRankInt;
		result = prime * result + ((cardSuit == null) ? 0 : cardSuit.hashCode());
		result = prime * result + cardSuitInt;
		return result;
	}
	/**
	 * An implementation of the equals method for Card objects
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (cardRank == null) {
			if (other.cardRank != null)
				return false;
		} else if (!cardRank.equals(other.cardRank))
			return false;
		if (cardRankInt != other.cardRankInt)
			return false;
		if (cardSuit == null) {
			if (other.cardSuit != null)
				return false;
		} else if (!cardSuit.equals(other.cardSuit))
			return false;
		if (cardSuitInt != other.cardSuitInt)
			return false;
		return true;
	}
	/**
	 * Generates an int representing the rank of the card
	 * based on its String rank
	 * @return		An int representing the rank of the card (1-13)
	 * 
	 * Requirement: 1.9.2
	 */
	private int setRankInt() {
		  //Print the integer value of the card ranking 
		  if (cardRank.equals("Jack"))
		  {
			  return 11; 
		  }
		  else if (cardRank.equals("Queen"))
		  {
			  return 12; 
		  }
		  else if (cardRank.equals("King"))
		  {
			  return 13; 
		  }
		  else if (cardRank.equals("Ace"))
		  {
			  return 1; 
		  }
		  else
		  {
			  return Integer.parseInt(cardRank); 
		  }
	  }
	/**
	 * Generates an int representing the suit of the card
	 * based on its String suit
	 * @return		An int representing the suit of the card (1-4)
	 * 
	 * Requirement: 1.9.2
	 */
	  private int setSuitInt() {
		  if (cardSuit.equals("Spades")) { // spades = 1 
			  return 1; 
		  }
		  else if (cardSuit.equals("Clubs")) { // clubs = 2
			  return 2; 
		  }
		  else if (cardSuit.equals("Hearts")) { //hearts = 3 
			  return 3; 
		  }
		  else {
			  return 4;    //diamonds = 4
		  } 
	  }
	  /**
	   * A simple toString implementation for Card objects.
	   * @return		A String containing the Card's suit
	   * 				and rank in string form
	   * 
	   * Requirement: 1.9.3
	   */
	  public String toString() {
	    return String.format("%s of %s", cardRank, cardSuit);
	  }
	  /**
	   * Builds and returns a String containing the Card's suit and
	   * rank in int form
	   * @return		A String containing the Card's suit and rank
	   * 				in int form
	   * 
	   * Requirement: 1.9.2
	   */
	  public String printIntVals() {
	    return String.format(getRankInt() + " of " + getSuitInt());
	  }
}
