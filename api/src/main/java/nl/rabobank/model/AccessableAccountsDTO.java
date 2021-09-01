package nl.rabobank.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rabobank.account.Account;

/**
 * DTO response of accessible accounts
 */
@Data
@AllArgsConstructor
public class AccessableAccountsDTO {

  List<Account> accountsWithReadAccess;
  List<Account> accountsWithWriteAccess;
}
