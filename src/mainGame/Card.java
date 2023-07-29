/* Card Class
- In charge of ALL THE CARD RELATED METHODS
- basically the root of the program for the game to run
- houses all the card generation, checkers, cards, etc.
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame;

//import java packages
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import mainGame.MainUI.*;
import static mainGame.storage.Fonts.*;
import static mainGame.Gameplay.*;


public class Card {

    //initialize all card variables
    static String currentColor;
    static String currentNumber;
    static String currentCard = "   ";
    static WildCard wild;
    public static String[] playerCards = new String[playerAmount];
    static int cardGenAmount = 10;
    static int cardDebt = 0;

    //initialize final variables
    static final int TYPE_NORMAL = 0;
    static final int TYPE_SIDE_CARD = 1;
    static final int TYPE_DECK_CARD = 2;
    //--------------------------------------------------------------------------------------------------------------
    //This method basically determines if a card is valid or not that the player selected and what to do with the card once inputted
    public static boolean cardSelection(int i){
        //initialize variables
        String selectedColor;
        String selectedNumber;
        boolean validInput;
        int selectedPlace;

        //determines what the selected color of the player is and the selected number
        selectedColor = Character.toString(playerCards[currentPlayer-1].charAt(i*3)); //change 0 to current player
        selectedNumber = Character.toString(playerCards[currentPlayer-1].charAt((i*3)+1));
        //selected place basically means what place in the buttons was pressed
        selectedPlace = i;
        //determines the current color and number
        currentColor = Character.toString(currentCard.charAt(0));
        currentNumber = Character.toString(currentCard.charAt(1));

        //calls the cardChecker method to check if the card is valid, returns a boolean
        validInput = cardChecker(selectedColor, selectedNumber, selectedPlace);

        //if there is a current event, then the input is not valid
        if (currentEvent == true){
            validInput = false;
        //if there is a card debt (cards to pick up)
        } else if (cardDebt != 0){
            //these are the valid cards that they can play so if they play these, its true
            if (selectedNumber.equals("p") || (selectedColor.equals(currentColor) && selectedNumber.equals("r")) || selectedNumber.equals("+")){
                validInput = true;
            } else{
                validInput = false;
            }
        //if the player has been skipped, then any input is false as well
        } else if (skipValue != 0){
            validInput = false;
        }
        //if the input is true, then
        if (validInput == true) {
            //updates the current card
            cardUpdater(selectedColor, selectedNumber, selectedPlace);
            //checks if there is a special event
            SpecialEvent.eventChecker(selectedColor, selectedNumber);
            //checks if it is a wild card or not
            wildUpdater(selectedColor, selectedNumber);
            //renders the card to the current card button
            Card.render(1, 0, currentCardButton, 1);
        }
        //returns the valid input value
        return validInput;
    }

    //--------------------------------------------------------------------------------------------------------------
    //basically checks if the card selected is valid or not
    public static boolean cardChecker(String selectedColor, String selectedNumber, int selectedPlace) {
        boolean validInput = false;

        //gets the current color and numbers
        currentColor = Character.toString(currentCard.charAt(0));
        currentNumber = Character.toString(currentCard.charAt(1));

        //if nothings played, then valid Input is true
        if (currentCard.equals("   ")) {
            validInput = true;
        }else {
            //if the input is equal to the current color, number, or is a wild card, then the valid Input is true
            if (selectedColor.equals(currentColor)) {
                validInput = true;
            } else if (selectedNumber.equals(currentNumber)) {
                validInput = true;
            } else if (selectedColor.equals("5")) {
                validInput = true;
            }
        }
        if (validInput){
            gameWindow.repaint();
        }
        //returns the valid input
        return validInput;
    }
    //--------------------------------------------------------------------------------------------------------------
    //checks it the card is a wild card or not
    public static void wildUpdater(String selectedColor, String selectedNumber){
        //if the selected color is one of the valid wild card numbers, then it creates a new Wild object and initiates the wild creation
        if ((selectedColor.equals("5") && selectedNumber.equals("c") && cardDebt == 0) || (selectedColor.equals("5") && selectedNumber.equals("+"))){
            wild = new WildCard();
            //creates the wild window
            wild.windowCreation(selectedNumber);
            //creates the wild card buttons
            wild.buttonCreation();
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //updates the current card, and removes the card in the deck
    public static void cardUpdater(String selectedColor, String selectedNumber, int selectedPlace) {
        currentCard = selectedColor + selectedNumber + " ";
        removeCard(selectedColor, selectedNumber, selectedPlace);
    }



    //--------------------------------------------------------------------------------------------------------------
    //creates the starting deck
    public static void startDeck() {
        String card;
        //repeats for the amount of players and creates a deck of cards for the amount selected to be generated
        for (int i = 0; i < playerAmount; i++) {
            String deck = "";
            for (int x = 0; x < cardGenAmount; x++) {
                card = cardCreation();
                deck = deck + card;
            }
            //sets the array value to be equal to the deck generated
            playerCards[i] = deck;
            playerCards[i] = cardSorter(playerCards[i]);
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method creates a randomly generated card through a card code
    public static String cardCreation(){
        //this method creates a singular card
        //  number on the card (1,2,3,4,5,6,7,8,9,10 reverse (r), 11 swap(w), 12 plus 2 (p)), 13 skip (s), 14 three (t), 15 + random, 16 plus 4 (+), 17 color change (c)
        // 1 = red, 2 = yellow, 3 = green, 4 = blue

        //initialize variables
        String cardCode = "";
        String numberValue = "";
        String colorValue = "";
        int numberInteger;
        int colorInteger;

        //generates a random color
        colorInteger = (int)(5*Math.random()+1);

        if (colorInteger != 5) {
            //gen a random number
            numberInteger = (int)(15*Math.random()+1);
        }else {
            //does some random math to generate the color or +4 cards
            if ((int)(2*Math.random()+1) == 1){
                if ((int)(2*Math.random()+1) == 1){
                    numberInteger = 16;
                } else{
                    numberInteger = 17;
                }
            //generates random numbers
            } else{
                colorInteger = (int)(4*Math.random()+1);
                numberInteger = (int)(15*Math.random());
            }
        }
        //does some random math to make the odds smaller
        if (numberInteger >= 10 && numberInteger != 16 && numberInteger != 17){
            if ((int)(10*Math.random()+1) > 7){
                numberInteger = (int)(9*Math.random()+1);
            }
        }

        //reduces the amount of toThree cards because it's too OP
        if (numberInteger == 14 && ((int)(10*Math.random()+1) < 7)){
            numberInteger = (int)(9*Math.random()+1);
        }

        //determines and sets the number and letter values
        numberValue = Integer.toString(numberInteger);
        colorValue = Integer.toString(colorInteger);

        if (numberInteger == 10) {
            numberValue = "r";
        }else if (numberInteger == 11) {
            numberValue = "w";
        }else if (numberInteger == 12) {
            numberValue = "p";
        }else if (numberInteger == 13) {
            numberValue = "s";
        }else if (numberInteger == 14) {
            numberValue = "t";
        }else if (numberInteger == 15){
            numberValue = "?";
        }else if (numberInteger == 16){
            numberValue = "+";
        }else if (numberInteger == 17){
            numberValue = "c";
        }

        //returns the card code, which represents a card
        cardCode = colorValue + numberValue + " ";
        return cardCode;
    }

    //--------------------------------------------------------------------------------------------------------------
    //this method removes a card from the playerCards array for the current player
    public static void removeCard(String selectedColor, String selectedNumber, int selectedPlace) {
        int i = 0;
        int numberPlayed = 0;

        String newDeck = "";
        String playerDeck = playerCards[currentPlayer-1];

        //scans through the entire deck for the selected place. if it is, then it skips it and moves on
        while (i < (playerDeck.length()/3)) {
            if (i == selectedPlace && numberPlayed < 1) {
                i+=1;
                numberPlayed++;
            } else {
                newDeck = newDeck + playerDeck.charAt(i * 3) + playerDeck.charAt(i*3+1) + playerDeck.charAt(i*3+2);
                i+=1;
            }
        }
        playerCards[currentPlayer-1] = newDeck;
    }

    //--------------------------------------------------------------------------------------------------------------
    //gets the amount of cards
    public static int amount(){
        return (playerCards[currentPlayer-1].length())/3;
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method RENDERS THE CARDS BUTTONS: takes four parameters for the row amount, rotation level, card, and the type of generation
    public static void render(int rowAmount, int rotationLevel, JButton[] card, int type){
        String cardColor = "";
        String cardNumber = "";

        //creates fonts
        Font montserrat = fontCreator(MONTSERRAT_BLACK, 60f);
        Font arial = fontCreator("arial-unicode-ms", 60f);

        //repeats for the amount of cards in the row
        for (int i = 0; i < rowAmount; i++) {
            //TYPE == 0 means that the generation is the normal generation in the deck
            if (type == 0) {
                cardColor = Character.toString(playerCards[currentPlayer - 1].charAt((i * 3) + rotationLevel * 21));
                cardNumber = Character.toString(playerCards[currentPlayer - 1].charAt((i * 3 + 1) + rotationLevel * 21));
                //TYPE == 1 means that it is rendering the current card on the side
            } else if (type == 1) {
                if (!currentCard.equals("   ")) {
                    cardColor = Character.toString(currentCard.charAt(0));
                    cardNumber = Character.toString(currentCard.charAt(1));
                }
                //TYPE == 2 renders the card in the deck when drawn
            } else if (type == 2) {
                cardColor = Character.toString(Deck.drawnCard.charAt(0));
                cardNumber = Character.toString(Deck.drawnCard.charAt(1));
            }
            //sets the borders and fonts and olors
            card[i].setBorder(new LineBorder(Color.WHITE, 4));
            card[i].setFont(montserrat);
            card[i].setForeground(Color.decode("#7F7F7F"));

            //sets border and color and fonts and everything
            if (cardColor.equals("1") && type == 0) {
                card[i].setBackground(Color.decode("#5B3A3C"));
            } else if (cardColor.equals("2") && type == 0) {
                card[i].setBackground(Color.decode("#365D76"));
            } else if (cardColor.equals("3") && type == 0) {
                card[i].setBackground(Color.decode("#8A753C"));
            } else if (cardColor.equals("4") && type == 0) {
                card[i].setBackground(Color.decode("#546A32"));
            } else if (cardColor.equals("5") && type == 0) {
                card[i].setBackground(Color.decode("#232b2b"));
                //type == 1 means the current card render, type == 2 means the drawn card render
            } else if (cardColor.equals("1") && (type == 1 || type == 2)) {
                card[i].setBackground(Color.decode("#ff595e"));
            } else if (cardColor.equals("2") && (type == 1 || type == 2)) {
                card[i].setBackground(Color.decode("#1982c4"));
            } else if (cardColor.equals("3") && (type == 1 || type == 2)) {
                card[i].setBackground(Color.decode("#ffca3a"));
            } else if (cardColor.equals("4") && (type == 1 || type == 2)) {
                card[i].setBackground(Color.decode("#8ac926"));
            } else if (cardColor.equals("5") && (type == 1 || type == 2)) {
                card[i].setBackground(Color.decode("#232b2b"));
            }
            if (cardNumber.equals("r")) {
                card[i].setText("↻");
                card[i].setFont(new Font("Semoge UI", 1, 60));
            } else if (cardNumber.equals("w")) {
                card[i].setText("?");
                if (type == 1) {
                    card[i].setText("⇅");
                    card[i].setFont(new Font("Semoge UI", 1, 60));
                }
            } else if (cardNumber.equals("p")) {
                card[i].setText("+2");
            } else if (cardNumber.equals("s")) {
                card[i].setText("\uD83D\uDEAB");
                card[i].setFont(new Font("Semoge UI", 1, 60));
            } else if (cardNumber.equals("t")) {
                card[i].setText("?");
            } else if (cardNumber.equals("+")) {
                card[i].setText("+4");
            } else if (cardNumber.equals("c")) {
                card[i].setText("C");
            } else if (cardNumber.equals("?")) {
                card[i].setText("?");
                if (type == 1) {
                    card[i].setText("+RANDOM");
                    card[i].setFont(fontCreator(POPPINS_BOLD, 40f));
                }
            }else {
                card[i].setText(cardNumber);
            }
            //disables all cards until further notice
            card[i].setEnabled(false);

        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //checks if the cards are valid or not
    public static boolean playerDeckChecker(){
        boolean valid = false;
        //basically scans through the deck to see if a valid card exists and returns the boolean
        for (int i = 0; i < playerCards[currentPlayer-1].length()/3; i++){
            String cardColor;
            String cardNumber;
            cardColor = Character.toString(playerCards[currentPlayer-1].charAt(i*3));
            cardNumber = Character.toString(playerCards[currentPlayer-1].charAt(i*3+1));
            if (cardColor.equals(Character.toString(currentCard.charAt(0))) || cardNumber.equals(Character.toString(currentCard.charAt(1))) || currentCard.equals("   ") || cardColor.equals("5")){
                valid =  true;
            }
        }
        return valid;
    }
    //--------------------------------------------------------------------------------------------------------------
    //checks if the deck is valid or not
    public static boolean deckValidator(int rotationLevel, int i){
        boolean valid = false;
        String cardColor;
        String cardNumber;
        cardColor = Character.toString(playerCards[currentPlayer-1].charAt(i*3));
        cardNumber = Character.toString(playerCards[currentPlayer-1].charAt(i*3+1));
        if (Gameplay.currentEvent == false && skipValue == 0 && cardDebt == 0 && (cardColor.equals(Character.toString(currentCard.charAt(0))) || cardNumber.equals(Character.toString(currentCard.charAt(1))) || currentCard.equals("   ") || cardColor.equals("5"))){
            valid =  true;
        }else if (Gameplay.currentEvent == false && cardDebt != 0 && (cardNumber.equals("+") || cardNumber.equals("p") || (cardNumber.equals("r") && cardColor.equals(Character.toString(currentCard.charAt(0)))))){
            valid = true;
        }
        return valid;
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method creates a font with the font name and size
    public static Font fontCreator(String fontName, Float fontSize){
        Font font = null;
        try {GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //creates font to the file place
            font = Font.createFont(Font.TRUETYPE_FONT, new File(fontName)).deriveFont(fontSize);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontName)));
        } catch (IOException|FontFormatException e) {}
        //returns the font
        return font;


    }
    //--------------------------------------------------------------------------------------------------------------
    //this method makes all the cards to three for the special card
    public static void toThree(){
        String newDeck = "";

        //repeats and makes all cares to three
        for (int player = 0; player < playerAmount; player++){
            String playerDeck = playerCards[player];
            int counter = 0;
            //if the amount of cards is larger than three, then it takes three random places and just removes everything else
            if (playerCards[player].length()/3 > 3){
                while (counter < 3){
                    int randomNumber = (int)((playerCards[player].length()/3) * Math.random());
                    for (int i = 0; i < playerCards[player].length()/3; i++){
                        if (randomNumber == i){
                            newDeck = newDeck + playerDeck.charAt(i * 3) + playerDeck.charAt(i*3+1) + playerDeck.charAt(i*3+2);
                        }
                    }
                    counter++;
                }
            //generates missing cards if less than 3
            } else{
                newDeck = playerCards[player];
                for (int i = 0; i < (3-playerCards[player].length()/3); i++){
                    newDeck = newDeck + cardCreation();
                }
            }
            playerCards[player] = newDeck;
            newDeck = "";

        }
        //sets rotation level to 0
        Gameplay.rotationLevel = 0;
    }
    //--------------------------------------------------------------------------------------------------------------
    //sets the disabled button color to normal (NO DISABLED TINT)
    public static void setDisabledColor(JButton[] card){
        for (int i = 0; i < card.length; i++){
            card[i].setUI(new MetalButtonUI() {
                protected Color getDisabledTextColor() {
                    return Color.WHITE;
                }
            });
        }

    }

    //--------------------------------------------------------------------------------------------------------------
    //sorts all the cards by putting the cards into a new array
    public static String cardSorter(String currentDeck){
        int deckLength = currentDeck.length()/3;
        String[] deckArray = new String[deckLength];
        String card;
        //scans through the deck and puts the entire deck into a new array
        for (int i = 0; i < deckLength; i++){
            card = "";
            card = Character.toString(currentDeck.charAt(i*3)) + Character.toString(currentDeck.charAt(i*3+1)) + " ";
            deckArray[i] = card;
        }
        //sorts the new array so that it is in order
        Arrays.sort(deckArray);
        String newDeck = "";
        //puts the new array into a new deck string
        for (int i = 0; i < deckLength; i++){
            newDeck = newDeck + deckArray[i];
        }
        return newDeck;

    }

    //--------------------------------------------------------------------------------------------------------------
    //checks if an uno is present among us
    public static boolean unoChecker(){
        boolean value = false;
        //checks the amount of cards with one value
        for (int i = 0; i < Gameplay.playerAmount; i++){
            if (Card.playerCards[i].length()/3 == 1){
                value = true;
            }
        }
        //returns the amount of cards
        return value;
    }





}
