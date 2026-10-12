package com.anurag.cs.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
      @ExceptionHandler(MethodArgumentNotValidException.class)
      @ResponseBody
      public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error -> {
                  errors.put(error.getField(), error.getDefaultMessage());
            });
            return errors;
      }
}
