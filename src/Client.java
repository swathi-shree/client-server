import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    static String cType;
   // static Socket socket = null;
    static DataOutputStream dataOutputStream = null;
    static String retrievedFile;
    public static void main (String[] args){
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        // improve efficiency using buffers
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
     //   DataOutputStream dataOutputStream = null;

        try{
            socket = new Socket("localhost", 1234);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in);


            while(true) {
                String msgToSend = scanner.nextLine();
                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                // flushes the stream to improve I/O performance
                bufferedWriter.flush();

                System.out.println("Server:" + bufferedReader.readLine());
                System.out.println("MSGtoSend:" + msgToSend);

                String clientCmd = msgToSend.substring(0, 4);
                switch (clientCmd){

                    case "TYPE":
                        dataOutputStream.writeBytes(msgToSend);
                        // client's side of file type (A,B,C)
                        cType = msgToSend.substring(5);
                        break;

                    case "SEND":
                        clientSEND(msgToSend);
                        System.out.println("print client cmd: " + clientCmd);
                        break;
                }


                if (msgToSend.equalsIgnoreCase("DONE")) {
                    System.out.println("Client:if:");
                    socket.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                System.out.println("Client:finally");
                if(socket!=null) {
                    System.out.println("Client:finally:socket");
                    socket.close();
                }
                if(inputStreamReader != null) {
                    System.out.println("Client:finally:inputstreamreader");
                    inputStreamReader.close();
                }
                if(outputStreamWriter != null) {
                    System.out.println("Client:finally:outputstreamwriter");
                    outputStreamWriter.close();
                }
                if(bufferedReader != null) {
                    System.out.println("Client:finally:bufferedreader");
                    bufferedReader.close();
                }
                if(bufferedWriter != null) {
                    System.out.println("Client:finally:bufferedwriter");
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


  /*  private static void clientTYPE(String msgToSend) {
        // client's side of file type (A,B,C)
        String cType = msgToSend.substring(5);

    }
*/
  private static void clientRETR(String msgToSend){

  }

    private static void clientSEND(String msgToSend) throws IOException {

        dataOutputStream.writeBytes(msgToSend);
        dataOutputStream.flush();

    }


}


                 /*   case "RETR":
                        retrievedFile = msgToSend.substring(5);
                        receivedFileSize = Integer.parseInt(r);
                        long totalFreeSpace =  new File("c:").getFreeSpace() ;
                        if(totalFreeSpace < receivedFileSize) {
                            msgToSend = "STOP";
                            dataOutputStream.writeBytes(msgToSend);
                            dataOutputStream.flush();
                            break;*/