
public class HandStats {
	private static final int AHEAD = 2;
	private static final int BEHIND = 0;
	private static final int TIED = 1;
	
	public static class Potential {
		private double ppot;
		private double npot;
		
		public Potential(double ppot, double npot) {
			this.ppot = ppot;
			this.npot = npot;
		}
		
		public double getPPOT() {
			return this.ppot;
		}
		
		public double getNPOT() {
			return this.npot;
		}
	}
	
	public static double handStrength(Player p, Card[] boardCards) {
		int ahead = 0, tied = 0, behind = 0;
		int playerHandRank = HandRanker.getHandRank(); /*Specify parameters*/
		for (/*Each possible combination of opponent cards*/) {
			int opponentHandRank = HandRanker.getHandRank(); /*Specify parameters*/
			if (playerHandRank > opponentHandRank) {
				ahead++;
			} else if (playerHandRank == opponentHandRank) {
				tied++;
			} else {
				behind++;
			}
		}
		return (ahead + tied / 2) / (ahead + tied + behind);
	}
	
	public static Potential handPotential(Player p, Card[] boardCards) {
		int[][] handPotential = new int[3][3];
		int[] handPotentialTotal = new int[3];
		int playerCardsRank = HandRanker.getHandRank(/*Specify type of structure for providing cards*/);
		
		// Examine all two-card combinations the opponent might have
		for (Integer opponentCardsRank : possibleOpponentRanks) {
			int index = 0;
			
			// Determine the higher hand
			if (playerCardsRank > opponentCardsRank) {
				index = AHEAD;
			} else if (playerCardsRank == opponentCardsRank) {
				index = TIED;
			} else {
				index = BEHIND;
			}
			handPotentialTotal[index]++;
			
			// Examine all possible turn/river pairs possible given the opponent's cards
			for (/*Each possible turn/river pair*/) {
				int playersBest = HandRanker.getHandRank()/*Specify type of structure for providing cards*/;
				int opponentsBest = HandRanker.getHandRank()/*Specify type of structure for providing cards*/;
				
				// Determine the higher hand
				if (playersBest > opponentsBest) {
					handPotential[index][AHEAD]++;
				} else if (playersBest == opponentsBest) {
					handPotential[index][TIED]++;
				} else {
					handPotential[index][BEHIND]++;
				}
			}
			
			// Calulate positive and negative potential
			double positivePotential = (handPotential[BEHIND][AHEAD] + handPotential[BEHIND][TIED] / 2 
					+ handPotential[TIED][AHEAD] / 2) / (handPotentialTotal[BEHIND] + handPotentialTotal[TIED]);
			double negativePotential = (handPotential[AHEAD][BEHIND] + handPotential[TIED][BEHIND] / 2 
					+ handPotential[AHEAD][TIED] / 2) / (handPotentialTotal[AHEAD] + handPotentialTotal[tied]);
			
		}
	}
}
