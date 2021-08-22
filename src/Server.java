import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static boolean existsInList = false;
    private static boolean connection = true;
    static Login login = new Login();
    static boolean loggedIn = false;



    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ServerSocket serverSocket = null;

        // this port number must match the port number the client is using
        serverSocket = new ServerSocket(1234);


        // first while loop is to accept client connection
        // second while loop to constantly send messages

        while (true) {

            try {
                // welcome socket
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);


                // check connection - not complete
                if (connection == true) {
                    System.out.println("+COMPSYS725 SFTP Service");
                } else {
                    System.out.println("-COMPSYS725 Out to Lunch");
                }

                while (true) {
                    // user input
                    String msgFromClient = bufferedReader.readLine();
                    // System.out.println("Client: " + msgFromClient);
                    // USER,ACCT,PASS etc..
                    String cmd = msgFromClient.substring(0, 4);
                    switch (cmd) {
                        case "USER":
                            USER(msgFromClient);
                            break;

                        case "ACCT":
                            System.out.println("Checking: " + msgFromClient);
                            ACCT(msgFromClient);
                            break;

                        case "PASS":
                            System.out.println("Checking pass:" + msgFromClient);
                            PASS(msgFromClient);
                            break;
                    }
                    bufferedWriter.write("MSG Received");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    if (msgFromClient.equalsIgnoreCase("BYE"))
                        break;
                }
                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void USER(String inputUser) {
        String username = inputUser.substring(5);
        System.out.println("server:user:inputuser = " + inputUser);
        System.out.println("server:user:username = " + username);
        loggedIn = false;
        if (login.checkUser(username)) {
            // see how to change
            if (login.checkLogin(username)) {
                loggedIn = true;
                System.out.println("!" + username + " logged in");
            } else {
                System.out.println("+User-id valid, send account and password");
            }
        } else {
            System.out.println("-Invalid user-id, try again");
        }
    }

    private static void ACCT(String inputAcct) {
        String username = login.user;
        String account = inputAcct.substring(5);

        //System.out.println("Server.ACCT.username:" + username);
        //System.out.println("Server.ACCT.Account:" + account);

        if (login.account.equals(account)) {
            System.out.println("+Account valid, send password");
        } else {
            System.out.println("Invalid account, try again");
        }

        }

    private static void PASS(String inputPass) {

        String username = login.user;
        String password = inputPass.substring(5);

        //System.out.println("Server.ACCT.username:" + username);
        //System.out.println("Server.ACCT.Account:" + account);

        if (login.password.equals(password)) {
            System.out.println("   ! Logged in Password is ok and you can begin file transfers.");
        } else {
            System.out.println("-Wrong password, try again");
        }
    }




}


