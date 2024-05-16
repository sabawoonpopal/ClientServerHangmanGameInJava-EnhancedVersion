import java.io.*;
import java.net.*;

public class IMClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: java IMClient <host name> <port number> <client name>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String clientName = args[2];

        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(kkSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(kkSocket.getInputStream());
        ) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            Message fromServer;
            String fromUser;

            // Send client name to the server
            out.writeObject(new Message(clientName, clientName));

            while ((fromServer = (Message) in.readObject()) != null) {
                if (fromServer.getMessageContent().equals("Goodbye!")) {
                    break;
                }

                System.out.println(fromServer.getName() + ": " + fromServer.getMessageContent());

                fromUser = stdIn.readLine();
                if (fromUser != null && fromUser.length() != 1) {
                    Message clientMessage = new Message(clientName, fromUser);
                    System.out.println(clientMessage.getMessageContent());
                    out.writeObject(clientMessage);
                }
                if (fromUser.length() == 1) {
                    char charFromUser = fromUser.charAt(0);
                    Message clientMessage = new Message(clientName, charFromUser);
                    out.writeObject(clientMessage);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Data received is not a message.");
            System.exit(1);
        }
    }
}
