package nl.rabobank.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nl.rabobank.exception.AccountNotFoundException;
import nl.rabobank.exception.GrantorDoesNotOwnAccountException;
import nl.rabobank.exception.RoleAlreadyGrantedException;
import nl.rabobank.model.AccessableAccountsDTO;
import nl.rabobank.model.PowerOfAttorneyDTO;
import nl.rabobank.service.PowerOfAttorneyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main POA Controller
 */
@RestController
@RequiredArgsConstructor
public class PowerOfAttorneyController {

  private final PowerOfAttorneyService powerOfAttorneyService;

  /**
   * @param powerOfAttorney RequestBody DTO for Granting POA
   * @throws AccountNotFoundException if account not found
   * @throws GrantorDoesNotOwnAccountException if Grantor not owner of account
   */
  @PostMapping(value = "grantPowerOfAttorney")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "Users must be able to create write or read access for payments and savings accounts")
  public void grantPowerOfAttorney(@RequestBody PowerOfAttorneyDTO powerOfAttorney)
      throws AccountNotFoundException, GrantorDoesNotOwnAccountException, RoleAlreadyGrantedException {
    powerOfAttorneyService.postPowerOfAttorney(powerOfAttorney);
  }

  /**
   * @param userName userName
   * @return AccessableAccountsDTO with 2 lists for READ and WRITE accounts.
   */
  @GetMapping("getAccessibleAccountsForUser")
  @Operation(description = "Users need to be able to retrieve a list of accounts they have read or write access for")
  public AccessableAccountsDTO getAccessibleAccountsForUser(
      @RequestParam("userName") String userName) {
    return powerOfAttorneyService.getAccessibleAccountsForUser(userName);
  }

}
