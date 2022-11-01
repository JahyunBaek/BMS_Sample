package com.szs.jobis.Exception;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super();
    }
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateUserException(String message) {
        super(message);
    }
    public DuplicateUserException(Throwable cause) {
        super(cause);
    }
}
