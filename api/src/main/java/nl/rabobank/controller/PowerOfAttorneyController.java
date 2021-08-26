package nl.rabobank.controller;

import lombok.RequiredArgsConstructor;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.service.PowerOfAttorneyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequiredArgsConstructor
public class PowerOfAttorneyController {

  private final PowerOfAttorneyService powerOfAttorneyService;

  @PostMapping("grantPowerOfAttorney")
  public void grantPowerOfAttorney(PowerOfAttorney powerOfAttorney) {
    powerOfAttorneyService.post(powerOfAttorney);
  }
}
