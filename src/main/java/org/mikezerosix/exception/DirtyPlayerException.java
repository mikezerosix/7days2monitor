package org.mikezerosix.exception;

/**
 * Created by michael on 8.9.2014.
 */
public class DirtyPlayerException extends RuntimeException {

    public DirtyPlayerException(String message) {
        super(message);
    }

    public DirtyPlayerException(Exception e) {
        super(e);
    }
}
