import java.util.Arrays;



public class handRank {
	Card[] boardCards;
	Card[] holeCards;
	Card[] sortedCards;
	Card[] bestHand;
	
	int totalPossibleOptions = 0;
	int pairOrBetter = 0;
	int twoPairOrBetter = 0;
	int threeOfKindOrBetter = 0;
	int straightOrBetter = 0;
	int flushOrBetter = 0;
	int fullHouseOrBetter = 0;
	int fourOfKindOrBetter = 0;
	int straightFlushOrBetter = 0;
	
	boolean flushFlag = false;
	boolean straightFlag = false;
	boolean aceLowStraightFlag = false;
	boolean boardCardsFlag = false;
	
	int handValue;
	
	public handRank(Card[] hCards){
		holeCards = hCards.clone();
		insertionSort(holeCards,0,2);
		System.out.print("HoleCards: ");
		printCards(holeCards);
	}
	public handRank(Card[] hCards, Card[] bCards){
		holeCards = hCards.clone();
		insertionSort(holeCards,0,2);
		System.out.print("HoleCards: ");
		printCards(holeCards);
		
		boardCards = bCards.clone();
		boardCardsFlag = true;
		insertionSort(boardCards,0,boardCards.length);
		System.out.print("BoardCards: ");
		printCards(boardCards);
		
		sortedCards = new Card[boardCards.length + 2];
		sortedCards[0] = holeCards[0];
		sortedCards[1] = holeCards[1];
		for(int x = 2; x < boardCards.length + 2; x++){
			sortedCards[x] = boardCards[x-2];
		}
		insertionSort(sortedCards,0,sortedCards.length);
		System.out.print("SortedCards: ");
		printCards(sortedCards);
		getVal(sortedCards);
		System.out.println(handValue);
		System.out.print("BestHand: ");
		printCards(bestHand);
		
		//check for possible outcomes
		//generate randomDeck
		Deck randomDeck = new Deck();
		if(sortedCards.length == 6){//more cards to come
			//remove known cards from Deck
			Card[] randomCards = new Card[46];
			Card nextCard;
			for(int x = 0; x < 46; x++){
				nextCard = randomDeck.drawCard();
				while(sameCard(nextCard,sortedCards[0]) || sameCard(nextCard,sortedCards[1]) || sameCard(nextCard,sortedCards[2]) || sameCard(nextCard,sortedCards[3]) || sameCard(nextCard,sortedCards[4]) || sameCard(nextCard,sortedCards[5])){
					nextCard = randomDeck.drawCard();
				}
				randomCards[x] = nextCard;
			}
			//cycle through each card and increment
		}
			//
			//printCards(randomCards);
			//**random Cards = the cards not in our hand
			//cycle through each random card and resubmit to handEvaluator to track possible outcomes
		
	}
	private void insertionSort(Card[] a, int first, int last){
		for(int unsorted = first + 1; unsorted < last; unsorted++){
			Card nextToInsert = a[unsorted];
			insertInOrder(nextToInsert, a, first, unsorted - 1);
		}
	}
	private void insertInOrder(Card anEntry,Card[] a, int begin, int end){
		int index = end;
		while(index >= begin && anEntry.getRankInt() > a[index].getRankInt()){
			a[index + 1] = a[index];
			index--;
		}
		a[index+1] = anEntry;
	}
	private void printCards(Card[] cards){
		for(int x = 0; x < cards.length; x++){
			System.out.print(cards[x].printCard() + " | ");
		}
		System.out.println();
	}
	private void getVal(Card[] cards){
		Card[] hand = new Card[5];
		int handVal = 0;
		int handindex = 0; //handindex
		int allcardindex = 0; //index for all cards
		//***7 card hand****
		if(cards.length == 7){//7 cards in hand
			for(int x = 0; x < cards.length - 1; x++){
				for (int y = x+1; y < cards.length; y++){
					handindex = 0;
					allcardindex = 0;
					while(handindex < 5){
						if(allcardindex != x && allcardindex != y){
							hand[handindex] = cards[allcardindex];
							handindex++;
						}
						allcardindex++;
					}
					handVal = handValue(hand);
					if(handVal > handValue){
						handValue = handVal;
//						printCards(hand);
						bestHand = hand.clone();
					}
				}
			}
		}
		//***6 card hand****
		if(cards.length == 6){//6 cards in hand
			for(int x = 0; x < cards.length ; x++){
				handindex = 0;
				allcardindex = 0;
				while(handindex < 5){
					if(allcardindex != x){
						hand[handindex] = cards[allcardindex];
						handindex++;
					}
					allcardindex++;
				}
				handVal = handValue(hand);
				if(handVal > handValue){
					handValue = handVal;
//					printCards(hand);
					bestHand = hand.clone();
				}
			}
		}
		//***5 card hand****
		if(cards.length == 5){//5 cards in hand
			System.out.println("In 5 card");
			handindex = 0;
			allcardindex = 0;
			while(handindex < 5){
				hand[handindex] = cards[allcardindex];
				handindex++;
				allcardindex++;
			}
			handVal = handValue(hand);
			if(handVal > handValue){
				handValue = handVal;
//				printCards(hand);
				bestHand = hand.clone();
			}
		}
		if(handVal > 9000000){
			straightFlushOrBetter++;
			fourOfKindOrBetter++;
			fullHouseOrBetter++;
			flushOrBetter++;
			straightOrBetter++;
			threeOfKindOrBetter++;
			twoPairOrBetter++;
			pairOrBetter++;
		} else if(handVal > 8000000){
			fourOfKindOrBetter++;
			fullHouseOrBetter++;
			flushOrBetter++;
			straightOrBetter++;
			threeOfKindOrBetter++;
			twoPairOrBetter++;
			pairOrBetter++;
		} else if(handVal > 7000000){
			fullHouseOrBetter++;
			flushOrBetter++;
			straightOrBetter++;
			threeOfKindOrBetter++;
			twoPairOrBetter++;
			pairOrBetter++;
		} else if(handVal > 6000000){
			flushOrBetter++;
			straightOrBetter++;
			threeOfKindOrBetter++;
			twoPairOrBetter++;
			pairOrBetter++;
		} else if(handVal > 5000000){
			straightOrBetter++;
			threeOfKindOrBetter++;
			twoPairOrBetter++;
			pairOrBetter++;
		}
		 else if(handVal > 4000000){
				threeOfKindOrBetter++;
				twoPairOrBetter++;
				pairOrBetter++;
		}  else if(handVal > 3000000){
			twoPairOrBetter++;
			pairOrBetter++;
		} else if(handVal > 2000000){
			pairOrBetter++;
		}
	}
	private boolean sameCard(Card card1, Card card2){
		boolean returnVal = false;
		if(card1.getRankInt() == card2.getRankInt() && card1.getSuitInt() == card2.getSuitInt()){
			returnVal = true;
		}
		return returnVal;
	}
	private int handValue(Card[] cards){
		int val = 0;
		if(cards.length == 5){//fullhand
			//add up card values multiplied by place in hand aces = 13,k=12...3=2, 2=1 ***slot 0 x 38416 -- slot 1 x 2744 -- slot 2 x 196 -- slot 3 x 14 -- slot 4 x 1
			//if ace is low card in straight it counts 0
			val = (cards[0].getRankInt() - 1) * 38416 + (cards[1].getRankInt() - 1) * 2744 + (cards[2].getRankInt() - 1) * 196 + (cards[2].getRankInt() - 1) * 14 + (cards[4].getRankInt() - 1);
			straight(cards);		//set straight flag
			aceLowStraight(cards);	//set aceLowStraight flag
			flush(cards);			//set flush flag
			if((straightFlag || aceLowStraightFlag) && flushFlag){//straight flush
				if(aceLowStraightFlag){//adjust value--the ace becomes the lowest card with a value of 0 instead of 13
					val = (val - (13*38416)) * 14;
				}
				val = val + 9000000;
			} else if(cards[0].getRankInt() == cards[3].getRankInt() || cards[1].getRankInt() == cards[4].getRankInt()){//checking for four of a kind
				if(cards[0].getRankInt() == cards[3].getRankInt()){//check to see if first or last card is oddball
					//last card is oddball
					val = cards[3].getRankInt() * 41370 + cards[4].getRankInt() + 8000000;
				} else {//last card is oddball
					val = cards[3].getRankInt() * 41370 + cards[0].getRankInt() + 8000000;
				}
			} else if(cards[0].getRankInt() == cards[2].getRankInt() && cards[3].getRankInt() == cards[4].getRankInt()){//check for 3-2 fullhouse
				val = cards[0].getRankInt() * 41356 + cards[3].getRankInt() * 15 + 7000000;
			} else if(cards[0].getRankInt() == cards[1].getRankInt() && cards[2].getRankInt() == cards[4].getRankInt()){//check for 2-3 fullhouse
				val = cards[0].getRankInt() * 41160 + cards[3].getRankInt() * 211 + 7000000;
			} else if(flushFlag){//check for flush
				val = val + 6000000;
			} else if (straightFlag){//check for straight
				val = val + 5000000;
			} else if (aceLowStraightFlag){//check for aceLowStraight
				val = (val - 38416 * 13) * 14 + 5000000; 
			} else if(cards[2].getRankInt() == cards[0].getRankInt() || cards[2].getRankInt() == cards[4].getRankInt() || cards[1].getRankInt() == cards[3].getRankInt()){//check for trips
				if (cards[0].getRankInt() == cards[2].getRankInt()){//card 4 and 5 are oddballs
					val = cards[2].getRankInt() *41356 + cards[3].getRankInt() * 14 + cards[4].getRankInt() + 4000000;
				} 
				if(cards[1].getRankInt() == cards[3].getRankInt()){//1 and 5 are oddballs
					val = cards[2].getRankInt() *41356 + cards[0].getRankInt() * 14 + cards[4].getRankInt() + 4000000;
				}
				if(cards[2].getRankInt() == cards[4].getRankInt()){// 1 and 2 are oddballs
					val = cards[2].getRankInt() *41356 + cards[0].getRankInt() * 14 + cards[1].getRankInt() + 4000000;
				}
				
			} else if(cards[0].getRankInt() == cards [1].getRankInt() && cards[2].getRankInt() == cards [3].getRankInt()){//check for 2 pair 1-2 and 3-4
				val = val + 3000000; //initial val ranking works
			} else if(cards[0].getRankInt() == cards [1].getRankInt() && cards[3].getRankInt() == cards [4].getRankInt()){//check for 2 pair 1-2 and 4-5
				val = cards[0].getRankInt() * 41160 + 15 * cards[3].getRankInt() + cards[2].getRankInt() + 3000000;
			} else if(cards[1].getRankInt() == cards [2].getRankInt() && cards[3].getRankInt() == cards [4].getRankInt()){//check for 2 pair 2-3 and 4-5
				val = cards[1].getRankInt() * 41160 + 15 * cards[3].getRankInt() + cards[0].getRankInt() + 3000000;
			} else if(cards[0].getRankInt() == cards [1].getRankInt()){//check for 1-2 pair
				val = val + 2000000;
			} else if(cards[1].getRankInt() == cards [2].getRankInt()){//check for 2-3 pair
				val = cards[1].getRankInt() * 41160 + cards[0].getRankInt() * 196 + cards[3].getRankInt() * 14 + cards[4].getRankInt() + 2000000;
			} else if(cards[2].getRankInt() == cards [3].getRankInt()){//check for 3-4 pair
				val = cards[2].getRankInt() * 41160 + cards[0].getRankInt() * 196 + cards[1].getRankInt() * 14 + cards[4].getRankInt() + 2000000;
			} else if(cards[3].getRankInt() == cards [4].getRankInt()){//check for 4-5 pair
				val = cards[3].getRankInt() * 41160 + cards[0].getRankInt() * 196 + cards[1].getRankInt() * 14 + cards[2].getRankInt() + 2000000;
			}
		}
		flushFlag = false;
		straightFlag = false;
		aceLowStraightFlag = false;
		return val;
	}
	private void flush(Card[] cards){
		if(cards[0].getSuitInt() == cards[1].getSuitInt() && cards[0].getSuitInt() == cards[2].getSuitInt() && cards[0].getSuitInt() == cards[3].getSuitInt() && cards[0].getSuitInt() == cards[4].getSuitInt()){
			flushFlag = true;
		}
	}
	private void straight(Card[] cards){
		if(cards[0].getRankInt() - 1 == cards[1].getRankInt() && cards[1].getRankInt() - 1 == cards[2].getRankInt() && cards[2].getRankInt() - 1 == cards[3].getRankInt() && cards[3].getRankInt() - 1 == cards[4].getRankInt()){
			straightFlag = true;
		}
	}
	private void aceLowStraight(Card[] cards){
		if(cards[0].getRankInt() == 14 && cards[1].getRankInt() - 1 == cards[2].getRankInt() && cards[2].getRankInt() - 1 == cards[3].getRankInt() && cards[3].getRankInt() - 1 == cards[4].getRankInt() && cards[4].getRankInt() == 2){
			aceLowStraightFlag = true;
		}
	}
}
