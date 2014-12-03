package client;

import shared.BiddingItem;
import shared.ConstantValues;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author Matthew O'Neill
 *
 * This thread reads the data sent by the server and outputs it appropriately
 */
public class ReadFromServerThread extends Thread {

    InputStream in;
    ObjectInputStream objectInputStream;

    BiddingItem currentItem;

    public ReadFromServerThread(InputStream in){
        this.in = in;

        try {
            objectInputStream = new ObjectInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            currentItem = (BiddingItem) objectInputStream.readObject();
            printItemDetails(currentItem);

            for(;;) {

                BiddingItem newItem = (BiddingItem) objectInputStream.readObject();

                if(! newItem.isEqualTo(currentItem))
                {
                    if(!newItem.getName().equals(currentItem.getName()))  //If it's a new item, not a new price
                    {
                        try {
                            System.out.println("\nAuction for "+currentItem.getName()+ " has ended.\n" +
                                    "Winning Bid was: "+currentItem.getBid()+"\n\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    currentItem = newItem;
                    printItemDetails(currentItem);
                }

                sleep(1000);
            }
        } catch (IOException e) {
            System.out.println(ConstantValues.exitingNow);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printItemDetails(BiddingItem item){
        System.out.println("\nCurrent Item: "+ item.getName());
        System.out.println("Current Bid: "+ item.getBid());
        System.out.println("Time Left to Bid: "+ item.getRemainingTime());
        System.out.print("Enter Bid or type 'quit' to exit the program: ");
    }

}
