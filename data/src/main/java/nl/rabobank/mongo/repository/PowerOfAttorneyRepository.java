package nl.rabobank.mongo.repository;


import java.util.List;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * POA Mongo Repo
 */
@Repository
public interface PowerOfAttorneyRepository extends MongoRepository<PowerOfAttorney, Long> {

  /**
   * FindAllByGranteeNameAndAuthorization
   *
   * @param granteeName granteeName
   * @param authorization authorization
   */
  List<PowerOfAttorney> findAllByGranteeNameAndAuthorization(String granteeName,
      Authorization authorization);
}
