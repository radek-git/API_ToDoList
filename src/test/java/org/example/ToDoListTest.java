package org.example;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.example.ApiRoutes.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToDoListTest {


    private static String email;
    private static String token;
    public static String taskId;
    public static String taskDescription;


    @BeforeAll
    static void beforeAll() {
        Faker faker = new Faker();
        email = faker.bothify("??????????##@gmail.com");
    }














}
