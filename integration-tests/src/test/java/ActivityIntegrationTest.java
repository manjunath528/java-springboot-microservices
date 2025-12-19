import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ActivityIntegrationTest extends BaseIntegrationTest{

    @Test
    void shouldCreateActivity() {
        String payload = """
        {
          "userId": "11111111-1111-1111-1111-111111111111",
          "workoutType": "RUNNING",
          "durationMinutes": 30,
          "caloriesBurned": 300,
          "activityDate": "2025-01-01",
          "status": "COMPLETED"
        }
        """;

        given()
                .header("Authorization", "Bearer " + token)   // 🔴 REQUIRED
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/activities")
                .then()
                .statusCode(201);
    }
    @Test
    void shouldGetUserActivities(){
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

