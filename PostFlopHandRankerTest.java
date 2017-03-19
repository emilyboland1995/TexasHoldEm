import static org.junit.Assert.*;

import org.junit.Test;

public class PostFlopHandRankerTest {

	@Test
	public void testGetRelativeHandStrength() {
		Deck deck = new Deck();
		
		while (true) {
			Card[] hand = new Card[7];
			for (int j = 0; j < 7; j++) {
				hand[j] = deck.drawCard();
			}
			System.out.println(PostFlopHandRanker.getRelativeHandStrength(hand));
			deck.resetDeck();
		}
	}

	@Test
	public void testGetAbsoluteHandStrength() {
		Deck deck = new Deck();
		
		for (int i = 0; i < 100000; i++) {
			Card[] hand = new Card[7];
			for (int j = 0; j < 7; j++) {
				hand[j] = deck.drawCard();
			}
			System.out.println(PostFlopHandRanker.getAbsoluteHandStrength(hand));
			deck.resetDeck();
		}
	}

}
