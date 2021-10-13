package org.example;


import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static org.example.ApiRoutes.TASK;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskTests {

    public static String taskId;
    private static String token;
    public static String taskDescription;


    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        RegisterUserResponse registerResponse = TodoApiHelper.register();
        token = registerResponse.getToken();
    }

    @Test
    @Order(1)
    public void shouldReturnEmptyListWhenUserHasNoTasks() {
        TasksListResponse tasksListResponse = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when().get(TASK)
                .then()
                .extract().as(TasksListResponse.class);

        assertThat(tasksListResponse.getCount()).isEqualTo(0);
    }

    @Test
    @Order(2)
    public void shouldAddTask() {

        PostTaskRequest postTaskRequest1 = new PostTaskRequest("reading book3");
        PostTaskRequest postTaskRequest2 = new PostTaskRequest("reading book3");

        PostTaskResponse postTaskResponse = given().contentType(ContentType.JSON).header("Authorization", "Bearer " + token)
                .body(postTaskRequest1)
                .when().post(TASK)
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().as(PostTaskResponse.class);

        given().contentType(ContentType.JSON).header("Authorization", "Bearer " + token)
                .body(postTaskRequest2)
                .when().post(TASK)
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().as(PostTaskResponse.class);

        taskId = (postTaskResponse.getData().get_id());
        taskDescription = postTaskResponse.getData().getDescription();
    }


    @Test
    @Order(3)
    public void shouldFindUserTaskById() {
        PostTaskResponse postTaskResponse = given().contentType(ContentType.JSON)       // TODO: change
                .header("Authorization", "Bearer " + token)
                .when().get(TASK + "/" + taskId)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(PostTaskResponse.class);

        assertThat(postTaskResponse.getData().get_id()).isEqualTo(taskId);
        assertThat(postTaskResponse.getData().getDescription()).isEqualTo(taskDescription);
    }


    @Test
    @Order(4)
    public void shouldGetAllUserTasks() {

        TasksListResponse tasksListResponse = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when().get(TASK)
                .then()
                .extract().as(TasksListResponse.class);

        assertThat(tasksListResponse.getCount()).isEqualTo(2);

    }


    @Test
    @Order(5)
    public void shouldGetTaskByCompleted() {
        TasksListResponse responses = given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("completed", "true")
                .when().get(TASK)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(TasksListResponse.class);

        assertThat(responses.getCount()).isEqualTo(0);
    }

    @Test
    @Order(6)
    public void shouldGetTasksByPagination() {
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("limit", "2")
                .queryParam("skip", "10")
                .when().get(TASK)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }


    @Test
    @Order(7)
    public void shouldUpdateTaskById() {
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(true);

        PostTaskResponse postTaskResponse = given().contentType(ContentType.JSON)
                .body(updateTaskRequest)
                .header("Authorization", "Bearer " + token)
                .when().put(TASK + "/" + taskId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(PostTaskResponse.class);

//        assertThat(taskResponse.isSuccess()).isTrue();
    }


    @Test
    @Order(8)
    public void shouldDeleteTaskById() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(TASK + "/" + taskId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
}
