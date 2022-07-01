package nl.rabobank.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import nl.rabobank.mongo.repository.AccountRepository;
import org.junit.jupiter.api.Test;

public class AccountServiceTest {

  private AccountRepository accountRepository = mock(AccountRepository.class);

  private AccountService subject = new AccountServiceImpl(
      accountRepository);

  @Test
  public void itShouldFindByAccountNumberFromRepo() {
    //given

    //when
    subject.findByAccountNumber("123");

    //then
    verify(accountRepository, times(1)).findByAccountNumber("123");
  }
}
