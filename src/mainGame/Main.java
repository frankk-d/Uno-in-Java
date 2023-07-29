/* Main Class
 * Description:
 * - Basically calls the Menu class for the main menu creation
 *  @author: Frank Ding
 *  @version: January 25, 2023
 */
package mainGame;

/*
list of things to do:
- DONE: program in the different special cards and what they do
- DONE: make a card shuffling function
- DONE: make the introduction: amount of players, game rule setting, rules window
- DONE: make the game replayable: resetting all variables to original state
- DONE: design the background to make it aesthetically pleasing
- DONE: comment lines
 */

//import java classes
import java.io.*;
public class Main {
    //initialize path variable
    public static String path;

    //basically gets the path so the user doesn't have to change the path every time they run the program
    static {
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //--------------------------------------------------------------------------------
    //This method starts the program by calling the menuCreation method in the Menu class
    public static void main(String[] args){
        //calls the menuCreation method to start the game, with parameter false to signify that a menu has not been created yet
        Menu.menuCreation(false);
    }
}
