package io.github.neherim.spec.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = ApplicationTestContext.class)
public abstract class BaseContextTest {

    @Autowired
    public MockMvc mockMvc;
}
