package nl.rabobank.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import nl.rabobank.mongo.repository.AccountRepository;
import org.springframework.stereotype.Service;

/**
 * Default Account Service
 */
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  /**
   * @param accountNumber account number
   * @return Optional Account
   */
  @Override
  public Optional<Account> findByAccountNumber(String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

}
