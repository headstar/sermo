package demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    @Test
    public void applicationContextBuilds() {
        // do nothing, test fails is context doesn't build
    }
}
