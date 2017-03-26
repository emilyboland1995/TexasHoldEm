import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class Deck {
	  private static final String[] SUITS = {"Spades", "Clubs", "Hearts", "Diamonds"};
	  private static final String[] VALUES = {
	    "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"
	  };

	  private Card[] deckOfCards;
	  private int deckIndex;
	  
	  
	  public Deck() 
	  {
		  deckIndex = 0;
		  int counter = 0; 
		  deckOfCards = new Card[52];
		  while (counter < 51)
		  {
			  for (String suit : SUITS) 
			  {
			      for ( String value : VALUES) 
			      {
			        Card newCard = new Card(suit, value);
			        deckOfCards[counter] = newCard;
			        counter++;     
			      }
			  }
		  }
	  }
	  
	  public void shuffleDeck() {
		  //Fisher-Yates 
	  
		  Random rnd = ThreadLocalRandom.current();
		    for (int i = deckOfCards.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      // Simple swap
		      Card a = deckOfCards[index];
		      deckOfCards[index] = deckOfCards[i];
		      deckOfCards[i] = a;
		    }
	  }
	  
	  public void stackDeck(){
		  String[] suit = new String[9];
		  String[] rank = new String[9];
		  Card tempCard;
		  
		  //non-dealer holds
		  suit[0]="Hearts";
		  rank[0]="Queen";
		  suit[1]="Diamonds";
		  rank[1]="4";
		  //dealer holds
		  suit[2]="Spades";
		  rank[2]="Queen";
		  suit[3]="Clubs";
		  rank[3]="6";
		  //boardCards
		  suit[4]="Diamonds";
		  rank[4]="3";
		  suit[5]="Hearts";
		  rank[5]="8";
		  suit[6]="Spades";
		  rank[6]="2";
		  suit[7]="Clubs";
		  rank[7]="9";
		  suit[8]="Clubs";
		  rank[8]="5";
		  
		  //stack the deck
		  for(int x = 0; x < 9; x++){//cycle through cards to stack
			  //find card and deck and swap
			  tempCard = deckOfCards[x];
			  for(int y = x; y < 52; y++){
				  if(deckOfCards[y].getSuitString() == suit[x] && deckOfCards[y].getRankString() == rank[x]){//found card to swap
					  //swap cards
					  deckOfCards[x] = deckOfCards[y];
					  deckOfCards[y] = tempCard;
				  }  
			  }
		  }
	  }
	  
	  /**
	   * Resets the deck by "re-adding" any removed
	   * cards and shuffling the deck.
	   */
	  public void resetDeck() {
		  deckIndex = 0;
		  this.shuffleDeck();
		  this.stackDeck();
	  }
	  
	  public Card drawCard() {
		  if (deckIndex == deckOfCards.length) {
			  throw new NoSuchElementException("The deck is empty.");
		  }
		  return deckOfCards[deckIndex++]; // Return card then increment counter
	  }
	  
	  public void printDeck() {
		  for (Card card : deckOfCards) {
			  System.out.println(card.toString());
		  }
	  }
}
