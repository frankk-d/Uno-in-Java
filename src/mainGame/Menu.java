/* Menu Class
- Creates the main menu at the start of the game
- Creates the buttons on the main menu
- Controls where the buttons go - calls the GameStartGeneration or the Rules class to start the lobby, or to start the rules
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame;

//import java classes
import mainGame.storage.Sounds;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static mainGame.storage.Fonts.POPPINS_BOLD;

public class Menu {

    //initialize Java Swing classes
    static JFrame menuWindow;

    static JPanel menuPanel;

    static JLabel titleLogo;

    static JLabel largeLogo;
    static JButton playButton;

    static JLabel titleLabel;

    static JLabel pictureLabel;

    static JButton rulesButton;

    //create a mouse listener object
    private static MyMouseListener mouseListener = new MyMouseListener();

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the menu, with parameter createdValue to represent if a menu has been created or not
    public static void menuCreation(boolean createdValue){
        //if a menu hasn't been created
        if (!createdValue) {
            //play background music
            Sounds.backgroundMusic(1);

            //create the menuWindow JFrame and setting properties
            menuWindow = new JFrame("UNO Game");
            menuWindow.setSize(MainUI.windowWidth, 1000);
            menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuWindow.setLayout(null);
            menuWindow.setResizable(false);
            menuWindow.setLocationRelativeTo(null);
            menuWindow.setVisible(true);

            //sets the image for the menuWindow icon
            ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogoPurple.png");
            Image image = icon.getImage();
            menuWindow.setIconImage(image);

        }

        //sets the background image for the menuWindow icon to the desired file path
        try {
            menuWindow.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File(Main.path + "\\src\\mainGame\\images\\menu-background.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //creates a new JButton object for the play button
        playButton = new JButton("PLAY");
        playButton.setFont(Card.fontCreator(POPPINS_BOLD, 60f)); //calls fontCreator in the Card class to make a font for Poppins BOLD
        playButton.addActionListener(new menuSelect());
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(Color.BLACK);
        playButton.setFocusPainted(false);
        playButton.setBounds(1500, 300, 300, 100);
        playButton.addMouseListener(mouseListener);
        playButton.setBorder(new LineBorder(Color.WHITE, 5));
        menuWindow.add(playButton);

        //creates a new JButton object for the rules button
        rulesButton = new JButton("RULES");
        rulesButton.setFont(Card.fontCreator(POPPINS_BOLD, 60f));
        rulesButton.addActionListener(new menuSelect());
        rulesButton.setForeground(Color.WHITE);
        rulesButton.setBackground(Color.BLACK);
        rulesButton.setFocusPainted(false);
        rulesButton.setBounds(1500, 450, 300, 100);
        rulesButton.setBorder(new LineBorder(Color.WHITE, 5));
        rulesButton.addMouseListener(mouseListener);
        menuWindow.add(rulesButton);

        //creates new JLabel object for the title
        titleLabel = new JLabel("MAIN MENU");
        titleLabel.setFont(Card.fontCreator(POPPINS_BOLD, 100f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(100, 700, 800, 100);
        menuWindow.add(titleLabel);

        //sets the icon for the title by creating titleIcon object
        ImageIcon titleIcon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoIcon.png");
        titleLogo = new JLabel(titleIcon);
        titleLogo.setBounds(100, 100, 500, 300);
        menuWindow.add(titleLogo);

        //creates a new JLabel object for the large logo in the menu
        largeLogo = new JLabel();
        largeLogo.setBounds(700, 100, 800, 800);

        //sets the icon for the large logo
        ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogoPurple.png");
        //converts from ImageIcon to Image object
        Image img = icon.getImage();
        //sets the size for the icon
        Image newImg = img.getScaledInstance(800, 800, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        largeLogo.setIcon(icon);
        menuWindow.add(largeLogo);

        //refreshes everything so no bugs occur
        menuWindow.revalidate();
        menuWindow.repaint();
    }

    //--------------------------------------------------------------------------------------------------------------
    //new menuSelect class for the ActionListener for button presses
    static class menuSelect implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //if playButton is pressed
            if (e.getSource() == playButton) {
                //plays the toThree sound from the Sounds class
                Sounds.cardSelect();
                //removes everything and creates the game lobby (in the GameStartGeneration class)
                GameStartGeneration.lobbyRemove();
                GameStartGeneration.lobbyCreation(true);

            //if rulesButton is pressed
            } else if (e.getSource() == rulesButton) {
                //plays the cardSelect sound from the Sounds class
                Sounds.cardSelect();
                //removes everything and creates the rules (in the Rules class)
                GameStartGeneration.lobbyRemove();
                Rules.rulesCreation();
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
            Sounds.cardClick();
            //if the hover button is playButton
            if (e.getSource() == playButton) {
                //sets the border extra thick
                playButton.setBorder(new LineBorder(Color.WHITE, 8));
            //if the hover button is rulesButton
            } else if (e.getSource() == rulesButton) {
                //sets the border thick
                rulesButton.setBorder(new LineBorder(Color.WHITE, 8));
            }
            //refreshes everything
            menuWindow.repaint();
        }
        public void mouseExited(MouseEvent e){
            //if the exited button is playButton
            if (e.getSource() == playButton) {
                //sets border thin
                playButton.setBorder(new LineBorder(Color.WHITE, 5));
            //if the exited is rulesButton
            } else if (e.getSource() == rulesButton) {
                //sets border thin
                rulesButton.setBorder(new LineBorder(Color.WHITE, 5));
            }
            //refreshes everything
            menuWindow.repaint();
        }
    }
}
