package org.lb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class LoadBalancer {
    public static void main(String[] args) {
        try  {
            ServerSocket serverSocket = new ServerSocket(8081);
            System.out.println("Load Balancer started at port :" + 8081);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("TCP connection established with client : "+socket.toString());
                handleSocket(socket);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void handleSocket(Socket socket) {
        ClientSocketHandler clientSocketHandler = new ClientSocketHandler(socket);
        Thread clientSocketHandlerThread = new Thread(clientSocketHandler);
        clientSocketHandlerThread.start();
    }
}
