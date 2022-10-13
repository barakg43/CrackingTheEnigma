package src.examples.nested;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimpleServer {

    private final ServerSocket serverSocket;
    private final int port;
    private Thread mainServerThread;
    private boolean isStopped;
    private List<Socket> openConnections;

    public SimpleServer(int port) throws IOException {
        this.port = port;
        mainServerThread = null;

        serverSocket = new ServerSocket(port);
        isStopped = false;
        openConnections = new ArrayList<>();
    }

    public void startInternalServerOnDifferentThread() {
        mainServerThread = new Thread(this::runServer, "Server thread on - " + port);
        mainServerThread.start();
    }

    private void runServer() {

        int clientID = 1;

        try {
            System.out.println("Server is listening on port " + port);
            while (!isStopped) {
                //wait until a client connects to this server at the listening port
                Socket socket = serverSocket.accept();
                openConnections.add(socket);
                //this code is run only AFTER a client has successfully initialized a connection
                //open a stream to/from the client and start communicating with it

                //in order to make the server available again, meaning, listen to the port
                //we do all the communication WITH EACH CLIENT IN A DIFFERENT THREAD
                new Thread(() -> {
                    try (BufferedReader in =
                                 new BufferedReader(
                                         new InputStreamReader(
                                                 socket.getInputStream()));
                         PrintWriter out =
                                 new PrintWriter(
                                         socket.getOutputStream(),
                                         true)) {

                        out.println("Welcome to our server!");
                        String line;
                        try {
                            int lineCounter = 0;
                            while ((line = in.readLine()) != null) {
                                System.out.println(++lineCounter + " " + line);
                                out.println("Server Says: No, " + line.toUpperCase());
                            }
                            System.out.println("Session has ended");
                        } catch (IOException e) {
                            // we handle the exception by ending the thread (when it's socket will be closed by any of the parties)
                            // which happens automatically by being thrown
                            // from the loop
                            System.out.println(Thread.currentThread().getName() + " Is halting out...");
                        }
                    } catch (Exception exception) {
                        System.out.println(Thread.currentThread().getName() + " Is halting out...");
                    }
                }, "Client id " + clientID++).start();
            }
            System.out.println("Server was stopped nicely. Finish main server thread execution...");
        } catch (IOException e) {
            System.out.println("Seems that server socket was closed unexpectedly... Halting out. ");
        }
        catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " got error. Halting out... " + e.getMessage());
        }
    }

    public void stop() {
        isStopped = true;
        try {

            // close server socket thread - no more accepting connections
            serverSocket.close();

            // close all open connections
            openConnections.forEach(socket -> {
                try {
                    System.out.println("Closing socket " + socket.getRemoteSocketAddress().toString() + " ...");
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error when closing socket... " + socket.getRemoteSocketAddress().toString());
                }
            });

            // wait for the server socket to finish before returning back
            // question: which thread does all that code ???
            mainServerThread.join();

        } catch (IOException e) {
            System.out.println("Error when trying to close the server socket...");
        } catch (InterruptedException e) {
            // nothing to do here...
        }
        
    }
}