

import static org.junit.Assert.*;
import java.util.Scanner;
import org.junit.Test;

public class PostFlopHandRankerTest {
	private static String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
	private static String[] ranks = {
			"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"
	  };
	
	// builds a Card[] containing the cards specified in S. Card format
	// used is as follows: first digit indicates suit (1-4) second two digits
	// indicates the card type (1-14), where 1 is an Ace and 14 is a King.
	private Card[] setupHand(String s) {
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

	@Test
	public void testGetRelativeHandStrength() {
		System.out.println("Testing relative strength generation...");
		Deck deck = new Deck();
		
		for (int i = 0; i < 100; i++) {
			Card[] hand = new Card[7];
			for (int j = 0; j < 7; j++) {
				hand[j] = deck.drawCard();
			}
			//System.out.println(PostFlopHandRanker.getRelativeHandStrength(hand));
			deck.resetDeck();
		}
		//System.out.println();
	}

	
	@Test
	public void testGetAbsoluteHandStrength() {
		//speedTest();
		testHandSort();
		testDifferentHandSizes();
		testStraightFlushes();
		testFourOfAKind();
		testFullHouse();
		testFlush();
		testStraights();
		testThreeOfAKind();
		testTwoPairs();
		testPair();
		testHighCard();
		//testEqualsStrength();
	}
	
	private void testEqualsStrength() {
		Deck d = new Deck();
		Card[] holeCards1 = new Card[2];
		Card[] holeCards2 = new Card[2];
		Card[] boardCards = new Card[3];
		while (true) {
			for (int i = 0; i < 2; i++) {
				holeCards1[i] = d.drawCard();
				holeCards2[i] = d.drawCard();
			}
			d.resetDeck();
			for (int i = 0; i < 3; i++) {
				boardCards[i] = d.drawCard();
			}
			long rank1 = PostFlopHandRanker.getAbsoluteHandStrength(holeCards1, boardCards);
			long rank2 = PostFlopHandRanker.getAbsoluteHandStrength(holeCards2, boardCards);
			
			
			if (rank1 == rank2) {
				System.out.println(rank1 + " " + rank2);
				System.out.println(holeCards1[0].toString() + " " + holeCards1[1].toString());
				System.out.println(holeCards2[0].toString() + " " + holeCards2[1].toString());
				for (int i = 0; i < 3; i++) {
					System.out.print(boardCards[i] + " ");
				}
				System.out.println();
			}
			d.resetDeck();
		}
	}
	
	// Test case #1
	private void testHandSort() {
		Deck d = new Deck();
		for (int repeats = 0; repeats < 100; repeats++) {
			Card[] hand = new Card[7];
			for (int i = 0; i < 7; i++) {
				hand[i] = d.drawCard();
			}
			PostFlopHandRanker.sortByRank(hand);
			for (int j = 0; j < hand.length; j++) {
				System.out.print(hand[j].getRankInt() + " ");
			}
			System.out.println();
			for (int i = 1; i < 7; i++) {
				assertTrue(hand[i - 1].getRankInt() <= hand[i].getRankInt());
			}
			d.resetDeck();
		}
	}
	
	// Test case #2: Speed test
	private void speedTest() {
		System.out.println("Testing speed of getAbsoluteHandStrength()");
		Deck deck = new Deck();
		
		for (long i = 0; i < 1000000000; i++) {
			Card[] hand = new Card[7];
			for (int j = 0; j < 7; j++) {
				hand[j] = deck.drawCard();
			}
			System.out.println(PostFlopHandRanker.getAbsoluteHandStrength(hand));
			deck.resetDeck();
		}
	}
	
	// Test case #3: Test various hand-sizes
	private void testDifferentHandSizes() {
		System.out.println("Testing different hand sizes...");
		Card[] h5 = setupHand("5 12 13 14 15 16");
		Card[] h6 = setupHand("6 12 13 14 15 16 17");
		Card[] h7 = setupHand("7 12 13 14 15 16 17 18");
		
		try {
		
			long rank5 = PostFlopHandRanker.getAbsoluteHandStrength(h5);
			System.out.println(rank5);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank5));
			long rank6 = PostFlopHandRanker.getAbsoluteHandStrength(h6);
			System.out.println(rank6);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank6));
			long rank7 = PostFlopHandRanker.getAbsoluteHandStrength(h7);
			System.out.println(rank7);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank7));
			
			assertTrue(true);
		} catch(Exception e) {
			System.out.println("Error!");
			e.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	// Test case #4: Test straight flushes
	private void testStraightFlushes() {
		Card[][] hands = new Card[10][];
		hands[0] = setupHand("5 11 12 13 14 15");
		hands[1] = setupHand("6 11 12 13 14 15 16");
		hands[2] = setupHand("6 12 13 14 15 16 17");
		hands[3] = setupHand("7 12 13 14 15 16 17 18");
		hands[4] = setupHand("5 15 16 17 18 19");
		hands[5] = setupHand("5 16 17 18 19 110");
		hands[6] = setupHand("5 17 18 19 110 111");
		hands[7] = setupHand("5 18 19 110 111 112");
		hands[8] = setupHand("6 19 110 111 112 113 18");
		hands[9] = setupHand("5 11 110 111 112 113");
		
		System.out.println("Testing straight flushes...");
		long rank1 = PostFlopHandRanker.getAbsoluteHandStrength(hands[0]);
		long rank2 = 0;
		System.out.println(rank1);
		System.out.println(PostFlopHandRanker.getHandStringFromRank(rank1));
		for (int i = 1; i < 10; i++) {
			rank2 = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			assertTrue(rank2 > rank1);
			System.out.println(rank2);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank2));
			rank1 = rank2;
		}
	}
	
	// Test case # : Test four of a kind
	private void testFourOfAKind() {
		System.out.println("Testing four of a kind sets...");
		Card[][] hands = new Card[13][];
		hands[0] = setupHand("7 12 22 32 42 19 29 39");
		hands[1] = setupHand("7 13 43 33 16 23 11 12");
		hands[2] = setupHand("7 14 24 34 44 110 111 12");
		hands[3] = setupHand("7 15 25 35 45 112 113 11");
		hands[4] = setupHand("7 16 26 36 46 11 11 12");
		hands[5] = setupHand("7 17 27 37 47 16 26 38");
		hands[6] = setupHand("7 18 28 38 48 37 46 25");
		hands[7] = setupHand("7 19 29 39 49 18 26 28");
		hands[8] = setupHand("7 110 210 310 410 111 21 37");
		hands[9] = setupHand("7 111 211 311 411 112 35 27");
		hands[10] = setupHand("7 112 212 312 412 11 21 31");
		hands[11] = setupHand("7 113 213 313 413 11 34 22");
		hands[12] = setupHand("7 11 21 31 41 47 26 34");
		
		long rank1 = PostFlopHandRanker.getAbsoluteHandStrength(hands[0]);
		long rank2 = 0;
		System.out.println(rank1);
		System.out.println(PostFlopHandRanker.getHandStringFromRank(rank1));
		for (int i = 1; i < hands.length; i++) {
			rank2 = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank2 );
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank2));
			assertTrue(rank2 > rank1);
			rank1 = rank2;
		}
	}
	
	// Test case # : Test full house
	private void testFullHouse() {
		System.out.println("Testing full houses...");
		Card[][] unevenHands = new Card[10][];
		unevenHands[0] = setupHand("7 12 22 32 11 21 31 47");
		unevenHands[1] = setupHand("7 13 43 33 12 12 24 34");
		unevenHands[2] = setupHand("7 13 43 33 12 12 31 21");
		unevenHands[3] = setupHand("7 15 25 33 45 112 112 11");
		unevenHands[4] = setupHand("7 11 21 31 112 212 113 213");
		unevenHands[5] = setupHand("7 113 213 313 410 110 26 38");
		unevenHands[6] = setupHand("7 18 28 38 47 37 46 25");
		unevenHands[7] = setupHand("7 19 29 39 31 18 26 28");
		unevenHands[8] = setupHand("7 110 210 310 112 212 21 37");
		unevenHands[9] = setupHand("7 111 211 311 41 15 35 25");
		
		for (int i = 0; i < 10; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(unevenHands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
	
	// Test case # : Test Flush
	private void testFlush() {
		System.out.println("Testing flushes...");
		Card[][] hands = new Card[10][];
		hands[0] = setupHand("7 11 13 15 16 17 112 19");
		hands[1] = setupHand("7 21 31 25 212 211 24 43");
		hands[2] = setupHand("7 34 36 21 25 312 310 32");
		hands[3] = setupHand("7 41 44 410 46 412 411 35");
		hands[4] = setupHand("5 31 32 34 39 312");
		hands[5] = setupHand("7 410 41 21 47 49 42 310");
		hands[6] = setupHand("7 29 36 27 47 210 211 212");
		hands[7] = setupHand("7 112 212 312 26 29 210 211");
		hands[8] = setupHand("7 31 32 37 48 39 42 38");
		hands[9] = setupHand("7 42 49 47 412 41 36 39");
		
		for (int i = 0; i < hands.length; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
	
	// Test case # : Test straights
	private void testStraights() {
		System.out.println("Testing straights...");
		Card[][] hands = new Card[10][];
		hands[0] = setupHand("7 11 35 12 13 47 44 15");
		hands[1] = setupHand("7 22 43 31 24 25 35 16");
		hands[2] = setupHand("7 17 26 35 14 23 12 11");
		hands[3] = setupHand("7 48 37 26 15 21 34 31");
		hands[4] = setupHand("7 19 48 27 36 45 12 31");
		hands[5] = setupHand("7 410 32 42 39 28 16 37");
		hands[6] = setupHand("7 39 48 22 17 210 211 211");
		hands[7] = setupHand("7 311 18 312 26 29 410 212");
		hands[8] = setupHand("7 313 412 45 111 210 39 47");
		hands[9] = setupHand("7 41 31 412 313 17 211 410");
		
		for (int i = 0; i < hands.length; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
	
	// Test case # : Test triples
	private void testThreeOfAKind() {
		System.out.println("Testing triples...");
		Card[][] hands = new Card[13][];
		hands[0] = setupHand("5 12 22 32 34 36");
		hands[1] = setupHand("5 13 43 23 47 28");
		hands[2] = setupHand("5 44 34 24 12 11");
		hands[3] = setupHand("5 15 35 45 18 29");
		hands[4] = setupHand("5 16 36 26 19 212");
		hands[5] = setupHand("5 47 37 27 39 28");
		hands[6] = setupHand("5 18 38 48 212 211");
		hands[7] = setupHand("5 49 39 29 410 212");
		hands[8] = setupHand("5 210 310 410 39 47");
		hands[9] = setupHand("7 411 311 211 39 28 410 46");
		hands[10] = setupHand("7 412 312 212 39 27 410 46");
		hands[11] = setupHand("7 413 313 213 39 25 410 46");
		hands[12] = setupHand("7 41 31 21 310 29 411 46");
		
		for (int i = 0; i < hands.length; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
	// Test case # : Test two pairs
	private void testTwoPairs() {
		System.out.println("Testing two pairs...");
		Card[][] hands = new Card[13][];
		hands[0] = setupHand("5 12 22 33 33 36");
		hands[1] = setupHand("5 13 43 24 44 28");
		hands[2] = setupHand("5 44 34 25 15 11");
		hands[3] = setupHand("5 15 35 46 16 29");
		hands[4] = setupHand("5 16 36 27 17 212");
		hands[5] = setupHand("5 47 37 28 38 29");
		hands[6] = setupHand("5 18 38 49 29 211");
		hands[7] = setupHand("5 49 39 210 410 212");
		hands[8] = setupHand("5 210 310 411 311 47");
		hands[9] = setupHand("7 411 311 212 312 28 410 46");
		hands[10] = setupHand("7 412 312 213 313 210 410 46");
		hands[11] = setupHand("7 413 313 212 312 210 410 46");
		hands[12] = setupHand("7 41 31 212 312 211 411 46");
		
		for (int i = 0; i < hands.length; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
	// Test case # : Test pairs
	private void testPair() {
		System.out.println("Testing pairs...");
		Card[][] hands = new Card[13][];
		hands[0] = setupHand("5 12 22 37 33 36");
		hands[1] = setupHand("5 13 43 25 44 28");
		hands[2] = setupHand("5 44 34 27 15 11");
		hands[3] = setupHand("5 15 35 410 16 29");
		hands[4] = setupHand("5 16 36 211 17 212");
		hands[5] = setupHand("5 47 37 22 38 29");
		hands[6] = setupHand("5 18 38 41 29 211");
		hands[7] = setupHand("5 49 39 21 410 212");
		hands[8] = setupHand("5 210 310 412 311 47");
		hands[9] = setupHand("7 411 311 213 312 28 410 46");
		hands[10] = setupHand("7 412 312 213 38 29 410 46");
		hands[11] = setupHand("7 413 313 212 311 21 49 46");
		hands[12] = setupHand("7 41 31 212 39 28 411 46");
		
		for (int i = 0; i < hands.length; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
	// Test case # : Test high card hands
	private void testHighCard() {
		System.out.println("Testing high card...");
		Card[][] hands = new Card[13][];
		hands[0] = setupHand("5 12 21 37 33 36");
		hands[1] = setupHand("5 13 411 25 44 28");
		hands[2] = setupHand("5 44 312 27 15 11");
		hands[3] = setupHand("5 15 31 410 16 29");
		hands[4] = setupHand("7 16 313 29 18 211 17 212");
		hands[5] = setupHand("7 47 31 22 38 29 211 212");
		hands[6] = setupHand("7 18 37 41 29 211 26 312");
		hands[7] = setupHand("7 49 32 21 410 212 13 14");
		hands[8] = setupHand("7 210 313 412 311 47 13 25");
		hands[9] = setupHand("7 411 32 213 312 28 410 46");
		hands[10] = setupHand("7 412 31 213 38 29 47 46");
		hands[11] = setupHand("7 413 31 212 311 23 49 46");
		hands[12] = setupHand("7 41 310 212 37 28 411 46");
		
		for (int i = 0; i < hands.length; i++) {
			long rank = PostFlopHandRanker.getAbsoluteHandStrength(hands[i]);
			System.out.println(rank);
			System.out.println(PostFlopHandRanker.getHandStringFromRank(rank));
		}
	}
}
