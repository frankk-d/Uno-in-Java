/* WildCard Class
- This class is called when a wild card is played
//color scheme: https://coolors.co/palette/ff595e-ffca3a-8ac926-1982c4-6a4c93
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame;

//import packages
import mainGame.storage.Sounds;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import static mainGame.MainUI.currentCardButton;
import static mainGame.storage.Fonts.*;
public class WildCard {
    private static String selectedNumber;
    private static JFrame wildWindow;
    private static JPanel wildPanel;
    private static JPanel wildLargePanel;
    private static JButton wildButtons[];
    private static JLabel wildLabel;
    private static JPanel wildTitle;

    private static Font newFont;

    //creates the mouse listener
    private static MyMouseListener mouseListener = new MyMouseListener();

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the wild card window
    public void windowCreation(String num){
        selectedNumber = num;

        //initialize unchanging variables
        final int WINDOW_X = 600;
        final int WINDOW_Y = 600;

        //creates the window for the wild card selection
        wildWindow = new JFrame();
        wildWindow.setLayout(null);
        wildWindow.setSize(WINDOW_X,WINDOW_Y);
        wildWindow.setLocationRelativeTo(null);
        wildWindow.setAlwaysOnTop(true);
        wildWindow.getContentPane().setBackground(Color.BLACK);
        wildWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wildWindow.setUndecorated(true);
        wildWindow.setShape(new RoundRectangle2D.Double(0, 0, WINDOW_X, WINDOW_Y, 50, 50));
        wildWindow.setVisible(true);
        Font newFont = Card.fontCreator(MONTSERRAT_BLACK, 50f);

        //creates the title panel
        wildTitle = new JPanel();
        wildTitle.setBounds(0,30,600, 70);
        wildTitle.setBackground(Color.BLACK);
        wildWindow.add(wildTitle);

        //creates the title label
        wildLabel = new JLabel("Color Change!");
        wildLabel.setForeground(Color.WHITE);
        wildLabel.setFont(newFont);
        wildTitle.add(wildLabel, BorderLayout.CENTER);

        //creates the panel that centers the selections
        wildLargePanel = new JPanel();
        wildLargePanel.setLayout(new GridBagLayout());
        wildLargePanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        wildLargePanel.setBounds(0,100,WINDOW_X,WINDOW_Y-100);
        wildWindow.add(wildLargePanel);

        //creates the panel that holds the buttons to select the color
        wildPanel = new JPanel();
        wildPanel.setSize(400,400);
        wildPanel.setLayout(new GridLayout(2,2));
        wildPanel.setBackground(Color.black);
        wildLargePanel.add(wildPanel, gbc);

        wildWindow.repaint();
        wildWindow.validate();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method creates the buttons
    public void buttonCreation(){
        //creates the buttons that go on the wildPanel
        wildButtons = new JButton[4];
        //cycles through each button and changes the color in order (red, blue, yellow, green)
        for (int i = 0; i<4; i++){
            wildButtons[i] = new JButton("");
            if (i == 0){
                wildButtons[i].setBackground(Color.decode("#ff595e")); //red
            } else if (i == 1){
                wildButtons[i].setBackground(Color.decode("#1982c4")); //blue
            } else if (i == 2){
                wildButtons[i].setBackground(Color.decode("#ffca3a")); //yellow
            } else if (i == 3){
                wildButtons[i].setBackground(Color.decode("#8ac926")); //green
            }
            wildButtons[i].setBorder(new LineBorder(Color.WHITE,3 ));
            wildButtons[i].addActionListener(new buttonSelect());
            wildButtons[i].addMouseListener(mouseListener);
            wildButtons[i].setPreferredSize(new Dimension(200, 200));
            wildPanel.add(wildButtons[i]);

        }
        //refreshes everything
        wildPanel.repaint();
        wildPanel.revalidate();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method removes the buttons on the wild card window
    public static void removeButtons(){
        //removes the buttons for each button
        for (int color = 0; color<4; color++){
            //removes the button
            wildButtons[color].setVisible(false);
            wildPanel.remove(wildButtons[color]);
            wildWindow.getContentPane().removeAll();
        }
        //refreshes everything
        wildWindow.repaint();
        wildPanel.revalidate();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This class is called when a color button is selected
    static class buttonSelect implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Sounds.colorChange();
            //scans through each button to see which button is pressed
            for (int i = 0; i<4; i++){
                //once the button is found
                if (e.getSource() == wildButtons[i]){
                    //removes the button
                    removeButtons();
                    //sets the current card to the selected color
                    Card.currentCard = (i+1) + selectedNumber;
                    System.out.println("Color changed to: " + Card.currentCard);
                    //renders the current card
                    Card.render(1, 0, currentCardButton, Card.TYPE_SIDE_CARD);
                    wildWindow.setVisible(false);
                    wildWindow.dispose();


                }
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //This class is called when the mouse enters
    static class MyMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e){   // moves the box at the mouse location
        }
        public void mousePressed(MouseEvent e){   // MUST be implemented even if not used!
        }
        public void mouseReleased(MouseEvent e){  // MUST be implemented even if not used!
        }
        public void mouseEntered(MouseEvent e){   // MUST be implemented even if not used!
            //scans through to find the button
            for (int i = 0; i < 4; i++){
                if (e.getSource() == wildButtons[i]){
                    //sets the border
                    wildButtons[i].setBorder(new LineBorder(Color.WHITE, 6));
                }
            }
        }
        public void mouseExited(MouseEvent e){    // MUST be implemented even if not used!
            //scans through to find the button
            for (int i = 0; i < 4; i++){
                if (e.getSource() == wildButtons[i]){
                    //sets the border
                    wildButtons[i].setBorder(new LineBorder(Color.WHITE, 2));
                }
            }
        }
    } // MyMouseListener class end
}
