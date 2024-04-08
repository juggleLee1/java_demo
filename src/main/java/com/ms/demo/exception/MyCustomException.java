package com.ms.demo.exception;

public class MyCustomException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public MyCustomException (String message) {
        super(message);
    }

    public MyCustomException (String message, Throwable cause) {
        super(message, cause);
    }

}
