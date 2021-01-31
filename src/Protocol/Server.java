package Protocol;

import Protocol.Exceptions.ServerUnavailableException;
import controller.GameState;
import model.board.Board;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static Protocol.Protocol.PORT;

/**
 * Server for the Battleship project
 */
public class Server implements Runnable {
    private ServerSocket ssock;
    private List<ClientHandler> clients;
    private Map<String, String[][]> boards;
    private boolean firstPlayerReady = false;
    private Map<String, Integer> scores;
    private GameState gameState;

    private int numberOfPlayers = 0;

    private int turn = 0;
    private ClientHandler currClient;

    private boolean radarEnabled = false, lobbyEnabled = false;
    private boolean bothReady = false;

    public Server() {
        clients = new ArrayList<>();
        gameState = gameState.JOINING;
        boards = new HashMap<>();
        scores = new HashMap<>();
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
                            new ClientHandler(sock, this, ++numberOfPlayers);
                    new Thread(handler).start();
                    clients.add(handler);
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

    /**
     * Sends message to all clients
     * @param message message that gets send to all clients
     * @throws ServerUnavailableException when the server is unavailable
     */
    private void sendMessageToAllClients(String message) throws ServerUnavailableException {
        for(ClientHandler c: clients) {
            c.sendMessage(message);
        }
    }

    /**
     * @param client client that wants to ask to join the match
     * @return message if joining the server is succeeded;
     */
    public String join(ClientHandler client) {
       if(client.getName() != null) {
           for(ClientHandler c: clients) {
               if(c != client && c.getName().equals(client.getName()))
               {
                   return Protocol.fail("Name is taken!");
               }
           }
           if(clients.size() == 1) {
               if(clients.get(0).isRadarEnabled() && client.isRadarEnabled()) {
                   radarEnabled = true;
               }
               bothReady = true;
           }

           scores.put(client.getName(), 0);

           return Protocol.success();
       } else {
           removeClient(client.getName());
           return Protocol.fail("Name is not set!");
       }
    }

    /**
     * @return true if radar is enabled in this match
     */
    public boolean isRadarEnabled() {
        return radarEnabled;
    }

    /**
     * @return true if lobby is enabled on this server
     */
    public boolean isLobbyEnabled() {
        return lobbyEnabled;
    }

    /**
     * @param x X-index of the board
     * @param y Y-index of the board
     * @param user name of the user that you want to get a tile of
     * @return string with what is on the tile
     */
    public String getTile(int x, int y, String user) {
        return boards.get(user)[y][x];
    }

    /**
     * Checks whether two players have joined and acts accordingly
     * @throws ServerUnavailableException when server is unavailable
     */
    public synchronized void play() throws ServerUnavailableException {
        if(clients.size() == 2) {
            System.out.println(clients.size());
            for (ClientHandler client : clients) {
                client.begin(clients, isRadarEnabled());
            }
        }
    }

    /**
     * @param name name of the client from whom the board is
     * @param board two-dimensional array with all the information from the users board
     * @return a sign that the match can start
     */
    public void setBoard(String name, String[][] board) throws ServerUnavailableException {
        boards.put(name, board);
        if(firstPlayerReady) {
            turn();
            currClient = clients.get(0);
            sendMessageToAllClients(Protocol.readyToPlay());
            System.out.println("Board setup done");
        }
        firstPlayerReady = true;
    }

    /**
     * @return string whose turn it is
     */
    public void turn() throws ServerUnavailableException {
        if(hasWinner() != null) {
            sendMessageToAllClients(Protocol.end(hasWinner().getNumberOfPlayer()));
        }

        currClient.turn(scores.get(currClient.getName()));

        if(turn == 0) {
            turn = 1;
        } else {
            turn = 0;
        }
        currClient = clients.get(turn);
    }

    private ClientHandler hasWinner() {
        for(ClientHandler c: clients) {
            if(scores.get(c.getName()) == 91) {
                return c;
            }
        }

        return null;
    }

    /**
     *
     * @param coordinates the x- and y-coordinates that the client shoots
     * @param client the client which shoots
     * @return string with the value of the score
     */
    public String move(String coordinates, ClientHandler client) {
        String[] args = coordinates.split(Protocol.AS);
        int x = (Integer.parseInt(args[0]));
        int y = (Integer.parseInt(args[1]));
        String[][] board = boards.get(client.getName());
        String tile = board[y][x];

        if(x < 0 || x >= Board.WIDTH || y < 0 || y >= Board.HEIGHT) {
            return Protocol.hit(-1, 0, 0, new Integer[]{0, 0});
        }

        if(!tile.isEmpty()) {
            for(int i=0; i<Board.HEIGHT; i++) {
                for(int j=0; j<Board.WIDTH; j++) {
                    if((i != y && j != x) && board[i][j].equals(tile)) {
                        board[y][x] = "X";
                        scores.put(client.getName(), scores.get(client.getName())+1);
                        return Protocol.hit(1, 0, 0, new Integer[]{0, 0});
                    }
                }
            }
            scores.put(client.getName(), scores.get(client.getName())+2);
            return Protocol.hit(2, 0, 0, new Integer[]{0, 0});
        } else {
            return Protocol.hit(0, 0, 0, new Integer[]{0, 0});
        }
    }

    /**
     * Removes a client from the server
     * @param client client that needs to be removed
     */
    synchronized void removeClient(String client) {
        Set<ClientHandler> s = new HashSet<ClientHandler>();
        for (ClientHandler handler : clients) {
            if (!handler.getName().equals(client)) {
                s.add(handler);
            }
        }
        clients.retainAll(s);
    }
}
