import java.io.*;

public class PreFlopTableBuilder {
	private static final int NUM_GAMES = 1000000;
	
	public static void main(String[] args) {
		
		/*Deck d = new Deck();
		double rank = 0;
		Card[] holeCards = new Card[2];
		while (true) {
			holeCards[0] = d.drawCard();
			holeCards[1] = d.drawCard();
			rank = PreFlopHandRanker.getRelativeHandStrengthWeak(holeCards);
			System.out.println(holeCards[0].toString() + ", " + holeCards[1].toString() + ", Rank: " + rank);
			d.resetDeck();
		}*/
		print2DArray(getPreFlopHandRankings());
		
	}
	public static void print2DArray(double[][] arr) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream("PreFlopTable.dat"), "utf-8"))) {
			for (int i = 0; i < arr.length; i++) {
				writer.write("{");
				for (int j = 0; j < arr[i].length; j++) {
					writer.write(arr[i][j] + ", ");
				}
				writer.write("},\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static double[][] getPreFlopHandRankings() {
		String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
		String[] ranks = {
		    "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"
		  };
		
		Deck deck = new Deck();
		
		double[][] rankings = new double[13][13];
		
		Card first = null;
		Card second = null;
		
		// Handle off-suit hole cards
		for (int i = 0; i < rankings.length; i++) {
			for (int j = 0; j <= i; j++) {
				// Setup hole cards
				first = new Card(suits[0], ranks[12 - i]);
				second = new Card(suits[3], ranks[12 - j]);
				
				System.out.println("Testing: " + first.getRankString() + " of " + first.getSuitString()
				+ " and " + second.getRankString() + " of " + second.getSuitString());
				
				// Play mock games and record result
				rankings[i][j] = getActualPlayStats(first, second, deck);
				System.out.println(rankings[i][j]);
			}
		}
		
		// Handle same-suit hole cards
		for (int i = 0; i < rankings.length; i++) {
			for (int j = 12; j > i; j--) {
				// Setup hole cards
				first = new Card(suits[0], ranks[12 - i]);
				second = new Card(suits[0], ranks[12 - j]);
				
				// Play mock games and record result
				rankings[i][j] = getActualPlayStats(first, second, deck);
			}
		}
		return rankings;
	}
	private static double getActualPlayStats(Card first, Card second, Deck deck) {
		int wins = 0, loses = 0, ties = 0;
		for (int i = 0; i < NUM_GAMES; i++) {
			int result = playMockGame(first, second, deck);
			if (result == 1) {
				wins++;
			} else if (result == -1) {
				loses++;
			} else {
				ties++;
			}
		}
		return (double) wins / NUM_GAMES;
	}
	
	private static int playMockGame(Card first, Card second, Deck deck) {
		Card[] otherHand = new Card[7];
		Card[] thisHand = new Card[7];
		for (int i = 0; i < 7; i++) {
			Card next = deck.drawCard();
			while (next.equals(first) || next.equals(second)) {
				next = deck.drawCard();
			}
			otherHand[i] = next;
			thisHand[i] = next;
		}
		long otherRank = PostFlopHandRanker.getAbsoluteHandStrength(otherHand);
		
		thisHand[0] = first;
		thisHand[1] = second;
		
		long thisRank = PostFlopHandRanker.getAbsoluteHandStrength(thisHand);
		
		deck.resetDeck();
		
		if (otherRank > thisRank) {
			return -1;
		} else if (otherRank == thisRank) {
			return 0;
		} else { // otherRank < thisRank
			return 1;
		}
	}
}
