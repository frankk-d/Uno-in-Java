/* GameState Class
- This class holds all the game related variables
- updates game related JLabels
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame;

//import Java packages
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import static mainGame.storage.Fonts.*;

public class GameState extends Gameplay{
    //initialize Java Swing objects
    static JPanel playerCardsNamePanel;
    static JLabel[] playerCardsNameLabel;
    static JPanel playerCardsPanel;
    static JLabel[] playerCardsLabel;

    //initialize holding variables
    static String[] playerNames = new String[playerAmount];
    static int[] playerScore = new int[playerAmount];
    static int[] playerAttackCounter = new int[playerAmount];

    //--------------------------------------------------------------------------------------------------------------
    //This method updates the current player label to the current players turn
    public static void updatePlayer(int currentPlayer){
        currentPlayerLabel.setText(playerNames[currentPlayer-1] + "'s Turn:");

    }

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the player card name panel (tells the player the name)
    public static void playerCardsCreation(){
        //create new JPanel for the playerCardsName
        playerCardsNamePanel = new JPanel();
        playerCardsNamePanel.setLayout(new GridLayout(1, Gameplay.playerAmount));
        playerCardsNamePanel.setBounds(550, 20, 420, 60);
        playerCardsNamePanel.setBackground(Color.WHITE);
        playerCardsNamePanel.setBorder(new LineBorder(Color.BLACK,1 ));
        titlePanel.add(playerCardsNamePanel);

        //creates JLabel for the names of the players
        playerCardsNameLabel = new JLabel[Gameplay.playerAmount];
        //repeats for the amount of players in the game (basically scans through to get the name and put it on the label)
        for (int i = 0; i<Gameplay.playerAmount; i++){
            playerCardsNameLabel[i] = new JLabel(GameState.playerNames[i], SwingConstants.CENTER);
            playerCardsNameLabel[i].setFont(Card.fontCreator(POPPINS_BOLD, 20f));
            playerCardsNameLabel[i].setForeground(Color.BLACK);
            //if the current player, then set the font to red
            if (i == currentPlayer-1){
                playerCardsNameLabel[i].setForeground(Color.RED);
            }
            playerCardsNamePanel.add(playerCardsNameLabel[i]);
        }

        //create new JPanel object
        playerCardsPanel = new JPanel();
        playerCardsPanel.setLayout(new GridLayout(1, Gameplay.playerAmount));
        playerCardsPanel.setBounds(550, 90, 420, 60);
        playerCardsPanel.setBackground(Color.WHITE);
        playerCardsPanel.setBorder(new LineBorder(Color.BLACK,1 ));
        titlePanel.add(playerCardsPanel);

        //creates JLabel for the amount of cards per player
        playerCardsLabel = new JLabel[Gameplay.playerAmount];
        //scans through each player to display the card
        for (int i = 0; i<Gameplay.playerAmount; i++){
            playerCardsLabel[i] = new JLabel(Integer.toString(Card.playerCards[i].length()/3), SwingConstants.CENTER);
            playerCardsLabel[i].setFont(Card.fontCreator(POPPINS_BOLD, 20f));
            playerCardsPanel.add(playerCardsLabel[i]);
            playerCardsLabel[i].paintImmediately( playerCardsLabel[i].getVisibleRect());
            playerCardsLabel[i].setForeground(Color.BLACK);
            //if the current player, then set the font to red
            if (i == currentPlayer-1){
                playerCardsLabel[i].setForeground(Color.RED);
            }

        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method updates the player card amount
    public static void playerCardAmountUpdater(){
        //loops through every player and updates the text to the amount of cards in the deck
        for (int i = 0; i<Gameplay.playerAmount; i++){
            playerCardsLabel[i].setText(Integer.toString(Card.playerCards[i].length()/3));
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method updates the rotation (current roation value)
    public static void rotationLabelUpdater(int values){
        rotationLabel.setText(Integer.toString(rotationLevel+1) + "/" + values);

    }

    //--------------------------------------------------------------------------------------------------------------
    //This method updates all the game text
    public static void gameTextUpdater(String event){
        //sets te font to montserrat black
        gameUpdatesLabel.setFont(Card.fontCreator(MONTSERRAT_BLACK, 60f));
        //if reverse
        if (event.equals("r")){
            gameUpdatesLabel.setText("REVERSE!");
        //if swap
        } else if (event.equals("w")){
            gameUpdatesLabel.setFont(Card.fontCreator(MONTSERRAT_BLACK, 35f));
            gameUpdatesLabel.setText("SWAP DECKS WITH NEXT PLAYER");
        //if plus two
        } else if (event.equals("p")){
            if (Card.cardDebt > 0){
                gameUpdatesLabel.setText("+" + Integer.toString(Card.cardDebt));
            } else{
                gameUpdatesLabel.setText("");
            }
        //if skip
        } else if (event.equals("s")){
            gameUpdatesLabel.setText("SKIP!");
        //if to three
        } else if (event.equals("t")){
            gameUpdatesLabel.setFont(Card.fontCreator(MONTSERRAT_BLACK, 55f));
            gameUpdatesLabel.setText("ALL PLAYERS 3 CARDS");
        //if plus 4
        } else if (event.equals("+")){
            if (Card.cardDebt > 0){
                gameUpdatesLabel.setText("+" + Integer.toString(Card.cardDebt));
            } else{
                gameUpdatesLabel.setText("");
            }
        //if Color change
        } else if (event.equals("c")) {
            gameUpdatesLabel.setText("COLOR CHANGE!");
        //if plus random
        } else if (event.equals("?")) {
            gameUpdatesLabel.setText("+" + SpecialEvent.plusRandom);
        //if theres an uno
        } else if (event.equals("uno")){
            gameUpdatesLabel.setFont(Card.fontCreator(MONTSERRAT_BLACK, 30f));
            gameUpdatesLabel.setText("NOT WITHIN 2 SECONDS! DRAW 2 CARDS!");
        }
        gameUpdatesPanel.repaint();

    }
}
