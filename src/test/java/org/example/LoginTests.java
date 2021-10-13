package org.example;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static org.example.ApiRoutes.*;
import static org.example.TodoApiHelper.DEFAULT_USER_PASSWORD;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests {

    private static String email;
    private static String token;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        RegisterUserResponse registerResponse = TodoApiHelper.register();
        email = registerResponse.getUser().getEmail();
        token = registerResponse.getToken();
    }

    @Test
    @Order(1)
    public void userShouldBeLoggedInWhenUsingValidCredentials() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, DEFAULT_USER_PASSWORD);

        given().contentType(ContentType.JSON)
                .body(loginUserRequest)
                .when().post(LOGIN_USER)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(LoginUserResponse.class);
    }

    @Test
    @Order(2)
    public void userShouldNotBeLoggedWhenPasswordIsIncorrect() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, "1234");

        String response = given().contentType(ContentType.JSON).body(loginUserRequest).when().post(LOGIN_USER)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().asString();

        assertThat(response).isEqualTo("\"Unable to login\"");
    }

    @Test
    @Order(3)
    public void userShouldBeLoggedInViaToken() {
        given().header("Authorization", "Bearer " + token)
                .when().get(FETCH_USER)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UserResponse.class);
    }

    @Test
    @Order(4)
    public void userShouldBeLoggedOut() {
        given().header("Authorization", "Bearer " + token)
                .when().post(LOGOUT_USER)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
