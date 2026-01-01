package com.viettel.vss.constant.common.exceptions;

import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.util.MessageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageCommon messageCommon;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex, Locale locale) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> {
                    // field name
                    String fieldName = err.getField();
                    // default message = key trong messages.properties
                    String messageKey = err.getDefaultMessage();
                    // arguments từ annotation, vd min/max size
                    Object[] args = err.getArguments();
                    // messageCommon hoặc MessageSource
                    return messageCommon.getMessage(messageKey, args, locale)
                            .replace("{field}", fieldName); // nếu bạn muốn
                })
                .collect(Collectors.toList());

        Map<String, Object> body = new HashMap<>();
        body.put("code",BusinessExceptionCode.VALIDATE_FAILED);
        body.put("message", errors);
        body.put("data", null);

        return ResponseEntity.badRequest().body(body);
    }
}

