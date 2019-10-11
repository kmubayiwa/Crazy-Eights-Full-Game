import java.util.Arrays;
import java.util.Scanner;

public class Bot extends Player{
    /*This is the computer player.
    * It inherits from the Player class, but the play methods
    * have been modified to allow for automated input*/

    private int deckSize; // this will hold the size of the deck
    private Deck myDeck; // this will hold the player's Deck object

    public Bot(int numCards){
		/*This is the constructor method.
		It initialises the player deck-size
		to be a specified number of cards.
		This is usually initialised to zero,
		as the players have no cards before the
		cards are dealt*/

        super(numCards);
        this.deckSize = numCards;
        this.myDeck = new Deck(numCards);
    }

    public Card[] append(Card[] cardArr, Card card) {




            Card[] newArr = new Card[cardArr.length + 1]; //a new array is created that is one unit longer than the input array

            for (int i = 0; i < cardArr.length; i++){ //all the elements of the input array are added to this new array
                newArr[i] = cardArr[i];

            }
            newArr[cardArr.length] = card; //the new element is added to the last index of the new array
            cardArr = newArr.clone(); //the new array is assigned to one of the Deck's sub-decks

            return cardArr;
    }

    public Card[] stackFinder(Card topCard, String currentSuit){
        /*This method helps the bot to select a hand to be played.
        * It attempts to play a stack of cards, but if that is impossible,
        * a single card is played*/

        Card[] hand = this.myDeck.getDeck("storage");

        Card[] stack = {};

        boolean[] checked = new boolean[hand.length];
        for (int i = 0; i < checked.length; i++) {
            checked[i] = false;
        }

        boolean kingFound = false;
        int idx = 0;
        for (Card card : hand){ //this loop looks for a king in the player's hand
            if (card.getNumber() == 13 && card.getSuit().equals(currentSuit)){
                kingFound = true;
                break;
            }
            idx ++;
        }

        if(kingFound){
            //if a king is found in the bot's hand

            checked[idx] = true;
            Card firstKing = hand[idx];


            Card[] pairedKings = {};
            Card[] matchingSuit = {};
            Card[] loneKings = {};
            boolean partnerFound = false;
            for(int i = 0; i < hand.length; i++){ //this loop looks for kings, and other cards that are the same suit as those kings
                if(hand[i].getSuit().equals(firstKing.getSuit()) && !checked[i]){
                    checked[i] = true;
                    pairedKings = append(pairedKings, firstKing);
                    matchingSuit = append(matchingSuit, hand[i]);
                    partnerFound = true;
                    break;
                }
            }
            if(!partnerFound){
                loneKings = append(loneKings, firstKing);
            }



            if(pairedKings.length > 0 && matchingSuit.length > 0){ //this plays a king and some card of the same suit

               stack = append(stack, pairedKings[0]);

               stack = append(stack, matchingSuit[0]);
            }
        }


        if(stack.length == 0) { //if the king-stacking was unsuccessful, the code in this condition is executed

            for (int i = 0; i < hand.length; i++) {

                /*this nested for-loop checks every card in the array, and constructs a stack,
                * adding a card to it at every iteration, until no more cards can be added,
                * at which point the loop breaks, and the stack is played*/

                boolean nextCardFound = false;

                for (int j = 0; j < hand.length; j++) {

                    if (i == 0) {
                        if (!checked[i] && (hand[j].getSuit().equals(currentSuit) || hand[j].getNumber() == topCard.getNumber() || hand[j].getNumber() == 8 || hand[j].getNumber() == 1)) {
                            stack = append(stack, hand[j]); //appends the card to the stack if it meets the criteria
                            checked[j] = true;
                            nextCardFound = true;
                            break;
                        }
                    } else {
                        if (!checked[j]) {
                            if (hand[j].getNumber() == stack[stack.length - 1].getNumber()) {
                                stack = append(stack, hand[j]);
                                checked[j] = true;
                                nextCardFound = true;
                                break;
                            }
                        }
                    }

                }

                if ((stack.length > 0)) {
                    Card topOfStack = stack[stack.length - 1];
                    int topValue = topOfStack.getNumber();
                    if (topValue == 2 || topValue == 7 || topValue == 8 || topValue == 14 || topValue == 15) {
                        break;
                    }
                }

                if (!nextCardFound) {
                    break;
                }
            }
        }




        if (stack.length > 0 && stack[stack.length - 1].getNumber() != 13) {
            return stack; //the stack is returned
        }
        return new Card [] {}; //this means no valid card or stack could be found
    }

    public boolean play(Card topCard, String currentSuit){
        /*This is an automated version of the player's play method*/

        this.myDeck.setDeck("play", new Card[]{});

        String topSuit = topCard.getSuit();
        int topValue = topCard.getNumber();

        System.out.println();
        if (this.myDeck.getDeck("storage").length == 1){
            System.out.printf("I have %d card " ,this.myDeck.getDeck("storage").length);
        }
        else if (this.myDeck.getDeck("storage").length <= 4){
            System.out.printf("I have %d cards " ,this.myDeck.getDeck("storage").length);
        }
        System.out.println();



        Card[] stack = stackFinder(topCard, currentSuit);  // a hand of cards is found

        if (stack.length > 0){ // the hand is played if it is valid
            for (Card playCard : stack){
                this.myDeck.moveToEnd("storage", playCard.getPosition(), playCard.getSuit());
                Card poppedCard = this.myDeck.pop("storage");
                this.myDeck.append("play", poppedCard);
            }
            System.out.println("I am playing: " + this.myDeck.toString("play"));
            return false; //the Bot draws a card if no valid hand is played
        }

       System.out.println("I am drawing a card");
        System.out.println();
        return true;
    }

    public String chooseSuit(){
        /*If the Bot plays an 8, it uses this method to choose which suit it wants
        * It will choose the suit that it has the most cards of*/

        int spadeCount = 0;
        int clubCount = 0;
        int heartCount = 0;
        int diamondCount = 0;

        for (Card card : this.myDeck.getDeck("storage")){
            String suit = card.getSuit();

            if (suit.equals("none")){
                continue;
            }
            else if (suit.equals("spades")){
                spadeCount ++;
            }
            else if (suit.equals("clubs")){
                clubCount ++;
            }
            else if (suit.equals("hearts")){
                heartCount ++;
            }
            else if (suit.equals("diamonds")){
                diamondCount ++;
            }

            int maxIndex = 0;
            int maximum = 0;
            int[] counts = {spadeCount, clubCount, heartCount, diamondCount};

            for (int i = 0; i < counts.length; i++){
                if (counts[i] > maximum){
                    maximum = counts[i];
                    maxIndex = i;
                }
            }

            if(maxIndex == 0){
                System.out.println("***SUIT HAS BEEN CHANGED TO SPADES***");
                return "spades";
            }
            else if (maxIndex == 1){
                System.out.println("***SUIT HAS BEEN CHANGED TO CLUBS***");
                return "clubs";
            }
            else if (maxIndex == 2){
                System.out.println("***SUIT HAS BEEN CHANGED TO HEARTS***");
                return "hearts";
            }
            else if (maxIndex == 3){
                System.out.println("***SUIT HAS BEEN CHANGED TO DIAMONDS***");
                return "diamonds";
            }

        }
        return "error";

    }

    public boolean play2(Card topCard, String currentSuit) {
        /*This works the same way as the aforementioned play method, but it handles defence situations,
        * and does not allow for stacking*/

        System.out.println();
        if (this.myDeck.getDeck("storage").length == 1){
            System.out.printf("I have %d card " ,this.myDeck.getDeck("storage").length);
        }
        else if (this.myDeck.getDeck("storage").length <= 4){
            System.out.printf("I have %d cards " ,this.myDeck.getDeck("storage").length);
        }
        System.out.println();

        this.myDeck.setDeck("play", new Card[]{});
        int topValue = topCard.getNumber();

        for (Card card : this.myDeck.getDeck("storage")) {
            int cardValue = card.getNumber();
            if (cardValue == 1 || cardValue == 2 || cardValue == 14 || cardValue == 15) {
                this.myDeck.moveToEnd("storage", card.getPosition(), card.getSuit());
                Card poppedCard = this.myDeck.pop("storage");
                this.myDeck.append("play", poppedCard);
                System.out.println("I am playing: " + poppedCard + " as my defense");
                System.out.println();
                return false;
            }
        }


        System.out.println("I am drawing a card");
        return true;
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



}
