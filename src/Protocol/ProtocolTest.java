package Protocol;

import Protocol.Protocol;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtocolTest {
    public final String OP1 = "OP1";
    public final String OP2 = "OP2";
    public final String[][] BOARD = new String[][]{
                    {"", "", "", ""},
                    {"P1", "P1", "", ""},
                    {"", "", "", ""},
                    {"", "D1", "D1", "D1"}
    };
    public final String BOARD_REPRESENTATION = ",,,:P1,P1,,:,,,:,D1,D1,D1";

    @Test
    public void parseArrayTest() {
        String string = "1,2,3,5";
        assertArrayEquals(new Object[]{"1", "2", "3", "5"}, Protocol.parseArray(string));
    }

    @Test
    public void parseArrayTestTwoDimensions() {
        assertArrayEquals(BOARD, Protocol.parseArray(BOARD_REPRESENTATION));
    }

    @Test
    public void encodeArrayTest() {
        Integer[] array = {1, 3, 5, 6};
        String encodedArray = Protocol.encodeArray(array);
        assertEquals("1,3,5,6", encodedArray);
    }

    @Test
    public void test() {
        Object[] ob = new Object[2];
        ob[0] = Protocol.MessageIdentifier.S;
        ob[1] = "test";

        assertEquals("S;test", Protocol.protocolMessage(ob));
    }

    @Test
    public void testArray() {
        Object[] ob = new Object[2];
        ob[0] = Protocol.MessageIdentifier.S;
        ob[1] = new Integer[]{1, 3};
        assertEquals("S;1,3", Protocol.protocolMessage(ob));
    }

    @Test
    public void testDecode() {
        String test = Protocol.success(new String[]{"Strings", "Here"});
        System.out.println(test);
        ArrayList<Object> result = Protocol.decodeProtocolMessage(test);
        ArrayList<Object> expected = new ArrayList<>();
        expected.add("S");
        expected.add(new String[]{"Strings", "Here"});
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testJoin() {
        assertEquals("J;John;OP1,OP2", Protocol.join("John", new String[]{OP1, OP2}));
    }

    @Test
    public void testSuccess() {
        assertEquals("S", Protocol.success());
        assertEquals("S;OP1,OP2", Protocol.success(new String[]{OP1, OP2}));
    }

    @Test
    public void testFail() {
        assertEquals("F", Protocol.fail());
        assertEquals("F;OP1,OP2", Protocol.fail(new String[]{OP1, OP2}));
    }

    @Test
    public void testPlay() {
        assertEquals("P", Protocol.play());
    }

    @Test
    public void testTurn() {
        assertEquals("T;1", Protocol.turn(1));
    }

    @Test
    public void testBegin() {
        assertEquals("B", Protocol.begin());
    }

    @Test
    public void testDeploy() {
        assertEquals("D;,,,:P1,P1,,:,,,:,D1,D1,D1", Protocol.deploy(BOARD));
    }

    @Test
    public void testMove() {
        assertEquals("M;1,3", Protocol.move(new Integer[]{1, 3}));
    }

    @Test
    public void testHit() {
        assertEquals("H;1", Protocol.hit(1));
    }

    @Test
    public void testEnd() {
        assertEquals("E;1", Protocol.end(1));
    }

    @Test
    public void testChat() {
        assertEquals("C;John;Hello World", Protocol.chat("John", "Hello World"));
    }

    @Test
    public void testRadarRequest() {
        assertEquals("W;1,3", Protocol.radarRequest(new Integer[]{1, 3}));
    }

    @Test
    public void testRadarResponse() {
        assertEquals("W;,,,:P1,P1,,:,,,:,D1,D1,D1", Protocol.radarResponse(BOARD));
    }

    @Test
    public void testReadyToPlay() {
        assertEquals("R", Protocol.readyToPlay());
    }

    @Test
    public void testPlayerList() {
        assertEquals("L;OP1,OP2", Protocol.playerList(new String[]{OP1, OP2}));
    }

    @Test
    public void testChoosePlayers() {
        assertEquals("L;OP1,OP2", Protocol.choosePlayers(new String[]{OP1, OP2}));
    }
}
