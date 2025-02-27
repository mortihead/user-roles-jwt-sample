package ru.ibs.exception;

import com.google.common.util.concurrent.UncheckedExecutionException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);
        if (error instanceof ResponseStatusException) {
            errorAttributes.put("message", ((ResponseStatusException) error).getBody().getDetail());
        } else  if (error instanceof UncheckedExecutionException) {
            errorAttributes.put("message", error.getMessage());
        }
        return errorAttributes;
    }
}