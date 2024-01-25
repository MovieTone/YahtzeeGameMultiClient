package com.movietone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

// Server class 
public class Server {

    // Vector to store active clients
    static LinkedList<ClientHandler> players = new LinkedList<ClientHandler>();

    // counter for clients
    static int playersNumber = 0;

    static Yahtzee yahtzee;

    public static void main(String[] args) throws IOException {

        // server is listening on port 1234
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            Socket socket;
            yahtzee = new Yahtzee();

            System.out.println("Yahtzee Game Engine is starting. Waiting for players to join ...");

            while (true) {
                // Accept incoming request
                socket = serverSocket.accept();

                playersNumber++;

                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

                ClientHandler player = new ClientHandler(socket, playersNumber, dataInput, dataOutput, yahtzee);

                Thread t = new Thread(player);

                // add this player to active clients list
                players.add(player);

                // start the thread.
                t.start();
            }
        }
    }

}

// ClientHandler class
class ClientHandler implements Runnable {

    Scanner scn = new Scanner(System.in);
    private volatile int playerNumber;
    private volatile int round;
    final DataInputStream dataInput;
    final DataOutputStream dataOutput;
    Socket socket;
    boolean startedGame;
    private final Yahtzee yahtzee;
    private static volatile boolean finished = false;

    // constructor
    public ClientHandler(Socket socket, int playerNumber, DataInputStream dataInput, DataOutputStream dataOutput, Yahtzee yahtzee) {
        this.dataInput = dataInput;
        this.dataOutput = dataOutput;
        this.playerNumber = playerNumber;
        this.round = 1;
        this.socket = socket;
        this.startedGame = true;
        this.yahtzee = yahtzee;
    }

    public void run() {

        String line;

        try {
            yahtzee.createPlayer(dataOutput, dataInput);

            if (playerNumber == 1) {
                dataOutput.writeUTF("Ready player One?");
                read(dataInput);
                yahtzee.setStarted();
            } else {
                dataOutput.writeUTF("\nWaiting for player 1 to start the game ...");
                while (!yahtzee.isStarted())
                    ;
            }

            Thread.sleep(playerNumber * 10L);

            yahtzee.start(dataOutput, dataInput, playerNumber);

            while (true) {
                if (finished) {
                    return;
                }
                if (gameFinished()) {
                    displayWinner();
                    return;
                }

                for (ClientHandler player : Server.players) {
                    while ((player.playerNumber > playerNumber && player.round < round)
                        || (player.playerNumber < playerNumber && player.round == round))
                        ;
                }
                Thread.sleep(playerNumber * 10L);
                if (round < YahtzeeConstants.NUMBER_OF_ROUNDS + 1) {
                    round = yahtzee.nextRound(dataOutput, dataInput, playerNumber);
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            this.dataInput.close();
            this.dataOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayWinner() throws IOException {
        int max = 0;
        playerNumber = 0;
        for (ClientHandler player : Server.players) {
            int score = yahtzee.sumScores(player.playerNumber, YahtzeeConstants.ONES, YahtzeeConstants.YAHTZEE);
            if (score > max) {
                max = score;
                playerNumber = player.playerNumber;
            }
        }
        for (ClientHandler player : Server.players) {
            player.dataOutput.writeUTF("Congratulations, " + yahtzee.getName(playerNumber)
                + " has won the game with a score of " + max + " points!!!");
            player.dataOutput.writeUTF("Great game, everyone, and thanks for playing. Goodbye.");
        }

        System.out.println("Game Complete");
    }

    private boolean gameFinished() {
        for (ClientHandler player : Server.players) {
            if (player.round != YahtzeeConstants.NUMBER_OF_ROUNDS + 1) {
                return false;
            }
        }

        finished = true;
        return true;
    }

    public static String read(DataInputStream dis) throws IOException {
        String line = null;
        while ((line = dis.readUTF()) == null)
            ;
        return line;
    }
}