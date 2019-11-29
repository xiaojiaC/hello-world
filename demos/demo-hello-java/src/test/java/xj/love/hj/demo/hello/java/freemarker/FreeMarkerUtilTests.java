package xj.love.hj.demo.hello.java.freemarker;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class FreeMarkerUtilTests {

    @Test
    public void testProcessTeachers() {
        List<Object> teachers = new ArrayList<Object>();
        Teacher teacher = new Teacher();
        teacher.setName("Emmo");
        teacher.setAge(34);
        teacher.setCourse("Chinese");
        teachers.add(teacher);

        teacher = new Teacher();
        teacher.setName("Bob");
        teacher.setAge(30);
        teacher.setCourse("History");
        teachers.add(teacher);

        String jsonString = FreeMarkerUtil.processTeachers(teachers);
        assertThat(jsonString, containsString(teacher.getName()));
        assertThat(jsonString, containsString(teacher.getAge() + ""));
        assertThat(jsonString, containsString(teacher.getCourse()));
    }
}
