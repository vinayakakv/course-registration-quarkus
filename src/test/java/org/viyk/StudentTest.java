package org.viyk;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class StudentTest {
    @Inject
    CourseTest courseTest;

    JSONObject student = new JSONObject()
            .put("name", "student1")
            .put("email", "student1@test.edu");

    String studentId;

    @Test
    public void shouldAdminCreateStudent() {
        if (studentId == null)
            studentId = String.format("14CS%02d", new Random().nextInt() % 999);
        student.put("id", studentId);
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(student.toString())
                .post("/api/student")
                .then()
                .statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    public void shouldAdminUpdateStudent() {
        shouldAdminCreateStudent();
        String body = new JSONObject().put("email", "student@test.edu").toString();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(body)
                .put(String.format("/api/student/%s", studentId))
                .then()
                .statusCode(Status.OK.getStatusCode());
    }

    @Test
    public void shouldAdminDeleteStudent() {
        shouldAdminCreateStudent();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .delete(String.format("/api/student/%s", studentId))
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void shouldNoDuplicateStudents() {
        shouldAdminCreateStudent();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(student.toString())
                .post("/api/student")
                .then()
                .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    public void testCountStudents() {
        shouldAdminCreateStudent();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(student.toString())
                .get("/api/student")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .assertThat()
                .body("size()", greaterThan(0));
    }

    @Test
    public void shouldStudentTakeCourse() {
        shouldAdminCreateStudent();
        courseTest.shouldAdminCreateCourse();
        given()
                .auth()
                .basic(studentId, "")
                .put(String.format("/api/student/%s/courses/%s", studentId, courseTest.courseId))
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void shouldStudentRejectCourse() {
        shouldStudentTakeCourse();
        given()
                .auth()
                .basic(studentId, "")
                .delete(String.format("/api/student/%s/courses/%s", studentId, courseTest.courseId))
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testStudentTakeNonExistingCourse() {
        shouldAdminCreateStudent();
        given()
                .auth()
                .basic(studentId, "")
                .put(String.format("/api/student/%s/courses/%s", studentId, "LL000"))
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testAdminShouldNotModifyCourse() {
        shouldAdminCreateStudent();
        courseTest.shouldAdminCreateCourse();
        given()
                .auth()
                .basic("admin", "admin")
                .put(String.format("/api/student/%s/courses/%s", studentId, courseTest.courseId))
                .then()
                .statusCode(Status.FORBIDDEN.getStatusCode());
    }
}
