package nl.rabobank.service;

import nl.rabobank.exception.AccountNotFoundException;
import nl.rabobank.exception.GrantorDoesNotOwnAccountException;
import nl.rabobank.exception.RoleAlreadyGrantedException;
import nl.rabobank.model.AccessableAccountsDTO;
import nl.rabobank.model.PowerOfAttorneyDTO;

/**
 *
 */
public interface PowerOfAttorneyService {

  /**
   * @param powerOfAttorneyDTO inputDTO
   * @throws AccountNotFoundException if account not found
   * @throws GrantorDoesNotOwnAccountException if no rights for Grantor
   * @throws RoleAlreadyGrantedException if role already exists
   */
  void postPowerOfAttorney(PowerOfAttorneyDTO powerOfAttorneyDTO)
      throws AccountNotFoundException, GrantorDoesNotOwnAccountException, RoleAlreadyGrantedException;

  /**
   * @param userName user name
   * @return AccessableAccountsDTO with two lists for READ and WRITE
   */
  AccessableAccountsDTO getAccessibleAccountsForUser(String userName);
}
