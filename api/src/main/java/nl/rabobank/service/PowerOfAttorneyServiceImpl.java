package nl.rabobank.service;

import lombok.RequiredArgsConstructor;
import nl.rabobank.authorizations.PowerOfAttorney;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PowerOfAttorneyServiceImpl implements PowerOfAttorneyService {

  private  PowerOfAttorneyRepository powerOfAttorneyRepository;
  public void post(PowerOfAttorney powerOfAttorney) {
    powerOfAttorneyRepository.save(powerOfAttorney);
  }
}
