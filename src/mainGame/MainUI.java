/* MainUI Class
- This class creates the main ui for the game
@author: Frank Ding
@date: January 25, 2023
 */

package mainGame;

//import java packages
import mainGame.storage.Sounds;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import static mainGame.storage.Fonts.POPPINS_BOLD;

public class MainUI{
    //initialize Java Swing objects
    static JFrame gameWindow;
    static JPanel selectionPanel;
    static JPanel utilityPanel;
    static JPanel titlePanel;
    static JPanel deckPanel;

    //initialize sidebar objects
    static JPanel currentCardPanel;
    static JButton currentCardButton[] = new JButton[1];
    static JPanel gameUpdatesPanel;
    static JLabel gameUpdatesLabel;
    static JPanel hoverPanel;
    static JLabel hoverLabel;
    static JPanel currentPlayerPanel;
    static JLabel currentPlayerLabel;

    //initialize the rotation objects
    static JPanel rotationPanel;
    static JLabel rotationLabel;

    //declare unchanging variables
    public static final int windowWidth = 1900; //represents the window width
    static final int NOT_CREATED = 0; //represents if a window is not created

    //----------------------------------------------------------------------------------------------------
    //This method creates the main game window
    public static void gameCreation(int mode) {
        //if a window isn't created already
        if (mode == NOT_CREATED) {
            //creates the game window and sets the icon and layout for the window
            gameWindow = new JFrame("UNO Game");
            gameWindow.setSize(windowWidth, 1000);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setLayout(null);
            gameWindow.setLocationRelativeTo(null);
            gameWindow.setResizable(false);
            ImageIcon icon = new ImageIcon(Main.path + "\\src\\mainGame\\images\\unoLogoPurple.png");
            Image image = icon.getImage();
            gameWindow.setIconImage(image);
            gameWindow.setVisible(true);
        }
        try {
            gameWindow.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File(Main.path + "\\src\\mainGame\\images\\Uno-Background.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Creates the selectionPanel to hold teh cards
        selectionPanel = new JPanel();
        selectionPanel.setLayout(null);
        selectionPanel.setBounds(40, 750, 1000, 200);
        selectionPanel.setBorder(new LineBorder(Color.BLACK,1 ));
        gameWindow.add(selectionPanel);

        //creates the utilityPanel for the utility buttons
        utilityPanel = new JPanel();
        utilityPanel.setLayout(null);
        utilityPanel.setBounds(40, 575, 1000, 100);
        utilityPanel.setOpaque(false);
        gameWindow.add(utilityPanel);

        //creates the title panel for the title
        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setBounds(40, 40, 1000, 200);
        titlePanel.setBorder(new LineBorder(Color.BLACK,0 ));
        titlePanel.setBackground(Color.decode("#6e3d34"));
        gameWindow.add(titlePanel);

        //creates the deck panel to house the deck
        deckPanel = new JPanel();
        deckPanel.setLayout(new GridLayout(1,1));
        deckPanel.setBounds(800, 310, 140, 200);
        deckPanel.setBorder(new LineBorder(Color.BLACK,1 ));
        gameWindow.add(deckPanel);

        //creates the rotation panel for the rotation label at the bottom
        rotationPanel = new JPanel();
        rotationPanel.setBounds(800, 910, 100, 30);
        rotationPanel.setBorder(new LineBorder(Color.BLACK,1 ));
        gameWindow.add(rotationPanel);
        rotationLabel = new JLabel("");
        rotationPanel.add(rotationLabel);

        //refreshes everything
        gameWindow.repaint();

        //calls methods
        sideCreation(); //creates the side
        topCreation();  //creates the top
    }

    static JButton mainMenuButton;
    private static MyMouseListener mouseListener = new MyMouseListener();
    //----------------------------------------------------------------------------------------------------
    //This method creates the right side of the main UI
    public static void sideCreation(){
        //creates a JPanel object for the current card
        currentCardPanel = new JPanel();
        currentCardPanel.setLayout(new GridLayout(1,1));
        currentCardPanel.setBounds(1300, 250, 140*2, 200*2);
        currentCardPanel.setBorder(new LineBorder(Color.WHITE,5 ));
        currentCardPanel.setOpaque(false);
        gameWindow.add(currentCardPanel);

        //creates a button to represent the current card played
        currentCardButton[0] = new JButton();
        currentCardButton[0].setFocusPainted(false);
        currentCardButton[0].setEnabled(false);
        //disables the disabled text color (sets it to white)
        currentCardButton[0].setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return Color.WHITE;
            }
        });
        currentCardPanel.add(currentCardButton[0]);

        //creates new JPanel object for the game updates
        gameUpdatesPanel = new JPanel();
        gameUpdatesPanel.setBounds(1100, 30, 700, 100);
        gameUpdatesPanel.setOpaque(false);
        gameWindow.add(gameUpdatesPanel);

        //creates new JLabel for the text for the game updates
        gameUpdatesLabel = new JLabel();
        gameUpdatesLabel.setFont(Card.fontCreator(mainGame.storage.Fonts.MONTSERRAT_BLACK, 60f));
        gameUpdatesLabel.setForeground(Color.WHITE);
        gameUpdatesPanel.add(gameUpdatesLabel);

        //creates a new JPanel for the hover text
        hoverPanel = new JPanel();
        hoverPanel.setBounds(1100, 750, 700, 100);
        hoverPanel.setOpaque(false);
        gameWindow.add(hoverPanel);

        //creates new JLabel for the hover text
        hoverLabel = new JLabel();
        hoverLabel.setFont(Card.fontCreator(POPPINS_BOLD, 30f));
        hoverLabel.setForeground(Color.WHITE);
        hoverPanel.add(hoverLabel);

        //creates a JButton for the home button
        mainMenuButton = new JButton("\uD83C\uDFE0");
        mainMenuButton.setFont(new Font("Semgoe UI", 1, 20));
        mainMenuButton.addActionListener(new buttonSelect());
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setBackground(Color.BLACK);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBounds(1800, 875, 75, 75);
        mainMenuButton.setBorder(new LineBorder(Color.WHITE, 5));
        mainMenuButton.addMouseListener(mouseListener);
        gameWindow.add(mainMenuButton);

        //refreshes everything
        gameWindow.repaint();
    }

    //----------------------------------------------------------------------------------------------------
    //This panel creates the top bar of the main UI
    public static void topCreation(){
        //creates new JPanel for the current player
        currentPlayerPanel = new JPanel();
        currentPlayerPanel.setBounds(20, 30, 500, 100);
        currentPlayerPanel.setBorder(new LineBorder(Color.WHITE,4 ));
        currentPlayerPanel.setBackground(new Color(0f, 0f, 0f, .5f));
        titlePanel.add(currentPlayerPanel);

        //creates a new JLabel for the current player
        currentPlayerLabel = new JLabel("", SwingConstants.LEFT);
        currentPlayerLabel.setFont(Card.fontCreator(POPPINS_BOLD, 50f));
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerPanel.add(currentPlayerLabel);

        gameWindow.repaint();
    }
    //----------------------------------------------------------------------------------------------------
    //This method removes everything on the gameWindow
    public static void remove(){
        gameWindow.getContentPane().removeAll();
        gameWindow.revalidate();
        gameWindow.repaint();
    }

    //----------------------------------------------------------------------------------------------------
    //this class registers the inputs for the main menu button
    static class buttonSelect implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //plays the sound cardClick from the Sounds class
            Sounds.cardClick();
            //if the button pressed is equal to the mainMenuButton
            if (e.getSource() == mainMenuButton){
                Sounds.cardClick();
                //resets everything and creates the main menu
                MainUI.remove();
                gameWindow.dispose();
                Menu.menuCreation(false);
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
            //if the hover button is mainMenuButton
            if (e.getSource() == mainMenuButton) {
                //sets the border extra thick
                mainMenuButton.setBorder(new LineBorder(Color.WHITE, 8));
            }
            //refreshes everything
            gameWindow.repaint();
        }
        public void mouseExited(MouseEvent e){
            //if the exited button is mainMenuButton
            if (e.getSource() == mainMenuButton) {
                //sets border thin
                mainMenuButton.setBorder(new LineBorder(Color.WHITE, 3));
            }
            //refreshes everything
            gameWindow.repaint();
        }
    }
}
