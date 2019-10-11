import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;


public class Game {

	/* Thabiso Game v0.0 + improved checkValid method + improved readAbilities method + improved playGame method */

	private Player[] players;//this array will hold the individual players
	private int flow = 1;    //this will determine the flow of the game (clockwise/counter-clockwise)
	private Deck deck;        //this is the main deck, which will hold the 'stock' and 'discard' pile
	private String currentSuit;   //this will hold the suit of the last played card (or the suit the player chooses, if an 8 is played)


	private boolean pickUp; //true if a player has to pick up cards, false otherwise
	private int numPick; //holds the number of cards that have to be picked up after a pick-up card is played

	private Player currentPlayer; //holds the reference to the current player in any given round
	private int[] botOrHuman; //holds information on whether the corresponding index in the player array is occupied by a Bot or a Player

	public Game(int numPlayers) {
		/*constructor method*/

		this.players = new Player[numPlayers];
		this.deck = new Deck(52);
		this.flow = 1;

		this.pickUp = false;
		this.numPick = 0;
		this.currentSuit = "";
		this.currentPlayer = new Player(0);

		this.botOrHuman = new int[numPlayers];
		for (int i = 0; i < this.botOrHuman.length; i++){
			this.botOrHuman[i] = 0;
		}

	}

	public Game(int numHumans, int numBots) {
		/*overloaded constructor method*/

		this.players = new Player[numHumans + numBots];
		this.deck = new Deck(52);
		this.flow = 1;

		this.pickUp = false;
		this.numPick = 0;
		this.currentSuit = "";
		this.currentPlayer = new Player(0);
		this.botOrHuman = new int[numHumans + numBots];
		int idx = 0;
		for (int i = idx; i < numHumans; i++){
			botOrHuman[i] = 0;
			idx++;
		}
		for (int i = idx; i < botOrHuman.length; i++){
			botOrHuman[i] = 1;
			idx++;
		}
	}


	public void deal() {
		/*This method deals cards out to the players by removing cards from the main 'stock' deck,
		and putting them into the players' 'stock' decks. The print statements are just there to show what the
		main 'stock' deck looks like at each step. Do feel free to comment these print statements out, if they make
		the output look a bit cluttered*/

		Random rand = new Random();

		Card dealCard;
		int numDeal = 8;

		this.deck.populate(1, 1); // the main 'stock deck' is filled with Card objects
		this.deck.shuffle("storage");

		for (int i = 0; i < players.length; i++) {
			//=======================================
			if(botOrHuman[i] == 0){
				this.players[i] = new Player(0);
			}
			else if (botOrHuman[i] ==1){
				this.players[i] = new Bot(0);
			}

			//=======================================

			for (int j = 0; j < numDeal; j++) {
				dealCard = this.deck.pop("storage");
				players[i].append("storage", dealCard); //each player receives a card

			}

		}

		this.deck.moveToEnd("none"); //a card with no ability is searched for
		Card poppedCard = this.deck.pop("storage");
		this.currentSuit = poppedCard.getSuit();
		this.deck.append("play", poppedCard); //this card becomes the top card in the discard pile

	}


	public void readAbilities(Card[] playedCards) {
		String ability = "";
		/*This method loops through the played hand, and changes the game state, based on each ability*/

		for (int i = 0; i < playedCards.length; i++) {   //hand is looped through
			ability = playedCards[i].getAbility(); //ability is read

			if (ability.equals("pick 2")) { //pick-up abilities like "pick 2" increment the pickUp variable the corresponding number of cards
				this.pickUp = true;
				this.numPick += 2;
			}
			else if (ability.equals("pick 4")) {
				this.pickUp = true;
				this.numPick += 4;
			}
			else if (ability.equals("pick 5")) {
				this.pickUp = true;
				this.numPick += 5;
			}
			else if (ability.equals("block")){ //the block ability resets the pickUp and numPick variables
				this.pickUp = false;
				this.numPick = 0;
			}
			else if (ability.equals("reverse")){ //"reverse multiplies the game flow by -1
				System.out.println("***JACK REVERSED GAME FLOW***");
				if(this.players.length > 2) {
					this.flow *= -1;
				}
				else{
					this.flow = 2;
				}

			}
			else if(ability.equals("change suit")){


				this.currentSuit = this.currentPlayer.chooseSuit(); //the player's chooseSuit method is called
			}
			else if (ability.equals("skip")){ //this ability skips a player by increasing the game flow
 				if (this.flow < 0){
					this.flow = -2;
				}
				else if (this.flow >= 0){
					this.flow = 2;
				}
			}

		}

	}
	public void refill(){
		/*Refills stock deck when required*/

		Card topCard = this.deck.pop("play");
		int length = this.deck.getDeck("play").length;
		for (int i = 0; i < length; i++){ // this for-loop appends all of the cards in the discard pile to the stock deck, except for the top card
			Card poppedCard = this.deck.pop("play");
			this.deck.append("storage", poppedCard);
		}
		this.deck.append("play", topCard);
	}

	public boolean checkValid(Card[] deck, String mode) {
		/*This method checks if a played hand is valid, before it is put on the discard pile.
		* If the hand is valid, the method returns true, and false otherwise*

		 * the deck parameter is the hand, while the mode parameter denotes if the player is in a normal, or a defensive situation
		 * (where they have to draw a large number of cards, unless they defend themselves)
		 */

		Card[] discardPile = this.deck.getDeck("play"); //this is a reference to the game's discard pile

		if (deck.length == 0 || deck[0] == null){
			return false; //an empty hand is immediately considered invalid
		}

		if(mode.equals("normal")){ // a normal play situation
			if (discardPile.length == 0) {
				return false;
			}


			if(deck.length == 1){
				//if the player played a single card
				int currentValue = deck[0].getNumber();

				if(deck[0].getNumber() == 13){
					return false;
				}

				if(discardPile[discardPile.length - 1].getSuit().equals("none") || discardPile[discardPile.length - 1].getNumber() == 1){
					return true; //if the top card of the discard pile is an Ace or Joker, any card can be played on top of it
				}



				if (deck[0].getSuit().equals(this.currentSuit) || deck[0].getNumber() == discardPile[discardPile.length - 1].getNumber() || currentValue == 1 || currentValue == 14 || currentValue == 15 || currentValue == 8) {

					return true; //otherwise, the suit or value must match the top card, with the excepttion of 8's, Aces and Jokers
				}
				return false;
			}
			else if (deck.length > 1){
				//if the player played multiple cards at once


				int currentValue = deck[0].getNumber(); //first card of the hand
				String suit = deck[0].getSuit();
				Card top = discardPile[discardPile.length - 1];
				int numPowerCards = 0;


				if (currentValue == 2 || currentValue == 7 || currentValue == 8 || currentValue == 14 || currentValue == 15){ //stops power-card stacking
					return false;
				}

				if(top.getNumber() != 14 && top.getNumber() != 15) {
					if (!(currentValue == top.getNumber() || (suit.equals(this.currentSuit)) || currentValue == 1)) {

						return false; //the first card of the stack must match the discard pile's top card
					}
				}

				for(int i = 1; i < deck.length; i++){

					//this loop checks if successive values in the stack have the same value as preceding ones, in order for the stack to be considered valid
					if(!((deck[i-1].getNumber() == deck[i].getNumber()) || ((deck[i-1].getNumber() == 13 && deck[i-1].getSuit().equals(deck[i].getSuit()))))){

						return false;
					}
					currentValue = deck[i].getNumber();
					suit = deck[i].getSuit();

					//additional measures to prevent power-card stacking
					if (currentValue == 2 || currentValue == 7 || currentValue == 8 || currentValue == 14 || currentValue == 15){
						numPowerCards ++;
					}
					if(numPowerCards >= 2){
						return false;
					}

				}

				return true;
			}
		}
		else if (mode.equals("defence")){ //a defensive situation, in which stacking is not allowed, and the player must play a single card as a defense

			if (discardPile.length == 0) {
				return false;
			}

			int currentValue = deck[0].getNumber();

			if (currentValue == discardPile[discardPile.length - 1].getNumber() || currentValue == 1 || currentValue == 14 || currentValue == 15 || currentValue == 2) {
				//the card must be a 2, an Ace or a Joker to be considered a valid defense
				return true;
			}

		}

		return false;
	}

	public void drawCard(Player player, String mode){

		/*This method draws card from the stock deck, and gives them to a specific player*/

		if (mode.equals("voluntary")) { //if a player chose to draw a card because they had no valid hands to play
			Card poppedCard = this.deck.pop("storage"); //cards are popped from the stock deck
			player.append("storage", poppedCard);      //cards are appended to the player's deck
		}
		else if (mode.equals("involuntary")){ //if a player had to draw cards because of some other player playing a pick-up card (e.g. Joker)
			for (int i = 0; i < this.numPick; i++) {
				Card poppedCard = this.deck.pop("storage");
				player.append("storage", poppedCard);
			}
		}
	}






	public void playGame() {
		/*This method runs the game*/

		int currentPlayer = 0;
		int len = 0;
		Card[] playerCards;
		Card[] playDeck;
		Card topCard;
		this.currentPlayer = this.players[currentPlayer];


		while (true) {
			//this loop runs until a player wins the game


			this.flow /= Math.abs(this.flow); //flow is reset to 1 or -1, depending on whether it was positive or negative

			if (!this.pickUp){
				this.numPick = 0;
			}



			if(this.deck.getDeck("play").length > 5) {
				refill(); //the stock deck is refilled occasionally
				this.deck.shuffle("storage");
			}



			System.out.println();
			System.out.println("_________________________________________________________________________________________________________");
			System.out.println();
			System.out.printf("-- PLAYER %d --\n", currentPlayer + 1);
			playDeck = this.deck.getDeck("play");

			if (playDeck.length > 0 && playDeck[playDeck.length - 1].getNumber() != 8) {

				topCard = playDeck[playDeck.length - 1]; //this variable holds the top card of the discard pile
				this.currentSuit = topCard.getSuit(); //this.currentSuit is initialise to the top card's suit (unless an 8 is played)

				System.out.printf("Top card: %s\n", topCard.convertToString());
				System.out.println("The discard pile: " + deck.toString("play"));
				System.out.print("\n\n");
			} else {

				System.out.println("The discard pile: " + deck.toString("play"));
			}

			playerCards = new Card[len]; //this variable will hold the hand that a player plays

			if (!this.pickUp) { //if nobody has to draw cards up (i.e. a normal play situation)

				topCard = playDeck[playDeck.length - 1];
				boolean drawingCard = this.players[currentPlayer].play(topCard, this.currentSuit); //the player plays a move

				if (!drawingCard){ //if not drawing a card

					len = this.players[currentPlayer].getDeck("play").length;

					playerCards = new Card[len]; //playerCards is initialised to the length of the player's hand

					for (int i = len -1; i > -1; i--) {
						playerCards[i] = this.players[currentPlayer].pop("play"); //cars are popped from the player's hand
					}
					//System.out.println(checkValid(playerCards, "normal"));

					while (!checkValid(playerCards, "normal")) {
						//validity checking is done, and if the player plays an invalid hand, they are prompted to play a valid card, until they do, or they draw a card
						System.out.println("Invalid card");

						if (playerCards.length > 0 && playerCards[0] != null) {
							for (Card card : playerCards) {
								this.players[currentPlayer].append("storage", card);
							}
						}
						playerCards = new Card[0];
						drawingCard = this.players[currentPlayer].play(topCard, this.currentSuit);
						if (drawingCard) {
							//give cards to player
							drawCard(this.players[currentPlayer], "voluntary");
							break; //breaks input loop
						}
						len = this.players[currentPlayer].getDeck("play").length;
						playerCards = new Card[len];
						for (int i = len -1; i > -1; i--) {
							playerCards[i] = this.players[currentPlayer].pop("play");
						}
						break;
					}
					readAbilities(playerCards); //the abilities of the cards in the player's hand are read
					}
				else{
					drawCard(this.players[currentPlayer], "voluntary"); // if the player played no card, the player draws a card
				}

			} else { //this is the defence situation
				topCard = playDeck[playDeck.length - 1];
				boolean drawsCards = this.players[currentPlayer].play2(topCard, this.currentSuit);

				if(!drawsCards) {
					len = this.players[currentPlayer].getDeck("play").length;
					playerCards = new Card[len];
					for (int i = len - 1; i > -1; i--) {
						playerCards[i] = this.players[currentPlayer].pop("play");
					}
					//System.out.println(checkValid(playerCards));

					while (!checkValid(playerCards, "defence")) { //the player must play a valid defence, or they will be prompted continuously

						System.out.println("Invalid card");


						if (playerCards.length > 0 && playerCards[0] != null) {
							for (Card card : playerCards) {
								this.players[currentPlayer].append("storage", card);
							}
						}
						playerCards = new Card[0];
						drawsCards = this.players[currentPlayer].play2(topCard, this.currentSuit);

						//=======================================================================
						if (drawsCards) {
							//give cards to player
							System.out.println("drawing cards...");
							refill();
							this.deck.shuffle("storage");

							drawCard(this.players[currentPlayer], "involuntary");
							this.pickUp = false;
							break; //breaks input loop
						}
						//=======================================================================

						len = this.players[currentPlayer].getDeck("play").length;
						playerCards = new Card[len];
						for (int i = len - 1 ; i > -1; i--) {
							playerCards[i] = this.players[currentPlayer].pop("play");

						}
						break;
					}
					readAbilities(playerCards);
				}

				else if (drawsCards){
					drawCard(this.players[currentPlayer], "involuntary");
					this.pickUp = false;
				}
			}
			if (playerCards.length > 0 && playerCards[0] != null) { //the cards in the played hand are appended to the discard pile
				for (int i = 0; i < playerCards.length; i++) {
					this.deck.append("play", playerCards[i]);
				}
			}

			if (this.currentPlayer.getDeck("storage").length == 0){ //the game stops if a player successfully plays all of their cards
				System.out.println();
				System.out.println("============================================================");
				System.out.println("The winner is PLAYER " + (currentPlayer + 1));
				System.out.println("============================================================");
				System.out.println();
				break;
			}

			if (this.flow >= 0){ //these statements ensure that currentPLayer is incremented successfully, without any errors in modular arithmetic
				currentPlayer = (currentPlayer + this.flow) % this.players.length;
			}
			else{
				currentPlayer = ((currentPlayer + this.flow) % this.players.length);
				if (currentPlayer < 0){
					currentPlayer += 4;
				}
			}
			this.currentPlayer = this.players[currentPlayer];


		}
	}

	public static void main(String[] args) {

		/*The main method/driver for the application*/

		Scanner in = new Scanner(System.in);
		int numHumans = 0;
		int numBots = 0;

		System.out.println("Enter number of computer and human players (a total of 5 players maximum)\n");
		System.out.print("Enter number of human players: ");
		numHumans = in.nextInt();
		System.out.print("Enter number of computer players: ");
		numBots = in.nextInt();


		boolean quit = false;

		String userInput = "";

		while (!quit) { //new games will be played until the user quits
			//=======================================
			Game game = new Game(numHumans, numBots);
			game.deal();
			//==========================================
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			game.playGame();
			System.out.println();
			System.out.println("<Hit 'q' to quit. Press any other key to start a new game>");
			userInput = in.next();
			if (userInput.equals("q")){
				quit = true;
			}
		}
	}

}