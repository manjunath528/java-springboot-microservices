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
                            "name": "masss",
                            "email": "masss.sharma@example.com",
                            "address": "123 Main Street, Apt 22",
                            "dateOfBirth": "2000-09-09",
                            "weight": 60.5,
                            "height": 1.65,
                            "gender": "FEMALE",
                            "fitnessGoal": "LOSE_WEIGHT",
                            "dailyStepGoal": 10000,
                            "sleepGoalHours": 8.0,
                            "notificationsEnabled": true,
                            "registeredDate": "2024-11-28"
                          }
                
        """;

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)  // Change after debugging if necessary
                .log().body();
    }
}
