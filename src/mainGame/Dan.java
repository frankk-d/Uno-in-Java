/* Dan Class
- In charge of the UNO (Called DAN because its that in Chinese)
@author: Frank Ding
@date: January 25, 2023
 */

package mainGame;

//import Java packages
import mainGame.storage.Sounds;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static mainGame.storage.Fonts.*;

public class Dan {
    //Initialize Java Swing objects
    static JPanel unoButton;

    static JLabel iconImage;
    static JPanel unoButtonPanel;
    static JLabel timerLabel;
    private static MouseListener mouseListener = new MyMouseListener();

    //Initialize variables
    static long elapsedTime;
    static Timer timer;
    static long seconds = 0l;
    static boolean unoDraw = false;

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the uno button
    public static void unoButtonCreation() {
        //Sets the current event to true
        Gameplay.currentEvent = true;
        unoDraw = false;

        //Creates the icon for the UNO Button
        ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogo.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance( 200, 200,  java.awt.Image.SCALE_SMOOTH ) ;
        icon = new ImageIcon(newImg);

        //Creates new JPanel for the unoButton
        unoButtonPanel = new JPanel();
        unoButtonPanel.setBounds(100,275, 300, 275);
        unoButtonPanel.setLayout(new BorderLayout());
        unoButtonPanel.setOpaque(false);
        MainUI.gameWindow.add(unoButtonPanel);

        //creates new JPanel for the uno button clickable
        unoButton = new JPanel();
        unoButton.setBorder(BorderFactory.createEmptyBorder());
        unoButton.setBorder(null);
        unoButton.setOpaque(false);
        unoButton.setPreferredSize(new Dimension(200, 200));
        iconImage = new JLabel(icon);
        unoButton.add(iconImage);
        unoButton.addMouseListener(mouseListener);
        unoButtonPanel.add(unoButton, BorderLayout.CENTER);

        //Makes a label for the timer
        timerLabel = new JLabel("0", SwingConstants.CENTER);
        timerLabel.setFont(Card.fontCreator(MONTSERRAT_BLACK, 60f));
        timerLabel.setForeground(Color.WHITE);
        unoButtonPanel.add(timerLabel, BorderLayout.NORTH);

        //creates the timer and sets it so that its a millisecond
        elapsedTime = 0;
        seconds = 0;
        timer = new Timer(-1, new unoTimer());
        timer.restart();
        //starts the timer
        timer.start();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This class counts the time to press the uno button
    static class unoTimer implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //sets the elapsed time in increments of 1
            elapsedTime = elapsedTime + 1;
            //calculates the seconds passed
            seconds = (elapsedTime / 1000);
            //sets teh label to the current time
            timerLabel.setText(Long.toString(elapsedTime));
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //This class manages what happens with the mouse and what image appears when the mouse clicks, exits, etc
    static class MyMouseListener implements MouseListener {
        //when the mouse is clicked
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
            //stops the timer and removes the button
            timer.stop();
            unoButtonPanel.remove(unoButton);
            MainUI.gameWindow.remove(unoButtonPanel);
            //if the seconds when the button pressed is less than 2 seconds, then the turn ends
            if (seconds <= 1){
                Gameplay.endTurnButtonCreation();
            //if the seconds that it takes to press the button is longer than 2 seconds, then draw two cards
            } else if (seconds > 1){
                Deck.deck.setEnabled(true);
                unoDraw = true;
                Gameplay.currentEvent = false;
                GameState.gameTextUpdater("uno");

            }
        }
        //when the button is pressed
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
            ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogoClicked.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance( 200, 200, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            iconImage.setIcon(icon);
        }
        //when the mouse is released
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
            ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogo.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance( 200, 200,  java.awt.Image.SCALE_SMOOTH ) ;
            icon = new ImageIcon(newImg);
            iconImage.setIcon(icon);

        }
        //when the mouse enters
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogoHover.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance( 200, 200,  java.awt.Image.SCALE_SMOOTH ) ;
            icon = new ImageIcon(newImg);
            iconImage.setIcon(icon);
        }
        //when the mouse exits
        public void mouseExited(MouseEvent e){    // MUST be implemented even if not used!
            ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogo.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance( 200, 200, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            iconImage.setIcon(icon);
        }
    } // MyMouseListener class end
}
