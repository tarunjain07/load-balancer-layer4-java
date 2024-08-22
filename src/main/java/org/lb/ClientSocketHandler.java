package org.lb;

import org.lb.utils.BackendServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {
    private Socket clientSocket;

    public ClientSocketHandler(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try  {

            InputStream clientToLoadBalancerInputStream = clientSocket.getInputStream();
            OutputStream loadBalancerToClientOutputStream = clientSocket.getOutputStream();
            String[] host = BackendServers.getHostAndPort();
            System.out.println("Host selected to handle this request : " + host[0]+":"+host[1]);

            //create tcp connect with backend server
            Socket socket = new Socket(host[0], Integer.parseInt(host[1]));
            System.out.println("socket "+socket);
            InputStream backendServerToLoadBalancerInputStream = socket.getInputStream();
            OutputStream loadBalancerToBackendServerOutputStream = socket.getOutputStream();


            //Client                -->LoadBalancer                             --> backendServer
            // clientToLBInputStream                    LBToBackendOutputStream
            Thread clientDataHandler = new Thread(() -> {
                try{
                    int data;
                    //read data byte by byte
                    while ((data = clientToLoadBalancerInputStream.read()) !=-1){
                        loadBalancerToBackendServerOutputStream.write(data);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
            clientDataHandler.start();

            //Client                    <---LoadBalancer                                 <--- backendServer
            //   LBToClientOutputStream                         BackendToLBInputStream
            Thread backendDataHandler = new Thread(() -> {
                try{
                    int data;
                    //read data byte by byte
                    while ((data = backendServerToLoadBalancerInputStream.read()) !=-1){
                        loadBalancerToClientOutputStream.write(data);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
            backendDataHandler.start();



        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
