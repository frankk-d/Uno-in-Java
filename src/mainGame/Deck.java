/* MainUI Class
- This class creates the deck to draw cards, and what happens when you draw a card
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
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.Arrays;
import static mainGame.storage.Fonts.*;
import mainGame.storage.*;
public class Deck  extends Gameplay{
    //initialize Java Swing objects
    static JButton[] deckButton ;
    static JFrame deckWindow;
    static JPanel deckLargePanel;
    static JPanel deckCardPanel;
    static JPanel deckTitle;
    static JLabel deckTitleLabel;
    static JPanel bottomPanel;
    static JButton confirmButton;
    static JButton deck;

    static JButton[] deckDisplayButton = new JButton[1];
    private static MyMouseListener mouseListener = new MyMouseListener();

    //declare variables
    static String drawnCard; //the card drawn
    static boolean plusCards = false;
    static boolean drawNeed = false;
    static int drawn = 0; //represents if a card is drawn
    static int unoDrawn = 0; //represents if an UNO card is drawn
    static final int DECK_VALUE = 1; //represents the deck for the card validator

    //----------------------------------------------------------------------------------------------------
    //This method creates the deck button
    public static void creation(){
        //sets the font for the deck button and creates the clickable deck button on the gameWindow
        Font backFont = Card.fontCreator(KAITI, 70f);
        deck = new JButton("单");
        deck.setBackground(Color.decode("#232b2b"));
        deck.setFont(backFont);
        deck.setForeground(Color.WHITE);
        deck.setBorder(new LineBorder(Color.WHITE, 3));
        deck.addActionListener(new deckSelect());
        deck.setFocusPainted(false);
        deck.setEnabled(false);
        deck.setVisible(true);
        deck.addMouseListener(mouseListener);

        //checks if the deck is clickable or not
        cardValidator(DECK_VALUE);
        deckPanel.add(deck);

        //refreshes everything
        gameWindow.validate();
        gameWindow.repaint();
        deckPanel.repaint();
    }

    //----------------------------------------------------------------------------------------------------
    //This class is called when the deck is clicked
    static class deckSelect implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //when the deck is clicked and you don't need to draw a card
            if (!Gameplay.currentEvent && Card.cardDebt == 0 && skipValue == 0 && Card.playerDeckChecker() && !Dan.unoDraw){
                //draw a singular new card
                newCardDrawn();
            //when the deck is clicked because you need to DRAW TWO
            } else if (!Gameplay.currentEvent && Card.cardDebt != 0 && skipValue == 0 && !Dan.unoDraw){
                //begin drawing until there is no card debt
                plusCards = true;
                Card.cardDebt--;
                newCardDrawn();
            //when the deck is clicked because you can't play
            } else if (!Gameplay.currentEvent && !Card.playerDeckChecker() && !Dan.unoDraw){
                //begin drawing until a valid card
                drawNeed = true;
                newCardDrawn();
            //when the deck is drawn because you didn't say uno in time
            } else if (Dan.unoDraw){
                //draws two cards
                unoDrawn ++;
                newCardDrawn();
            }
        }
    }
    //----------------------------------------------------------------------------------------------------
    //This method draws the cards
    public static void newCardDrawn() {
        Sounds.deckSelect();
        //generates a new card for the deck
        drawnCard = generation(1);
        //creates a new drawn card and adds to the ceck
        Card.playerCards[currentPlayer - 1] = Card.playerCards[currentPlayer - 1] + drawnCard;
        deck.setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return Color.WHITE;
            }
        });
        deck.setEnabled(false);

        //sets current event to true so no other buttons are able to be pressed
        Gameplay.currentEvent = true;

        //craetes the deck display
        displayCreation();
        selectionRemove();
        selectionCreation(rotationLevel);

        //checks if the player has an available card in their deck
        boolean availableCard = Card.playerDeckChecker();

        //if there is no available card, then continue drawing
        if (availableCard == false && plusCards == false  && !Dan.unoDraw) {
            confirmButton.setText(" DRAW NEXT ");
        //if the card debt is still not done, then continue drawing cards
        } else if (Card.cardDebt != 0  && !Dan.unoDraw){
            confirmButton.setText(" DRAW " + Card.cardDebt + " MORE! ");
        //when all cards have been drawn
        } else if ((Card.cardDebt == 0 && plusCards == true   && !Dan.unoDraw) || (availableCard == true  && !Dan.unoDraw) || (unoDrawn == 2)){
            confirmButton.setText("    FINISH    ");
        //if uno hasn't finished drawing
        } else if (Dan.unoDraw && unoDrawn < 2){
            confirmButton.setText(" DRAW 1 MORE ");
            Dan.unoDraw = true;
        }
    }

    //----------------------------------------------------------------------------------------------------
    //This method generates a new card for the amount of cards needed for generation
    public static String generation(int amount){
        String newCards = "";
        for (int i = 0; i < amount; i++){
            newCards = newCards + Card.cardCreation();
        }
        return newCards;
    }

    //----------------------------------------------------------------------------------------------------
    //This method creates the display for the deck window
    public static void displayCreation(){
        //sets the font for the display and sets the size of the windows
        Font newFont = Card.fontCreator(MONTSERRAT_BLACK, 50f);
        final int WINDOW_X = 500;
        final int WINDOW_Y = 500;

        //creates a new JFrame object for the deck window
        deckWindow = new JFrame();
        deckWindow.setLayout(null);
        deckWindow.setSize(WINDOW_X,WINDOW_Y);
        deckWindow.setLocationRelativeTo(null);
        deckWindow.setAlwaysOnTop(true);
        deckWindow.getContentPane().setBackground(Color.BLACK);
        deckWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deckWindow.setUndecorated(true);
        deckWindow.setShape(new RoundRectangle2D.Double(0, 0, WINDOW_X, WINDOW_Y, 50, 50));
        deckWindow.setVisible(true);

        //creates new JPanel object for the title
        deckTitle = new JPanel();
        deckTitle.setBounds(0,30,WINDOW_X, 70);
        deckTitle.setBackground(Color.BLACK);
        deckWindow.add(deckTitle);

        //creates new JLabel for the title text
        deckTitleLabel = new JLabel("You drew: ");
        deckTitleLabel.setFont(newFont);
        deckTitleLabel.setForeground(Color.WHITE);
        deckTitle.add(deckTitleLabel);

        //creates new JPanel to house the deck centered
        deckLargePanel = new JPanel();
        deckLargePanel.setLayout(new GridBagLayout());
        deckLargePanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        deckLargePanel.setBounds(0,110, WINDOW_X,200);
        deckWindow.add(deckLargePanel);

        //creates new JPanel to house the real deck (But in a grid for the card)
        deckCardPanel = new JPanel();
        deckCardPanel.setSize(140,200);
        deckCardPanel.setLayout(new GridLayout(1,1));
        deckCardPanel.setBackground(Color.BLACK);
        deckLargePanel.add(deckCardPanel, gbc);

        //creates the bottom panel for the confirm button
        bottomPanel = new JPanel();
        bottomPanel.setBounds(0,340, WINDOW_X,125);
        bottomPanel.setBackground(Color.BLACK);
        deckWindow.add(bottomPanel);

        //creates the confirm button as new JButton object
        confirmButton = new JButton("    KEEP    ");
        confirmButton.setFont(newFont);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(new confirm());
        confirmButton.setBackground(Color.decode("#6A4C93"));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addMouseListener(mouseListener);
        confirmButton.setBorder(new LineBorder(Color.WHITE, 2));
        bottomPanel.add(confirmButton);

        //creates the display for the generated card
        cardRender();

        //renders the card
        Card.render(1, 0, deckButton, Card.TYPE_DECK_CARD);
        deckWindow.repaint();
    }

    //----------------------------------------------------------------------------------------------------
    //This class is called when the confirm button is pressed
    static class confirm implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            //sets the current event to false
            Gameplay.currentEvent = false;
            //removes the deckWindow
            deckWindow.setVisible(false);
            deckWindow.dispose();
            //refreshes the player deck display
            selectionRemove();
            selectionCreation(rotationLevel);
            //sets drawn to 1 to signal that a card has been drawn
            drawn = 1;
            //checks if a card can be played in the player's deck
            boolean availableCard = Card.playerDeckChecker();

            //if the player still has to draw cards (for +2 or +4)
            if (Card.cardDebt != 0 && !Dan.unoDraw) {
                Card.cardDebt--;
                newCardDrawn();
            //if the player has finished drawing cards after a +2 or +4
            } else if (Card.cardDebt == 0 && plusCards == true && !Dan.unoDraw ) {
                plusCards = false;
                endTurnButtonCreation();
            //if there isn't an available card to be played
            } else if (availableCard == false && plusCards == false && !Dan.unoDraw ) {
                newCardDrawn();
            //if UNO stil hasnt been called
            } else if (Dan.unoDraw && unoDrawn < 2){
                unoDrawn ++;
                newCardDrawn();
            //creates the end button creation (when you draw for fun)
            }else if (drawNeed == true && availableCard == true && !Dan.unoDraw) {

            } else{
                endTurnButtonCreation();
            }
        }
    }

    //----------------------------------------------------------------------------------------------------
    //This method renders the current drawn card button
    public static void cardRender(){
        //creates new JButton for the drawn card display
        deckButton = new JButton[1];
        deckButton[0] = new JButton("");
        deckButton[0].setFocusPainted(false);
        deckButton[0].setEnabled(false);
        Card.setDisabledColor(deckButton);
        deckButton[0].setPreferredSize(new Dimension(140, 200));
        deckButton[0].setBorder(new LineBorder(Color.WHITE, 3));
        deckCardPanel.add(deckButton[0]);
    }

    //----------------------------------------------------------------------------------------------------
    //THis method removes everything on the deck panel
    public static void removal(){
        deckPanel.remove(deck);
        deck.setVisible(false);
        deck.setEnabled(false);
    }
    //----------------------------------------------------------------------------------------------------
    //This class is called when a mouse enters or exits
    static class MyMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
        }
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        }
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
        }
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            //if the mouse enters deck button
            if (e.getSource() == deck){
                //sets the border thick, plays sound, and updates the hover label
                Sounds.cardSelect();
                deck.setBorder(new LineBorder(Color.WHITE, 10));
                hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
                hoverLabel.setText("Draw cards");
            }
            //if mouse enters the confirm button
            if (e.getSource() == confirmButton){
                confirmButton.setBorder(new LineBorder(Color.WHITE, 6));
            }
        }
        public void mouseExited(MouseEvent e){    // MUST be implemented even if not used!
            //if mouse exits the deck button
            if (e.getSource() == deck){
                //resets to default thickness
                deck.setBorder(new LineBorder(Color.WHITE, 3));
                //checks if the deck is still valid to be clicked or not
                if (drawn == 0){
                    cardValidator(1);
                }
                hoverLabel.setText("");

            }
            //if mouse exits the confirm button
            if (e.getSource() == confirmButton){
                confirmButton.setBorder(new LineBorder(Color.WHITE, 2));
            }
        }
    } // MyMouseListener class end

}
