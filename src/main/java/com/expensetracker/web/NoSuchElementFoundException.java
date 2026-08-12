package com.expensetracker.web;

/** Thrown by controllers when an id lookup misses; mapped to HTTP 404. */
public class NoSuchElementFoundException extends RuntimeException {
    public NoSuchElementFoundException(String message) {
        super(message);
    }
}
