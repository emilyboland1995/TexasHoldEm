
public class Card {
	private String cardRank;
	private String cardSuit;
	private int cardRankInt; //Ace=1, Jack=11, Queen=12, King=13
	private int cardSuitInt; //Spades=1, Clubs=2, Hearts=3, Diamonds=4 

	public Card(String suit, String value) {
		cardSuit = suit;
	    cardRank = value;
	    cardRankInt = setRankInt();
	    cardSuitInt = setSuitInt();
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
		return cardRankInt;
	}
	  
	public int getSuitInt() {
		return cardSuitInt;
	}
	  
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
	  
	  private int setSuitInt() {
		  //Print the integer value of the card suit 
		  if (cardSuit.equals("Spades")) // spades = 1 
		  {
			  return 1; 
		  }
		  else if (cardSuit.equals("Clubs")) // clubs = 2
		  {
			  return 2; 
		  }
		  else if (cardSuit.equals("Hearts")) //hearts = 3
		  {
			  return 3; 
		  }
		  else
		  {
			  return 4;    //diamonds = 4
		  } 
	  }
	  
	  public boolean equals(Card other) {
		  return this.cardRankInt == other.cardRankInt && this.cardSuitInt == other.cardSuitInt;
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
