/* WinGame Class
- This class is called when a player wins the game
- creates the win panel
- creates the awards
- creates the buttons to go to the main menu
@author: Frank Ding
@date: January 25, 2023
 */

package mainGame;

//import java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.border.LineBorder;

import mainGame.storage.Sounds;
import static mainGame.storage.Fonts.POPPINS_BOLD;

public class WinGame extends MainUI{
    //initialize Java Swing and Timer objects
    static JPanel playerPanel, awardPanel;
    static JLabel winTitleLabel;

    static Timer timer;
    static Timer soundTimer;
    static JLabel[] winNameLabel;
    static JLabel[] winScoreLabel;
    static JButton rematchButton;
    static JButton menuButton;
    static JLabel[] awardTitleLabel = new JLabel[2];
    static JLabel[] awardNameLabel = new JLabel[2];

    //initialize variables
    static boolean buttonPressed = false; //represents if a button is active or not
    static long counter = 0;
    static long soundCounter = 0;
    static int highScoreValue = 0;
    static boolean scoreFinish = false;
    static final int AWARD_KISSER = 0;
    static final int AWARD_MEANIE = 1;
    private static MyMouseListener mouseListener = new MyMouseListener();

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the win game screen
    public static void winCreation(){
        buttonPressed = false;
        Sounds.winMusic();

        //creates new JLabel for the title
        winTitleLabel = new JLabel(GameState.playerNames[Gameplay.currentPlayer-1] + " WINS !!!!!!!!!!!!");
        winTitleLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        winTitleLabel.setForeground(Color.WHITE);
        winTitleLabel.setBounds(100, 30, 700, 100);
        gameWindow.add(winTitleLabel);

        //creates new JButton for the rematch button
        rematchButton = new JButton("Rematch");
        rematchButton.setBorder(new LineBorder(Color.WHITE, 3));
        rematchButton.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        rematchButton.addActionListener(new buttonSelect());
        rematchButton.setForeground(Color.WHITE);
        rematchButton.setBackground(Color.BLACK);
        rematchButton.setFocusPainted(false);
        rematchButton.setBounds(1100, 30, 300, 100);
        rematchButton.addMouseListener(mouseListener);
        gameWindow.add(rematchButton);

        //creates new button for the menu home button
        menuButton = new JButton("\uD83C\uDFE0");
        menuButton.setBorder(new LineBorder(Color.WHITE, 3));
        menuButton.setFont(new Font("Semgoe UI", 1, 30));
        menuButton.addActionListener(new buttonSelect());
        menuButton.setForeground(Color.WHITE);
        menuButton.setBackground(Color.BLACK);
        menuButton.setFocusPainted(false);
        menuButton.setBounds(1500, 30, 100, 100);
        menuButton.addMouseListener(mouseListener);
        gameWindow.add(menuButton);

        //calls the methods
        scoreCreation(); //creates the score
        awardsCreation(); //creates the award generation
        scoreCounterCreation(); //creates the score counter
        gameWindow.repaint(); //refreshes everything

    }
    //--------------------------------------------------------------------------------------------------------------
    //This class is called when a button is pressed
    static class buttonSelect implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //if the button that is pressed is the rematch button (and a current button isn't active)
            if (e.getSource() == rematchButton && !buttonPressed){
                Sounds.cardClick();
                //makes the current button active
                buttonPressed = true;
                //generates the mode select lobby screen
                GameStartGeneration.modeSelectCreation();
            //if the button that is pressed is the menuButton button (and a current button isn't active)
            } else if (e.getSource() == menuButton && !buttonPressed){
                Sounds.cardClick();
                //makes the current button active
                buttonPressed = true;
                //removes everything
                MainUI.remove();
                gameWindow.dispose();
                //generates the lobby screen
                Menu.menuCreation(false);

            }
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method is called to create the score
    public static void scoreCreation(){
        //creates new JPanel for the player names
        playerPanel = new JPanel();
        playerPanel.setLayout(new GridLayout(2, Gameplay.playerAmount));
        playerPanel.setBounds(50, 200, 1800, 300);
        playerPanel.setBackground(new Color(0f, 0f, 0f, .5f));
        gameWindow.add(playerPanel);

        //creates new JLabels to hold the names and scores
        winNameLabel = new JLabel[Gameplay.playerAmount];
        winScoreLabel = new JLabel[Gameplay.playerAmount];

        //cycles through to create the JLabels for the names
        for (int i = 0; i < Gameplay.playerAmount; i++){
            //creates the winNameLabels as the player names
            winNameLabel[i] = new JLabel(GameState.playerNames[i], SwingConstants.CENTER);
            winNameLabel[i].setFont(Card.fontCreator(POPPINS_BOLD, 50f));
            winNameLabel[i].setForeground(Color.WHITE);
            playerPanel.add(winNameLabel[i]);

            //if the current JLabel is the winner, then the name is set to yellow
            if (i == Gameplay.currentPlayer-1){
                winNameLabel[i].setForeground(Color.YELLOW);
            }

            //refreshes everything
            playerPanel.revalidate();
            playerPanel.validate();
            playerPanel.repaint();

        }

        //cycles through to create the JLabels for the score
        for (int i = 0; i < Gameplay.playerAmount; i++){
            winScoreLabel[i] = new JLabel(Integer.toString(GameState.playerScore[i]), SwingConstants.CENTER);
            winScoreLabel[i].setFont(Card.fontCreator(POPPINS_BOLD, 50f));
            winScoreLabel[i].setForeground(Color.WHITE);
            playerPanel.add(winScoreLabel[i]);
            playerPanel.revalidate();
            playerPanel.validate();
            playerPanel.repaint();
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method creates the awards
    public static void awardsCreation(){
        //creates new JPanel to hold the awards
        awardPanel = new JPanel();
        awardPanel.setLayout(new GridLayout(2, 2));
        awardPanel.setBounds(50, 550, 1800, 350);
        awardPanel.setBackground(new Color(0f, 0f, 0f, .5f));
        gameWindow.add(awardPanel);

        //creates the two award titles
        for (int i = 0; i < 2; i++){
            awardTitleLabel[i] = new JLabel("",SwingConstants.CENTER);
            awardTitleLabel[i].setForeground(Color.WHITE);
            awardTitleLabel[i].setFont(Card.fontCreator(POPPINS_BOLD, 50f));
            //card french kisser means the person with the most cards
            if (i == AWARD_KISSER){
                awardTitleLabel[i].setText("CARD FRENCH KISSER");
            //means the person who plays the most skips, +2, and +4 cards
            } else if (i == AWARD_MEANIE){
                awardTitleLabel[i].setText("MEANEST PLAYER >:(");
            }
            awardPanel.add(awardTitleLabel[i]);
        }

        //cycles through the awards for each award
        for (int i = 0; i < 2; i++){
            int mostCards = 0;
            String mostCardsName = "";

            int mostAttack = 0;
            String mostAttackName = "";

            //creates new JLabel array to hold the names of the award people
            awardNameLabel[i] = new JLabel("",SwingConstants.CENTER);
            awardNameLabel[i].setForeground(Color.WHITE);
            awardNameLabel[i].setFont(Card.fontCreator(POPPINS_BOLD, 50f));
            awardPanel.add(awardNameLabel[i]);

            //if the current award is the kisser
            if (i == AWARD_KISSER){
                //cycles through to see who has the most cards
                for (int x = 0; x < Gameplay.playerAmount; x++){
                    //finds the most cards and saves the name
                    if (Card.playerCards[x].length() >= mostCards){
                        mostCards = Card.playerCards[x].length();
                        mostCardsName = GameState.playerNames[x];
                    }
                }
                if (mostCards == 1){
                    //sets the text to the player with the most cards
                    awardNameLabel[i].setText(mostCardsName + " WITH " + mostCards + " CARD!");
                } else{
                    //sets the text to the player with the most cards
                    awardNameLabel[i].setText(mostCardsName + " WITH " + mostCards + " CARDS!");
                }
            //if the current award is the meanie award
            } else if (i == AWARD_MEANIE){
                //finds the player with the highest attack counter
                for (int x = 0; x < Gameplay.playerAmount; x++){
                    //if the attack counter is larger than the most attacks, then it records it
                    if (GameState.playerAttackCounter[x] >= mostAttack){
                        mostAttack = GameState.playerAttackCounter[x];
                        mostAttackName = GameState.playerNames[x];
                    }
                }
                //if nobody attacked
                if (mostAttack == 0){
                    awardNameLabel[i].setText("Nobody was mean this time :D");
                } else if (mostAttack == 1){
                    //sets the text to the player with the most attacks
                    awardNameLabel[i].setText((mostAttackName) + " WITH " + mostAttack + " ATTACK!");
                } else{
                    //sets the text to the player with the most attacks
                    awardNameLabel[i].setText((mostAttackName) + " WITH " + mostAttack + " ATTACKS!");
                }

            }

        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //This method counts the score
    public static void scoreCounterCreation(){
        //reset variables
        scoreFinish = false;
        counter = 0;
        highScoreValue = 0;
        //finds the winning player
        for (int i = 0; i < Gameplay.playerAmount; i++){
            //if it is the winning player
            if (i == Gameplay.currentPlayer-1){
                //sets the highs core value
                highScoreValue = i;

                //creates new Timer object to time the time for the score
                timer = new Timer(0, new scoreTimer());
                timer.restart();
                timer.start();

                //creates new Timer object to play the sound for the score
                soundTimer = new Timer(70, new soundTimer());
                soundTimer.restart();
                soundTimer.start();
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //This class increases the score really quickly every MS for the score animation
    static class scoreTimer implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            counter+=3;
            winScoreLabel[highScoreValue].setText(Long.toString(counter));
            if (counter >= GameState.playerScore[Gameplay.currentPlayer-1]){
                winScoreLabel[highScoreValue].setText(Integer.toString(GameState.playerScore[Gameplay.currentPlayer-1]));
                timer.stop();
                scoreFinish = true;
            }
            gameWindow.repaint();
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //This class plays a sound every 70 ms
    static class soundTimer implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            soundCounter+=3;
            Sounds.scorePlus();
            if (scoreFinish == true){
                soundTimer.stop();
            }
        }
    }


    //--------------------------------------------------------------------------------------------------------------
    //Mouse listener class for the hovers and mouse exits for buttons
    static class MyMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
        }
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        }
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
        }
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            //if a mouse enters an object with mouseListener added, then it plays the cardClick sound from the Sounds class
            Sounds.cardSelect();
            //if the hover button is rematchButton
            if (e.getSource() == rematchButton) {
                //sets the border extra thick
                rematchButton.setBorder(new LineBorder(Color.WHITE, 8));
                //if the hover button is menuButton
            } else if (e.getSource() == menuButton) {
                //sets the border thick
                menuButton.setBorder(new LineBorder(Color.WHITE, 8));
            }
            //refreshes everything
            gameWindow.repaint();
        }
        public void mouseExited(MouseEvent e){
            //if the exited button is rematchButton
            if (e.getSource() == rematchButton) {
                //sets border thin
                rematchButton.setBorder(new LineBorder(Color.WHITE, 3));
                //if the exited is menuButton
            } else if (e.getSource() == menuButton) {
                //sets border thin
                menuButton.setBorder(new LineBorder(Color.WHITE, 3));
            }
            //refreshes everything
            gameWindow.repaint();
        }
    }
}
