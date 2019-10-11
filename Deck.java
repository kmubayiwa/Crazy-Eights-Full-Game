import java.util.Arrays;
import java.util.Random;

public class Deck {


	private Card[] storageDeck; //This is the 'stock' deck
	private Card[] playDeck;    //This is the 'play' deck, which the players place their cards on

	public Deck(int size){
		/*This is the constructor function, which initialises
		  a deck of size specified by the int 'size'*/
		this.storageDeck = new Card[size];
		this.playDeck = new Card[0];
	}


	public void populate(int numBlackJokers, int numRedJokers){
		this.storageDeck = new Card[this.storageDeck.length + numRedJokers + numBlackJokers];
		String[] suits = {"spades", "hearts", "clubs", "diamonds"};
		String[] abilities = {"none", "block", "pick 2", "none", "none", "none", "none", "skip", "change suit", "none", "none", "reverse", "none", "continue" };
		String[] positions = {"none", "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		Card card = new Card(0,"","","");

		int deckPosition = 0;
		for (String suit : suits){
			for (int i = 1; i < 14; i++){
				card = new Card(i, positions[i], suit, abilities[i]);
				this.storageDeck[deckPosition*13 + (i-1)] = card;
			}
			deckPosition ++;
		}

		for (int b = 0; b < numBlackJokers; b++){
			this.storageDeck[52 + b] = new Card(14, "black_joker", "none", "pick 4");
		}

		for (int r = 0; r < numRedJokers; r++){
			this.storageDeck[52 + numBlackJokers + r] = new Card(15, "red_joker", "none", "pick 5");
		}

	}

	public void shuffle(String location)
	{
		int count = 1;
		double entropy = 0.0;
		double threshold = 4.7; //4.7 is the preferred entropy

		int[] initialPlaces = new int[this.storageDeck.length];
		for(int i = 0; i < initialPlaces.length; i++)
		{
			initialPlaces[i] = i;
		}

		while(entropy < threshold)
		{
			entropy = riffleShuffle(this.storageDeck, initialPlaces);
			count++;

			if(count%50 == 0){
				threshold -= 0.2; //Threshold gets reduced a bit if it cannot be reached
			}
		}
	}

	public double riffleShuffle(Card[] deck, int[] places) {
		Card[] newDeck = new Card[deck.length];
		int[] newPlaces = new int[deck.length];
		int randSplit = 0, leftIndex = 0, rightIndex = 0, i = 0;
		int sidePick = 0;
		double entropy;

		Random r = new Random();
		randSplit = r.nextInt(2) + 27; //Deck is split somewhere in the middle

		leftIndex = 0;
		rightIndex = randSplit;
		int leftMark = rightIndex;

		/*Cards from the two sub-decks are interleaved into a new deck*/
		while (i < deck.length) {

			sidePick = r.nextInt(2);

			if (leftIndex < leftMark && rightIndex < deck.length) {
				if (sidePick == 0) { 		//A bottom card is picked from either sub-deck
					newDeck[i] = deck[leftIndex];
					newPlaces[i] = places[leftIndex];
					leftIndex += 1;

				} else {
					newDeck[i] = deck[rightIndex];
					newPlaces[i] = places[rightIndex];
					rightIndex += 1;
				}

			}
			else if (leftIndex < leftMark) {
					newDeck[i] = deck[leftIndex];
					newPlaces[i] = places[leftIndex];
					leftIndex += 1;

				} else {
					newDeck[i] = deck[rightIndex];
					newPlaces[i] = places[rightIndex];
					rightIndex += 1;
				}
				i++;
			}
			/*The shuffled deck gets assigned to the old deck
			* Shuffled places get assigned to places*/
			for (int j = 0; j < deck.length; j++) {

				deck[j] = newDeck[j];
				places[j] = newPlaces[j];
			}
			entropy = shannonEntropy(newPlaces);
			return entropy;
		}

	public double shannonEntropy(int[] places) {

		int[] differences = new int[places.length];
		double entropy = 0.0;

		for(int i = 0; i < places.length-1; i++)
		{
			int distance = places[i] - places[(i+1)%places.length];

			if(distance < 0)
			{
				distance = distance + places.length;
			}
			differences[distance]++;
		}

		for(int i = 0; i < places.length-1; i++)
		{
			double p = (double)differences[i]/(double) places.length;
			if(p > 0.0)
			{
				entropy -= p*Math.log(p)/Math.log(2);
			}
		}
		return entropy;
	}




	public int moveToEnd(String location, String position, String suit){

		/*This method moves an input card to the end of an array so that it can be popped
		* The three parameters it takes in are:
		* The location (i.e. the sub-deck that must be searched through)
		* the position of the input card
		* the suit of the input card
		* */


		Card poppedCard = null;
		position = position.toLowerCase();
		suit = suit.toLowerCase();
		int idx = 0;
		boolean cardFound = false;
		int length = 0;

		if(location.equals("storage")){
			length = this.storageDeck.length;
			for (int i = 0; i < this.storageDeck.length; i++){
				/*this for loop searches through the deck for the card.
				* Once it is found, the card and its index are assigned to variables
				* for later use*/
				if (this.storageDeck[i].getPosition().toLowerCase().equals(position)){
					if (this.storageDeck[i].getSuit().toLowerCase().equals(suit)){
						cardFound = true;
						poppedCard = this.storageDeck[i];
						idx = i;
						break;
					}
				}
			}

			if (!cardFound){ //if the card isn't found, the method returns
				System.out.println("That card is not in your deck");
				return -1;
			}

			for(int i = idx + 1; i < this.storageDeck.length; i++){ //all method ahead of the input card's index are shifted to the left
				this.storageDeck[i-1] = this.storageDeck[i];
			}
			this.storageDeck[length - 1] = poppedCard; //the input card is added to the end of the array
		}
		else if(location.equals("play")){ //this executes the same operations as the above code, but in a different location
			length = this.playDeck.length;
			for (int i = 0; i < this.playDeck.length; i++){
				if (this.playDeck[i].getPosition().toLowerCase().equals(position)){
					if (this.playDeck[i].getSuit().toLowerCase().equals(suit)){
						cardFound = true;
						poppedCard = this.playDeck[i];
						idx = i;
						break;
					}
				}
			}

			if (!cardFound){
				System.out.println("That card is not in your deck");
				return -1;
			}
			for(int i = idx + 1; i < this.playDeck.length; i++){
				this.playDeck[i-1] = this.playDeck[i];
			}
			this.storageDeck[length - 1] = poppedCard;

		}

		return 1;
	}

	public int moveToEnd(String ability){
		/*This is an overloaded version of the above moveToEnd
		* method, except that it searches for a card based on its
		* ability. This is useful for setting up a starting card with no
		* ability at the beginning of the game*/

		Card poppedCard = null;

		ability = ability.toLowerCase();
		int idx = 0;
		boolean cardFound = false;
		int length = 0;


			length = this.storageDeck.length;
			for (int i = 0; i < this.storageDeck.length; i++){
					if (this.storageDeck[i].getAbility().toLowerCase().equals(ability)) {
						cardFound = true;
						poppedCard = this.storageDeck[i];
						idx = i;
						break;
					}
			}

			if (!cardFound){
				System.out.println("That card is not in your deck");
				return -1;
			}

			for(int i = idx + 1; i < this.storageDeck.length; i++){ //might cause problems if chosen card is already at the end of the array(i.e IndexOutOfRange error)
				this.storageDeck[i-1] = this.storageDeck[i];
			}
			this.storageDeck[length - 1] = poppedCard;



		return 1;
	}




	public void append(String location, Card card){

		/*This method adds a card to a sub-deck,
		  specified by the 'location' String.
		  */

		if (location.equals("storage")){
			Card[] newArr = new Card[this.storageDeck.length + 1]; //a new array is created that is one unit longer than the input array

			for (int i = 0; i < this.storageDeck.length; i++){ //all the elements of the input array are added to this new array
				newArr[i] = this.storageDeck[i];

			}
			newArr[this.storageDeck.length] = card; //the new element is added to the last index of the new array
			this.storageDeck = newArr.clone(); //the new array is assigned to one of the Deck's sub-decks


		}
		else if (location.equals("play")){

			Card[] newArr = new Card[this.playDeck.length + 1];
			for (int i = 0; i < this.playDeck.length; i++){
				newArr[i] = this.playDeck[i];
			}
			newArr[this.playDeck.length] = card;
			this.playDeck = newArr.clone();

		}


	}

	public String toString(String location){
		String[] cards = new String[] {};
		if (location.equals("storage")){
			cards = new String[this.storageDeck.length];
			for (int i = 0; i < this.storageDeck.length; i++){
				cards[i] = this.storageDeck[i].convertToString();
			}
		}
		else if (location.equals("play")){
			cards = new String[this.playDeck.length];
			for (int i = 0; i < this.playDeck.length; i++){
				cards[i] = this.playDeck[i].convertToString();
			}
		}
		return Arrays.toString(cards);
		}

	public Card pop(String location){

		/*This method removes a card from a sub-deck,
		  specified by the 'location' String. This 'popped' card is
		  returned by the method*/

		if (location.equals("storage")){

			Card[] newArr = new Card[this.storageDeck.length - 1]; //a new array is created that is one unit shorter than the input array

			for (int i = 0; i < this.storageDeck.length - 1; i++){ //all the elements of the input array (except the last one) are added to this new array
				newArr[i] = this.storageDeck[i];
			}

			Card lastValue = this.storageDeck[newArr.length]; //this is the last value of the input array, which will be returned
			this.storageDeck = newArr.clone(); //the new array is assigned to one of the Deck's sub-decks
			return lastValue; //lastValue is returned

		}
		else if (location.equals("play")){

			Card[] newArr = new Card[this.playDeck.length - 1]; //

			for (int i = 0; i < this.playDeck.length - 1; i++){
				newArr[i] = this.playDeck[i];
			}

			Card lastValue = this.playDeck[newArr.length];
			this.playDeck = newArr.clone();
			return lastValue;

		}

		return new Card (-1, "", "", ""); //returned if the input parameter was invalid


	}

	public Card[] getDeck(String location){
		/*This is a 'getter' function. It returns
		* the deck's sub-deck to be used by another class*/

		if (location.equals("storage")) {
			return this.storageDeck;
		}
		else if (location.equals("play")){
			return this.playDeck;
		}
		return new Card[] {};
	}


	public void setDeck(String location, Card[] deck){
		/*This is a 'setter' function. It allows
		 * the deck's sub-deck to be set by another class*/

		if (location.equals("storage")) {
			this.storageDeck = deck;
		}
		else if (location.equals("play")){
			this.playDeck = deck;
		}

	}




}
