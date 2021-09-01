package nl.rabobank;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.exception.AccountNotFoundException;
import nl.rabobank.exception.GrantorDoesNotOwnAccountException;
import nl.rabobank.exception.RoleAlreadyGrantedException;
import nl.rabobank.model.PowerOfAttorneyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class RaboAssignmentApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  public static PowerOfAttorneyDTO givenValidPOA() {
    PowerOfAttorneyDTO powerOfAttorney = new PowerOfAttorneyDTO();
    powerOfAttorney.setAccountNumber("123");
    powerOfAttorney.setAuthorization(Authorization.READ);
    powerOfAttorney.setGranteeName("john");
    powerOfAttorney.setGrantorName("moselim");
    return powerOfAttorney;
  }

  public static String asJsonString(final Object obj) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(obj);
  }

  @Test
  public void givenPOA_whenPostValidPOA_thenCreated201() throws Exception {

    //given
    PowerOfAttorneyDTO powerOfAttorney = givenValidPOA();

    //when & then
    this.mockMvc.perform(post("/grantPowerOfAttorney").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(powerOfAttorney))).
        andDo(print()).
        andExpect(status().isCreated());
  }

  @Test
  public void givenPOAWithInvalidAccount_whenPostPOA_thenNotFound404() throws Exception {

    //given
    String notExistingAccountNumber = "999";
    PowerOfAttorneyDTO powerOfAttorney = new PowerOfAttorneyDTO();
    powerOfAttorney.setAccountNumber(notExistingAccountNumber);
    powerOfAttorney.setAuthorization(Authorization.READ);
    powerOfAttorney.setGranteeName("john");
    powerOfAttorney.setGrantorName("moselim");

    //when & then
    Class expectedException = this.mockMvc.perform(
            post("/grantPowerOfAttorney").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(powerOfAttorney))).
        andDo(print()).
        andExpect(status().isNotFound()).andReturn().getResolvedException().getClass();

    assertEquals(expectedException, AccountNotFoundException.class);
  }

  @Test
  public void givenPOAWithGrantorNotAccountOwner_whenPostPOA_thenForbidden403() throws Exception {

    //given
    String notAccountOwner = "Jan";
    PowerOfAttorneyDTO powerOfAttorney = new PowerOfAttorneyDTO();
    powerOfAttorney.setAccountNumber("123");
    powerOfAttorney.setAuthorization(Authorization.READ);
    powerOfAttorney.setGranteeName("john");
    powerOfAttorney.setGrantorName(notAccountOwner);

    //when & then
    Class expectedException = this.mockMvc.perform(
            post("/grantPowerOfAttorney").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(powerOfAttorney))).
        andDo(print()).
        andExpect(status().isForbidden()).andReturn().getResolvedException().getClass();

    assertEquals(expectedException, GrantorDoesNotOwnAccountException.class);
  }

  @Test
  public void givenPOAAlreadyExists_whenPostPOA_thenBadRequest4xx() throws Exception {

    //given
    PowerOfAttorneyDTO powerOfAttorney = new PowerOfAttorneyDTO();
    powerOfAttorney.setAccountNumber("123");
    powerOfAttorney.setAuthorization(Authorization.READ);
    powerOfAttorney.setGranteeName("john");
    powerOfAttorney.setGrantorName("moselim");
    this.mockMvc.perform(post("/grantPowerOfAttorney").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(powerOfAttorney))).
        andDo(print()).
        andExpect(status().isCreated());

    //when granting again & then
    Class expectedException = this.mockMvc.perform(
            post("/grantPowerOfAttorney").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(powerOfAttorney))).
        andDo(print()).
        andExpect(status().is4xxClientError()).andReturn().getResolvedException().getClass();

    assertEquals(expectedException, RoleAlreadyGrantedException.class);
  }

  @Test
  public void givenEmptyPOA_whenGet_thenEmptyList() throws Exception {
    this.mockMvc.perform(get("/getAccessibleAccountsForUser").param("userName", "moselim")).
        andDo(print()).
        andExpect(status().is2xxSuccessful()).
        andExpect(jsonPath("$.accountsWithReadAccess", hasSize(0))).
        andExpect(jsonPath("$.accountsWithWriteAccess", hasSize(0)));

  }

  @Test
  public void givenValidPOAExist_whenGet_thenValidJson() throws Exception {
    //given
    PowerOfAttorneyDTO powerOfAttorney = givenValidPOA();
    String userWhomHasBeenGranted = "john";
    this.mockMvc.perform(post("/grantPowerOfAttorney").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(powerOfAttorney))).
        andDo(print()).
        andExpect(status().isCreated());
    //when & then
    this.mockMvc.perform(
            get("/getAccessibleAccountsForUser").param("userName", userWhomHasBeenGranted)).
        andDo(print()).
        andExpect(status().is2xxSuccessful()).
        andExpect(jsonPath("$.accountsWithReadAccess", hasSize(1))).
        andExpect(jsonPath("$.accountsWithReadAccess[0].accountHolderName", is("moselim")));
  }
}
