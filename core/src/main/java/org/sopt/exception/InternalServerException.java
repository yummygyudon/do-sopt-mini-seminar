package org.sopt.exception;

import org.sopt.exception.base.MiminarException;
import org.sopt.exception.error.InternalServerError;

public class InternalServerException extends MiminarException {
    public InternalServerException(InternalServerError error) {
        super(error);
    }
}
