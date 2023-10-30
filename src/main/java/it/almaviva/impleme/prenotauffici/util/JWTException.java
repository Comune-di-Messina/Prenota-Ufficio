package it.almaviva.impleme.prenotauffici.util;

import org.springframework.http.HttpStatus;

public class JWTException  extends RuntimeException {
    private HttpStatus code;
    private String message;

    public JWTException(HttpStatus code) {
        this.code = code;
    }

    public JWTException(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public JWTException(HttpStatus code, String message, Exception e) {
        super(message, e);
        this.code = code;
        this.message = message;
    }

    public JWTException(String message, Exception e) {
        super(message, e);
        this.message = message;
    }

    public JWTException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
