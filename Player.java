import java.util.Scanner;
import java.util.Arrays;


public class Player {
	

	
	private int deckSize; // this will hold the size of the deck
	private Deck myDeck; // this will hold the player's Deck object

	public Player(int numCards){
		/*This is the constructor method.
		It initialises the player deck-size
		to be a specified number of cards.
		This is usually initialised to zero,
		as the players have no cards before the
		cards are dealt*/

		this.deckSize = numCards;
		this.myDeck = new Deck(numCards);
	}

	public boolean play(Card topCard, String currentSuit){
		/*This is one of two methods that human player can use to play the game
		* This one is used on a normal situation, where no defence needs to be played
		* */

		this.myDeck.setDeck("play", new Card[] {});
		String[] deck = null;
		int inc = 0;
		Scanner in = new Scanner(System.in); //a Scanner object is initiated to allow the use to provide input
		String position = null;
		String suit = null;
		String quit = null;
		boolean done = false;
		Card transferCard = null;
		int playMode = 0;
		System.out.println();
		System.out.println("Your deck: " + this.myDeck.toString("storage") + "\n");
		System.out.print("Mode 1 to play a single card\nMode 2 to stack cards\nMode 3 to draw a card\n"); //the player can choose to play a single card, to stack, or to draw a card
		playMode = in.nextInt();
		in.nextLine();
		System.out.println();


		if (playMode == 1){



			System.out.print("Enter card number: ");
			position = in.nextLine(); //the player enters input

			if (!position.toLowerCase().equals("red_joker") && !position.toLowerCase().equals("black_joker")) {
				System.out.print("Enter card suit: ");
				suit = in.nextLine();
			} else {
				suit = "none";
			}
			int cardFound = myDeck.moveToEnd("storage", position, suit); //the chosen card is moved to the end of their deck
			if (cardFound == 1) {
				transferCard = myDeck.pop("storage");
				myDeck.append("play", transferCard); // the chosen card is moved to the playDeck
			}


		}

		else if (playMode == 2) { //this is where the user stacks cards
			while (!done) {
				//the loop allows the user to continuously select cards, until they are satisfied with their combination

				System.out.print("Enter card number: ");
				position = in.nextLine();
				if (!position.toLowerCase().equals("red_joker") && !position.toLowerCase().equals("black_joker")) {
					System.out.print("Enter card suit: ");
					suit = in.nextLine();
				} else {
					suit = "none";
				}
				int cardFound = myDeck.moveToEnd("storage", position, suit);
				if (cardFound == 1) {
					transferCard = myDeck.pop("storage");
					myDeck.append("play", transferCard);
				}

				System.out.print("<Press 'q' to stop, press any other key to continue> ");
				quit = in.nextLine();
				if (quit.equals("q")) {
					done = true;
				}
			}
		}
		else if (playMode == 3){
			return true; //this is if the user decides to draw cards. 'true' is returned, and the Game object will know that it means that the user made this choice
		}

		return false; //false means that the player has chosen not to draw cards
	}

	public boolean play2(Card topCard, String currentSuit) {
		/*Used if someone played a pick-up card, and a defense must be played
		* it works in the same way as play, but only a single card is allowed
		* to be played, and it must be a defense*/

		this.myDeck.setDeck("play", new Card[]{});
		String[] deck = null;
		int inc = 0;
		Scanner in = new Scanner(System.in);
		String position = null;
		String suit = null;
		String quit = null;
		boolean done = false;
		Card transferCard = null;
		int playMode = 0;
		System.out.println();
		System.out.println("Your deck: " + this.myDeck.toString("storage") + "\n");
		System.out.print("Mode 1 to play a defence\nMode 2 to draw card\n");
		playMode = in.nextInt();
		in.nextLine();
		System.out.println();

		if (playMode == 1) {


			System.out.print("Enter card number: ");
			position = in.nextLine();

			if (!position.toLowerCase().equals("red_joker") && !position.toLowerCase().equals("black_joker")) {
				System.out.print("Enter card suit: ");
				suit = in.nextLine();
			} else {
				suit = "none";
			}
			int cardFound = myDeck.moveToEnd("storage", position, suit);
			if (cardFound == 1) {
				transferCard = myDeck.pop("storage");
				myDeck.append("play", transferCard);
			}


		} else if (playMode == 2) {
			return true; //if the user has no valid defense, it draws cards, and true is returned
		}

		return false;
	}

	public void append(String location, Card card){
		/*This is a wrapper function that appends a card to the player's 'stock' deck*/
		this.myDeck.append(location, card);
	}

	public Card pop (String location){
		/*This is a wrapper function that pops a card from the player's 'stock' deck*/
		return this.myDeck.pop(location);
	}

	public Card[] getDeck(String location){
		/*This 'getter' function returns a sub-deck (specified by the input String, location), to be returned to another class*/
		return this.myDeck.getDeck(location);
	}
	public void setDeck(String location, Card[] deck){
		/*This 'setter' function allows for a sub-deck (specified by the input String, location), to be set by another class*/
		this.myDeck.setDeck(location, deck);
	}

	public String chooseSuit(){
		/*If the player plays an eight, this method allows them to choose a suit*/
		Scanner in  = new Scanner(System.in);
		System.out.print("What suit would you like? ");

		String choice = in.next().toLowerCase();

		if (!(choice.equals("spades") || choice.equals("clubs") || choice.equals("hearts") || choice.equals("diamonds"))){

			while(!(choice.equals("spades") || choice.equals("clubs") || choice.equals("hearts") || choice.equals("diamonds"))){
				System.out.println();
				System.out.println("Invalid choice");
				System.out.print("Enter a suit (spades/ clubs / hearts / diamonds): ");
				choice = in.next().toLowerCase();

			}

		}
		System.out.printf("***SUIT HAS BEEN CHANGED TO %s***", choice.toUpperCase());
		return choice;

	}

}
