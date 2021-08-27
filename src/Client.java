import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

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
                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                // flushes the stream
                bufferedWriter.
                        flush();


                System.out.println("Server:" + bufferedReader.readLine());
                System.out.println("MSGtoSend:" + msgToSend);
                //  if (msgToSend.equalsIgnoreCase("BYE"))
                //      break;

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


}
