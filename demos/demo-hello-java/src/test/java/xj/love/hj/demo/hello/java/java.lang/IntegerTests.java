package xj.love.hj.demo.hello.java.java.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerTests {

    @Test
    public void testReverse() {
        int a = 0x12345678;
        assertEquals("10010001101000101011001111000", Integer.toBinaryString(a));

        int reverseResult = Integer.reverse(a);
        assertEquals("11110011010100010110001001000", Integer.toBinaryString(reverseResult));
    }

    @Test
    public void testReverseBytes() {
        int a = 0x12345678;
        int reverseResult = Integer.reverseBytes(a);
        assertEquals("78563412", Integer.toHexString(reverseResult));
    }

    @Test
    public void testNegativeIntBinary() {
        int a = -8;
        assertEquals("11111111111111111111111111111000", Integer.toBinaryString(a));
    }
}
