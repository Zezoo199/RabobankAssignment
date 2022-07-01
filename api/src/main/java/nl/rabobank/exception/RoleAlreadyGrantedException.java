package nl.rabobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class RoleAlreadyGrantedException extends Exception {

  public RoleAlreadyGrantedException(String message) {
    super(message);
  }

}
