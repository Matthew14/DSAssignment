package shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * @author Matthew O'Neill
 */
public class BiddingItem implements Serializable{

    String name;
    double bid;
    int remainingTime;

    transient Vector<BiddingItem> allItems;

    public BiddingItem() throws IOException {
        loadItemsFromFile(ConstantValues.itemFilePath);
        setNextItem();
        startTimer();
    }

    public BiddingItem(String name, double startingBid) throws IOException {
        this.name = name;
        bid = startingBid;
        remainingTime = ConstantValues.maxTime;
    }


    /**
     * Uses a java.util.Timer to decrement the remaining time to bid every second
     */
    private void startTimer(){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                synchronized (this){
                    if(--remainingTime <= 0)
                        setNextItem();
                }
            }
        }, 0, 1000);

    }

//region Getters and Setters

    public int getRemainingTime() {
        return remainingTime;
    }

    public double getBid() {
        return  bid;
    }

    public String getName() {
        return name;
    }

//endregion

    public synchronized void setCurrentBid(double newBid){
        bid = newBid;
        remainingTime = ConstantValues.maxTime;
        notify();
    }

    public synchronized void setNextItem(){

        BiddingItem item;
        do {
            Random random = new Random();
            int index = random.nextInt(allItems.size() - 1);
            item = allItems.elementAt(index);
        }while(name!=null && name.equals(item.getName()));

        name = item.getName();
        bid = item.getBid();
        remainingTime = ConstantValues.maxTime;
    }


    /**
     * Method for comparing the name and bid of an item
     * @param other the item to compare this item to
     * @return whether the given item is equal to this one
     */
    public boolean isEqualTo(BiddingItem other){
        return Math.abs(bid-other.getBid()) < 0.00001 && name.equals(other.name);
    }

    /**
     * Loads BiddingItems from a file.
     * The file should be in the format:
     * Item Name 1| 2.45
     * Item Name 2| 3.65
     *
     *
     * @param itemFilePath A file containing items to load
     * @throws IOException
     */
    private void loadItemsFromFile(String itemFilePath) throws IOException {

        Vector<BiddingItem> items = new Vector<BiddingItem>();

        BufferedReader br = new BufferedReader(new FileReader(itemFilePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineSplit = line.split(ConstantValues.itemFileLineSeparator);

            items.add(new BiddingItem(lineSplit[0], Double.parseDouble(lineSplit[1])));
        }
        br.close();

        allItems = items;
    }
}