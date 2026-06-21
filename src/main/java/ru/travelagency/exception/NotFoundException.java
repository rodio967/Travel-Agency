package ru.travelagency.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(Class<?> entityClass, Object id) {
        return new NotFoundException(
                "%s with id=%s not found".formatted(entityClass.getSimpleName(), id));
    }
}
