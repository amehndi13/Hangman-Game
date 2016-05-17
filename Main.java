//Anuj Mehndiratta JHED: amehndi1
//Surya Ram JHED: sram1
//Data Structures Project 1
//September 14th, 2015

import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
/**
 * This is the class.
 */
public final class Main {
    /**
     * The list of all words in the dictionary being fed into the program.
     */
    private static ArrayList<String> list = new ArrayList<String>();
    /**
     * The list of all words currently in play.
     */
    private static ArrayList<String> temp = new ArrayList<String>();
    /**
     * The list of all the guesses the user has entered.
     */
    private static ArrayList<String> guesses = new ArrayList<String>();
    /**
     * The length of the word that is being used for that game.
     */
    private static int wordLength;
    /**
     * The guess that the user enters.
     */
    private static String guess;
    /**
     * The number of guesses the user has left.
     */
    private static int guessesLeft;
    /**
     * The StringBuffer of dashes that is being updated throughout the 
     * game and that is being displayed to the user.
     */
    private static StringBuffer wordDashes;
    /**
     * Just here to avoid Checkstyle errors.
     */
    private Main() {
    }
    /**
     * This is the main driver of our program. It handles all of
     *  the game play and makes the necessary calls to the methods below.
     * @param args The first argument is the .txt dictionary file being 
     * read into the game  and there is an optional debug mode, which 
     * would be the second element in the String[].
     * @throws IOException if the input file isnt there.
     */
    public static void main(String[] args) throws IOException {
        boolean wantsToPlay = true;
        System.out.println("Welcome to the hangman game!");
        System.out.println("You may guess the full word at any "
                + "point in the game, but if you get it wrong you lose");
        System.out.println("You must guess the word correctly "
                + "before guessing six incorrect letters. \nEnjoy!");
        Scanner kb = new Scanner(System.in);
        Scanner inFile = new Scanner(new File(args[0]));

        while (inFile.hasNext()) {
            list.add(inFile.next());
        }

        inFile.close();

        int wins, losses;
        wins = 0;
        losses = 0;

        while (wantsToPlay) {
            System.out.println("Wins: " + wins + " Losses: " + losses);
            System.out.println("Total Games: " + (wins + losses));
            String word = getRandom(list);
            wordLength = word.length();
            fillTemporaryArray();

            wordDashes = dashCreate(word);
            System.out.println("Here is the word that you will be "
                    + "trying to guess " + wordDashes);
            guessesLeft = 2 + 2 + 2;
            //IN GAME LOOP

            while (guessesLeft > 0) {
                if (args.length > 1) {
                    if (args[1].equals("DEBUG")) {
                        printTemporaryArray();
                    }
                }
                System.out.println("Remaining guesses: " + guessesLeft);
                System.out.println("" + wordDashes);
                System.out.print("Enter a character or word guess: ");
                guess = kb.nextLine();

                if (guess.length() == 1) {
                    letterGuess(guess);
                } else {
                    wordGuess(guess);
                }
            }
            temp.clear();
            if (guessesLeft == 0) {
                System.out.println("Sorry, you lose");
                losses++;
            } else if (guessesLeft == -1) {
                System.out.println("Congrats, you won!");
                wins++;
            }
            System.out.println("Do you want to play again? (y/n)");
            String yesOrNo = kb.nextLine();
            if (yesOrNo.equals("n")) {
                wantsToPlay = false;
            }
        }
    }

    /**
     * This method deals with all the possible outcomes from 
     * when the user enters a letter as a guess. This is where
     * we spent the majority of our time.
     * @param guessInput This is the users guess.
     */
    public static void letterGuess(String guessInput) { 
        boolean badguess = false;
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).indexOf(guessInput) == -1) {
                badguess = true; // Check and see if we can say badguess.
            }
        }
        if (badguess) {
            System.out.println("Sorry, that is a bad guess.");
            guesses.add(guessInput);
            for (int i = temp.size(); i > 0; i--) {
                if (temp.get(i - 1).indexOf(guessInput) != -1) {
                    temp.remove(i - 1);
                }
            }
            guessesLeft--;
        } else { // for a correct guess.
            int lowestguess = wordLength;
            for (int i = 0; i < temp.size(); i++) {
                if (countOccurrences(temp.get(i),
                            guess.toCharArray()[0]) < lowestguess) {
                    lowestguess = countOccurrences(temp.get(i),
                            guessInput.toCharArray()[0]);
                }
            }
            for (int i = 0; i < temp.size(); i++) {
                if (countOccurrences(temp.get(i),
                            guess.toCharArray()[0]) > lowestguess) {
                    temp.remove(i);
                }
            }
            goodGuess();
            setGuessesLeft();
        }
    }
    /**
     * This method deals with a full word guess by the user.
     * @param guessEntry This is the word that the user has guessed.
     */
    public static void wordGuess(String guessEntry) {
        if (guessEntry.equals(temp.get(0))) {
            guessesLeft = -1;
        } else {
            guessesLeft = 0;
        }
    }
    /**
     * This method updates the dashes that the user sees as 
     * he/she correctly guesses letters in the word. 
     * This method is based off of Professor Selinski's code for Intro Java.
     * @param word This is the word that the method is 
     * converting into letters and dashes.
     * @param dashes This is the StringBuffer that the method 
     * is adding letters to in the appropriate locations.
     * @param letter This is the letter that the method is 
     * adding to the StringBuffer.
     */
    public static void mergeDashes(String word, StringBuffer dashes,
            char letter) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                dashes.setCharAt(i, letter);
            }
        }
    }
    /**
     * This method takes a string and converts it to a series 
     * of dashes based on the length of the word.
     * This method is based off of Professor Selinski's code for Intro Java.
     * @param word This is the string that the method is converting to dashes.
     * @return StringBuffer This is the StringBuffer that the method returns
     * with dashes and letters in the appropriate indices.
     */
    public static StringBuffer dashCreate(String word) {
        StringBuffer dashes = new StringBuffer(word.length());
        for (int count = 0; count < word.length(); count++) {
            dashes.append('-');
        }
        return dashes;
    }

    /**
     * This method gets a string from a random index in an ArrayList.
     * @param alist this is the ArrayList that the method 
     * is getting the string from.
     * @return a randomly selected string.
     */
    public static String getRandom(ArrayList<String> alist) {
        int rnd = new Random().nextInt(alist.size());
        return alist.get(rnd);
    }

    /**
     * This method counts the number of times a given letter
     *  appears in a string.
     * @param word This is the string that the method is counting 
     * the number of occurences of a specific letter in.
     * @param letter This is the character that the method is counting 
     * the occurences of in a given string.
     * @return integer represent number of occurrences.
     */
    public static int countOccurrences(String word, char letter) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method prints the array of words that are currently in play.
     */
    public static void printTemporaryArray() {
        for (int i = 0; i < temp.size(); i++) {
            System.out.println("" + temp.get(i));
        }
    }
    // Filling the temporary ArrayList with the words that we 
    // need for that game
    /**
     * This method fills the array with words that are in play during 
     * the given game.
     */
    public static void fillTemporaryArray() {
        for (int i = 0; i < list.size(); i++) {
            int counter = 0;
            if (list.get(i).length() == wordLength) {
                String a = list.get(i).toLowerCase();
                temp.add(counter, a);
                counter++;
            }
        }
    }
    /**
     * This method deals with a correct letter guess.
     */
    public static void goodGuess() {
        if (temp.size() > 1) {
            mergeDashes(getRandom(temp), wordDashes,
                    guess.toCharArray()[0]);
            System.out.println("Good guess!");
            System.out.println("" + wordDashes);
            for (int i = temp.size(); i > 0; i--) {
                String comp1 = wordDashes.toString();
                StringBuffer comp2 = dashCreate(temp.get(i - 1));
                mergeDashes(temp.get(i - 1),
                        comp2, guess.toCharArray()[0]);
                if (!comp1.equals(comp2.toString())) {
                    temp.remove(i - 1);
                }
            }
        } else {
            mergeDashes(temp.get(0), wordDashes, guess.toCharArray()[0]);
            System.out.println("Good guess!");
            System.out.println("" + wordDashes);
        }
    }
    /**
     * This method sets the guesses left int.
     */
    public static void setGuessesLeft() {
        if (temp.size() == 1 && temp.get(0).equals(wordDashes.toString())) {
            guessesLeft = -1;
        }
    }
}

