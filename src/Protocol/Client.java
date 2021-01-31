package Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import Protocol.Exceptions.*;
import controller.GameState;
import model.GameManager;
import view.GameView;

/**
     * Client for BattleShip
     */
    public class Client implements Runnable {

        private Socket serverSock;
        private BufferedReader in;
        private BufferedWriter out;

        private boolean joined = false;

        /**
         * Constructs a new Client. Initialises the view.
         */
        public Client() {
            serverSock = null;
            in = null;
            out = null;
        }

        /**
         * Starts a new Client by creating a connection, followed by the
         * HELLO handshake as defined in the protocol. After a successful
         * connection and handshake, the view is started. The view asks for
         * used input and handles all further calls to methods of this class.
         *
         * When errors occur, or when the user terminates a server connection, the
         * user is asked whether a new connection should be made.
         */
        public void start() {
            try {
                createConnection();
            } catch (ExitProgram e) {
                e.printStackTrace();
            }
        }

        /**
         * Creates a connection to the server. Requests the IP and port to
         * connect to at the view (TUI).
         *
         * The method continues to ask for an IP and port and attempts to connect
         * until a connection is established or until the user indicates to exit
         * the program.
         *
         * @throws ExitProgram if a connection is not established and the user
         * 				       indicates to want to exit the program.
         * @ensures serverSock contains a valid socket connection to a server
         */
        public void createConnection() throws ExitProgram {
            clearConnection();
            while (serverSock == null) {
                String host = "127.0.0.1";
                int port = 5000;

                // try to open a Socket to the server
                try {
                    InetAddress addr = InetAddress.getByName(host);
                    System.out.println("Attempting to connect to " + addr + ":"
                            + port + "...");
                    serverSock = new Socket(addr, port);
                    in = new BufferedReader(new InputStreamReader(
                            serverSock.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(
                            serverSock.getOutputStream()));
                } catch (IOException e) {
                    System.out.println("ERROR: could not create a socket on "
                            + host + " and port " + port + ".");

                    //Do you want to try again? (ask user, to be implemented)
                    if(false) {
                        throw new ExitProgram("User indicated to exit.");
                    }
                }
            }
        }

        /**
         * Resets the serverSocket and In- and OutputStreams to null.
         *
         * Always make sure to close current connections via shutdown()
         * before calling this method!
         */
        public void clearConnection() {
            serverSock = null;
            in = null;
            out = null;
        }

        /**
         * Sends a message to the connected server, followed by a new line.
         * The stream is then flushed.
         *
         * @param msg the message to write to the OutputStream.
         * @throws ServerUnavailableException if IO errors occur.
         */
        public synchronized void sendMessage(String msg)
                throws ServerUnavailableException {
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
         * Reads and returns one line from the server.
         *
         * @return the line sent by the server.
         * @throws ServerUnavailableException if IO errors occur.
         */
        public String readLineFromServer()
                throws ServerUnavailableException {
            if (in != null) {
                try {
                    // Read and return answer from Server
                    String answer = in.readLine();
                    if (answer == null) {
                        throw new ServerUnavailableException("Could not read "
                                + "from server.");
                    }
                    return answer;
                } catch (IOException e) {
                    throw new ServerUnavailableException("Could not read "
                            + "from server.");
                }
            } else {
                throw new ServerUnavailableException("Could not read "
                        + "from server.");
            }
        }

        /**
         * Reads and returns multiple lines from the server until the end of
         * the text is indicated using a line containing ProtocolMessages.EOT.
         *
         * @return the concatenated lines sent by the server.
         * @throws ServerUnavailableException if IO errors occur.
         */
        public String readMultipleLinesFromServer()
                throws ServerUnavailableException {
            if (in != null) {
                try {
                    // Read and return answer from Server
                    StringBuilder sb = new StringBuilder();
                    for (String line = in.readLine(); line != null
                            && !line.equals("hoi");
                         line = in.readLine()) {
                        sb.append(line + System.lineSeparator());
                    }
                    return sb.toString();
                } catch (IOException e) {
                    throw new ServerUnavailableException("Could not read "
                            + "from server.");
                }
            } else {
                throw new ServerUnavailableException("Could not read "
                        + "from server.");
            }
        }

        /**
         * Closes the connection by closing the In- and OutputStreams, as
         * well as the serverSocket.
         */
        public void closeConnection() {
            System.out.println("Closing the connection...");
            try {
                in.close();
                out.close();
                serverSock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void join(String name, Boolean radarEnabled, Boolean lobbyEnabled) throws ServerUnavailableException, ProtocolException {
            String message = Protocol.join(name, radarEnabled, lobbyEnabled);
            sendMessage(message);

            message = null;
            while(message == null) {
                message = readLineFromServer();
            }

            String[] args = Protocol.parseCommands(message);
            if(!message.startsWith(String.valueOf(Protocol.MessageIdentifier.S))) {
                throw new ProtocolException("No valid response");
            } else {
                // TELL CLIENT THAT JOIN IS SUCCESFUL EN IDK!?
            }

            joined = true;
            play(2);
        }

        public void play(int gameSize) throws ServerUnavailableException{
            String message = Protocol.play(gameSize);
            sendMessage(message);
        }

        public void deploy(String[][] board) throws ServerUnavailableException {
            String message = Protocol.deploy(board);
            sendMessage(message);
        }

        public void move(Integer[] coordinate) throws ServerUnavailableException {
            String message = Protocol.move(coordinate);
            sendMessage(message);
        }

        public void chat(String playername, String msg) throws ServerUnavailableException {
            String message = Protocol.chat(playername, msg);
            sendMessage(message);
        }

        public void radarRequest(Integer[] coordinate) throws ServerUnavailableException {
            String message = Protocol.radarRequest(coordinate);
            sendMessage(message);
        }

        public void getPlayers() throws ServerUnavailableException {
            String message = Protocol.getPlayers();
            sendMessage(message);
        }

        public void choosePlayer(String[] players) throws ServerUnavailableException {
            String message = Protocol.choosePlayers(players);
            sendMessage(message);
        }


        public void sendExit() throws ServerUnavailableException {
            String message ="> Received: " + "\r\n";
            System.out.print(message);
            closeConnection();
        }

        private void handleCommand(String msg) {
            String command = msg.split(Protocol.CS)[0];
            String[] options = msg.split(Protocol.CS)[1].split(Protocol.CS);

            switch(command) {
                case "F" -> {
                    System.out.println(options[0]);
                }

                case "B" -> {
                    GameManager.setGameState(GameState.SETUP);
                    for(int i=0; i<2; i++) {
                        if(!options[0].split(Protocol.AS)[i].equals("Teun")) {
                            GameManager.setOpponent(options[i]);
                            break;
                        }
                    }
                    GameManager.setRadarEnabled(Boolean.parseBoolean(options[1]));
                }

                case "T" -> {
                    GameManager.setGameState(GameState.USERROUND);
                    GameView.setEnemyPoints(Integer.parseInt(options[0]));
                }
            }
        }

    @Override
    public void run() {
        if(joined) {
            String message = null;
            try {
                message = readLineFromServer();
            } catch (ServerUnavailableException e) {
                e.printStackTrace();
            }
            while (message != null) {
                handleCommand(message);
            }
        } else {
            try {
                join("name", true, false);
            } catch (ServerUnavailableException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
        }
    }
}

