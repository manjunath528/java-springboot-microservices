import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIT extends BaseIntegrationTest {

  @Test
  void shouldLoginSuccessfully() {
    String loginPayload = """
        {
          "email": "testuser@test.com",
          "password": "password123"
        }
        """;

    given()
            .contentType("application/json")
            .body(loginPayload)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .body("token", notNullValue());
  }

  @Test
  void shouldRejectInvalidCredentials() {
    String loginPayload = """
        {
          "email": "invalid@test.com",
          "password": "wrongpassword"
        }
        """;

    given()
            .contentType("application/json")
            .body(loginPayload)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401);
  }
}
