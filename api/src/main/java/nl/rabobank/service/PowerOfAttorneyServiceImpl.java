package nl.rabobank.service;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.AccountNotFoundException;
import nl.rabobank.exception.GrantorDoesNotOwnAccountException;
import nl.rabobank.exception.RoleAlreadyGrantedException;
import nl.rabobank.model.AccessableAccountsDTO;
import nl.rabobank.model.PowerOfAttorneyDTO;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Default POA Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PowerOfAttorneyServiceImpl implements PowerOfAttorneyService {

  private final PowerOfAttorneyRepository powerOfAttorneyRepository;
  private final AccountService accountService;

  /**
   * Validate and save the POA
   *
   * @param powerOfAttorneyDTO inputDTO
   * @throws AccountNotFoundException if account not found
   * @throws GrantorDoesNotOwnAccountException if no rights for Grantor
   * @throws RoleAlreadyGrantedException if role already exists
   */
  public void postPowerOfAttorney(PowerOfAttorneyDTO powerOfAttorneyDTO)
      throws AccountNotFoundException, GrantorDoesNotOwnAccountException, RoleAlreadyGrantedException {
    log.info("Recieved Request for granting Power of attorney {}", powerOfAttorneyDTO);
    Optional<Account> account = accountService
        .findByAccountNumber(powerOfAttorneyDTO.getAccountNumber());
    if (account.isPresent()) {
      if (!account.get().getAccountHolderName().equals(powerOfAttorneyDTO.getGrantorName())) {
        log.error("Grantor Write error");
        throw new GrantorDoesNotOwnAccountException("Grantor " + powerOfAttorneyDTO.getGrantorName()
            + " has no rights on account number: " + powerOfAttorneyDTO.getAccountNumber());
      } else {
        mapAndSaveRole(powerOfAttorneyDTO);
      }
    } else {
      throw new AccountNotFoundException(
          "No Account Found for : " + powerOfAttorneyDTO.getAccountNumber());
    }
  }

  private void mapAndSaveRole(PowerOfAttorneyDTO powerOfAttorneyDTO)
      throws RoleAlreadyGrantedException {
    PowerOfAttorney powerOfAttorneyDO = mapToPersistable(powerOfAttorneyDTO);
    log.debug("Mapped to persistable succesfully ...");
    try {
      powerOfAttorneyRepository.save(powerOfAttorneyDO);
    } catch (DuplicateKeyException ex) {
      log.error("Role Already Exists exception");
      throw new RoleAlreadyGrantedException("Given role already granted ");
    }
  }

  /**
   * @param userName user name
   * @return AccessableAccountsDTO with two lists for READ and WRITE
   */
  @Override
  public AccessableAccountsDTO getAccessibleAccountsForUser(String userName) {
    log.info("Received request of accessible accounts for user {}", userName);
    return new AccessableAccountsDTO(
        powerOfAttorneyRepository
            .findAllByGranteeNameAndAuthorization(userName, Authorization.READ).stream()
            .map(PowerOfAttorney::getAccount)
            .collect(Collectors.toList()),
        powerOfAttorneyRepository
            .findAllByGranteeNameAndAuthorization(userName, Authorization.WRITE).stream()
            .map(PowerOfAttorney::getAccount)
            .collect(Collectors.toList()));
  }

  private PowerOfAttorney mapToPersistable(PowerOfAttorneyDTO powerOfAttorney) {
    return PowerOfAttorney.builder().granteeName(powerOfAttorney.getGranteeName())
        .grantorName(powerOfAttorney.getGrantorName())
        .authorization(powerOfAttorney.getAuthorization()).account(
            accountService.findByAccountNumber(powerOfAttorney.getAccountNumber()).orElse(null)
        ).build();
  }

}
