package nl.rabobank.service;

import java.util.Optional;
import nl.rabobank.account.Account;

public interface AccountService {

  /**
   * @param accountNumber account number
   * @return Optional Account
   */
  Optional<Account> findByAccountNumber(String accountNumber);
}
