import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;



public class Deck {
	  private static final String[] SUITS = {"Spades", "Clubs", "Hearts", "Diamonds"};
	  private static final String[] VALUES = {
	    "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"
	  };

	  Card[] deckOfCards;
	  int counter = 0; 
	  
	  public Deck() 
	  {
		  deckOfCards = new Card[52];
		  while (counter < 51)
		  {
			  for (String suit : SUITS) 
			  {
			      for ( String value : VALUES) 
			      {
			        Card newCard = new Card(suit, value);
			        deckOfCards[counter] = newCard;
			        //System.out.println(newCard.printCard()); 
			        //System.out.println(newCard.printIntVals());
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
	  
	  /**
	   * Resets the deck by "re-adding" any removed
	   * cards and shuffling the deck.
	   */
	  public void resetDeck() {
		  counter = 0;
		  this.shuffleDeck();
	  }
	  
	  public Card drawCard() {
		  if (counter == deckOfCards.length) {
			  throw new NoSuchElementException("The deck is empty.");
		  }
		  return deckOfCards[counter++]; // Return card then increment counter
	  }
	  
	  public void printDeck()
	  {
		  int counter = 0; 
		  while (counter < 51)
		  {
			  for (String suit : SUITS) 
			  {
			      for ( String value : VALUES) 
			      {
			        //Card newCard = new Card(suit, value);
			       // deckOfCards[counter] = newCard;
			        System.out.println(deckOfCards[counter].printCard()); 
			        //System.out.println(newCard.printIntVals());
			        counter++;     
			      }
			      
			      

			  }
		  }

}
}
