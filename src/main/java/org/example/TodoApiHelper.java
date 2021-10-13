package org.example;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TodoApiHelper {

    public static final String DEFAULT_USER_PASSWORD = "pass1234!@#$";

    private static Faker faker = new Faker();

    public static RegisterUserResponse register() throws IOException, InterruptedException {
        RegisterUserRequest user = new RegisterUserRequest("Adam", faker.bothify("??????????##@gmail.com"),
                DEFAULT_USER_PASSWORD, "29");

        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(ApiRoutes.REGISTER_USER)
                .then()
                .extract()
                .as(RegisterUserResponse.class);
    }
}
