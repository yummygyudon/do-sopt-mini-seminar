package org.sopt.exception;

import org.sopt.exception.base.MiminarException;
import org.sopt.exception.error.ForbiddenError;

public class ForbiddenException extends MiminarException {
    public ForbiddenException(ForbiddenError error) {
        super(error);
    }
}
