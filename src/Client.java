import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    static String cType;

    // for SEND client
    static DataOutputStream dataOutputStream;
    static String retrievedFile;
    static int receivedFileSize;

    // for STOR
    static String fileToChange;

    private static BufferedReader inFromServer;



    public static void main (String[] args){
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
       OutputStreamWriter outputStreamWriter = null;


        // improve efficiency using buffers
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;


        try{
            socket = new Socket("localhost", 1234);

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in);

            while(true) {
                String msgToSend = scanner.nextLine();
             //   System.out.println("Before MSG send");
                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();



         //  System.out.println("After MSG send");



                String clientCmd = msgToSend.substring(0, 4);
                if(clientCmd.equals("LIST")) {
                    String[] fileList = bufferedReader.readLine().split("<CRLF>");
                    for (String file: fileList){
                        System.out.println(file);
                    }
                }
                else {
                    System.out.println("Reply from Server : " + bufferedReader.readLine());
                }
                switch (clientCmd){

                 /*
                    case "TYPE":
                        dataOutputStream.writeBytes(msgToSend);
                        // client's side of file type (A,B,C)
                        cType = msgToSend.substring(5);
                        break;


                    case "SEND":
                        clientSEND();
                      //  System.out.println("print client cmd: " + clientCmd);
                        break;

                    case "STOR":
                        //clientSTOR(msgToSend);
                        break;


                  */


                }


                if (msgToSend.equalsIgnoreCase("DONE")) {
                 //   System.out.println("+closing connection ");
                    socket.close();
                    break;

                }

            }

        } catch (IOException e) {
            e.printStackTrace();


        } finally {
            try{
            //    System.out.println("Client:finally");
                if(socket!=null) {
                  //  System.out.println("Client:finally:socket");
                    socket.close();
                }
                if(inputStreamReader != null) {
                  //  System.out.println("Client:finally:inputstreamreader");
                    inputStreamReader.close();
                }
                if(outputStreamWriter != null) {
                 //   System.out.println("Client:finally:outputstreamwriter");
                    outputStreamWriter.close();
                }
                if(bufferedReader != null) {
                 //   System.out.println("Client:finally:bufferedreader");
                    bufferedReader.close();
                }
                if(bufferedWriter != null) {
                  //  System.out.println("Client:finally:bufferedwriter");
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

