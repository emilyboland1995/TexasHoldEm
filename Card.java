
public class Card {
	
	  String cardValue;
	  String cardSuit;
	  int cardValInt; //Ace=1, Jack=11, Queen=12, King=13
	  int cardSuitInt; //Spades=1, Clubs=2, Hearts=3, Diamonds=4 

	  public Card(String suit, String value) {
	    cardSuit = suit;
	    cardValue = value;
	  }

	  public String getValueStr() {
		  //Print the string value of the card ranking
	    return cardValue;
	  }

	  public String getSuitStr() {
		  //Print the string value of the card suit 
	    return cardSuit;
	  }
	  
	  public int getValInt()
	  {
		  //Print the integer value of the card ranking 
		  if (cardValue.equals("Jack"))
		  {
			  cardValInt = 11; 
		  }
		  else if (cardValue.equals("Queen"))
		  {
			  cardValInt = 12; 
		  }
		  else if (cardValue.equals("King"))
		  {
			  cardValInt = 13; 
		  }
		  else if (cardValue.equals("Ace"))
		  {
			  cardValInt = 1; 
		  }
		  else
		  {
			  cardValInt = Integer.parseInt(cardValue); 
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

	  public String printCard() {
		  //Print the formatted rank and suit of the card 
	    return String.format("%s of %s", cardValue, cardSuit);
	  }
	  
	  public String printIntVals() {
		  //Print the formatted rank and suit of the card 
	    return String.format(getValInt() + " of " + getSuitInt());
	  }
}
