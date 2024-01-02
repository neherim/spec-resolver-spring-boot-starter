package io.github.neherim.web.table.filtering.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;
import java.util.Map;

public class RequestBodyParser {
    private final ObjectMapper objectMapper;

    public RequestBodyParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, String> parse(NativeWebRequest webRequest) {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("Request body not present");
        }

        try (var inputStream = request.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            throw new IllegalArgumentException("Can't parse Json body", ex);
        }
    }
}
