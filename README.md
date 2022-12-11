

Andrew Vu

CS 158A

12/9/22

https://youtu.be/d_zY65QWP_4

CS158A Project Formal Report - Speedle (Speed Wordle)

Wordle is a popular web game in which players have to guess a random 5-letter word by

uncovering letters of the answer. However, since Wordle is a single-player game, there is no

real-time competition against other people. Thus, using Java client and server programming in

my project, I created a version of Wordle called “Speedle” that incorporates both client and

server programming to allow players to compete against each other and play the game over a

network. In my version of the game, players can connect to a server and take turns guessing the

word, with each guess receiving a hint of which letters are in the correct position or in the word

but in the wrong position. The goal of the game is to guess the word the fastest and before all

other players, regardless of the number of turns it takes. The game ends when one of the players

successfully guesses the word or when the player runs out of guesses. This version of the game

adds an extra layer of challenge and interactivity by allowing players to compete against each

other over a network.

In this project, there are two classes: a SpeedleClient and a SpeedleServer class, along

with a text file of all possible 5-letter words that are available from the original Wordle game.

Like all other client-server programs, the server must be run before the client to set up the game,

and they work together to communicate in order for the game to work properly. The server reads

all of the words in the file of all possible 5-letter words into an ArrayList and selects a random

word from this list as the answer for the current game.


![image](https://user-images.githubusercontent.com/68661570/206899428-2fb827e9-47a4-496c-9704-def3eeb9f95e.png)



When each client connects to the server, the client is given a set of introductory rules about the

game. Specifically, the rules state that the letters revealed in uppercase are in the correct position

of the answer, whereas the letters revealed in lowercase are in the word but in the wrong position

of the answer. It also mentions the purpose and goal of the game.![image](https://user-images.githubusercontent.com/68661570/206899442-fb556ece-d37b-4b62-a628-55a09f86d1d3.png)


After introducing the rules, the client program prompts the user to guess the word with their

attempt number next to it. If the guess is invalid (not 5 letters long), the client prompts the user to

guess again. After the client receives a valid guess, it uses a DataOutputStream of this socket and

the writeUTF method to write the guess to the server.

![image](https://user-images.githubusercontent.com/68661570/206899470-731a8157-0a94-436e-aafc-13dab1a925e4.png)


In the SpeedleServer class, there is another ClientHandler class that deals with multiple clients at

the same time using threads. The ClientHandler class within the SpeedleServer implements the

Runnable interface and has a run method that handles each client. Each ClientHandler has its

own DataInputStream and DataOutputStream to read information from the client and write out

results to the client.

![image](https://user-images.githubusercontent.com/68661570/206899479-96bd325d-ed5c-4304-8249-0792a4480be0.png)


The server reads the guess from the client and passes it to a function called getClue, which

calculates which letters are in correct or incorrect positions in the guess compared to the actual

answer.

![image](https://user-images.githubusercontent.com/68661570/206899484-9a8de010-1271-40b5-8842-d8179bdc2bb8.png)




Based on this tuned guess with clues, the server will decide whether the guess matches the

correct answer or not. If it does, the server will write back to the client that they have won and

notify the other clients that they have lost and the game has ended. If the guess is incorrect, the

server writes the guess with hints back to the client so that the client can guess again based on

this new information. The client and server keep going back and forth until someone has won or

the client has reached their guess limit and has won or lost.![image](https://user-images.githubusercontent.com/68661570/206899501-e3cd1e27-00a2-4518-b563-635a6e2a439b.png)


In conclusion, the game runs smoothly and well.

![image](https://user-images.githubusercontent.com/68661570/206899521-5a04b604-58e9-4daf-bd34-98df4a25ba71.png)
![image](https://user-images.githubusercontent.com/68661570/206899529-45fff31d-56c5-4b38-89aa-b08a9c861737.png)
![image](https://user-images.githubusercontent.com/68661570/206899534-3c207774-9f4a-469b-a054-5ce3b5ee6cc7.png)




Initially, I started by making the basic Wordle functionality and understanding how it worked, as

I knew that incorporating client-server programming into Wordle would make sense. However, I

felt that it wasn’t complex enough with just one client, which led to me attempting to have

multiple clients involved in the game. It was difficult to understand, but I learned a lot from

completing this project. Specifically, I learned how to recreate the functionality of Wordle, as

well as incorporate client-server programming into it and eventually learn to be able to have

multi-threading to have multiple clients. One shortcoming of this project is that my

implementation requires an exact number of players in order for the game to be played. It can be

changed based on the number of iterations in the for loop of the main method of the

SpeedleServer class to fit a certain number of players, but ideally the program should be able to

accept any number of players up to a set max number of players. Another shortcoming of the

project is that the game currently is played in the terminal, which is not as accessible for people

who are not familiar with the terminal. Ideally, there should be a GUI that makes it easier for the

user to access the game, similarly to the original Wordle. However, I still believe that my

implementation of both client and server programs gets the main idea across in creating this

project, which is that the goal of the game is to compete against other clients and guess the word

before they can. Thus, I would say that this project has been a successful experience in using

client-server programming.

