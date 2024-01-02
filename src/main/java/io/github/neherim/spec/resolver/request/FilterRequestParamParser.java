package io.github.neherim.spec.resolver.request;

import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterRequestParamParser {
    private final Converter converter;

    public FilterRequestParamParser(Converter converter) {
        this.converter = converter;
    }

    public FilterRequest parse(NativeWebRequest webRequest) {
        var params = webRequest.getParameterMap().entrySet().stream()
                .filter(entry -> entry.getValue().length > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Arrays.asList(entry.getValue())));

        return new FilterRequest(params, converter);
    }
}
