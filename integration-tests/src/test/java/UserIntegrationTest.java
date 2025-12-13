import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetAllUsers() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldCreateUser() {
        String payload = """
        {
          "name": "aa Doe",
          "email": "cc.doe@test.com",
          "address": "Street 1",
          "dateOfBirth": "1995-01-01",
          "registeredDate": "2025-01-01"
        }
        """;

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200);
    }
}
