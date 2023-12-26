package org.sopt.common.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.common.model.ApiResponse;
import org.sopt.exception.base.MiminarException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class MiminarExceptionHandler {

    @ExceptionHandler(MiminarException.class)
    protected ResponseEntity<?> handlePalmsException(MiminarException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.error(e.getError()));
    }
}
