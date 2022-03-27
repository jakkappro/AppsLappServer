package org.appslapp.AppsLappServer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserDoesntExistException extends RuntimeException {
    public UserDoesntExistException(String username) {
        super(username);
    }
}
