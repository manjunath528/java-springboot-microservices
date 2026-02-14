import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BillingIT {

    @Test
    void billingHealthShouldBeUp() {
        String billingUrl = System.getenv().getOrDefault("BILLING_SERVICE_URL", "http://localhost:4001");

        given()
                .baseUri(billingUrl)
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200);
    }
}
