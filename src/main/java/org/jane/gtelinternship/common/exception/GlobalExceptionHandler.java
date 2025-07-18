package org.jane.gtelinternship.common.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

@Order
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().get();
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, fieldError.getDefaultMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(NotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(MissingRequestValueException.class)
    public ProblemDetail missingRequestValueException(MissingRequestValueException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ProblemDetail typeMismatchException(TypeMismatchException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail forbiddenException(ForbiddenException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail conflictException(ConflictException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getLocalizedMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail authorizationDeniedException(AuthorizationDeniedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void asyncRequestNotUsableException(AsyncRequestNotUsableException ignoredEx) {
        // Nothing to do
    }

    @Order()
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) throws Exception {
        if (ex instanceof ErrorResponse) {
            // Already an error response, rethrow and let the framework handle it
            throw ex;
        } else {
            ex.printStackTrace();
            return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
}
