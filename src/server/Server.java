package server;

import shared.BiddingItem;
import shared.ConstantValues;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Matthew O'Neill
 *
 * The main server entry point.
 *
 * Listens for incoming connections and assigns a clientThread to each
 */


public class Server {
    private static int portNo;

    public static void main(String[] args) throws IOException {

        if(args.length < 1) {
            exitOnError(ConstantValues.noPortSpecifiedError);
        }
        try{
            portNo = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            exitOnError(ConstantValues.portNotAnIntError);
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNo);
        } catch (IOException e) {
            exitOnError(ConstantValues.cannotUseThatPortError);
        }

        BiddingItem currentItem = new BiddingItem();

        if(serverSocket != null){
            for (;;) {
                Socket client = serverSocket.accept();
                System.out.println(ConstantValues.clientConnected);
                ClientThread clientThread = new ClientThread(client, currentItem);
                clientThread.start();
            }
        }
    }

    /**
     * Displays an error, then exits with the exit code 1
     *
     * @param errorMessage The error to display to the user
     */
    private static void exitOnError(String errorMessage){
        System.out.println(errorMessage);
        System.exit(0x1);
    }
}
