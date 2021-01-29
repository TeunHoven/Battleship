package Protocol;

import controller.GameState;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Protocol.Protocol.PORT;

public class Server implements Runnable {
    private ServerSocket ssock;
    private List<ClientHandler> clients;
    private int next_client_no;
    private GameState gameState;

    private boolean radarEnabled = false, lobbyEnabled = false;

    public Server() {
        clients = new ArrayList<ClientHandler>();
        gameState = gameState.SETUP;
        while (ssock == null) {
            setup();
        }

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
                            //client.begin(clients, isRadarEnabled);
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

   // public String join(ClientHandler client) {
   //    if(client.getName() != null) {
   //        if(client.isRadarEnabled()) {
   //            radarEnabled = true;
   //        }
   //        if(client.isLobbyEnabled()) {
   //            lobbyEnabled = true;
   //        }
    //
     //      return Protocol.success();
    //   } else {
     //      return Protocol.fail("Name is not set!");
     //  }
   // }

    public String getShip(int x, int y) {
        return null;
    }

    public List<ClientHandler> getClients(){
        return clients;
    }

    public ServerSocket getServerSocket(){
        return ssock;
    }

    private void addClient(ClientHandler client) {
        clients.add(client);
    }

    synchronized void removeClient(String client) {
        Set<ClientHandler> s = new HashSet<ClientHandler>();
        for (ClientHandler handler : clients) {
            if (!handler.getClientName().equals(client)) {
                s.add(handler);
            }
        }
        clients.retainAll(s);
    }

}
