/* Rules Class
- Creates the rules instructions for the game for the player to see
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame;

//import Java packages
import mainGame.storage.Sounds;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static mainGame.storage.Fonts.POPPINS_BOLD;

public class Rules extends Menu {
    //initialize Java Swing objects
    static JButton mainMenuButton;

    static JLabel instructionLabel;
    static JLabel titleLabel;
    private static MyMouseListener mouseListener = new MyMouseListener();

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the title panels
    public static void rulesCreation(){
        //creates a JButton for the menu button
        mainMenuButton = new JButton("\uD83C\uDFE0");
        mainMenuButton.setFont(new Font("Semgoe UI", 1, 30));
        mainMenuButton.addActionListener(new buttonSelect());
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setBackground(Color.BLACK);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBounds(30, 30, 100, 100);
        mainMenuButton.setBorder(new LineBorder(Color.WHITE, 5));
        mainMenuButton.addMouseListener(mouseListener);
        menuWindow.add(mainMenuButton);

        //creates the instruction label with the instructions on how this game works
        instructionLabel = new JLabel();
        instructionLabel.setBounds(200, 150, 1280, 720);
        ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\instructions.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance( 1280, 720, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        instructionLabel.setIcon(icon);
        menuWindow.add(instructionLabel);

        //creates a JLabel for the title of the game
        titleLabel = new JLabel("RULES AND TACTICS");
        titleLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(200, 30, 2000, 100);
        menuWindow.add(titleLabel);
    }

    //--------------------------------------------------------------------------------------------------------------
    //this class recieves the input from thee JButtons
    static class buttonSelect implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //if the button pressed is the home button, then remove everything and create the menu again
            if (e.getSource() == mainMenuButton) {
                Sounds.cardClick();
                GameStartGeneration.lobbyRemove();
                Menu.menuCreation(true);
            }
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //mouse listener class for mouse hovers
    static class MyMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
        }
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        }
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
        }
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            //if a mouse entered, then play a click sound and set the border to thick for the main menu button
            Sounds.cardClick();
            if (e.getSource() == mainMenuButton) {
                mainMenuButton.setBorder(new LineBorder(Color.WHITE, 8));
            }
            menuWindow.repaint();
        }
        public void mouseExited(MouseEvent e){
            //return the border back to normal if mouse is not in button range
            if (e.getSource() == mainMenuButton) {
                mainMenuButton.setBorder(new LineBorder(Color.WHITE, 5));
            }
            menuWindow.repaint();
        }
    }



}
