
public class Player {
	String playerName;
	Integer chips;
	Integer chipsInPot;
	Cards[] holeCards = new Cards[2];
	
	Player(){
		this("",0);
	}
	Player(String pName){
		this(pName, 0);		
	}
	Player(String pName,int startingChips){
		playerName = pName;
		chips = startingChips;
		chipsInPot = 0;		
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Integer getChips() {
		return chips;
	}
	public void setChips(Integer chips) {
		this.chips = chips;
	}
	public Integer getChipsInPot() {
		return chipsInPot;
	}
	public void setChipsInPot(Integer chipsInPot) {
		this.chipsInPot = chipsInPot;
	}
	public Cards[] getHoleCards() {
		return holeCards;
	}
	public void setHoleCards(Cards[] holeCards) {
		this.holeCards = holeCards;
	}
}

