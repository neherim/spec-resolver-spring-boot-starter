package io.github.neherim.web.table.filtering.request;

import io.github.neherim.web.table.filtering.request.converter.Converter;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Map;

public class FilterRequestParser {
    private final Converter converter;
    private final RequestBodyParser requestBodyParser;
    private final RequestParamsParser requestParamsParser;

    public FilterRequestParser(Converter converter,
                               RequestBodyParser requestBodyParser,
                               RequestParamsParser requestParamsParser) {
        this.converter = converter;
        this.requestBodyParser = requestBodyParser;
        this.requestParamsParser = requestParamsParser;
    }

    public FilterRequest fromRequestBody(NativeWebRequest webRequest) {
        Map<String, String> params = requestBodyParser.parse(webRequest);
        return new FilterRequest(params, converter);
    }

    public FilterRequest fromRequestParams(NativeWebRequest webRequest) {
        Map<String, String> params = requestParamsParser.parse(webRequest);
        return new FilterRequest(params, converter);
    }
}
