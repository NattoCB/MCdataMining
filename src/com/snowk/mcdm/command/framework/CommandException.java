package com.snowk.mcdm.command.framework;

public class CommandException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public CommandException(String message) {
        super(message);
    }
}