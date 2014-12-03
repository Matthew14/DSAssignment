package shared;

/**
 * @author Matthew O'Neill / C11354316
 *
 * A class to hold all constants used
 */
public class ConstantValues {


    //The maximum bid time
    public static final int maxTime = 60;

    public static final String noPortSpecifiedError = "You need to specify a port, friend.";
    public final static String portNotAnIntError = "The port should really be a number, friend.";
    public final static String cannotUseThatPortError = "Can't start server with that port, friend.";

    public final static String unknownHostError = "Host ID not found, friend.";
    public final static String errorCreatingSocket = "There was an error creating a socket using the specified port";
    public final static String itemFilePath = "items.txt";
    public final static String itemFileLineSeparator = "\\|";
    public final static String quitWord = "quit";
    public final static String exitingNow = "exiting...";
    public final static String clientExiting = "A client is exiting";
    public final static String clientConnected = "A client connected";

}
