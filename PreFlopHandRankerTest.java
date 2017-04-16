import static org.junit.Assert.*;

import org.junit.Test;

public class PreFlopHandRankerTest {

	@Test
	public void testGetRelativeHandStrengthWeak() {
		Deck deck = new Deck();
		
		for (int i = 0; i < 50; i++) {
			Card[] holeCards = {deck.drawCard(), deck.drawCard()};
			double rank = PreFlopHandRanker.getHoleCardWinRate(holeCards);
			deck.resetDeck();
			if (rank > 0.2) {
				System.out.println(rank);
			}
		}
	}

	@Test
	public void testGetAbsoluteHandStrengthWeak() {
		Deck deck = new Deck();
		for (int i = 0; i < 50; i++) {
			Card[] holeCards = {deck.drawCard(), deck.drawCard()};
			double rank = PreFlopHandRanker.getHoleCardWinRate(holeCards);
			deck.resetDeck();
			System.out.println(rank);
		}
	}

}
