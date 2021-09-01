package nl.rabobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when account not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends Exception {

  public AccountNotFoundException(String message) {
    super(message);
  }
}
