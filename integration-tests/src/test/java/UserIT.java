import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserIT extends BaseIntegrationTest {

    @Test
    void shouldCreateUser() {
        String payload = """
        {
          "name": "User-%s",
          "email": "user-%s@test.com",
          "address": "123 Main Street",
          "dateOfBirth": "2000-09-09",
          "weight": 65.0,
          "height": 1.70,
          "gender": "FEMALE",
          "fitnessGoal": "LOSE_WEIGHT",
          "dailyStepGoal": 8000,
          "sleepGoalHours": 8,
          "notificationsEnabled": true,
          "registeredDate": "2024-11-28"
        }
        """.formatted(UUID.randomUUID(), UUID.randomUUID());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void shouldGetAllUsers() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }
}
