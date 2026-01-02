import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public abstract class BaseIntegrationTest {

    protected static String token;

    @BeforeAll
    static void authenticate() {
        // Use environment variable if set, fallback to localhost
        RestAssured.baseURI = System.getenv().getOrDefault("API_GATEWAY_URL", "http://api-gateway:4004");

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
