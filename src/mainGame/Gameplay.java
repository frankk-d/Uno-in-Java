/* Gameplay Class
- In charge of the main gameplay loop.
@author: Frank Ding
@date: January 25, 2023
 */


package mainGame;

//import java packages
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import mainGame.storage.*;

import static mainGame.storage.Fonts.*;

public class Gameplay extends MainUI{
    //initialize Java Swing Objects
    static JButton[] cardButtons;
    static JButton endTurnButton;
    static JButton leftButton;
    static JButton rightButton;
    private static MyMouseListener mouseListener = new MyMouseListener();

    //initialize all variables
    static boolean validInput = false;
    static int currentPlayer = 1;
    static int playerAmount;
    static boolean reverseMode = false;
    static int skipValue = 0;
    static boolean currentEvent = false;
    static int rotationLevel = 0;
    static int rowAmount;
    static final int NORMAL_MODE = 0;

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the cards at the bottom to be selected
    public static void selectionCreation(int rotationLevel) {
        //determine the card amount and the amount of full and extra rows (and extra values in those rows)
        int cardAmount = Card.amount();
        int fullRows = cardAmount / 7;
        int extraValues = cardAmount % 7;
        int totalRows;
        rowAmount = 0;
        //if the rotation level is less than the amount of full rows, then that means the amount of cards in the rotation level is maxed out
        if (rotationLevel <= (fullRows-1)) {
            rowAmount = 7;
        //if the rotation level is larger than the amount of full rows, then that means that the amount of cards in the rotation level are the extras
        } else if (rotationLevel > fullRows-1){
            rowAmount = extraValues;
        }
        //finds the total rows
        if (extraValues != 0){
            totalRows = fullRows + 1;
        } else{
            totalRows = fullRows;
        }

        //sorts the the cards of all the players
        for (int i = 0; i < playerAmount; i++){
            Card.playerCards[i] = Card.cardSorter(Card.playerCards[i]);
        }

        //sets the boundaries of the selection panel and sets it to grid layout
        selectionPanel.setBounds(40, 700, rowAmount * 140, 200);
        cardButtons = new JButton[rowAmount];
        selectionPanel.setLayout(new GridLayout(1, rowAmount));
        //creates each individual card button for the cards and adds it to the selection panel
        for (int i = 0; i <= rowAmount - 1; i++) {
            cardButtons[i] = new JButton("");
            cardButtons[i].addActionListener(new Gameplay.cardSelect());
            cardButtons[i].addMouseListener(mouseListener);
            cardButtons[i].setFocusPainted(false);
            MainUI.selectionPanel.add(cardButtons[i]);
        }
        //renders each individual card button
        Card.render(rowAmount, rotationLevel, cardButtons, 0);

        //makes all the valid cards enabled
        cardValidator(NORMAL_MODE);

        //updates the amount of player cards in the upper update panel
        GameState.playerCardAmountUpdater();

        //gets the current number value
        String currentNumber = Character.toString(Card.currentCard.charAt(1));

        //resets the game updates label
        if (Card.cardDebt == 0 && skipValue == 0 && (currentNumber.equals("0") || currentNumber.equals("1") || currentNumber.equals("2") || currentNumber.equals("3") || currentNumber.equals("4") || currentNumber.equals("5") || currentNumber.equals("6") || currentNumber.equals("7")) || currentNumber.equals("8") || currentNumber.equals("9")){
            gameUpdatesLabel.setText("");
        }
        //updates the rotation label at the bottom
        GameState.rotationLabelUpdater(totalRows);

        //refreshes everything
        gameWindow.validate();
        gameWindow.repaint();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the deck to be created
    public static void deckCreation(){
        Deck.creation();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the end turn button creation
    public static void endTurnButtonCreation(){
        //creates the end turn button
        endTurnButton = new JButton("END TURN");
        endTurnButton.setFont(Card.fontCreator(POPPINS_BOLD, 30f));
        endTurnButton.setFocusPainted(false);
        endTurnButton.addActionListener(new Gameplay.endTurnSelect());
        endTurnButton.addMouseListener(mouseListener);
        endTurnButton.setBounds(600, 0, 200, 100);
        endTurnButton.setForeground(Color.WHITE);
        endTurnButton.setBorder(new LineBorder(Color.WHITE, 4));
        endTurnButton.setBackground(Color.decode("#5e514d"));
        utilityPanel.add(endTurnButton);
        //makes the current event to true so no other things can be pressed
        currentEvent = true;
        //refreshes the selection creation
        selectionRemove();
        selectionCreation(rotationLevel);
        //refreshes the deck
        Deck.removal();
        Deck.creation();
        Dan.unoDraw = false;
        gameWindow.repaint();
    }

    //--------------------------------------------------------------------------------------------------------------
    //this class is called when the endTurnButton is pressed
    static class endTurnSelect implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            //checks if the current card is not 5
            if (Character.toString(Card.currentCard.charAt(0)).equals("5") == false){
                //ends the turn and changes all events to false
                Sounds.endTurn();
                currentEvent = false;
                //switches the current player
                switchPlayer(true);
                //removes the button
                utilityPanel.remove(endTurnButton);
                endTurnButton.setVisible(false);
                utilityPanel.repaint();
            }
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //this method basically creates the left and right buttons
    public static void rotationCreation(){
        leftButton = new JButton("<");
        rightButton = new JButton(">");
        leftButton.addActionListener(new Gameplay.rotationSelect());
        leftButton.setFocusPainted(false);
        rightButton.addActionListener(new Gameplay.rotationSelect());
        rightButton.setFocusPainted(false);
        leftButton.setBackground(Color.BLACK);
        rightButton.setBackground(Color.BLACK);
        leftButton.setForeground(Color.WHITE);
        rightButton.setForeground(Color.WHITE);
        leftButton.setBorder(new LineBorder(Color.WHITE, 6));
        rightButton.setBorder(new LineBorder(Color.WHITE, 6));
        leftButton.setFont(Card.fontCreator(ARIAL_UNICODE_MS, 60f));
        rightButton.setFont(Card.fontCreator(ARIAL_UNICODE_MS, 60f));
        utilityPanel.add(leftButton);
        utilityPanel.add(rightButton);
        leftButton.setBounds(60, 0, 100, 100);
        rightButton.setBounds(170, 0, 100, 100);
        leftButton.addMouseListener(mouseListener);
        rightButton.addMouseListener(mouseListener);
    }
    //-------------------------------------------------------------------------------------------------------------
    //This class
    static class rotationSelect implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            Sounds.cardClick();
            int cardAmount = Card.amount();
            //calculates the full rows, extra rows, and total
            int fullRows = cardAmount / 7;
            int extraValues = cardAmount % 7;
            int totalRows;
            //calculates the total rows to rotate to
            if (extraValues != 0){
                totalRows = fullRows + 1;
            } else{
                totalRows = fullRows;
            }
            //if left button is pressed, then decreases a rotation level, same right button but reverse
            if (event.getSource() == leftButton){
                if (rotationLevel > 0){
                    rotationLevel--;
                    selectionRemove();
                    selectionCreation(rotationLevel);
                }

            } else if (event.getSource() == rightButton){
                //stops at the max of the total rows
                if (cardAmount > 7 && rotationLevel < totalRows-1) {
                    rotationLevel++;
                    selectionRemove();
                    selectionCreation(rotationLevel);
                }
            }

        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //this class is called when a card is selected
    static class cardSelect implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            //scans through each cardButton to see which one was pressed
            for (int i = 0; i < cardButtons.length; i++) {
                //if the card button that was pressed is found
                if (event.getSource() == cardButtons[i]) {
                    //determine if the card is valid by calling the cardSelection method
                    validInput = Card.cardSelection(i + (rotationLevel*7));

                    //if the input is valid and the amount of cards is not equal to 1
                    if (validInput == true && Card.playerCards[currentPlayer-1].length()/3 != 1){
                        //if stress music is playing and there is not uno
                        if (Sounds.stressMusic == true && !Card.unoChecker()){
                            //then play normal background music
                            Sounds.backgroundMusic(0);
                        //if there is a uno and no music is playing
                        }else if (Card.unoChecker() && Sounds.stressMusic == false){
                            //then play the stress music
                            Sounds.stress();
                        }
                        //refresh the selection and end the turn
                        selectionRemove();
                        selectionCreation(rotationLevel);
                        Gameplay.endTurnButtonCreation();

                    //if the input is valid and the amount of cards is EQUAL TO 1
                    } else if (validInput == true  && Card.playerCards[currentPlayer-1].length()/3 == 1){
                        //play the stress music
                        if (Card.unoChecker() && Sounds.stressMusic == false){
                            Sounds.stress();
                        }
                        //refresh everything
                        currentEvent = true;
                        selectionRemove();
                        selectionCreation(rotationLevel);
                        //INITIATE THE UNO in the Dan class
                        Dan.unoButtonCreation();
                    }
                }
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //removes all the cards in the selectionPanel
    public static void selectionRemove() {
        selectionPanel.removeAll();
        selectionPanel.repaint();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method switches the players (part 1)
    public static void switchPlayer(boolean validInput) {
        //if the input is valid and the current card amount is larger than 0 (game didnt end)
        if (validInput == true && Card.playerCards[currentPlayer-1].length()/3 > 0) {
            //if the mode isn't reverse mode
            if (reverseMode == false) {
                //switches the player accordingly
                if (currentPlayer < playerAmount) {
                    currentPlayer++;
                }else if (currentPlayer == playerAmount) {
                    currentPlayer = 1;
                }
            //if the mode is reverse mode
            } else if (reverseMode == true) {
                //switches the player counterclockwise
                if (currentPlayer > 1) {
                    currentPlayer--;
                }else if (currentPlayer == 1) {
                    currentPlayer = playerAmount;
                }
            }
            //disables the deck and refreshes the selection
            Deck.deck.setEnabled(false);
            rotationRemove();
            selectionRemove();

            //creates the transition screen in the PlayerTransition class
            PlayerTransition.transitionCreation(currentPlayer);

        //basically if a player wins (cards = 0), then the UI gets removed and the WinGame class is called to start the winCreation
        } else if (validInput == true && Card.playerCards[currentPlayer-1].length()/3 == 0){
            MainUI.remove();
            GameState.playerScore[Gameplay.currentPlayer-1] += 1000;
            WinGame.winCreation();
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //removes the rotation buttons
    public static void rotationRemove(){
        utilityPanel.remove(leftButton);
        utilityPanel.remove(rightButton);
        utilityPanel.repaint();
    }

    //--------------------------------------------------------------------------------------------------------------
    //initiates the second part of the player switching after a new player's turn starts
    public static void switchPlayerTwo(){
        //resets the variables and labels
        rotationLevel = 0;
        rotationCreation();
        hoverLabel.setText("");

        //refreshes the cards to match the new player
        selectionRemove();
        selectionCreation(rotationLevel);

        //refreshes the deck to enabled
        Deck.drawn = 0;
        Deck.deck.setEnabled(true);
        Deck.drawNeed = false;
        Deck.removal();
        Deck.creation();

        //updates the player to the current player
        GameState.updatePlayer(currentPlayer);

        //if the player is skipped, then end the turn
         if (skipValue == 1){
             skipValue = 0;
             endTurnButtonCreation();
         }
         //if the amount of cards the player has is 0, then end the turn
         if (Card.playerCards[currentPlayer-1].length() == 0){
             endTurnButtonCreation();
         }
         //refresh
        gameWindow.repaint();
    }
    //--------------------------------------------------------------------------------------------------------------
    //determines if a card is valid or not so that it can be enabled
    public static void cardValidator(int mode){
        //if the current mode is 0 (for regular card generations)
        if (mode == NORMAL_MODE){
            //scans through teach row and checks if the card is valid or not
            for (int x = 0; x < rowAmount; x++){
                boolean valid = false;
                valid = Card.deckValidator(rotationLevel, x+(rotationLevel*7));
                //if the card is valid, then make the card a higher colour, and make the card enabled to be clicked
                if (valid == true){
                    int buttonValue = (x + (rotationLevel*7)) - (rotationLevel*7);
                    String cardColor = Character.toString(Card.playerCards[currentPlayer - 1].charAt((x * 3) + rotationLevel * 21));
                    String cardNumber = Character.toString(Card.playerCards[currentPlayer - 1].charAt((x * 3 + 1) + rotationLevel * 21));
                    cardButtons[buttonValue].setForeground(Color.WHITE);
                    cardButtons[buttonValue].setEnabled(true);
                    if (cardColor.equals("1")){
                        cardButtons[buttonValue].setBackground(Color.decode("#FF595E"));
                    } else if (cardColor.equals("2")){
                        cardButtons[buttonValue].setBackground(Color.decode("#1982C4"));
                    } else if (cardColor.equals("3")){
                        cardButtons[buttonValue].setBackground(Color.decode("#FFCA3A"));
                    } else if (cardColor.equals("4")){
                        cardButtons[buttonValue].setBackground(Color.decode("#8AC926"));
                    }
                }
            }
        //if the mode is 1 (for the card generations of the deck)
        } else if (mode == 1){
            //if there is no current event, then the deck is enabled
            if (currentEvent == false){
                Deck.deck.setEnabled(true);
            }
        }

    }

    //--------------------------------------------------------------------------------------------------------------
    //calculates the row amount
    public static int getRowAmount(int rotationLevel){
        int cardAmount = Card.amount();
        int fullRows = cardAmount / 7;
        int extraValues = cardAmount % 7;
        rowAmount = 0;
        //if the rotation level is less than the amount of full rows, then the row amount is 7
        if (rotationLevel <= (fullRows-1)) {
            rowAmount = 7;
        //if the rotation level is larger, then the extra values is the amount in the row
        } else if (rotationLevel > fullRows-1){
            rowAmount = extraValues;
        }
        return rowAmount;
    }

    //--------------------------------------------------------------------------------------------------------------
    //this class basically responds to all the mouse inputs
    static class MyMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
        }
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        }
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
        }
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            //  number on the card (1,2,3,4,5,6,7,8,9,reverse (r), swap(w),plus 2 (p)), skip (s), three (t), plus 4 (+), color change (c)
            // 1 = red, 2 = yellow, 3 = green, 4 = blue
            //gets the row amount
            rowAmount = getRowAmount(rotationLevel);
            //scans through each card in the row
            for (int i = 0; i < rowAmount; i++){
                //if the selected card is the card button, then hover tooltip label and the border size of the button
                if (e.getSource() == cardButtons[i]){
                    Sounds.cardSelect();
                    cardButtons[i].setBorder(new LineBorder(Color.WHITE, 8));
                    String hoverColor = Character.toString(Card.playerCards[currentPlayer - 1].charAt((i * 3) + rotationLevel * 21));
                    String hoverNumber = Character.toString(Card.playerCards[currentPlayer - 1].charAt((i * 3 + 1) + rotationLevel * 21));

                    hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
                    //if the mouse hovers over reverse
                    if (hoverNumber.equals("r")){
                        hoverLabel.setText("Reverses the direction of the game");
                        hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 35f));
                    //swap
                    } else if (hoverNumber.equals("w")){
                        hoverLabel.setText("Mystery card!");
                   // plus 2
                    } else if (hoverNumber.equals("p")){
                        hoverLabel.setText("Makes the next player draw 2");
                        hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 40f));
                    //skip
                    } else if (hoverNumber.equals("s")){
                        hoverLabel.setText("Skips the next player");
                    //to three cards
                    } else if (hoverNumber.equals("t")){
                        hoverLabel.setText("Mystery card!");
                    //adds 4
                    } else if (hoverNumber.equals("+")){
                        hoverLabel.setText("Change Color and next player draws 4");
                        hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 35f));
                    //change color
                    } else if (hoverNumber.equals("c")) {
                        hoverLabel.setText("Change Color");
                    //plus random
                    } else if (hoverNumber.equals("?")){
                        hoverLabel.setText("Mystery card!");
                    //a number card
                    } else{
                        hoverLabel.setText("Number Card");
                    }
                    hoverPanel.repaint();
                }
            }

            //if the selected is the left button
             if (e.getSource() == leftButton){
                 leftButton.setBorder(new LineBorder(Color.WHITE, 10));
                hoverLabel.setText("Click to scroll deck to the left");
                hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 40f));

             //if the selected is the right button
            } else if (e.getSource() == rightButton){
                 rightButton.setBorder(new LineBorder(Color.WHITE, 10));
                hoverLabel.setText("Click to scroll deck to the right");
                hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 40f));

            //if the selected is the endTurnButton
            } else if (e.getSource() == endTurnButton){
                 Sounds.cardSelect();
                endTurnButton.setBorder(new LineBorder(Color.WHITE, 8));
                utilityPanel.repaint();
            }

        }

        //This method resets all the changes back to default
        public void mouseExited(MouseEvent e){    // MUST be implemented even if not used!
            rowAmount = getRowAmount(rotationLevel);
            for (int i = 0; i < rowAmount; i++){
                if (e.getSource() == cardButtons[i]){
                    String hoverNumber = Character.toString(Card.playerCards[currentPlayer-1].charAt((((i+rotationLevel*7)*3)+1)));
                    cardButtons[i].setBorder(new LineBorder(Color.WHITE, 4));
                    if (hoverNumber.equals("r")){
                        hoverLabel.setText("");
                    } else if (hoverNumber.equals("w")){
                        hoverLabel.setText("");
                    } else if (hoverNumber.equals("p")){
                        hoverLabel.setText("");
                    } else if (hoverNumber.equals("s")){
                        hoverLabel.setText("");
                    } else if (hoverNumber.equals("t")){
                        hoverLabel.setText("");
                    } else if (hoverNumber.equals("+")){
                        hoverLabel.setText("");
                    } else if (hoverNumber.equals("c")){
                        hoverLabel.setText("");
                    } else{
                        hoverLabel.setText("");
                    }
                    hoverPanel.repaint();


                }
            }
            if (e.getSource() == leftButton){
                leftButton.setBorder(new LineBorder(Color.WHITE, 6));
                hoverLabel.setText("");
            } else if (e.getSource() == rightButton){
                rightButton.setBorder(new LineBorder(Color.WHITE, 6));
                hoverLabel.setText("");
            } else if (e.getSource() == endTurnButton){
                endTurnButton.setBorder(new LineBorder(Color.WHITE, 4));
                utilityPanel.repaint();
            }
        }
    } // MyMouseListener class end


}
