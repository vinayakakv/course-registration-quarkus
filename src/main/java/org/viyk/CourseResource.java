package org.viyk;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

class Course {
    String id;
    String name;
}

@Path("/course")
public class CourseResource {
    @POST
    public void create() {

    }

    @PUT
    public void update() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getMany() {

    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getOne(String id) {

    }

    @DELETE
    public void delete() {

    }
}
