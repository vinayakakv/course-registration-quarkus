package org.viyk;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("")
public class CourseRegistration {
    @Path("enroll/{courseId}")
    @PUT
    public void enroll(String courseId) {

    }

    @Path("leave/{courseId}")
    @DELETE
    public void leave(String courseId) {

    }
}