package nl.rabobank;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.MongoConfiguration;
import nl.rabobank.mongo.repository.AccountRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@SpringBootApplication
@Import(MongoConfiguration.class)
@Slf4j
public class RaboAssignmentApplication {

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private MongoTemplate mongoTemplate;

  public static void main(final String[] args) {
    SpringApplication.run(RaboAssignmentApplication.class, args);
  }

  /**
   * Init Bean to load some example accounts
   */
  @Bean
  InitializingBean loadAccounts() {
    log.info("Saving example accounts to MongoDb");
    return () ->
        accountRepository.saveAll(givenAccounts());
  }

  @Bean
  InitializingBean createIndex() {
    log.info("Creating INDEX For Unique POA");
    return () ->
        mongoTemplate.indexOps(PowerOfAttorney.class).ensureIndex(
            new Index().named("roleIndex_").
                on("grantorName", Direction.ASC).
                on("granteeName", Direction.ASC).
                on("account", Direction.ASC).
                on("authorization", Direction.ASC).unique());
  }

  private List<Account> givenAccounts() {
    return Arrays.asList(givenAccount("123", "moselim", 1.0d, SavingsAccount.class),
        givenAccount("456", "sander", 2.0d, PaymentAccount.class));
  }

  private Account givenAccount(String accountNumber, String accountHolderName,
      double accountBalance, Class accountType) {
    if (accountType == SavingsAccount.class) {
      return new SavingsAccount(accountNumber, accountHolderName,
          accountBalance);
    } else {
      return new PaymentAccount(accountNumber, accountHolderName,
          accountBalance);
    }
  }
}
