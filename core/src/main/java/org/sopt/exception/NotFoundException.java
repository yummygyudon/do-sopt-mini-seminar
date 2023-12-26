package org.sopt.exception;

import org.sopt.exception.base.MiminarException;
import org.sopt.exception.error.NotFoundError;

public class NotFoundException extends MiminarException{
    public NotFoundException(NotFoundError error) {
        super(error);
    }
}
