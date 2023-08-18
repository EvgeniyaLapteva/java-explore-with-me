package ru.practicum.ewm.exception.model;

public class IncorrectRequestException extends RuntimeException {
    public IncorrectRequestException(String message) {
        super(message);
    }
}
