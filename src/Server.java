import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    //  private static boolean existsInList = false;
    //private static boolean connection = false;
    static Login login = new Login();
    static FileDir fileDir = new FileDir();
    static boolean userLogged = false;
    static String type; // file transfer type A - ascii, B - binary, C - continuous

    // new fri
    private static String args;
    private static final File defaultDirectory = FileSystems.getDefault().getPath("").toFile().getAbsoluteFile();
    private static File currentDirectory = defaultDirectory;




    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        // this port number must match the port number the client is using
        ServerSocket serverSocket = new ServerSocket(1234);
        boolean connected = true;
        if(serverSocket.isBound()){
            System.out.println("+COMPSYS725 SFTP Service");
        }else{
            System.out.println("-COMPSYS725 Out to Lunch");
            return;
        }

        //binding client
        clientSocket = serverSocket.accept();
        boolean listeningClient = true;


        // first while loop is to accept client connection
        while (connected) {

            try {
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);



                // second while loop to constantly send messages
                while (listeningClient) {
                    System.out.println("Server:while:listening");
                    // user input
                    String msgFromClient = bufferedReader.readLine();
                    // System.out.println("Client: " + msgFromClient);
                    // USER,ACCT,PASS etc..
                    String cmd = msgFromClient.substring(0, 4);


                    String currentDirectory = System.getProperty("user.dir");


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

                        case "TYPE":
                            TYPE(msgFromClient);
                            break;

                        case "LIST":
                             LIST(msgFromClient, fileDir,currentDirectory);
                           // handleList(msgFromClient, currentDirectory);
                            break;

                        case "CDIR":
                          System.out.println("CDIR");
                          CDIR();
                          break;


                        case "KILL":
                            System.out.println("KILL");
                            break;

                        case "NAME":
                            System.out.println("NAME");
                            break;

                        case "RETR":
                            System.out.println("RETR");
                            RETR();
                            break;


                        case "STOR":
                            System.out.println("STOR");
                            STOR();
                            break;

                        case "DONE":
                            System.out.println("DONE");
                                System.out.println(login.user + " + while: closing connection ");
                                listeningClient = false;
                                break;




                        default:
                            System.out.println("Unknown option");

                    }
                    bufferedWriter.write("MSG Received");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    // not working - client side working.. server side not closing
                     if (msgFromClient.equalsIgnoreCase("DONE")) {
                        System.out.println(login.user +" + closing connection ");
                        connected = false;
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            inputStreamReader.close();
            outputStreamWriter.close();
            bufferedReader.close();
            bufferedWriter.close();
            serverSocket.close();
            clientSocket.close();
        }

        if(serverSocket.isClosed()){
            System.out.println("-COMPSYS725 Out to Lunch");

        } else {

            System.out.println("+COMPSYS725 SFTP Service");
        }

    }


    private static void USER(String inputUser) {
        String username = inputUser.substring(5);
        System.out.println("server:user:inputuser = " + inputUser);
        System.out.println("server:user:username = " + username);
        userLogged = false;
        if (login.checkUser(username)) {
            // see how to change
            System.out.println("server:user:if");
            if (login.checkLogin(username)) {
                System.out.println("server:user:if:if");
                userLogged = true;
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
            System.out.println("-Invalid account, try again");
        }

    }

    private static void PASS(String inputPass) {

        String username = login.user;
        String password = inputPass.substring(5);

        //System.out.println("Server.ACCT.username:" + username);
        //System.out.println("Server.ACCT.Account:" + account);

        if (login.password.equals(password)) {
            System.out.println("! Logged in Password is ok and you can begin file transfers.");
            userLogged = true;
            System.out.println("server:pass:userlogged " + userLogged);

        } else {
            System.out.println("-Wrong password, try again");
        }
    }

    private static void TYPE(String inputType) {
         type = inputType.substring(5);
        if (userLogged) {
            if (type.equals("A")) {
                System.out.println("+Using Ascii mode");
            } else if (type.equals("B")) {
                System.out.println(" +Using Binary mode");
            } else if (type.equals("C")) {
                System.out.println("+Using Continuous mode");
            } else {
                System.out.println("-Type not valid");
            }

        } else {
            System.out.println("Please login to continue");
        }
    }

  private static void LIST(String inputList, FileDir fileDir, String currentDirectory) throws IOException {
        if (userLogged) {
            // listing - F or V
            String listing = inputList.substring(5, 6);

          //  FileDir fileDir;

            String dir = currentDirectory + "\\" + inputList.substring(6).trim();
            if (listing.contentEquals("F")) {
                String toPrint = fileDir.listFiles(dir, "F");
               // System.out.println("F works");
                System.out.println(toPrint);
            } else if (listing.contentEquals("V")) {
                String toPrint =  fileDir.listFiles(dir, "V");
               // System.out.println("V works");
                System.out.println(toPrint);
            } else {
                System.out.println("login");
            }
        }

    }

    private static void CDIR(){
        System.out.println("inside CDIR");
      }

    private static void STOR(){
        System.out.println("inside STOR");
        System.out.println("file will be stored in the format, type : " +type );
    }

    private static void RETR(){
        System.out.println("inside RETR");
        System.out.println("file will be returned in the format, type : " +type );
    }









}






