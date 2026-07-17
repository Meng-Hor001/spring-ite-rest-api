package co.istad.productapisimpledemo.advisor;

import co.istad.productapisimpledemo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestControllerAdvisor {

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse<?>> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
        var response  = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    // handle not found issue
    // ExceptionHandler(NoSuchElementException.class)
    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse<?>> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<?>> handleMethodNotValidException(MethodArgumentNotValidException exception ){
        Map<String,String > errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error ->
                        errors.put(error.getField(), error.getDefaultMessage())
        );
        // should use entity response for better message
        return new ResponseEntity<>(
                ErrorResponse.builder().
                        message("Provided data is invalid")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errors(errors)
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
