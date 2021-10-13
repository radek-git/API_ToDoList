package org.example;


import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static org.example.ApiRoutes.REGISTER_USER;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterTests {

    private static String email;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        Faker faker = new Faker();
        email = faker.bothify("??????????##@gmail.com");
    }

    @Test
    @Order(1)
    public void shouldRegisterUser() {
        RegisterUserRequest user = new RegisterUserRequest("Jan", email, "qwersfgsgty", "29");

        RegisterUserResponse registerUserResponse = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER_USER)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(RegisterUserResponse.class);

        assertThat(registerUserResponse.getUser().getAge()).isEqualTo(Integer.valueOf(user.getAge()));
        assertThat(registerUserResponse.getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @Order(2)
    public void userShouldNotBeRegistered() {
        RegisterUserRequest user = new RegisterUserRequest("Jan", email, "qwersfgsgty", "29");

        Response response = given().contentType(ContentType.JSON).body(user).when().post(REGISTER_USER);
        response.then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
