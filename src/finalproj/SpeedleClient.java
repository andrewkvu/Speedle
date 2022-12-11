// CREATOR: ANDREW VU

package finalproj;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class SpeedleClient {
	public static void main(String[] args) throws UnknownHostException, IOException {
		// socket setup
		Socket s = new Socket("localhost", 6666);  
		DataInputStream din = new DataInputStream(s.getInputStream());  
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());  
		Scanner scanner = new Scanner(new InputStreamReader(System.in)); 
		
		// introduction and rules to the program
		System.out.println("\nWelcome to Speedle (Speed Wordle)! \n"
				+ "~ This program is based off of the popular web game Wordle in which you have 6 guesses to guess a random word that is chosen.\n"
				+ "~ This program uses client and server programming to communicate with each other.\n"
				+ "~ Every guess, you will have a chance to uncover letters of the answer. \n"
				+ "~ Letters that are revealed in UPPERCASE mean that they are in the CORRECT position of the answer. \n"
				+ "~ Letters that are revealed in LOWERCASE mean that they are in the answer but in the WRONG position. \n"
				+ "~ This game is basically like Wordle, but you play against other players at the same time. \n"
				+ "~ Whoever guesses the word first will win the game, regardless of if it took you more turns than your opponent. \n"
				+ "~ However, if you use up all 6 guesses and do not guess the word or guess the word after someone has already won, you lose. \n"
				+ "~ Thus, the whole point of the game is to show how fast you can think rather than how many turns it takes you to guess. \n"
				+ "~ ENJOY! \n\n"
				);
		
		// wordle setup
		int guess_limit = 6;
		int numChars = 5;
		
		// runs for guess_limit attempts
		for (int i = 1; i <= guess_limit; i++) {
			System.out.print("Guess the word (" + i + "): ");
			String guess = scanner.next();
			
			// checking to make sure the guess is a valid guess based on number of characters, and reguess based on that
			while (guess.length() != numChars) {
				System.out.print("Invalid guess, guess again (" + i + "): ");
				guess = scanner.next();
			}
			
			// write to the server this valid guess and flush after to reset
			dout.writeUTF(guess);
			dout.flush();
			
			String guessWithHints = din.readUTF();
			if (guessWithHints.contains("Congrats")) { // right answer
				System.out.println(guessWithHints);
				break;
			}
			else if (guessWithHints.contains("The game has ended")) { // someone already guessed the right answer so it is written to UTF that the game is over
				System.out.println(guessWithHints); // separate conditions for simpler understanding
				break;
			}
			else { // wrong answer
				System.out.println("Your guess returned this: " + guessWithHints);
				if (i == guess_limit) {
					String loserMessage = din.readUTF();
					System.out.println(loserMessage);
				}
			}
		}
		
		dout.close();  
		s.close();  
	}
}
