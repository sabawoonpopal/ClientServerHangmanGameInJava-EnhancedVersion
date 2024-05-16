import java.io.*;
import java.net.*;
import java.util.Random;

public class IMProtocol {
    private static final int WAITING = 0;
    private static final int IN_GAME = 1;
    private static final int FINISHED_GAME = 2;
    private static final int REQUEST_NEW_GAME = 3;
    private static final int NEW_GAME = 4;
    private int attemptsLeft;
    private char letterFound;
    private int state = WAITING;
    private Random random;
    private int chosenWord;
    private char[] splittedWord;
    private char[] guessedWord;
    private int amountOfLetters;
    private String clientName;
    private Leaderboard leaderboard;
    private String[] words = { "turnip", "little", "ligament", "please", "tractor" };

    public IMProtocol(String clientName, Leaderboard leaderboard) {
        this.clientName = clientName;
        this.leaderboard = leaderboard;
        random = new Random();
    }

    public String processInput(Message theInput) {
        String theOutput = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        if (state == WAITING) {
            theOutput = "Welcome to hangman! A word has already been selected. You have 6 attempts\nto guess the word!";
            attemptsLeft = 6;
            letterFound = ' ';
            chosenWord = random.nextInt(5);
            splittedWord = words[chosenWord].toCharArray();
            guessedWord = new char[splittedWord.length];
            amountOfLetters = guessedWord.length;
            state = IN_GAME;
        } else if (state == IN_GAME) {
            if (attemptsLeft > 0) {
                try {
                    char guessedLetter = theInput.getCharContent();
                    boolean found = false;
                    for (int i = 0; i < splittedWord.length; i++) {
                        if (splittedWord[i] == guessedLetter) {
                            found = true;
                            guessedWord[i] = guessedLetter;
                            theOutput = "Good job! " + guessedLetter + " was in the word!";
                            for (int j = 0; j < guessedWord.length; j++) {
                                if (guessedWord[j] == '\u0000') {
                                    guessedWord[j] = '_';
                                    theOutput += " " + guessedWord[j];
                                } else {
                                    theOutput += " " + guessedWord[j];
                                }
                            }
                        }
                    }
                    int count = 0;
                    for (int k = 0; k < guessedWord.length; k++) {
                        if (guessedWord[k] == splittedWord[k]) {
                            count++;
                        }
                        if (count == amountOfLetters) {
                            leaderboard.addWin(clientName);
                            theOutput = "CONGRATULATIONS! YOU WIN THE GAME! You now have " + leaderboard.getWins(clientName) + " win(s).\n\n"; 
                            theOutput += leaderboard.getLeaderboard();
                            theOutput += "Would you like to play again? (y/n)";
                            state = REQUEST_NEW_GAME;
                        }
                    }
                    if (!found) {
                        attemptsLeft--;
                        theOutput = "I'm sorry, " + guessedLetter + " is not in the word.\nYou have " + attemptsLeft + " attempt(s) left.\n";
                        if (attemptsLeft == 0) {
                            theOutput += "You ran out of guesses. Press any key to finalize the round..";
                        }
                    }
                } catch (IllegalArgumentException e) {
                    theOutput = "You entered more than one character, or an illegal character. Please try again.";
                }
            } else {
                theOutput = "Game over. You lose.\n";
                theOutput += "The word was " + words[chosenWord] + "\n";
                theOutput += "Would you like to play again?";
                state = REQUEST_NEW_GAME;
            }
        } else if (state == REQUEST_NEW_GAME) {
            if (theInput.getCharContent() == 'y') {
                theOutput = "Selecting another word to be guessed... press any key to continue...";
                state = WAITING;
            } else {
                theOutput = "Goodbye!";
            }
        }
        return theOutput;
    }
}
