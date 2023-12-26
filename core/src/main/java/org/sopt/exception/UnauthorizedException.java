package org.sopt.exception;

import org.sopt.exception.base.MiminarException;
import org.sopt.exception.error.UnauthorizedError;

public class UnauthorizedException extends MiminarException {
    public UnauthorizedException(UnauthorizedError error) {
        super(error);
    }
}
