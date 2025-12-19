import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class NutritionIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateNutrition() {
        String payload = """
        {
          "userId": "%s",
          "mealType": "Breakfast",
          "calories": 500,
          "protein": 25,
          "carbs": 60,
          "fat": 20,
          "date": "%s"
        }
        """.formatted(UUID.randomUUID(), LocalDate.now());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/nutrition")
                .then()
                .statusCode(201)
                .body("mealType", equalTo("Breakfast"))
                .body("calories", equalTo(500));
    }

    @Test
    void shouldGetUserNutrition() {
        UUID userId = UUID.randomUUID();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/nutrition/user/" + userId)
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }
}
