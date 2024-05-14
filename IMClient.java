
/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.io.*;
import java.net.*;
import java.util.Random;

public class IMClient{
    
    public static void main(String[] args) throws IOException {
    
        // CSC 450 LAB 6: ARGS LENGTH WILL BE 3 NOW, SINCE WE NEED AN EXTRA ARGS ELEMENT TO
        // WRITE THE NAME OF THE CLIENT IN THE COMMAND LINE.
        if (args.length != 3) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number> <client name>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        // CSC 450 LAB 6: SAVE THE NAME OF THE SERVER FROM COMMAND LINE ARGUMENT.
        String clientName = args[2];
        
        try (
            // STEP 2: INITIATE A CONNECTION REQUEST TO SERVER'S IP ADDRESS, PORT.
            // THIRD STEP IN THE SERVER CLASS.
            Socket kkSocket = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(kkSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(kkSocket.getInputStream());
            //ObjectInputStream stdIn = new ObjectInputStream(System.in);
            //BufferedReader in = new BufferedReader(
                //new InputStreamReader(kkSocket.getInputStream()));
        ) {
            // BUFFERED READER ALLOWS CLIENT TO SEND MESSAGE.
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            //ObjectInputStream stdIn = new ObjectInputStream(System.in);
            Message fromServer; // = new Message("Server", null);
            String fromUser; // = new Message(clientName, null);

            // STEP 5: RECEIVE MESSAGE FROM SERVER.
            while ((fromServer = (Message)in.readObject()) != null) {
                
                if(fromServer.getMessageContent().equals("Goodbye!"))
                {
                    break;
                }
                //if (fromServer.getMessageContent().equals("Bye"))
                //    break;
                    
                // STEP 6: PRINT MESSAGE FROM SERVER.
                // CSC LAB 450 LAB 6: READ MESSAGE COMING IN FROM SERVER.
                try
                {
                    // Message receivedMessage = (Message)in.readObject();
                    System.out.println(fromServer.getName() + fromServer.getMessageContent());
                }
                catch (Exception cnfe)
                {
                    cnfe.printStackTrace();
                }
                
                //System.out.println("Server: " + fromServer); // NO LONGER NEEDED SINCE WE 
                                                                // OBTAIN THE SERVER NAME FROM COMMAND LINE.
                
                
                // System.out.print("Client: "); // NO LONGER NEED THIS SINCE WE GET THE NAME OF THE
                                                 // CLIENT FROM COMMAND LINE ARGUMENT.
                
                // STEP 7: RECEIVE MESSAGE FROM SERVER.
                // Reads in what the client will send.
                fromUser = stdIn.readLine();
                
                if (fromUser != null && fromUser.length() != 1) 
                {
                    Message clientMessage = new Message(clientName, fromUser);
                    // out.println(fromUser); // CSC 450 LAB 6: WE ARE NOW SENDING A MESSAGE OBJECT,
                                              // NOT A SIMPLE STRING. USE THE LINE BELOW:
                    System.out.println(clientMessage.getMessageContent());
                    out.writeObject(clientMessage);
                }
                if(fromUser.length() == 1)
                    {
                        char charFromUser = fromUser.charAt(0);
                        Message clientMessage = new Message(clientName, charFromUser);
                        out.writeObject(clientMessage);
                    }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e)
        {
            System.err.println("Data received is not a message.");
            System.exit(1);
        }
        
    }
}
