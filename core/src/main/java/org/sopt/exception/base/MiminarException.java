package org.sopt.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MiminarException extends RuntimeException{

    HttpStatus status;
    ErrorEnum error;

    private MiminarException() {
    }
    public MiminarException(ErrorEnum error){
        super(error.getErrorMessage());
        this.error = error;
    }

    public int getHttpStatus() {
        return error.getStatusCode();
    }
}
