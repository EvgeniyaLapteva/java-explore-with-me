package ru.practicum.ewm.exception.model;

public class ConditionsAreNotMetException extends RuntimeException {
    public ConditionsAreNotMetException(String message) {
        super(message);
    }
}
