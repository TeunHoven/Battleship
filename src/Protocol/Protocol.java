import java.util.ArrayList;

public class Protocol {
    /**
     * Server Listens for connections on port 5000 by default.
     */
    public static final int PORT = 5000;
    /**
     * Dimensions of the board are 10 x 15.
     */
    public static final int[] DIMENSIONS = {10, 15};
    /**
     * The command separator is ";".
     */
    public static final String CS = ";";
    /**
     * The array separator is ",".
     */
    public static final String AS = ",";

    /**
     * The two dimensional array separator is ":".
     */
    public static final String TAS = ":";

    /**
     * The MessageIdentifiers also known as "commands", give the context to each message.
     */
    public enum MessageIdentifier {
        J, // Join
        S, // Success
        P, // Play
        C, // Chat
        F, // Fail
        B, // Begin
        T, // Turn
        D, // Deploy
        R, // Ready to play
        M, // Move
        H, // Hit
        E, // End
        W, // RadioWave
        L, // List of Players
        G, // Get list of Players
    }

    /**
     * Letters that represent the ship class when sending a board.
     */
    public enum ShipId {
        C, // Carrier
        B, // Battleship
        D, // Destroyer
        S, // Super Patrol
        P  // Patrol Boat
    }

    /**
     * Used by this class to code an array of Objects into an protocol acceptable String that can be
     * sent with (for example) a PrintWriter between client and server.
     * if an Object in the array, is an array itself it will be unrolled into the format for arrays.
     * if not the toString method of the object will be appended.
     * @param objects the array of Objects to encode.
     * @return a protocol String that can be sent.
     */
    public static String protocolMessage(Object[] objects) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];

            if (object.getClass().isArray()) {
                stringBuilder.append(encodeArray((Object[]) object));
            } else {
                stringBuilder.append(object.toString());
            }

            if (i != objects.length - 1) {
                stringBuilder.append(CS);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * This method is used by this class to decode a String received
     * by either the client or the server and use it in the Game logic.
     * @param string the protocol string.
     * @return the array of objects that were
     */
    public static ArrayList<Object> decodeProtocolMessage(String string) {
        ArrayList<Object> result = new ArrayList<>();
        String[] commands = parseCommands(string);

        for (String command : commands) {
            if (command.contains(AS)) {
                result.add(parseArray(command));
            } else {
                result.add(command);
            }
        }

        return result;
    }


    /**
     * parses array from a string.
     * @param segment message to be parsed.
     * @return an array of objects.
     */
    public static Object[] parseArray(String segment) {
        if (segment.contains(TAS)) {
            return parse2DArray(segment);
        }

        return segment.strip().split(AS);
    }

    /**
     * parses a two dimensional array from a String.
     * @param segment message to be parsed.
     * @return an two dimensional array of strings.
     */
    public static String[][] parse2DArray(String segment) {
        String[] arrays = segment.split(TAS);
        String[][] result = new String[arrays.length][];

        for (int i = 0; i < arrays.length; i++) {
            // negative limit to include trailing empty strings.
            String[] arr = arrays[i].split(AS, -1);
            result[i] = arr;
        }

        return result;
    }

    /**
     * parses the commands from a string.
     * @param segment message to be parsed.
     * @return an array of strings.
     */
    public static String[] parseCommands(String segment) {
        return segment.strip().split(CS);
    }

    /**
     * encodes an array into the protocol's format.
     * checks if the array is 1-dimensional or 2-dimensional.
     * @param array the array to encode.
     * @return the array as a string.
     */
    public static String encodeArray(Object[] array) {
        // check if items are arrays.
        if (array.getClass().getComponentType().isArray()) {
            // use the 2d array method
            return encode2DArray(array);
        }

        // use the 1d array encoding.
        return encode1DArray(array);
    }

    /**
     * Encodes an array into the protocol's format.
     * @param array the array to encode.
     * @return the created string that represents the array.
     */
    public static String encode1DArray(Object[] array) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i].toString());
            if (i != array.length - 1) {
                stringBuilder.append(AS);
            }
        }

        return stringBuilder.toString();
    }

    /**array
     * Encodes a two dimensional array into the protocol's format.
     * @param array the two dimensional array.
     * @return a String representing the array.
     */
    public static String encode2DArray(Object[] array) {
        StringBuilder stringBuilder = new StringBuilder();

        // cast to two dimensional array.
        Object[][] twoDimensional = (Object[][]) array;
        for (int i = 0; i < twoDimensional.length; i++) {
            for (int j = 0; j < twoDimensional[i].length; j++) {
                stringBuilder.append(twoDimensional[i][j].toString());

                if (j != twoDimensional[i].length - 1) {
                    stringBuilder.append(AS);
                }
            }

            if (i != twoDimensional.length - 1) {
                stringBuilder.append(TAS);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * join message.
     * used by client.
     * @param name the players name.
     * @param radarEnabled does client support radar.
     * @param lobbyEnabled does client support lobby.
     * @return a protocol message with the name and game options
     */
    public static String join(String name, Boolean radarEnabled, Boolean lobbyEnabled) {
        return protocolMessage(new Object[]{MessageIdentifier.J, name, radarEnabled, lobbyEnabled});
    }

    /**
     * Success message with no parameters.
     * used by the server.
     * @return a protocol success message.
     */
    public static String success() {
        return success((String[]) null);
    }

    /**
     * Success message with single {@link String} response
     * used by the server.
     * @param message the error message.
     * @return a protocol fail message.
     */
    public static String success(String message) {
        return success(new String[]{message});
    }

    /**
     * Fail message with no parameters.
     * used by the server.
     * @return a protocol fail message.
     */
    public static String fail() {
        return fail((String[]) null);
    }

    /**
     * Fail message with single error {@link String}
     * used by the server.
     * @param message the error message.
     * @return a protocol fail message.
     */
    public static String fail(String message) {
        return fail(new String[]{message});
    }

    /**
     * success message with parameters.
     * @param parameters extra parameters for success message.
     * @return a protocol success message with parameters.
     */
    public static String success(String[] parameters) {
        if (parameters != null) {
            return protocolMessage(new Object[]{MessageIdentifier.S, parameters});
        }
        return MessageIdentifier.S.toString();
    }

    /**
     * fail message with parameters.
     * @param parameters extra parameters for fail message.
     * @return a protocol fail message with parameters.
     */
    public static String fail(String[] parameters) {
        if (parameters != null) {
            return protocolMessage(new Object[]{MessageIdentifier.F, parameters});
        }
        return MessageIdentifier.F.toString();
    }

    /**
     * play message.
     * sent by client.
     * @return a protocol Play message.
     */
    public static String play() {
        return MessageIdentifier.P.toString();
    }

    /**
     * begin message.
     * sent by server.
     * @return a protocol Begin message.
     */
    public static String begin(String[] playerNames, Boolean radar) {
        return protocolMessage(new Object[]{MessageIdentifier.B, playerNames, radar});
    }

    /**
     * Turn message.
     * sent by server.
     * @param turn the player which will take the next turn.
     * @return a protocol Turn message.
     */
    public static String turn(Integer turn, Integer[] scores) {
        return protocolMessage(new Object[]{MessageIdentifier.T, turn, scores});
    }

    /**
     * Deploy message.
     * sent by client.
     * @param board the board of the player placing the ships.
     * @return a protocol deploy message.
     */
    public static String deploy(String[][] board) {
        return protocolMessage(new Object[]{MessageIdentifier.D, board});
    }

    /**
     * ready to play message.
     * @return a protocol ready to play message.
     */
    public static String readyToPlay() {
        return MessageIdentifier.R.toString();
    }

    /**
     * Move message, used to fire to enemy ships.
     * sent by client.
     * @param coordinate the coordinate to fire upon
     * @return a protocol move message.
     */
    public static String move(Integer[] coordinate) {
        return protocolMessage(new Object[]{MessageIdentifier.M, coordinate});
    }

    /**
     * Hit message, reports results of move.
     * sent by server.
     * @param value the result, {-1: invalid, 0: miss, 1: hit, 2: sunk}
     * @return a protocol hit message.
     */
    public static String hit(Integer value, Integer attacker, Integer receiver, Integer[] coordinate) {
        return protocolMessage(new Object[]{MessageIdentifier.H, value, attacker, receiver, coordinate});
    }

    /**
     * message sent by server at endgame.
     * @param winner the id of the winner or -1 for disconnected player.
     * @return a protocol endgame message.
     */
    public static String end(Integer winner) {
        return protocolMessage(new Object[]{MessageIdentifier.E, winner});
    }

    /**
     * Used by both client and server to send messages.
     * @param playerName the player that is sending the message.
     * @param message the content of the message.
     * @return a protocol message used to send a chat.
     */
    public static String chat(String playerName, String message) {
        return protocolMessage(new Object[]{MessageIdentifier.C, playerName, message});
    }

    /**
     * request radar.
     * sent by client.
     * @param coordinate the central coordinate of the area to be revealed.
     * @return a protocol radar request message.
     */
    public static String radarRequest(Integer[] coordinate) {
        return protocolMessage(new Object[]{MessageIdentifier.W, coordinate});
    }

    /**
     * radar response.
     * sent by server.
     * @param boardSection board of the opponent.
     * @return a protocol radar response message
     */
    public static String radarResponse(String[][] boardSection) {
        return protocolMessage(new Object[]{MessageIdentifier.W, boardSection});
    }

    /**
     * asks for a list of players connected to the lobby from the server.
     * sent by the client.
     */
    public static String getPlayers() {
        return MessageIdentifier.G.toString();
    }

    /**
     * player list message, contains the players that can be challenged (lobby feature).
     * sent by server.
     * @return a protocol player list message.
     */
    public static String playerList(String[] playerList) {
        return protocolMessage(new Object[]{MessageIdentifier.L, playerList});
    }

    /**
     * choose players message, used to choose players to play with (lobby feature)
     * sent by client.
     * @return a protocol player list message.
     */
    public static String choosePlayers(String[] players) {
        return protocolMessage(new Object[]{MessageIdentifier.L, players});
    }
}
