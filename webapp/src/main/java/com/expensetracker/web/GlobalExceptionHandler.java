package com.expensetracker.web;

import com.expensetracker.model.InvalidExpenseException;
import com.expensetracker.model.InvalidInventoryException;
import com.expensetracker.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Every InvalidExpenseException / InvalidInventoryException thrown deep
 * inside the model layer lands here and becomes a proper HTTP error with
 * the same message that used to go in a JOptionPane dialog.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidExpenseException.class)
    public ResponseEntity<ErrorResponse> handleInvalidExpense(InvalidExpenseException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidInventoryException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInventory(InvalidInventoryException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNumberFormat(NumberFormatException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Amount, quantity, or price must be a valid number."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Unexpected error: " + ex.getMessage()));
    }
}
