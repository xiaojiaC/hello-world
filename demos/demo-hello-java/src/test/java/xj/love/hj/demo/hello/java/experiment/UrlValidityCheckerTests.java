package xj.love.hj.demo.hello.java.experiment;

import java.util.Map;
import org.junit.Test;
import xj.love.hj.demo.hello.java.experiment.UrlValidityChecker.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UrlValidityCheckerTests {

    @Test
    public void testCheck() throws Exception {
        Person person = new Person("https://www.baidu.com");
        Map<String, String> results = UrlValidityChecker.check(person);
        assertTrue(results.isEmpty());

        person = new Person("invalidUrl");
        results = UrlValidityChecker.check(person);
        assertTrue(!results.isEmpty());
        assertEquals(person.getAvatarUrl(), results.get(Person.AVATAR_URL));
    }
}
