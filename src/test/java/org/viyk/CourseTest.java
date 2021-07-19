package org.viyk;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response.Status;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class CourseTest {
    JSONObject course = new JSONObject()
            .put("name", "Intro to CSE");

    String courseId;

    @Test
    public void shouldAdminCreateCourse() {
        courseId = String.format("CS%02d", new Random().nextInt() % 999);
        course.put("id", courseId);
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(course.toString())
                .post("/api/course")
                .then()
                .statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    public void shouldAdminUpdateStudent() {
        shouldAdminCreateCourse();
        String body = new JSONObject().put("name", "Intro to C").toString();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(body)
                .put(String.format("/api/course/%s", courseId))
                .then()
                .statusCode(Status.OK.getStatusCode());
    }

    @Test
    public void shouldAdminDeleteCourse() {
        shouldAdminCreateCourse();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .delete(String.format("/api/course/%s", courseId))
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void shouldNoDuplicateStudents(){
        shouldAdminCreateCourse();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(course.toString())
                .post("/api/course")
                .then()
                .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    public void testCountStudents() {
        shouldAdminCreateCourse();
        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("admin", "admin")
                .body(course.toString())
                .get("/api/course")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .assertThat()
                .body("size()", greaterThan(0));
    }

}
