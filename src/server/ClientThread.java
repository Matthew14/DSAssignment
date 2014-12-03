package server;

import shared.BiddingItem;
import shared.ConstantValues;

import java.io.*;
import java.net.Socket;

/**
 * The thread that is assigned to a client
 *
 * @author Matthew O'Neill
 */
public class ClientThread extends Thread {

    Socket clientSocket;
    BiddingItem currentItem;
    ObjectOutputStream objectOutputStream;
    BufferedReader in;

    public ClientThread(Socket clientSocket, BiddingItem currentItem) throws IOException {
        this.clientSocket = clientSocket;
        this.currentItem = currentItem;

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        OutputStream outputStream = clientSocket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);

    }

    public void run(){

        for (;;){
            try {
                objectOutputStream.writeUnshared(currentItem);
                objectOutputStream.flush();

                if(in.ready())
                {
                    double bid = Double.parseDouble(in.readLine());

                    if(bid > currentItem.getBid())
                        currentItem.setCurrentBid(bid);
                }
                sleep(1000);

            } catch (InterruptedException e) {
                System.out.println(ConstantValues.clientExiting);
                break;
            } catch (IOException e) {
                System.out.println(ConstantValues.clientExiting);
                break;
            }
        }
    }
}
