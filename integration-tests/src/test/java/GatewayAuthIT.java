import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GatewayAuthIT extends BaseIntegrationTest {

    @Test
    void shouldRejectRequestsWithoutToken() {
        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldRejectRequestsWithInvalidToken() {
        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/api/users")
                .then()
                .statusCode(401);
    }
}
