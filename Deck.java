/**
 * This class defines a simple implementation of a deck of cards
 * This class allows common operations such as drawing a card and
 * shuffling, along with a some other utilities for working with
 * the deck.
 * 
 * Requirement Sets: 1.6.0
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Deck {
	private static final String[] SUITS = {"Spades", "Clubs", "Hearts", "Diamonds"};
	private static final String[] VALUES = {
			"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"
	};

	private Card[] deckOfCards; // The Deck
	private int deckIndex; // Top-card index
	  
	/**
	 * A basic constructor for Deck. Creates a new deck
	 * containing all 52 cards.
	 */
	public Deck() {
		deckIndex = 0;
		int counter = 0; 
		deckOfCards = new Card[52];
		while (counter < 51) {
			for (String suit : SUITS) {
			    for ( String value : VALUES) {
			    	Card newCard = new Card(suit, value);
			        deckOfCards[counter] = newCard;
			        counter++;     
			    }
			}
		}
	}
	  
	  /**
	   * Shuffles the deck using the Fisher-Yates shuffling algorithm.
	   * 
	   * Requirement: 1.6.2, 1.6.4
	   */
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
	   * Allows a deck to be created with
	   * a certain number of predefined cards at the top.
	   */
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
	   * 
	   * Requirement: 1.6.3
	   */
	  public void resetDeck() {
		  deckIndex = 0;
		  this.shuffleDeck();
	  }
	  
	  /**
	   * Draws one card from the deck.
	   * @return		The next Card from the deck
	   * 
	   * Requirement: 1.6.1
	   */
	  public Card drawCard() {
		  if (deckIndex == deckOfCards.length) {
			  throw new NoSuchElementException("The deck is empty.");
		  }
		  return deckOfCards[deckIndex++]; // Return card then increment counter
	  }
	  
	  /**
	   * Prints the entire contents of the deck, card by card.
	   */
	  public void printDeck() {
		  for (Card card : deckOfCards) {
			  System.out.println(card.toString());
		  }
	  }
	
	  /**
	   * A simple iterator for iterating through any
	   * remaining cards in the deck
	   * @return		An Iterator<card> that can iterate
	   * 				through all the remaining cards in 
	   * 				the deck
	   */
	  public Iterator<Card> getPossibleCardsIterator() {
		  return new Iterator<Card>() {
			  private int nextCard = deckIndex;
			  
			  @Override
			  public boolean hasNext() {
				  return nextCard < deckOfCards.length;
			  }
			  
			  @Override
			  public Card next() {
				  if (nextCard >= deckOfCards.length) { // No further pairs can be generated
					  throw new NoSuchElementException("No further Cards available.");
				  } else { // Additional Cards available
					  return deckOfCards[nextCard++]; // Return next card and increment
				  }
			  }
		  };
	  }
	
	/**
	 * Builds and returns HashSet containing all
	 * 52 possible cards
	 * @return		A HashSet<Card> containing all
	 * 				52 possible cards
	 */
	public Set<Card> getDeckAsSet() {
		Set<Card> set = new HashSet<Card>();
		for (Card c : deckOfCards) {
			set.add(c);
		}
		return set;
	}
}
