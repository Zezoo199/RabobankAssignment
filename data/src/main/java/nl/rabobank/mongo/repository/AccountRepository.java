package nl.rabobank.mongo.repository;

import java.util.Optional;
import nl.rabobank.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Accounts Mongo Repo
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

  /**
   * findByAccountNumber
   *
   * @param accountNumber accountNumber
   * @return Optional Account
   */
  Optional<Account> findByAccountNumber(String accountNumber);
}
