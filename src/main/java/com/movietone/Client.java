package com.movietone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    final static int PORT = 1234;
    static Scanner in;
    static DataInputStream dis;
    static DataOutputStream dos;

    public static void main(String[] args) throws IOException {
        in = new Scanner(System.in);

        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection 
        Socket s = new Socket(ip, PORT);

        // obtaining input and out streams 
        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread 
        Thread sendMessage = new Thread(() -> {
            while (true) {
                // read the message to deliver.
                String msg = in.nextLine();

                try {
                    // write on the output stream
                    dos.writeUTF(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // readMessage thread 
        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    // read the message sent to this client
                    String msg = dis.readUTF();
                    if (msg.equals("CLEAR_SCREEN")) {
                        // clear screen
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } else {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }
} 