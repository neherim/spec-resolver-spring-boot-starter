package io.github.neherim.web.table.filtering.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.nio.charset.StandardCharsets;

class RequestBodyParserTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestBodyParser filterParamParserFromRequestBody =
            new RequestBodyParser(objectMapper);

    @Test
    void parseFilterValueFromJsonObject() {
        var json = """
                {
                  "firstName": "Ivan",
                  "age": 45,
                  "registered": false,
                  "score": 98.45
                }
                """;
        var request = makeMockHttpRequest(json);
        var parsed = filterParamParserFromRequestBody.parse(request);

        Assertions.assertEquals("Ivan", parsed.get("firstName"));
        Assertions.assertEquals("45", parsed.get("age"));
        Assertions.assertEquals("false", parsed.get("registered"));
        Assertions.assertEquals("98.45", parsed.get("score"));
    }

    private NativeWebRequest makeMockHttpRequest(String body) {
        var request = new MockHttpServletRequest();
        request.setContent(body.getBytes(StandardCharsets.UTF_8));
        return new ServletWebRequest(request);
    }
}