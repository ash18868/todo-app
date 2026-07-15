package com.teamtetra.todoapp.rest;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.teamtetra.todoapp.entity.Subtask;
import com.teamtetra.todoapp.entity.User;
import com.teamtetra.todoapp.entity.Todo;
import com.teamtetra.todoapp.repo.SubtaskRepo;
import com.teamtetra.todoapp.repo.TodoRepo;
import com.teamtetra.todoapp.repo.UserRepo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class RestAssuredTests {

    // Make sure you save the random port the web server is running on in a variable
    @LocalServerPort private int port;

    // Inject repos to control DB state directly
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TodoRepo todoRepo;
    @Autowired
    private SubtaskRepo subtaskRepo;

    // Shared test objects — initialised fresh before each test in setup()
    private User testUser;
    private Todo testTodo;
    private Subtask testSubtask;
    private String token;

    @BeforeEach
    void setup() {
        // we can specify where we want all of our requests to be sent
        RestAssured.baseURI = "http://localhost";
        // we can specify the port for the requests
        RestAssured.port = port;

        // Wipe all data before each test — guaranteed clean slate
        subtaskRepo.deleteAll();
        todoRepo.deleteAll();
        userRepo.deleteAll();

        // Pre-seed a user so login/todo tests have someone to work with
        testUser = new User();
        testUser.setUsername("T3ster");
        testUser.setPassword("P0ssword");
        userRepo.save(testUser);

        testTodo = new Todo();
        testTodo.setUserId(testUser.getUserId());
        testTodo.setTitle("Implement my REST assured test");
        testTodo.setCompleted(false);
        Todo savedTodo = todoRepo.save(testTodo);

        testSubtask = new Subtask();
        testSubtask.setTitle("wash dishes");
        testSubtask.setTodoId(savedTodo.getTodoId());
        testSubtask.setCompleted(false);
        subtaskRepo.save(testSubtask);

        // Log in once and store the token — available to all tests via the token field
        token = given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .extract().asString(); // response body is the raw JWT string
    }
    
    //--------------------------
    //       User tests
    //--------------------------
    @Test
    void registerTest() {

        // Use a different username than testUser ("T3ster") to avoid duplicate conflict
        User newUser = new User();
        newUser.setUsername("NewUser1");
        newUser.setPassword("P0ssword");

        given()
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);
    }

    @Test
    void loginTest() {
        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/login")
        .then()
            .statusCode(200);
    }

    //--------------------------
    //       To-do tests
    //--------------------------
    @Test // Add todo
    void addTodoTest() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testTodo)
        .when()
            .post("/todo")
        .then()
            .statusCode(201);
    }

    @Test // Update todo
    void updateTodoTest() {
        testTodo.setTitle("This is a new title");
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testTodo)
        .when()
            .put("/todo")
        .then()
            .statusCode(200);
    }

    @Test // Get todos
    void getTodoTest() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/todo")
        .then()
            .statusCode(200);
    }

    @Test // Delete todos
    void deleteTodoTest() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testTodo)
        .when()
            .delete("/todo")
        .then()
            .statusCode(200);
    }

    //subtask get post delete put

    @Test 
    void addSubtaskTest() {

         given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testSubtask)
            .when()
            .post("/subtask")
            .then()
            .statusCode(201);
    }

    @Test 
    void updateSubtaskTest() {

        testSubtask.setTitle("new Title!");
         given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testSubtask)
            .when()
            .put("/subtask")
            .then()
            .statusCode(200);
    }

    @Test 
    void getSubtaskTest() {

         given()
            .header("Authorization", "Bearer " + token)
            .queryParam("todoId", testSubtask.getTodoId())
        .when()
            .get("/subtask")
        .then()
            .statusCode(200);
    }

    @Test 
    void deleteSubtaskTest() {

         given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testSubtask)
            .when()
            .delete("/subtask")
            .then()
            .statusCode(200);
    }
}