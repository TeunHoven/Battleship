package Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import Protocol.*;
import Protocol.Exceptions.ServerUnavailableException;

/**
    * ClientHandler for the BattleShip client.
    * This class can handle the communication with one
    * client.
    */
public class ClientHandler implements Runnable {

        /** The socket and In- and OutputStreams */
        private BufferedReader in;
        private BufferedWriter out;
        private Socket sock;

        /** The connected HotelServer */
        private Server server;

        /** Name of this ClientHandler and of the opponent*/
        private String name;
        private String opponentName;

        private int numberOfPlayer;

        /**
         * If radar is enabled for this match
         */
        private boolean radarEnabled;

        /**
         * Constructs a new ClientHandler. Opens the In- and OutputStreams.
         *
         * @param sock The client socket
         * @param server  The connected server
         */
        public ClientHandler(Socket sock, Server server, int numberOfPlayer) {
            try {
                in = new BufferedReader(
                        new InputStreamReader(sock.getInputStream()));
                out = new BufferedWriter(
                        new OutputStreamWriter(sock.getOutputStream()));
                this.sock = sock;
                this.server = server;
                this.numberOfPlayer = numberOfPlayer;
            } catch (IOException e) {
                shutdown();
            }
        }

        public synchronized void sendMessage(String msg) throws ServerUnavailableException {
            if (out != null) {
                try {
                    out.write(msg);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new ServerUnavailableException("Could not write "
                            + "to server.");
                }
            } else {
                throw new ServerUnavailableException("Could not write "
                        + "to server.");
            }
        }

        /**
         * Continuously listens to client input and forwards the input to the
         * {@link #handleCommand(String)} method.
         */
        @Override
        public void run() {
            String msg;
            try {
                msg = in.readLine();
                while (msg != null) {
                    System.out.println("> [" + name + "] Incoming: " + msg);
                    handleCommand(msg);
                    out.newLine();
                    out.flush();
                    msg = in.readLine();
                }
                shutdown();
            } catch (IOException | ServerUnavailableException e) {
                shutdown();
            }
        }

        /**
         * Handles commands received from the client by calling the according
         * methods at the Server. For example, when the message "i Name"
         * is received, the method doIn() of HotelServer should be called
         * and the output must be sent to the client.
         *
         * If the received input is not valid, send an "Unknown Command"
         * message to the server.
         *
         * @param msg command from client
         * @throws IOException if an IO errors occur.
         */
        private synchronized void handleCommand(String msg) throws IOException, ServerUnavailableException {
            // To be implemented
            String command = Protocol.parseCommands(msg)[0];
            String[] options = Protocol.parseCommands(msg);

            switch(command) {
                case "J" -> {
                    name = options[1];
                    radarEnabled = Boolean.parseBoolean(options[2]);
                    System.out.println("Joining");
                    sendMessage("" + server.join(this));
                }

                case "P" -> {
                    server.play();
                    System.out.println("Starting with play!!!!");
                }

                case "D" -> {
                    String[][] board = Protocol.parse2DArray(options[0]);
                    server.setBoard(name, board);
                    System.out.println("Set board");
                }

                case "T" -> {
                    System.out.println("Next turn");
                    if(Integer.parseInt(options[0]) == numberOfPlayer) {
                        sendMessage(msg);
                    }
                }

                case "M" -> {
                    System.out.println("move");
                    sendMessage(server.move(options[0], this));
                }
            }
        }

        /**
         * @param clients all clients in this match
         * @param isRadarEnabled
         */
        public void begin(List<ClientHandler> clients, boolean isRadarEnabled) throws ServerUnavailableException {
            String[] names = new String[2];

            for(int i=0; i<2; i++) {
                ClientHandler client = clients.get(i);

                names[i] = client.getName();
                if(!client.getName().equals(getName())) {
                    this.opponentName = client.getName();
                }
            }

            radarEnabled = isRadarEnabled;
            String message = Protocol.begin(names, isRadarEnabled());
            System.out.println(message);
            sendMessage(Protocol.begin(names, isRadarEnabled()));
        }

        public void turn(int score) throws ServerUnavailableException {
            sendMessage(Protocol.turn(1, new Integer[]{score}));
        }

        /**
         * 
         * @return name of this client
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @return true if radar is enabled for this match
         */
        public boolean isRadarEnabled() {
            return radarEnabled;
        }

        public int getNumberOfPlayer() {
            return numberOfPlayer;
        }

        /**
         * Shut down the connection to this client by closing the socket and
         * the In- and OutputStreams.
         */
        private void shutdown() {
            System.out.println("> [" + name + "] Shutting down.");
            try {
                in.close();
                out.close();
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.removeClient(getName());
        }
}
