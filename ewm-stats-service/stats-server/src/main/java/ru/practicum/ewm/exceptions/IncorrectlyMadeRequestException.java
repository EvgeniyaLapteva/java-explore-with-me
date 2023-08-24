package ru.practicum.ewm.exceptions;

public class IncorrectlyMadeRequestException extends RuntimeException {
    public IncorrectlyMadeRequestException(String message) {
        super(message);
    }
}
