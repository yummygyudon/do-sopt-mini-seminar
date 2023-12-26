package org.sopt.exception;

import org.sopt.exception.base.MiminarException;
import org.sopt.exception.error.BadRequestError;

public class BadRequestException extends MiminarException {
    public BadRequestException(BadRequestError error) {
        super(error);
    }
}
