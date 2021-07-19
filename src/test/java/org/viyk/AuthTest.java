package org.viyk;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class AuthTest {
    @Inject
    StudentTest studentTest;

    @Test
    public void shouldExistAdmin() {
        given()
                .queryParam("user_name", "admin")
                .queryParam("password", "admin")
                .when().get("/api/auth")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body("role", equalTo("admin"));
    }

    @Test
    public void shouldRejectWrongPassword() {
        given()
                .queryParam("user_name", "admin")
                .queryParam("password", "wrong")
                .when().get("/api/auth")
                .then()
                .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void shouldCreateStudentUser() {
        studentTest.shouldAdminCreateStudent();
        given()
                .queryParam("user_name", studentTest.student.get("id"))
                .queryParam("password", "")
                .when().get("/api/auth")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body("role", equalTo("student"));
    }
}