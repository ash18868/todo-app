package com.teamtetra.todoapp.rest;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.teamtetra.todoapp.dto.CreateSubtaskRequest;
import com.teamtetra.todoapp.dto.CreateTodoRequest;
import com.teamtetra.todoapp.dto.UpdateSubtaskRequest;
import com.teamtetra.todoapp.dto.UpdateTodoRequest;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Shared test objects — initialised fresh before each test in setup()
    private User testUser;
    private User loginCredentials;
    private Todo testTodo;
    private CreateTodoRequest testAddTodo;
    private Subtask testSubtask;
    private CreateSubtaskRequest testAddSubtask;
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
        testUser.setPassword(passwordEncoder.encode("P0ssword!"));
        userRepo.save(testUser);

        loginCredentials = new User();
        loginCredentials.setUsername("T3ster");
        loginCredentials.setPassword("P0ssword!");

        testAddTodo = new CreateTodoRequest(
            "Implement my REST assured addTodo test"
        );

        // Pre-seed a todo so update/delete todo tests have a todo to work with
        testTodo = new Todo();
        testTodo.setUser(testUser);
        testTodo.setTitle("Implement the rest of my REST assured tests");
        testTodo.setCompleted(false);
        Todo savedTodo = todoRepo.save(testTodo);

        testAddSubtask = new CreateSubtaskRequest(
            "Implement my REST assured addSubtask test"
        );

        // Pre-seed a subtask so update/delete subtask tests have a subtask to work with
        testSubtask = new Subtask();
        testSubtask.setTitle("Implement the rest of my REST assured tests");
        testSubtask.setTodo(savedTodo);
        testSubtask.setCompleted(false);
        subtaskRepo.save(testSubtask);

        // Log in once and store the token — available to all tests via the token field
        token = given()
            .contentType(ContentType.JSON)
            .body(loginCredentials)
        .when()
            .post("/login")
        .then()
            .statusCode(202)
            .extract().path("token");
    }
    
    //--------------------------
    //       User tests
    //--------------------------
    @Test
    void registerTest() {

        // Use a different username than testUser ("T3ster") to avoid duplicate conflict
        User newUser = new User();
        newUser.setUsername("NewUser1");
        newUser.setPassword("P0ssword!");

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
            .body(loginCredentials)
        .when()
            .post("/login")
        .then()
            .statusCode(202);
    }

    //--------------------------
    //       To-do tests
    //--------------------------
    @Test // Add todo
    void addTodoTest() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testAddTodo)
        .when()
            .post("/todo")
        .then()
            .statusCode(201);
    }

    @Test // Update todo
    void updateTodoTest() {
        UpdateTodoRequest updateRequest = new UpdateTodoRequest(
            "This is a new title",
            testTodo.isCompleted()
        );

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(updateRequest)
        .when()
            .put("/todo/{todoId}", testTodo.getTodoId())
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
            .delete("/todo/{todoId}", testTodo.getTodoId())
        .then()
            .statusCode(200);
    }

    //subtask get post delete put

    @Test 
    void addSubtaskTest() {

         given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(testAddSubtask)
            .when()
            .post("/todo/{todoId}/subtask", testTodo.getTodoId())
            .then()
            .statusCode(201);
    }

    @Test 
    void updateSubtaskTest() {

        UpdateSubtaskRequest updateRequest = new UpdateSubtaskRequest(
            "new Title!",
            testSubtask.isCompleted()
        );

         given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(updateRequest)
            .when()
            .put(
                "/todo/{todoId}/subtask/{subtaskId}",
                testSubtask.getTodo().getTodoId(),
                testSubtask.getSubtaskId()
            )
            .then()
            .statusCode(200);
    }

    @Test 
    void getSubtaskTest() {

         given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/todo/{todoId}/subtask", testSubtask.getTodo().getTodoId())
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
            .delete(
                "/todo/{todoId}/subtask/{subtaskId}",
                testSubtask.getTodo().getTodoId(),
                testSubtask.getSubtaskId()
            )
            .then()
            .statusCode(200);
    }
}
