import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public abstract class BaseIntegrationTest {

    protected static String token;

    @BeforeAll
    static void authenticate() {
        // Use environment variable if set, fallback to localhost
        String baseUrl = System.getenv().getOrDefault("API_GATEWAY_URL", "http://localhost:4004");
        RestAssured.baseURI = baseUrl;

        String loginPayload = """
        {
          "email": "testuser@test.com",
          "password": "password123"
        }
        """;

        token = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
