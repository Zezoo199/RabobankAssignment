package nl.rabobank.model;

import lombok.Data;
import nl.rabobank.authorizations.Authorization;

/**
 * DTO For inserting POA , Other Class in Domain not suitable since we just need account number
 * here.
 */
@Data
public class PowerOfAttorneyDTO {

  private String granteeName;
  private String grantorName;
  private String accountNumber;
  private Authorization authorization;
}
