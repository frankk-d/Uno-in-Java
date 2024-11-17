/* GameStartGeneration Class
- creates the main lobby to START the Game Generation
- initializes the game generation so it controls how many players, how many cards, etc..
- once it finishes, it starts the game by calling the gameCreation method, which creates the game
- this method also clears any variables so that the game is ready to be replayed
@author: Frank Ding
@date: January 25, 2023
 */

package mainGame;

//import java classes and packages
import mainGame.storage.Sounds;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

//import variables that are important
import static mainGame.Gameplay.playerAmount;
import static mainGame.MainUI.gameWindow;
import static mainGame.storage.Fonts.POPPINS_BOLD;

//this class inherits the variables and methods of Menu because it is still a subset of the Menu
public class GameStartGeneration extends Menu{
    //initialize Java Swing objects for the modeWindow
    static JFrame modeWindow;
    static JLabel modeTitleLabel;
    static JButton newPlayers;
    static JButton oldPlayers;

    //create a mouse listener object
    private static MyMouseListener mouseListener = new MyMouseListener();

    //initialize variables for the lobby selection window
    static JLabel[] playerName = new JLabel[10];
    static JLabel titleLabel;
    static JButton mainMenuButton;
    static JButton cardUp;
    static JButton cardDown;
    static JLabel cardLabel;
    static JLabel titleCardLabel;
    static JLabel updateLabel;
    static JButton removePlayer;
    static JButton addPlayer;
    static JButton startGame;
    static JPanel playerList;

    //initialize final and normal variables
    static final int MAX_PLAYERS = 10;
    static String[] tempNames = new String[MAX_PLAYERS]; // temporarily holds the player names
    static int counter = 0; // variable counts the players
    static boolean open = false; // variable signals if a window is open or not

    //--------------------------------------------------------------------------------------------------------------
    //initialize JFrame objects for the addWindow
    static JFrame addWindow;
    static JTextField playerNameField;
    static JButton confirmButton;

    //--------------------------------------------------------------------------------------------------------------
//This method creates the game
    public static void gameCreation() {
        //creates a new game with same players
        MainUI.gameCreation(0);
        //creates the start deck
        Card.startDeck();
        GameState.playerCardsCreation();
        //creates the buttons for cards
        Gameplay.selectionCreation(0);
        Gameplay.deckCreation();
        //creates the rotation
        Gameplay.rotationCreation();
        GameState.updatePlayer(1);
        Sounds.backgroundMusic(0);
        Arrays.fill(GameState.playerAttackCounter, 0);
        //creates a new game with new players
    }

    //--------------------------------------------------------------------------------------------------------------
//This method creates the modeSelect screen with the new player and old player screen.
//This is called from the WinGame class when the player is given the option to play with the same players or new
    public static void modeSelectCreation() {
        //creates a new JFrame for the modeWindow
        modeWindow = new JFrame();
        modeWindow.setSize(500, 500);
        modeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        modeWindow.setLayout(null);
        modeWindow.setLocationRelativeTo(null);
        modeWindow.setAlwaysOnTop(true);
        modeWindow.setUndecorated(true);
        modeWindow.setResizable(false);
        modeWindow.setVisible(true);

        //creates a new title to select the mode
        modeTitleLabel = new JLabel("Select a mode");
        modeTitleLabel.setFont(Card.fontCreator(POPPINS_BOLD, 55f));
        modeTitleLabel.setBounds(50, 10, 400, 100);
        modeWindow.add(modeTitleLabel, SwingConstants.CENTER);

        //creates a new JButton to select the same players
        oldPlayers = new JButton("Same Players");
        oldPlayers.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        oldPlayers.addActionListener(new buttonSelect());
        oldPlayers.setBorder(new LineBorder(Color.WHITE, 3));
        oldPlayers.setForeground(Color.WHITE);
        oldPlayers.setBackground(Color.BLACK);
        oldPlayers.setFocusPainted(false);
        oldPlayers.setBounds(50, 200, 400, 100);
        oldPlayers.addMouseListener(mouseListener);
        modeWindow.add(oldPlayers, SwingConstants.CENTER);

        //creates a new JButton object for new player select
        newPlayers = new JButton("New Players");
        newPlayers.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        newPlayers.addActionListener(new buttonSelect());
        newPlayers.setForeground(Color.WHITE);
        newPlayers.setBackground(Color.BLACK);
        newPlayers.setBorder(new LineBorder(Color.WHITE, 3));
        newPlayers.setFocusPainted(false);
        newPlayers.setBounds(50, 340, 400, 100);
        newPlayers.addMouseListener(mouseListener);
        modeWindow.add(newPlayers, SwingConstants.CENTER);
    }

    //this class registers the inputs for old or new players
    static class buttonSelect implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //plays the sound cardClick from the Sounds class
            Sounds.cardClick();
            //if the button pressed is equal to the oldPlayers
            if (e.getSource() == oldPlayers) {
                //closes the windows and resets everything
                modeWindow.dispose();
                gameWindow.dispose();
                resetAll();
                //starts the game
                gameCreation();
                //if the button pressed is equal to the newPlayers button
            } else if (e.getSource() == newPlayers) {
                //closes all windows
                modeWindow.dispose();
                gameWindow.dispose();
                //calls the lobbyCreation method with argument false to create the lobby
                lobbyCreation(false);
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------
//Creates the lobby for the game
    public static void lobbyCreation(boolean createdValue) {
        //resets the variables to their primitive forms
        playerName = new JLabel[MAX_PLAYERS];
        counter = 0;
        open = false;

        //creates the menu if the createdValue is false
        if (!createdValue) {
            Sounds.backgroundMusic(1);
            menuWindow = new JFrame("UNO Game");
            menuWindow.setSize(MainUI.windowWidth, 1000);
            menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuWindow.setLayout(null);
            menuWindow.setLocationRelativeTo(null);
            menuWindow.setResizable(false);
            menuWindow.setVisible(true);
            //sets icon
            try {
                ImageIcon icon = new ImageIcon(Objects.requireNonNull(Menu.class.getClassLoader().getResource("mainGame/images/unoLogoPurple.png")));
                menuWindow.setIconImage(icon.getImage());
            } catch (NullPointerException e) {
                System.err.println("Error: Icon image not found.");
                e.printStackTrace();
            }
        }

        //sets the background of the window
        try {
            menuWindow.setContentPane(new JLabel(new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(Menu.class.getClassLoader().getResourceAsStream("mainGame/images/menu-background.png")))
            )));
        } catch (IOException | NullPointerException e) {
            System.err.println("Error: Background image not found.");
            e.printStackTrace();
        }

        //--------------------------------------------------------------------------------------------------------------
        //creates the addPlayer button
        addPlayer = new JButton("Add Player");
        addPlayer.setBorder(new LineBorder(Color.WHITE, 5));
        addPlayer.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        addPlayer.addActionListener(new lobbyButtonSelect());
        addPlayer.setForeground(Color.WHITE);
        addPlayer.setBackground(Color.BLACK);
        addPlayer.setFocusPainted(false);
        addPlayer.setBounds(850, 30, 400, 100);
        addPlayer.addMouseListener(mouseListener);
        menuWindow.add(addPlayer, SwingConstants.CENTER);

        //creates the titleLabel button
        titleLabel = new JLabel("GAME CREATION", SwingConstants.CENTER);
        titleLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(100, 30, 600, 100);
        menuWindow.add(titleLabel);

        //creates the removePlayer button to remove players
        removePlayer = new JButton(" X ");
        removePlayer.setBorder(new LineBorder(Color.WHITE, 5));
        removePlayer.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        removePlayer.addActionListener(new lobbyButtonSelect());
        removePlayer.setForeground(Color.WHITE);
        removePlayer.setBackground(Color.BLACK);
        removePlayer.setFocusPainted(false);
        removePlayer.setBounds(700, 30, 100, 100);
        removePlayer.addMouseListener(mouseListener);
        menuWindow.add(removePlayer, SwingConstants.CENTER);

        //creates the startGame button to start the game
        startGame = new JButton("START GAME");
        startGame.setBorder(new LineBorder(Color.WHITE, 5));
        startGame.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        startGame.addActionListener(new lobbyButtonSelect());
        startGame.setForeground(Color.WHITE);
        startGame.setBackground(Color.BLACK);
        startGame.setFocusPainted(false);
        startGame.setBounds(1300, 30, 400, 100);
        startGame.addMouseListener(mouseListener);
        menuWindow.add(startGame, SwingConstants.CENTER);

        //creates the playerList panel to show the list of players
        playerList = new JPanel();
        playerList.setLayout(new GridLayout(5,5));
        playerList.setBounds(100, 250, 1600, 650);
        playerList.setBorder(new LineBorder(Color.BLACK,0 ));
        playerList.setBackground(new Color(0f, 0f, 0f, .5f));
        menuWindow.add(playerList);

        //--------------------------------------------------------------------------------------------------------------
        //creates the cardUp button to increase cards
        cardUp = new JButton("+");
        cardUp.setBorder(new LineBorder(Color.WHITE, 5));
        cardUp.setFont(Card.fontCreator(POPPINS_BOLD, 60f));
        cardUp.addActionListener(new lobbyButtonSelect());
        cardUp.setForeground(Color.WHITE);
        cardUp.setBackground(Color.BLACK);
        cardUp.setFocusPainted(false);
        cardUp.setBounds(1750, 550, 100, 100);
        cardUp.addMouseListener(mouseListener);
        menuWindow.add(cardUp);

        //creates the cardDown button to decrease cards
        cardDown = new JButton("-");
        cardDown.setBorder(new LineBorder(Color.WHITE, 5));
        cardDown.setFont(Card.fontCreator(POPPINS_BOLD, 60f));
        cardDown.addActionListener(new lobbyButtonSelect());
        cardDown.setForeground(Color.WHITE);
        cardDown.setBackground(Color.BLACK);
        cardDown.setFocusPainted(false);
        cardDown.setBounds(1750, 750, 100, 100);
        cardDown.addMouseListener(mouseListener);
        menuWindow.add(cardDown);

        //creates the cardLabel label to show number of cards to gen
        cardLabel = new JLabel("", SwingConstants.CENTER);
        cardLabel.setText(Integer.toString(Card.cardGenAmount));
        cardLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        cardLabel.setForeground(Color.WHITE);
        cardLabel.setBounds(1750, 650, 100, 100);
        menuWindow.add(cardLabel);

        //creates the title for the cards
        titleCardLabel = new JLabel("Cards", SwingConstants.CENTER);
        titleCardLabel.setFont(Card.fontCreator(POPPINS_BOLD, 30f));
        titleCardLabel.setForeground(Color.WHITE);
        titleCardLabel.setBounds(1750, 450, 100, 100);
        menuWindow.add(titleCardLabel);

        //--------------------------------------------------------------------------------------------------------------
        //creates the button to go to the main menu
        mainMenuButton = new JButton("\uD83C\uDFE0");
        mainMenuButton.setBorder(new LineBorder(Color.WHITE, 5));
        mainMenuButton.setFont(new Font("Semgoe UI", 1, 30));
        mainMenuButton.addActionListener(new lobbyButtonSelect());
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setBackground(Color.BLACK);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBounds(30, 30, 100, 100);
        mainMenuButton.addMouseListener(mouseListener);
        menuWindow.add(mainMenuButton);

        //creates the label that responds to button presses
        updateLabel = new JLabel("", SwingConstants.CENTER);
        updateLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        updateLabel.setForeground(Color.WHITE);
        updateLabel.setBounds(100, 140, 1600, 100);
        menuWindow.add(updateLabel);

        //refreshes everything
        menuWindow.revalidate();
        menuWindow.repaint();
    }

    //this class operates ALL the button presses for the lobby
    static class lobbyButtonSelect implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //if the pressed button is addPlayer and a tab isn't open
            if (e.getSource() == addPlayer && !open){
                //plays a sound
                Sounds.cardClick();
                //calls the addPlayerCreation method if the max players hasn't been reached
                if (counter < MAX_PLAYERS){
                    open = true;
                    addPlayerCreation();
                }
            //if pressedButton is the startGame and a tab isn't open
            } else if (e.getSource() == startGame && !open){
                //if the counter is larger than 1, then:
                if ( counter>1 ){
                    Sounds.matchStart();

                    //closes menuWindow and prepares for the game start
                    menuWindow.dispose();
                    Gameplay.playerAmount = counter;
                    GameState.playerNames = new String[counter];
                    GameState.playerScore = new int[counter];
                    Arrays.fill(GameState.playerScore, 0);

                    //basically, this converts the temp names into the playerNames array in the actual game
                    for (int i = 0; i < counter; i++){
                        GameState.playerNames[i] = tempNames[i];
                    }

                    //resets everything to prepare for the game creation
                    resetAll();
                    gameCreation();
                } else{
                    updateLabel.setText("Minimum 2 players needed to start");
                }

            //if the pressed button is the confirm button
            } else if (e.getSource() == confirmButton ){
                Sounds.cardClick();
                open = false;
                addWindow.dispose();
                tempNames[counter] = playerNameField.getText();

                //adds a name to the tempNames array and to the playerList panel
                playerName[counter] = new JLabel(tempNames[counter], SwingConstants.CENTER);
                playerName[counter].setFont(Card.fontCreator(POPPINS_BOLD, 50f));
                playerName[counter].setForeground(Color.WHITE);
                playerList.add(playerName[counter]);
                playerList.revalidate();
                playerList.repaint();
                counter++;
                updateLabel.setText("Added Player");
            //if the pressed button is the cardUp button adnd a tab isn't open, then add a card
            }  else if (e.getSource() == cardUp && !open){
                Card.cardGenAmount++;
                cardLabel.setText(Integer.toString(Card.cardGenAmount));
            //if cardDown is pressed and the current card amount is larger than 1 and a tab isn't open, then subtract a card
            } else if (e.getSource() == cardDown && Card.cardGenAmount > 1 && !open) {
                Card.cardGenAmount--;
                cardLabel.setText(Integer.toString(Card.cardGenAmount));
            //if removePlayer is pressed and a window isnt open and the counter of players is larger than 1
            } else if (e.getSource() == removePlayer && !open && counter >= 1){
                //removes a player
                Sounds.skipPlayer();
                counter -= 1;
                tempNames[counter] = "";
                playerList.remove(playerName[counter]);
                updateLabel.setText("Removed Player");
                playerList.revalidate();
                playerList.repaint();
            //if the mainMenuButton is pressed and a window isnt open
            } else if (e.getSource() == mainMenuButton && !open){
                Sounds.cardClick();
                lobbyRemove();
                Menu.menuCreation(true);
            }
            //refreshes everything
            menuWindow.repaint();

        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //this method creates the addWindow window
    public static void addPlayerCreation(){
        open = true;
        //creates the addWindow JFrame
        addWindow = new JFrame();
        addWindow.setSize(500, 400);
        addWindow.getContentPane().setBackground(Color.decode("#8959FF"));
        addWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindow.setLayout(null);
        addWindow.setLocationRelativeTo(null);
        addWindow.setAlwaysOnTop(true);
        addWindow.setUndecorated(true);
        addWindow.setVisible(true);

        //creates a playerField enter space
        playerNameField = new JTextField();
        playerNameField.setText("Player " + counter);
        playerNameField.setHorizontalAlignment(JTextField.CENTER);
        playerNameField.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        playerNameField.setBounds(50, 130, 400, 100);
        addWindow.add(playerNameField);

        //creates a confirm button
        confirmButton = new JButton("CONFIRM");
        confirmButton.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        confirmButton.setBorder(new LineBorder(Color.WHITE, 5));
        confirmButton.addMouseListener(mouseListener);
        confirmButton.addActionListener(new lobbyButtonSelect());
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(Color.BLACK);
        confirmButton.setFocusPainted(false);
        confirmButton.setBounds(50, 270, 400, 100);
        addWindow.add(confirmButton, SwingConstants.CENTER);

        //creates a title label
        titleLabel = new JLabel("Enter Name:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        titleLabel.setBounds(100, 10, 400, 100);
        addWindow.add(titleLabel);
    }
    //--------------------------------------------------------------------------------------------------------------
    //assuming: player amount and player names and player score have been set already, this method resets EVERYTHING
    public static void resetAll(){
        Card.currentColor = "";
        Card.currentNumber = "";
        Card.currentCard = "   ";
        Card.cardDebt = 0;
        Card.playerCards = new String[playerAmount];

        Dan.elapsedTime = 0;
        Dan.seconds = 0;
        Dan.unoDraw = false;

        Deck.drawnCard = "";
        Deck.plusCards = false;
        Deck.drawNeed = false;
        Deck.drawn = 0;
        Deck.unoDrawn = 0;

        Gameplay.validInput = false;
        Gameplay.currentPlayer = 1;
        Gameplay.reverseMode = false;
        Gameplay.skipValue = 0;
        Gameplay.currentEvent = false;
        Gameplay.rotationLevel = 0;
        Gameplay.rowAmount = 0;

        GameState.playerAttackCounter = new int[playerAmount];
        WinGame.counter = 0;
        WinGame.soundCounter = 0;
        WinGame.highScoreValue = 0;
        WinGame.scoreFinish = false;

        mainGame.storage.Sounds.stressMusic = false;

    }

    //--------------------------------------------------------------------------------------------------------------
    //this method removes everything in the lobby
    public static void lobbyRemove(){
        menuWindow.getContentPane().removeAll();
        menuWindow.revalidate();
        menuWindow.repaint();

    }

    //--------------------------------------------------------------------------------------------------------------
    //this class responds to mouse movement
    static class MyMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
        }
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        }
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
        }
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            //plays a sound and sets line borders extra thick if the desired button is hovered
            Sounds.cardSelect();
            if (e.getSource() == addPlayer ){
                addPlayer.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == startGame){
                startGame.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == confirmButton ){
                confirmButton.setBorder(new LineBorder(Color.WHITE, 8));
            }  else if (e.getSource() == cardUp ){
                cardUp.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == cardDown) {
                cardDown.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == removePlayer){
                removePlayer.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == mainMenuButton){
                mainMenuButton.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == oldPlayers){
                oldPlayers.setBorder(new LineBorder(Color.WHITE, 8));
            } else if (e.getSource() == newPlayers){
                newPlayers.setBorder(new LineBorder(Color.WHITE, 8));
            }
            menuWindow.repaint();
        }
        public void mouseExited(MouseEvent e){
            //resets all the borders once the cursor leaves
            if (e.getSource() == addPlayer ){
                addPlayer.setBorder(new LineBorder(Color.WHITE, 5));
            } else if (e.getSource() == startGame){
                startGame.setBorder(new LineBorder(Color.WHITE, 5));
            } else if (e.getSource() == confirmButton ){
                confirmButton.setBorder(new LineBorder(Color.WHITE, 5));
            }  else if (e.getSource() == cardUp ){
                cardUp.setBorder(new LineBorder(Color.WHITE, 5));
            } else if (e.getSource() == cardDown) {
                cardDown.setBorder(new LineBorder(Color.WHITE, 5));
            } else if (e.getSource() == removePlayer){
                removePlayer.setBorder(new LineBorder(Color.WHITE, 5));
            } else if (e.getSource() == mainMenuButton){
                mainMenuButton.setBorder(new LineBorder(Color.WHITE, 5));
            } else if (e.getSource() == oldPlayers){
                oldPlayers.setBorder(new LineBorder(Color.WHITE, 3));
            } else if (e.getSource() == newPlayers){
                newPlayers.setBorder(new LineBorder(Color.WHITE, 3));
            }
            menuWindow.repaint();
        }
    }

}
