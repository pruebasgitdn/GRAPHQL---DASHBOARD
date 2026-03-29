package com.back.exceptions;


public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credenciales Incorrectas");
    }
}
