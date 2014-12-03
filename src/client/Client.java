package client;

import shared.ConstantValues;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static int portNo;
    private static Socket socket;

    public static void main(String[] args){

        if(args.length < 1) {
            exitOnError(ConstantValues.noPortSpecifiedError);
        }
        try{
            portNo = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            exitOnError(ConstantValues.portNotAnIntError);
        }

        try {
            socket = new Socket(InetAddress.getLocalHost(), portNo);
            InputStream inStream = socket.getInputStream();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new ReadFromServerThread(inStream).start();

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String input;
            boolean isQuit;
            do {
                input = keyboard.readLine();

                isQuit = input.toLowerCase().equals(ConstantValues.quitWord);
                while(!isQuit&&!isNumeric(input)){
                    System.out.print("I'm looking for a number or 'quit': ");
                    input = keyboard.readLine();
                    isQuit = input.toLowerCase().equals(ConstantValues.quitWord);
                }
                if(!isQuit)
                    out.println(input);

            }while(!input.toLowerCase().equals(ConstantValues.quitWord));

        } catch (UnknownHostException e) {
            exitOnError(ConstantValues.unknownHostError);
        } catch (IOException e) {
            exitOnError(ConstantValues.errorCreatingSocket);
        } finally{
            try{
                socket.close();
            }
            catch(IOException e){
                exitOnError(ConstantValues.exitingNow);
            }
        }
    }

    private static boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
        }
        catch(NumberFormatException e){
            return false;
        }

        return true;
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
