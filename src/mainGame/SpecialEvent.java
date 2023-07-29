/* SpecialEvent Class
- This class is for the special event creation in the games
- it manages what each special event does
@author: Frank Ding
@date: January 25, 2023
 */

package mainGame;

//import packages
import mainGame.storage.Sounds;
import static mainGame.Gameplay.*;

public class SpecialEvent extends Card{
    //initialize variables
    public static int plusRandom;

    //--------------------------------------------------------------------------------------------------------------
    //This method checks if an event is true. If an event is true, it runs the event
    public static void eventChecker(String selectedColor, String selectedNumber){
        //initializes boolean special event to represent if there is a special event or not
        boolean specialEvent = false;
        //checks if a special event is declared
        if (selectedNumber.equals("r") || selectedNumber.equals("w") || selectedNumber.equals("p")|| selectedNumber.equals("s")|| selectedNumber.equals("t") || selectedNumber.equals("+") || selectedNumber.equals("c") || selectedNumber.equals("?")) {
            specialEvent = true;
        } else{
            Sounds.cardClick();
        }
        //starts a special event
        if (specialEvent == true) {
            event(selectedColor, selectedNumber);
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method runs and executes the event
    public static void event(String selectedColor, String selectedNumber) {
        // 1 = red, 2 = yellow, 3 = green, 4 = blue

        //uno reverse card
        if (selectedNumber.equals("r")) {
            Sounds.reverse();
            //if reverse mode isn't active, then reverse it
            if (reverseMode == false) {
                reverseMode = true;
            //if reverse mode is active, then put it back to normal
            } else if (reverseMode == true) {
                reverseMode = false;
            }
        //swap cards w the player next to you
        } else if (selectedNumber.equals("w")) {
            Sounds.swap();
            String playerOneDeck;
            String playerTwoDeck;
            //if reverse mode isn't active
            if (!reverseMode){
                //player one deck is equal to the current player
                playerOneDeck = playerCards[currentPlayer-1];
                //if the current player isn't the last player in the array
                if (currentPlayer < playerAmount){
                    //then the two decks swap
                    playerTwoDeck = playerCards[currentPlayer];
                    playerCards[currentPlayer] = playerOneDeck;
                    playerCards[currentPlayer-1] = playerTwoDeck;
                //if the current player IS the last player in the array
                } else{
                    //then the current deck gets swapped with the first deck
                    playerTwoDeck = playerCards[0];
                    playerCards[0] = playerOneDeck;
                    playerCards[currentPlayer-1] = playerTwoDeck;
                }
            //if reverse mode is active
            } else{
                //player one deck is equal to the current player
                playerOneDeck = playerCards[currentPlayer-1];
                //if the current player isn't the first player
                if (currentPlayer > 1){
                    //then swap decks with the next player in the array (backwards)
                    playerTwoDeck = playerCards[currentPlayer-2];
                    playerCards[currentPlayer-2] = playerOneDeck;
                    playerCards[currentPlayer-1] = playerTwoDeck;
                //if the current player IS the first person in the array
                } else{
                    //then swap decks with the last person in the deck
                    playerTwoDeck = playerCards[playerAmount-1];
                    playerCards[playerAmount-1] = playerOneDeck;
                    playerCards[currentPlayer-1] = playerTwoDeck;
                }
            }
            rotationLevel = 0;
        //plus 2 for the player next to you
        } else if (selectedNumber.equals("p")){
            //adds to the current player's attack counter
            GameState.playerAttackCounter[currentPlayer-1] += 1;
            //adds to the card debt
            cardDebt += 2;
            //if the amount of cards to draw is less than 4
            if (cardDebt < 4){
                //then play the plus two sound
                Sounds.plusTwo();
            //if the amount of cards to draw is larger than 4
            } else{
                //then play the plus four sound
                Sounds.plusFour();
            }
        //skip the player next to you's turn
        } else if (selectedNumber.equals("s")){
            Sounds.skipPlayer();
            //adds an attack
            GameState.playerAttackCounter[currentPlayer-1] += 1;
            //sets the skip value to true
            skipValue = 1;
        //to three
        } else if (selectedNumber.equals("t")){
            Sounds.toThree();
            //calls the toThree method to make all the cards to three cards exactly
            Card.toThree();
            rotationLevel = 0;
            //refreshes the deck
            selectionRemove();
            selectionCreation(0);
        //+4
        } else if (selectedNumber.equals("+")){
            //adds an attack
            GameState.playerAttackCounter[currentPlayer-1] += 1;
            //adds to the card debt
            cardDebt +=4;
            //if the card debt is less than 4, then play different sounds
            if (cardDebt < 4){
                Sounds.plusTwo();
            } else{
                Sounds.plusFour();
            }
        //color change (theres another Class dedicated to this (WildCard))
        } else if (selectedNumber.equals("c")){
            Sounds.cardClick();
        //+RANDOM
        } else if (selectedNumber.equals("?")) {
            //theres a 1/100 chance of a random mystery card being a +99
            plusRandom = (int)(100*Math.random()+1);
            if (plusRandom == 99){
                plusRandom = 99;
            //picks a random number to draw from 1 to 7
            } else{
                plusRandom = (int)(7*Math.random()+1);
            }
            //adds an attack
            GameState.playerAttackCounter[currentPlayer - 1] += 1;

            //adds to the card debt
            cardDebt += plusRandom;

            //if the card debt is less than 4, then play different sounds
            if (cardDebt < 4) {
                Sounds.plusTwo();
            } else {
                Sounds.plusFour();
            }
        }
        //updates the game text
        GameState.gameTextUpdater(selectedNumber);
    }

}
