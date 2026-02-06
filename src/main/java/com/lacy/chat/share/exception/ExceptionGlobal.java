package com.lacy.chat.share.exception;


import com.lacy.chat.share.dto.res.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionGlobal {

    /**
     * Handle FileValidationException
     */
    @ExceptionHandler(value = FileValidationException.class)
    public ResponseEntity<ExceptionResponse> handleFileValidationException(FileValidationException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle AccessDeniedException
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(ex.getMessage()).status(HttpStatus.FORBIDDEN.value()).build(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handle ServerErrorException
     */
    @ExceptionHandler(value = ServerErrorException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(ServerErrorException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(ex.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle NotFoundException
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND.value()).build(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle BadRequestException
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }


    /**
     * HttpMediaTypeNotSupportedException
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message("Content-Type not supported: " + ex.getContentType()).status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()).build(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(ex.getMessage()).status(HttpStatus.PAYLOAD_TOO_LARGE.value()).build(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    /**
     * Handle MethodArgumentTypeMismatchException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s.",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown");

        return new ResponseEntity<>(
                ExceptionResponse.builder().message(message).status(HttpStatus.BAD_REQUEST.value()).build(),
                HttpStatus.BAD_REQUEST
        );

    }

    /**
     * Handle MethodArgumentNotValidException
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException exception) {

        StringBuilder str = new StringBuilder();
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            str.append(fieldErrors.get(0).getField()).append(": ").append(fieldErrors.get(0).getDefaultMessage());
        } else {
            str.append("Validation failed.");
        }
        return new ResponseEntity<>(ExceptionResponse.builder().message(str.toString()).status(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }


}
