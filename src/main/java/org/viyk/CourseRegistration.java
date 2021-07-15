package org.viyk;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("")
public class CourseRegistration {

    @Path("/enroll/{course_id}")
    @POST
    @Transactional
    public Response enroll(
            @PathParam("course_id") String course_id,
            @HeaderParam("student_id") String student_id
    ) {
        Course course = Course.findById(course_id);
        Student student = Student.findById(student_id);
        if (course == null || student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        course.addStudent(student);
        student.enroll(course);
        return Response.ok().build();
    }

    @Path("/enroll/{course_id}")
    @DELETE
    @Transactional
    public Response delete_enroll(
            @PathParam("course_id") String course_id,
            @HeaderParam("student_id") String student_id
    ) {
        Course course = Course.findById(course_id);
        Student student = Student.findById(student_id);
        if (course == null || student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        course.removeStudent(student);
        student.delete_enroll(course);
        return Response.ok().build();
    }

    @Path("/enroll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnrolledCourses(@HeaderParam("student_id") String student_id) {
        Student student = Student.findById(student_id);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(student.courses.stream().map(x -> x.id).collect(Collectors.toList())).build();
    }

    @Path("/enroll/{course_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnrolledStudent(@PathParam("course_id") String course_id) {
        Course course = Course.findById(course_id);
        if (course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(course.students.stream().map(x -> x.id).collect(Collectors.toList())).build();
    }

}