// InvalidCredentialsException.java

package com.bridgelabz.authservice.exception;

public class InvalidCredentialException
        extends RuntimeException {

    public InvalidCredentialException(String message) {
        super(message);
    }
}