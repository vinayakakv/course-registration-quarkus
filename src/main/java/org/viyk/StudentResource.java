package org.viyk;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

class Student {
    private String id;
    private String name;
    private String email;
}

@Path("/student")
public class StudentResource {
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
