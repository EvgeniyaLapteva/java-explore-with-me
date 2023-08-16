package ru.practicum.ewm.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.exception.model.ConditionsAreNotMetException;
import ru.practicum.ewm.exception.model.ObjectNotFoundException;
import ru.practicum.ewm.exception.response.ApiError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.info(e.getMessage());
        return new ApiError("NOT_FOUND", "The required object was not found.", e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage;
        if (e.hasFieldErrors()) {
            StringBuilder builder = new StringBuilder();
            for (FieldError fieldError : e.getFieldErrors()) {
                builder.append("Field: ");
                builder.append(fieldError.getField());
                builder.append(". Error: ");
                builder.append(fieldError.getDefaultMessage());
                builder.append(". Value: ");
                builder.append(fieldError.getRejectedValue());
            }
            errorMessage = builder.toString();
        } else {
            errorMessage = e.getMessage();
        }
        return new ApiError("BAD_REQUEST", "Incorrectly made request.", errorMessage,
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.info(e.getMessage());
        return new ApiError("CONFLICT", "Integrity constraint has been violated.", e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConditionsAreNotMetException(final ConditionsAreNotMetException e) {
        log.info(e.getMessage());
        return new ApiError("CONFLICT", "For the requested operation the conditions are not met.",
                e.getMessage(), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.info(e.getMessage());
        return new ApiError("BAD_REQUEST", "Incorrectly made request.", e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

}
