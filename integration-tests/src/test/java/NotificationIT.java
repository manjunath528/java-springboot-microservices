import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class NotificationIT extends BaseIntegrationTest {

    @Test
    void shouldCreateNotificationOnUserCreated() {
        String payload = """
        {
          "name": "User-%s",
          "email": "user-%s@test.com",
          "address": "123 Main Street",
          "dateOfBirth": "2000-09-09",
          "weight": 65.0,
          "height": 1.40,
          "gender": "FEMALE",
          "fitnessGoal": "LOSE_WEIGHT",
          "dailyStepGoal": 8000,
          "sleepGoalHours": 8,
          "notificationsEnabled": true
        }
        """.formatted(UUID.randomUUID(), UUID.randomUUID());

        String userId = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(2))
                .untilAsserted(() ->
                        given()
                                .header("Authorization", "Bearer " + token)
                                .when()
                                .get("/api/notifications/user/" + userId)
                                .then()
                                .statusCode(200)
                                .body("size()", greaterThan(0))
                                .body("userId", hasItem(userId))
                );
    }
}
