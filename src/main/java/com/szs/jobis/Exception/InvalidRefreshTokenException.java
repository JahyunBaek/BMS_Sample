package com.szs.jobis.Exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(){
        super(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

}
