package io.github.neherim.web.table.filtering.request;

import org.springframework.web.context.request.NativeWebRequest;

import java.util.Map;
import java.util.stream.Collectors;

public class RequestParamsParser {

    public Map<String, String> parse(NativeWebRequest webRequest) {
        return webRequest.getParameterMap().entrySet().stream()
                .filter(entry -> entry.getValue().length > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()[0]));
    }
}
