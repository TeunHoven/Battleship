package Protocol;

import controller.GameState;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static Protocol.Protocol.PORT;

public class Server implements Runnable {
    private ServerSocket ssock;
    private List<ClientHandler> clients;
    private int next_client_no;
    private GameState gameState;

    private boolean radarEnabled = false, lobbyEnabled = false;

    public Server() {
        clients = new ArrayList<>();
        gameState = gameState.SETUP;
    }

    private void setup() {
        ssock = null;
        while (ssock == null) {
            // try to open a new ServerSocket
            try {
                System.out.println("Attempting to open a socket at 127.0.0.1 "
                        + "on port " + PORT + "...");
                ssock = new ServerSocket(PORT, 0,
                        InetAddress.getByName("127.0.0.1"));
                System.out.println("Server started at port " + PORT);
            } catch (IOException e) {
                System.out.println("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + PORT + ".");
            }
        }
    }

    @Override
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                // Sets up the hotel application
                setup();

                while (true) {
                    Socket sock = ssock.accept();
                    ClientHandler handler =
                            new ClientHandler(sock, this);
                    new Thread(handler).start();
                    clients.add(handler);
                    if(clients.size() == 2) {
                        for(ClientHandler client : clients) {
                            client.begin(clients, isRadarEnabled);
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("A server IO error occurred: "
                        + e.getMessage());
            }
        }
        System.out.println("Ended server!");
    }

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }

    public String join(ClientHandler client) {
       if(client.getName() != null) {
           if(client.isRadarEnabled()) {
               radarEnabled = true;
           }
           if(client.isLobbyEnabled()) {
               lobbyEnabled = true;
           }

           return Protocol.success();
       } else {
           return Protocol.fail("Name is not set!");
       }
    }

    public String getShip(int x, int y) {

    }

    public void succes(Object[] ){

    }
}
