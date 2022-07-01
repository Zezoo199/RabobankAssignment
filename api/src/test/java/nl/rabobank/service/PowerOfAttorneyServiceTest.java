package nl.rabobank.service;

import static nl.rabobank.RaboAssignmentApplicationTests.givenValidPOA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nl.rabobank.account.Account;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.AccountNotFoundException;
import nl.rabobank.exception.GrantorDoesNotOwnAccountException;
import nl.rabobank.exception.RoleAlreadyGrantedException;
import nl.rabobank.model.AccessableAccountsDTO;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PowerOfAttorneyServiceTest {

  private PowerOfAttorneyRepository powerOfAttorneyRepository = mock(
      PowerOfAttorneyRepository.class);

  private AccountService accountService = mock(AccountService.class);

  private PowerOfAttorneyService subject = new PowerOfAttorneyServiceImpl(
      powerOfAttorneyRepository, accountService);

  @Test
  public void itShouldPostPowerOfAttorneyTest()
      throws GrantorDoesNotOwnAccountException, RoleAlreadyGrantedException, AccountNotFoundException {
    //given
    when(accountService.findByAccountNumber(givenValidPOA().getAccountNumber())).thenReturn(
        givenAccount("moselim"));
    //when
    subject.postPowerOfAttorney(givenValidPOA());

    //then
    verify(powerOfAttorneyRepository, times(1)).save(any(PowerOfAttorney.class));
  }

  @Test
  public void itShouldNotPostPowerOfAttorneyWhenAccountNotFound() {
    //given
    Assertions.assertThrows((AccountNotFoundException.class), () -> {
      when(accountService.findByAccountNumber(givenValidPOA().getAccountNumber())).thenReturn(
          Optional.empty());
      //when
      subject.postPowerOfAttorney(givenValidPOA());

      //then
      verify(powerOfAttorneyRepository, never()).save(any(PowerOfAttorney.class));
    });
  }

  @Test
  public void itShouldNotPostPowerOfAttorneyWhenNoRightsForOwner() {
    //given
    Assertions.assertThrows((GrantorDoesNotOwnAccountException.class), () -> {
      when(accountService.findByAccountNumber(givenValidPOA().getAccountNumber())).thenReturn(
          givenAccount("johnny"));
      //when
      subject.postPowerOfAttorney(givenValidPOA());

      //then
      verify(powerOfAttorneyRepository, never()).save(any(PowerOfAttorney.class));
    });
  }

  @Test
  public void itShouldGetAccessibleAccountsForUser() {
    //given
    when(powerOfAttorneyRepository.
        findAllByGranteeNameAndAuthorization("moselim", Authorization.READ)).
        thenReturn(givenListOfPOA());
    //when
    AccessableAccountsDTO moselim = subject.getAccessibleAccountsForUser("moselim");
    //then
    assertEquals(1, moselim.getAccountsWithReadAccess().size());
    assertEquals(0, moselim.getAccountsWithWriteAccess().size());
  }

  private List<PowerOfAttorney> givenListOfPOA() {
    return Arrays.asList(givenValidPOADO());
  }

  private PowerOfAttorney givenValidPOADO() {
    return PowerOfAttorney.builder()
        .account(givenAccount("moselim").get()).
        authorization(Authorization.READ).granteeName("moselim").grantorName("SomeGrantor").
        build();
  }

  private Optional<Account> givenAccount(String accHolderName) {
    return Optional.of(new SavingsAccount("123", accHolderName, 1.0));
  }
}
