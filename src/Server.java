import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Server {

    static Login login = new Login();
    static boolean userLogged = false;
    static String type; // file transfer type A - ascii, B - binary, C - continuous

    static boolean isAcc = false;


    static String currentDirectory = System.getProperty("user.dir");

    // from NAME
    static String oldFileName;

    // from RETR
    static String filePath;
    static OutputStream outputStream;

    static String sendReplyToClient;


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



       // for send
        OutputStream out = clientSocket.getOutputStream();


        // first while loop is to accept client connection
        while (connected) {

            try {
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                // second while loop to constantly send messages
                while (listeningClient) {
                   // System.out.println("Server:while:listening");
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
                         //   System.out.println("Checking: " + msgFromClient);
                            ACCT(msgFromClient);
                            break;

                        case "PASS":
                         //   System.out.println("Checking pass:" + msgFromClient);
                            PASS(msgFromClient);
                            break;

                        case "TYPE":
                            TYPE(msgFromClient);
                            break;

                        case "LIST":
                            LIST(msgFromClient);
                            break;

                        case "CDIR":
                          CDIR(msgFromClient);
                          break;

                        case "KILL":
                            KILL(msgFromClient);
                            break;

                        case "NAME":
                            NAME(msgFromClient);
                            break;

                        case "TOBE":
                            TOBE(msgFromClient);
                            break;


                    /*    RETR, SEND not complete
                        case "RETR":
                          //  System.out.println("RETR");
                          //  RETR(msgFromClient);
                            break;

                        case "SEND":
                         //   System.out.println("SEND");
                            SEND(msgFromClient);
                            break;


                     */

                        case "DONE":
                          //  System.out.println("DONE");
                             //   System.out.println(login.user + " + while: closing connection ");
                                sendReplyToClient = "+closing connection ";
                                listeningClient = false;
                                break;

                        default:
                            sendReplyToClient = "Unknown option. Please enter a valid command";
                            System.out.println(sendReplyToClient);

                    }
                   // System.out.println("Server:1000: Length " + sendReplyToClient.length());
                    bufferedWriter.write(sendReplyToClient);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                     if (msgFromClient.equalsIgnoreCase("DONE")) {
                     //   System.out.println(login.user +" + closing connection ");
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
          //  sendReplyToClient = "-COMPSYS725 Out to Lunch";
            System.out.println("-COMPSYS725 Out to Lunch");
            System.out.println(sendReplyToClient);

        } else {
          //  sendReplyToClient = "+COMPSYS725 SFTP Service";
            System.out.println("+COMPSYS725 SFTP Service");
           // System.out.println(sendReplyToClient);
        }

    }


    private static void USER(String inputUser) {

        String username = inputUser.substring(5);
    //    System.out.println("server:user:inputuser = " + inputUser);
    //    System.out.println("server:user:username = " + username);
        userLogged = false;
        if (login.checkUser(username)) {
        //    System.out.println("server:user:if");
            if (login.checkLogin(username)) {
            //    System.out.println("server:user:if:if");
                userLogged = true;
                sendReplyToClient = "!" + username + " logged in";

            } else {
                sendReplyToClient = "+User-id valid, send account and password";
            }
        } else {
            sendReplyToClient = "-Invalid user-id, try again";
        }
      //  System.out.println(sendReplyToClient);

    }

    private static void ACCT(String inputAcct) {
        String username = login.user;
        String account = inputAcct.substring(5);

        //System.out.println("Server.ACCT.username:" + username);
        //System.out.println("Server.ACCT.Account:" + account);

        if (login.account.equals(account)) {

            isAcc = true;

            sendReplyToClient ="+Account valid, send password";
        } else {
            sendReplyToClient = "-Invalid account, try again";
        }
      //  System.out.println(sendReplyToClient);
    }

    private static void PASS(String inputPass) {

        String username = login.user;
        String password = inputPass.substring(5);

        //System.out.println("Server.ACCT.username:" + username);
        //System.out.println("Server.ACCT.Account:" + account);

        if (login.password.equals(password)){
            if(isAcc==true) {
                sendReplyToClient ="! Logged in Password is ok and you can begin file transfers.";
                userLogged = true;
              //  System.out.println("server:pass:userlogged " + userLogged);
            } else {
                sendReplyToClient = "+Password Valid, send account ";
            }

        } else {
            sendReplyToClient = "-Wrong password, try again";
        }
      //  System.out.println(sendReplyToClient);
    }

    private static void TYPE(String inputType) {
         type = inputType.substring(5);
        if (userLogged) {
            if (type.equals("A")) {
                sendReplyToClient ="+Using Ascii mode";
            } else if (type.equals("B")) {
                sendReplyToClient =" +Using Binary mode";
            } else if (type.equals("C")) {
                sendReplyToClient ="+Using Continuous mode";
            } else {
                sendReplyToClient ="-Type not valid";
            }

        } else {
            sendReplyToClient ="Please login to continue";
        }
      //  System.out.println(sendReplyToClient);
    }

    private static void LIST(String inputList) throws Exception {

        if (userLogged) {
            // F or V
            String listing = inputList.substring(5,6);
          //  System.out.println("listing:" + listing);
            // add to directory path e.g. test, src
            String dirPath = inputList.substring(6).trim();

            File directory = new File(currentDirectory.concat("/").concat(dirPath));
         //   System.out.println("directory:" + directory);

            if(directory.exists()){
                sendReplyToClient = "+ current connected directory: " + directory + "<CRLF>"; //currentDirectory;

                switch(listing) {
                    case "F":
                        File[] listOfFiles = directory.listFiles();

                        for (int i = 0; i < listOfFiles.length; i++) {
                            // append list of files
                            sendReplyToClient += listOfFiles[i].getName() + "<CRLF>";
                            //System.out.println("Server:List:F: " + sendReplyToClient);
                            //System.out.println("Server:List:F:expect list: " + listOfFiles[i].getName());
                        }
                        break;

                    case "V":
                      //  System.out.println("V works");
                        File[] files = directory.listFiles();
                        for (File aFile : files) {
                            Date date = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            String strDate = dateFormat.format(date);
                            long modifiedTime = aFile.lastModified();
                            String modifiedDate = dateFormat.format(new Date(modifiedTime));
                            UserPrincipal owner = Files.getOwner(aFile.toPath());

                            sendReplyToClient += "filename: "+ aFile.getName() + "   filesize: " + aFile.length() + "   last write date: "+ modifiedDate + "   owner: " +owner + "<CRLF>";
                        }
                        break;

                    default:
                        sendReplyToClient = "- invalid format, type F or V";

                }
            }  else {
                sendReplyToClient = "- directory does not exist";
            }
        } else {
            sendReplyToClient = "Please login to continue";
        }
      //  System.out.println(sendReplyToClient);
    }



    private static void CDIR(String inputCDIR) {
        String changeToDir = inputCDIR.substring(5);
        File finalDir = new File(currentDirectory.concat("/").concat(changeToDir));
        if (userLogged) {

            if(finalDir.isDirectory()) {
                switch (changeToDir) {
                    // go back one level
                    case "..":
                        currentDirectory = new File(currentDirectory).getParentFile().toString();
                        // System.out.println("!Changed working dir to " + currentDirectory);
                        sendReplyToClient = "!Changed working dir to " + currentDirectory;
                        break;
                    case "~":
                        currentDirectory = System.getProperty("user.dir");
                        sendReplyToClient = "!Changed to root dir: " + currentDirectory;
                        break;

                    default:
                        // currentDirectory = finalDir.toString();
                        // System.out.println("finalDir.toString" + currentDirectory);
                        if (finalDir.isDirectory()) {
                            currentDirectory = finalDir.toString();
                            //  System.out.println("!Changed working dir to " + currentDirectory);
                            sendReplyToClient = "!Changed working dir to " + currentDirectory;
                        } else {
                            // System.out.println("-Can't connect to directory because: directory doesn't exist");
                            sendReplyToClient = "-Can't connect to directory because: directory doesn't exist";
                        }
                        break;

                }
            }
            else {
                sendReplyToClient = "-Can't connect to directory because: directory doesn't exist";
                }

        } else {
            if (finalDir.isDirectory()) {
              //  System.out.println("not logged in: directory: " + currentDirectory);
                sendReplyToClient = "+directory ok, send account/password";
            } else {
                sendReplyToClient = "-Can't connect to directory because: directory doesn't exist";
               // System.out.println("-Can't connect to directory because: directory doesn't exist");
            }
        }

    //   System.out.println(sendReplyToClient);

    }


      private static void KILL(String inputFile) {
        if (userLogged) {
            try {
                String fileName = inputFile.substring(5);
              //  System.out.println("server:KILL:inputFile : " + fileName);
                File thisFile = new File(currentDirectory.concat("/").concat(fileName));
               // System.out.println("Server:KILL:thisFile " + thisFile);
                if (thisFile.delete()) {
                    sendReplyToClient = "+" + thisFile + " deleted";
                } else {
                    sendReplyToClient = "-not deleted because no such file";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            sendReplyToClient = "-Please login to continue";
        }
      //  System.out.println(sendReplyToClient);
    }



    private static void NAME(String inputName) {
        String filename = inputName.substring(5);
        File oldFile = new File(currentDirectory.concat("/").concat(filename));
        if(userLogged){
            if(oldFile.isFile()){
                oldFileName = filename;
                sendReplyToClient = "+File exists, send TOBE";
            } else{
                sendReplyToClient = "-Can't find " + filename + " ,NAME command is aborted, don't send TOBE";
            }
        } else{
            sendReplyToClient = "Please login to continue";
        }
      //  System.out.println(sendReplyToClient);
    }

    private static void TOBE(String inputTOBE) throws IOException {
        String newFile = inputTOBE.substring(5);
        if(userLogged){
            if(newFile.equals("")) {
              //  System.out.println("-File wasn't renamed as filename invalid");
                sendReplyToClient = "-File wasn't renamed as filename invalid";
            }
            else if(newFile.equals(oldFileName)){
              //  System.out.println("-File wasn't renamed as new filename same as old filename");
                sendReplyToClient = "-File wasn't renamed as new filename same as old filename";
            }
            else{
                Path source = Paths.get(currentDirectory,oldFileName);
                Files.move(source, source.resolveSibling(newFile));
                sendReplyToClient = "+" + oldFileName + " renamed to "+ newFile;
             //   System.out.println("+" + oldFileName + " renamed to "+ newFile);

            }
        }
        else{
            sendReplyToClient = "-File wasn't renamed as user not logged in. Please login to continue";
        }

     //   System.out.println(sendReplyToClient);
    }



// RETR, SEND, STOR commands //

  /*

   private static void RETR(String inputFile) {

        System.out.println("inside RETR");
        System.out.println("file will be returned in the format, type : " + type);

        if (userLogged) {
            String retrFile = inputFile.substring(5);
            File file = new File(currentDirectory.concat("/").concat(retrFile));
             System.out.println("file: " + file);
             System.out.println("filePath: " + filePath);
            if (file.exists()) {
                long ascii = file.length();
                System.out.println("bytes: " + ascii);
                filePath = (String.valueOf(new File(currentDirectory.concat("/").concat(retrFile))));
            } else {
                System.out.println("-File doesn't exist");
            }
        }
        else{
            System.out.println("-Not logged in, can't retrieve file");
        }

    }*/

 /*   private static void SEND(String toClient) throws IOException {
      //  file
      //  File fileSend = new File(toClient);

        byte[] content = Files.readAllBytes(Path.of(filePath));
        outputStream.write(content);
        outputStream.flush();
        System.out.println("file saved on client");


    }*/

  /*  private static void STOR(String inputCommand){
        //NEW,OLD,APP
        String command = inputCommand.substring(5,8);
        String fileName = inputCommand.substring(9);
        File file = new File(currentDirectory.concat("/").concat(fileName));
        System.out.println("file in STOR: " + file);

        System.out.println("inside STOR");
        System.out.println("file will be stored in the format, type : " +type );
    }
*/








}






