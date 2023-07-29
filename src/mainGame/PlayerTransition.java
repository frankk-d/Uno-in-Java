/* PlayerTransition Class
- This class is for the transition screen between player turns
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame;

//import java packages
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import mainGame.storage.*;
import static mainGame.storage.Fonts.*;
public class PlayerTransition extends MainUI{
    //initialize Java Swing objects
    static JFrame transitionWindow = new JFrame();
    static JPanel transitionPanel = new JPanel();
    static JLabel transitionLabel = new JLabel();
    static JPanel transitionButtonPanel = new JPanel();
    static JButton transitionButton = new JButton();
    private static MyMouseListener mouseListener = new MyMouseListener();

    //--------------------------------------------------------------------------------------------------------------
    //This method ccreates the transition screen
    public static void transitionCreation(int currentPlayer){
        //removes everything
        MainUI.remove();
        gameWindow.getContentPane().setLayout(new BorderLayout());

        //creates new JPanel object for the transition panel
        transitionPanel = new JPanel();
        transitionPanel.setBackground(Color.BLACK);
        gameWindow.add(transitionPanel, BorderLayout.NORTH);

        //creates new JLabel object for the player turn label
        transitionLabel = new JLabel();
        transitionLabel.setText(GameState.playerNames[currentPlayer-1] + "'s Turn: ");
        transitionLabel.setFont(Card.fontCreator(POPPINS_BOLD, 60f));
        transitionLabel.setForeground(Color.WHITE);
        transitionPanel.add(transitionLabel);

        //creates new transition button panel
        transitionButtonPanel = new JPanel();
        transitionButtonPanel.setLayout(new GridBagLayout());
        transitionButtonPanel.setBackground(Color.BLACK);
        gameWindow.add(transitionButtonPanel, BorderLayout.CENTER);

        //creates new JButton object to initialize the start of the turn
        transitionButton = new JButton("Start Turn");
        transitionButton.setFont(Card.fontCreator(POPPINS_BOLD, 30f));
        transitionButton.setFocusPainted(false);
        transitionButton.setBackground(Color.decode("#00C986"));
        transitionButton.setForeground(Color.WHITE);
        transitionButton.setBorder(new LineBorder(Color.WHITE, 15));
        transitionButton.setPreferredSize(new Dimension(300, 200));
        transitionButton.addActionListener(new transitionSelect());
        transitionButton.addMouseListener(mouseListener);
        transitionButtonPanel.add(transitionButton);

        //refreshes everything
        gameWindow.validate();
        gameWindow.repaint();
    }
    //--------------------------------------------------------------------------------------------------------------
    //This class is called when the button is pressed
    static class transitionSelect implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            //plays the endTurn sound
            Sounds.endTurn();
            //removes the transition panel
            MainUI.remove();
            transitionWindow.setVisible(false);
            transitionWindow.dispose();

            //creates the game for a different player
            gameWindow.setLayout(null);
            //creates the game window
            MainUI.gameCreation(1);
            //creates the player cards
            GameState.playerCardsCreation();
            //renders the side card
            Card.render(1, 0, currentCardButton, Card.TYPE_SIDE_CARD);
            GameState.gameTextUpdater(Character.toString(Card.currentCard.charAt(1)));

            //calls the second part of the switch player
            Gameplay.switchPlayerTwo();
        }
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
            if (e.getSource() == transitionButton ){
                transitionButton.setBorder(new LineBorder(Color.WHITE, 20));
            }
            gameWindow.repaint();
        }
        public void mouseExited(MouseEvent e){
            //resets all the borders once the cursor leaves
            if (e.getSource() == transitionButton ){
                transitionButton.setBorder(new LineBorder(Color.WHITE, 15));
            }
            gameWindow.repaint();
        }
    }


}
