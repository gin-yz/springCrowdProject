package com.atguigu.crowd.exception;

public class AddRoleAlreadyExistException extends RuntimeException{
    public AddRoleAlreadyExistException() {
        super();
    }

    public AddRoleAlreadyExistException(String message) {
        super(message);
    }

    public AddRoleAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddRoleAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected AddRoleAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
