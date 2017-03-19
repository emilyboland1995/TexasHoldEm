
public class Card {
	  private String cardRank;
	  private String cardSuit;
	  private int cardValInt; //Ace=1, Jack=11, Queen=12, King=13
	  private int cardSuitInt; //Spades=1, Clubs=2, Hearts=3, Diamonds=4 

	  public Card(String suit, String value) {
	    cardSuit = suit;
	    cardRank = value;
	  }

	  public String getRankString() {
		  //Print the string value of the card ranking
	    return cardRank;
	  }

	  public String getSuitString() {
		  //Print the string value of the card suit 
	    return cardSuit;
	  }
	  
	  public int getRankInt() {
		  //Print the integer value of the card ranking 
		  if (cardRank.equals("Jack"))
		  {
			  cardValInt = 11; 
		  }
		  else if (cardRank.equals("Queen"))
		  {
			  cardValInt = 12; 
		  }
		  else if (cardRank.equals("King"))
		  {
			  cardValInt = 13; 
		  }
		  else if (cardRank.equals("Ace"))
		  {
			  cardValInt = 1; 
		  }
		  else
		  {
			  cardValInt = Integer.parseInt(cardRank); 
		  }

		  return cardValInt; 
	  }
	  
	  public int getSuitInt()
	  {
		  //Print the integer value of the card suit 
		  if (cardSuit.equals("Spades")) // spades = 1 
		  {
			  cardSuitInt = 1; 
		  }
		  else if (cardSuit.equals("Clubs")) // clubs = 2
		  {
			  cardSuitInt = 2; 
		  }
		  else if (cardSuit.equals("Hearts")) //hearts = 3
		  {
			  cardSuitInt = 3; 
		  }
		  else
		  {
			  cardSuitInt = 4;    //diamonds = 4
		  } 
		  
		  return cardSuitInt; 
	  }

	  public String toString() {
		  //Print the formatted rank and suit of the card 
	    return String.format("%s of %s", cardRank, cardSuit);
	  }
	  
	  public String printIntVals() {
		  //Print the formatted rank and suit of the card 
	    return String.format(getRankInt() + " of " + getSuitInt());
	  }
}
