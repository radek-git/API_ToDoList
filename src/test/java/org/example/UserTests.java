package org.example;


import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.example.ApiRoutes.FETCH_USER;
import static org.example.ApiRoutes.GET_AVATAR;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {

    private static String token;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        RegisterUserResponse registerResponse = TodoApiHelper.register();
        token = registerResponse.getToken();
    }


    @Test
    @Order(1)
    public void userShouldBeUpdated() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("101");

        UpdateUserResponse updateUserResponse = given().contentType(ContentType.JSON)
                .body(updateUserRequest)
                .header("Authorization", "Bearer " + token)
                .when().put(FETCH_USER)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(UpdateUserResponse.class);

        assertThat(updateUserResponse.getData().getAge()).isEqualTo(Integer.valueOf(updateUserRequest.getAge()));
    }

    @Test
    @Order(2)
    public void shouldReturnNoImageFoundWhenUserHasNoAvatar() {
        ApiError apiError = when().get(GET_AVATAR)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(ApiError.class);

        assertThat(apiError.getError()).isEqualTo("No image found");
    }

    @Test
    @Order(3)
    public void deleteCurrentUser() {
        given().header("Authorization", "Bearer " + token)
                .when().delete(FETCH_USER)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(4)
    public void shouldNotDeleteUserThatNotExist() {
        given().header("Authorization", "Bearer " + token)
                .when()
                .delete(FETCH_USER)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
