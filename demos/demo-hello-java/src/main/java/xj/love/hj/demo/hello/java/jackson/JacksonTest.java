package xj.love.hj.demo.hello.java.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson简单使用示例。
 *
 * @author xiaojia
 * @since 1.0
 */
public class JacksonTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonTest.class);

    /**
     * <pre>
     * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
     * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
     *
     * writeValue(File arg0, Object arg1)          把arg1转成json序列，并保存到arg0文件中。
     * writeValue(OutputStream arg0, Object arg1)  把arg1转成json序列，并保存到arg0输出流中。
     * writeValueAsBytes(Object arg0)              把arg0转成json序列，并把结果输出成字节数组。
     * writeValueAsString(Object arg0)             把arg0转成json序列，并把结果输出成字符串。
     * </pre>
     */
    public static void main(String[] args)
            throws ParseException, IOException {
        Person person = new Person();
        person.setName("Emmo");
        person.setAge(1);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        person.setBirthday(dateformat.parse("1992-10-01"));
        person.setProfession(new String[]{"cook"});

        // 序列化自定义对象
        ObjectMapper objectMapper = new ObjectMapper();
        String personJson = objectMapper.writeValueAsString(person);
        LOGGER.info(personJson);

        // 反序列化自定义对象
        String json = "{\"name\":\"Jack\",\"profession\":[\"programmer\"],\"birth_day\":717868800000}";
        Person user = objectMapper.readValue(json, Person.class);
        LOGGER.info(user.toString());

        // Deserialize json(file) --> object
        URL url = JacksonTest.class.getClassLoader().getResource("jackson/data/person.json");
        user = objectMapper.readValue(new File(url.getFile()), Person.class);
        LOGGER.info(user.toString());

        // Deserialize json(file) --> map
        @SuppressWarnings("unchecked")
        Map<String, Object> data = objectMapper.readValue(new File(url.getFile()), Map.class);
        LOGGER.info(data.toString());
    }
}
