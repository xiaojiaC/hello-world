package xj.love.hj.demo.hello.java.java.lang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringTests {

    @Test
    public void testImmutable() throws Exception {
        String str = "old";
        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(str, new char[]{'n', 'e', 'w'});
        assertEquals("new", str);
    }

    @Test
    public void testIndexOf() {
        String str = "test";
        assertEquals(4, str.indexOf("", 10));
        assertEquals(2, str.indexOf("", 2));
    }

    @Ignore("java.lang.NullPointerException, why String is not ?")
    @Test
    public void testSerialPersistentFields() {
        try {
            PersistentTest test = new PersistentTest(new char[]{'A', 'B'});
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream("/tmp/test.data"));
            output.writeObject(test);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream("/tmp/test.data"));
            PersistentTest test = (PersistentTest) input.readObject();
            assertEquals(new char[]{'A', 'B'}, test.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PersistentTest implements Serializable {

        private final char[] value;

        private static final long serialVersionUID = 8738254809816584295L;

        private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];

        /*
        private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[] {
                new ObjectStreamField("value", char[].class) };
         */

        PersistentTest(char[] value) {
            this.value = value;
        }
    }
}
