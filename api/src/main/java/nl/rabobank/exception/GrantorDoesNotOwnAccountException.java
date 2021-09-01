package nl.rabobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no rights to the grantor on the account.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class GrantorDoesNotOwnAccountException extends Exception {

  public GrantorDoesNotOwnAccountException(String message) {
    super(message);
  }
}
