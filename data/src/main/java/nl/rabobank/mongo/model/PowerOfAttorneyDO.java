package nl.rabobank.mongo.model;

import lombok.Data;

@Data
public class PowerOfAttorneyDO {
  private String granteeName;
  private String grantorName;
  private String account;
  private String authorization;
}
