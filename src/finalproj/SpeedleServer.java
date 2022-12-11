// CREATOR: ANDREW VU

package finalproj;

import java.net.*;
import java.io.*;
import java.util.*;

public class SpeedleServer {
	private static List<Socket> clients = new ArrayList<>();	// list of clients to use in the future
	
	/**
	 * Function to get a clue on what word is trying to be guessed using a hashmap
	 * @param ans - the randomly chosen word that is trying to be guessed
	 * @param guess - the client's guess
	 * @return clue - the client's guess but with hints on where letters are positioned
	 */
	public static String getClue(String ans, String guess) {
		String clue = "";
		guess = guess.toLowerCase(); // for consistency

		// use hashmap to store frequencies of the characters in the answer
		HashMap<Character, Integer> counter = new HashMap<Character, Integer>();
		for (int i = 0; i < ans.length(); i++) {
			char curr = ans.charAt(i); // char at this moment
			counter.put(curr, counter.getOrDefault(curr, 0) + 1); // put the char into the map
		}

		// construct the clue string based on the guess
		for (int i = 0; i < guess.length(); i++) {
			char curr = guess.charAt(i);
			if (curr == ans.charAt(i) && counter.get(curr) > 0)			// give an uppercase letter for correct position
				clue += Character.toUpperCase(curr);
			else if (ans.indexOf(curr) != -1 && counter.get(curr) > 0)	// give lowercase letter for wrong position but in answer
				clue += Character.toLowerCase(curr);
			else														// give '_' for letter is not in the word at all
				clue += '_';
			counter.put(curr, counter.getOrDefault(curr, 0) - 1);		// subtract from the count hashmap count that we have
		}

		return clue;
	}
	
/**
 * 	Class to handle multiple clients (in this case 2 clients total) simultaneously
 * 	Each ClientHandler has its own socket and clientNumber and also has the word answer
 */
static class ClientHandler implements Runnable {
	private final Socket socket;
	private final String answer;
	private final int clientNumber;

	// constructor
	public ClientHandler(Socket socket, String answer, int clientNumber) {
		this.socket = socket;
		this.answer = answer;
		this.clientNumber = clientNumber;
	}

	// runnable interface for running multiple threads at once
	@Override
	public void run() {
		try {
			// each client handler has its own din and dout
			DataInputStream din = new DataInputStream(socket.getInputStream());
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

			// wordle setup
			int guess_limit = 6;

			// runs for guess_limit attempts
			for (int i = 1; i <= guess_limit; i++) {
				String readGuess = din.readUTF();											// reads a guess from the client
				System.out.println("Client " + clientNumber + " guessed: " + readGuess);	// output to follow along with the server
				String guessClue = getClue(answer, readGuess);								// uses getClue function to get the guess returned with the clues
				System.out.println("After getClue, server will pass: " + guessClue);		// output to follow along with the server
				
				// check if its the right word and write output to client, then we don't need to
				// go through the rest of the for loop iterations
				if (guessClue.toUpperCase().equals(answer.toUpperCase())) {
					System.out.println("Client " + clientNumber + " wins and correctly guessed the word");
					
					// write to the client that they have won the game
					dout.writeUTF("Congrats, you won the game with your guess: " + guessClue + "\n");		
					
					// notifies all other clients excluding this one that someone else has won
					for (Socket otherClient : clients) {
						if (otherClient == socket) {
							continue;
						}
						
						// make a new DataOutputStream to write to the other clients that they have lost
						// so that the original dout is not overwritten
						DataOutputStream otherDout = new DataOutputStream(otherClient.getOutputStream());
						otherDout.writeUTF("You lose! The game has ended because Client " + clientNumber + " has already won. "
										 + "The correct word was: " + answer + "\n");
						otherDout.flush();
					}
					break;
				} 
				// if its not the right word, just write the returned guess to the client
				else { 
					dout.writeUTF(guessClue);
				}

				dout.flush();
				// default losing condition and message to write to the client
				// only occurs on the guess_limit's wrong guess
				if (i == guess_limit && !guessClue.toUpperCase().equals(answer.toUpperCase())) {
					System.out.println("Client " + clientNumber + " has ran out of guesses and lost.");
					dout.writeUTF("You guessed " + guess_limit + " times and still lost. The correct word was: " + answer + "\n");
					dout.flush();
				}
			}

			// close the socket and remove it from the clients list
			din.close();
			dout.close();
			socket.close();
			clients.remove(socket);
			
		} catch (IOException e) {	// catch any exceptions and close the socket
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			clients.remove(socket);
		}
	}
}

	public static void main(String[] args) throws IOException {
		// read all the lines in the 5 letter words file to an arraylist of words
		BufferedReader reader = new BufferedReader(new FileReader("words5letter.txt"));
		String line = reader.readLine();
		List<String> words = new ArrayList<String>();
		while (line != null) {
			String[] wordsLine = line.split(" ");
			for (String word : wordsLine) {
				words.add(word);
			}
			line = reader.readLine();
		}
		reader.close();

		// socket setup
		ServerSocket ss = new ServerSocket(6666);

		// select random word from the list of words to be the answer
		Random rand = new Random(System.currentTimeMillis());
		String answer = words.get(rand.nextInt(words.size()));
		
		// output to server 
		System.out.println("Server online.");
		// to show that there's an answer generated in the server that is secure from the client
		System.out.println("test: answer is: " + answer);
		System.out.println("Waiting for clients to connect...");
		// create a thread for each client that connects to the server
		// can be more depending on how many iterations of the for loop are wanted
		int playerCount = 3;
		for (int i = 1; i <= playerCount; i++) {
			// accept up to playerCount clients
			Socket socket = ss.accept();
			System.out.println("Client " + i + " has joined!");
			System.out.println("Need " + (playerCount - i) + " more clients to start the game");
			// add the client to the list of client sockets
			clients.add(socket);
			// start their thread with a ClientHandler attached to each client
			new Thread(new ClientHandler(socket, answer, i)).start();
		}
		System.out.println("Game Started!");
		// close the ServerSocket after the for loop is over
		ss.close();
	}
}
