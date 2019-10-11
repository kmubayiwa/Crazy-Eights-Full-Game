public class Card {
	/*This is the Card class, the most basic class in the game
	* It holds a rank, suit, value, and ability, which can have various
	* in-game effects*/

	private int number; //1-15
	private String position; //ace - red_joker
	private String suit; //"hearts","clubs","spades","diamonds"
	private String ability; //reverse, pick 2/5/10, change suit, skip, continue, block

	public Card (int _number, String _position, String _suit, String _ability){
		this.number = _number;
		this.position = _position;
		this.suit = _suit;
		this.ability = _ability;

	}

	public int getNumber(){
		return this.number;
	}
	public void setNumber(int number){
		this.number = number;
	}



	public String getPosition() {
		return position;
	}

	public String toString(){
		if (!this.position.equals("red_joker") && !this.position.equals("black_joker")) {
			return this.position + " of " + this.suit;
		}

		if (this.position.equals("red_joker")){
			return "red_joker";
		}

		if (this.position.equals("black_joker")){
			return "black_joker";
		}

		return "";
	}

	public String convertToString(){
		if (!this.position.equals("red_joker") && !this.position.equals("black_joker")) {
			return this.position + " of " + this.suit;
		}

		if (this.position.equals("red_joker")){
			return "red_joker";
		}

		if (this.position.equals("black_joker")){
			return "black_joker";
		}

		return "";
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}


}
