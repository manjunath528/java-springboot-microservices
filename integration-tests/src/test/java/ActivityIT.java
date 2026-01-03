import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;

public class ActivityIT extends BaseIntegrationTest {

    @Test
    void shouldCreateActivity() {
        String payload = """
        {
          "userId": "%s",
          "workoutType": "RUNNING",
          "durationMinutes": 30,
          "caloriesBurned": 300,
          "activityDate": "2025-01-01",
          "status": "COMPLETED"
        }
        """.formatted(UUID.randomUUID());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/activities")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldGetUserActivities() {
        UUID userId = UUID.randomUUID();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/activities/user/" + userId)
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }
}
