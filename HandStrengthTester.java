

public class HandStrengthTester {
	public static void main(String[] args) {
		Deck d = new Deck();

		for (int i = 0; i < 7; i++) {
			d.drawCard();
		}
		Card[] hand1 = Utilities.setupHand("2 21 412");
		Card[] hand2 = Utilities.setupHand("3 33 44 311");
		
		System.out.println(HandStrengthCalculator.getHandStrength(hand1, hand2));
		
		HandPotential po = new HandPotential(hand1, hand2);
		System.out.println("Negative p: " + po.getNegativePotential());
		System.out.println("Positive p: " + po.getPositivePotential());
		
		boolean equal = false;
		Card[] holeCards = new Card[2];
		Card[] boardCards = new Card[3];
		while (!equal) {
			holeCards[0] = d.drawCard();
			holeCards[1] = d.drawCard();
			System.out.print(holeCards[0].toString() + " " + holeCards[1].toString() + " ");
			for (int i = 0; i < 3; i++) {
				boardCards[i] = d.drawCard();
				System.out.print(boardCards[i].toString() + " ");
			}
			System.out.println();
			
			HandPotential p = new HandPotential(holeCards, boardCards);
			System.out.println("Negative p: " + p.getNegativePotential());
			System.out.println("Positive p: " + p.getPositivePotential());
			
			System.out.println("EHS: " + HandStrengthCalculator.getEffectiveHandStrength(holeCards, boardCards));
			d.resetDeck();
		}
	}
}
