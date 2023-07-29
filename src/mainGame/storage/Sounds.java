/* Sounds Class
- This class basically stores the sounds in the game for easy access
@author: Frank Ding
@date: January 25, 2023
 */
package mainGame.storage;

import mainGame.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sounds {
    //initialize variables
    public static final String filePath = Main.path + "\\src\\mainGame\\sounds\\";

    static AudioInputStream audioStream;
    static Clip clip;
    static Clip music;
    public static boolean musicPlaying = false;
    public static boolean stressMusic = false;
    public static boolean winMusic = false;

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the cardSelect sound
    public static void cardSelect(){
        soundCreator("check-off.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the deckSelect sound
    public static void deckSelect(){
        soundCreator("card-turn.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the cardClick sound
    public static void cardClick(){
        soundCreator("card-select.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the plusTwo sound
    public static void plusTwo(){
        soundCreator("plus-card.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the plusFour sound
    public static void plusFour(){
        soundCreator("plus-four.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays a menu click sound
    public static void endTurn(){
        soundCreator("menuclick.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when the color changes
    public static void colorChange(){
        soundCreator("color-change.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when a player is skipped
    public static void skipPlayer(){
        soundCreator("skip-player.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when the turns are reversed
    public static void reverse(){
        soundCreator("change-player.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when cards are swapped
    public static void swap(){
        soundCreator("swap-sound.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when the cards are reduced to three
    public static void toThree(){
        soundCreator("to-three.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when the score increases
    public static void scorePlus(){
        soundCreator("score-plus.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays the sound when the match starts
    public static void matchStart(){ soundCreator("match-start.wav");}

    //--------------------------------------------------------------------------------------------------------------
    //This method plays background music
    public static void backgroundMusic(int value){
        final int MUSIC_GAME = 0;
        final int MUSIC_MENU = 1;
        //If there is music already playing, then stop the music playing
        if (musicPlaying){
            music.stop();
        }
        stressMusic = false;
        musicPlaying = true;

        //if the value is equal to the music in the game, then play the heart home city theme
        if (value == MUSIC_GAME){
            musicCreator("Hearthome City Night.wav");
        //if the value is equal to the music in the menu, then play a random song
        } else if (value == MUSIC_MENU){
            //plays a random generated song and plays it
            int generatedSong = (int)(2*Math.random());
            if (generatedSong == 0){
                musicCreator("Route 228 Night.wav");
            } else if (generatedSong >= 1){
                musicCreator("Route 209 Night.wav");
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //When a player has only one card left, this music is played for intensity
    public static void stress(){
        if (musicPlaying){
            music.stop();
        }
        stressMusic = true;
        musicPlaying = true;
        //plays the lake battle theme in pokemon diamond and pearl
        musicCreator("Battle Pokemon Diamond.wav");
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays a randomly generated song when you win
    public static void winMusic(){
        music.stop();
        winMusic = true;
        int generatedSong = (int)(3*Math.random());
        if (generatedSong == 0){
            musicCreator("Oogway Ascends.wav");
        } else if (generatedSong == 1){
            musicCreator("Fairytale.wav");
        } else if (generatedSong >= 2){
            musicCreator("Pokemon League Night.wav");
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays a sound
    public static void soundCreator(String fileName){
        try{
            //saves the file name as a string
            String soundName = fileName;
            AudioInputStream audioStream  = AudioSystem.getAudioInputStream(new File(filePath + soundName));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception ex){}
        //If a sound is playing
        if (clip.isRunning()){
            //stop iot
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
        }
        //start the sound
        clip.start();
    }

    //--------------------------------------------------------------------------------------------------------------
    //This method plays and creates music
    public static void musicCreator(String fileName){
        try{
            //creates new AudioInputStream object for the file
            String soundName = fileName;
            AudioInputStream audioStream  = AudioSystem.getAudioInputStream(new File(filePath + soundName));
            music = AudioSystem.getClip();
            music.open(audioStream);
        } catch (Exception ex){}
        //start the music
        music.start();
        //loops the music
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }
;
}
