import java.net.*;
import java.io.*;
import java.lang.ClassNotFoundException;

public class KKMultiServerThread extends Thread {
    private Socket socket = null;
    private Leaderboard leaderboard;

    public KKMultiServerThread(Socket socket, Leaderboard leaderboard) {
        super("KKMultiServerThread");
        this.socket = socket;
        this.leaderboard = leaderboard;
    }

    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            String clientName = ((Message) in.readObject()).getMessageContent();
            IMProtocol kkp = new IMProtocol(clientName, leaderboard);
            String outputLine = kkp.processInput(null);
            Message sendToClient = new Message("Server:", outputLine);
            out.writeObject(sendToClient);

            Message fromClient;
            while ((fromClient = (Message) in.readObject()) != null) {
                System.out.println(fromClient.getName() + ": " + fromClient.getCharContent());
                outputLine = kkp.processInput(fromClient);
                Message serversReply = new Message("Server:", outputLine);
                out.writeObject(serversReply);
                System.out.println("Server: " + outputLine);
                if (outputLine.equals("Goodbye!"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
