package org.jane.gtelinternship;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.jane.gtelinternship.common.service.DateTimeProvider;
import org.jane.gtelinternship.common.service.UuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;

@ActiveProfiles("test")
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  properties = {
    "woocommerce.api.url=http://localhost:\\${wiremock.server.port}/woo",
    "logicom.api.url=http://localhost:\\${wiremock.server.port}/logicom",
  }
)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public abstract class IntegrationTestBase {
  // July 21, 2025 7:48:26 PM
  protected static Instant now = Instant.ofEpochSecond(1753127306L);
  @Autowired
  protected MockMvc mvc;
  @MockitoBean
  protected UuidGenerator uuidGenerator;
  @MockitoBean
  protected DateTimeProvider dateTimeProvider;
  @MockitoBean
  protected PasswordEncoder userPasswordEncoder;

  @BeforeEach
  void setUp() {
    WireMock.resetToDefault();

    Mockito.when(userPasswordEncoder.encode(anyString()))
      .thenAnswer((invocation) -> invocation.getArgument(0) + "_encoded");

    Mockito.when(dateTimeProvider.getInstant()).thenReturn(now);
  }
}
