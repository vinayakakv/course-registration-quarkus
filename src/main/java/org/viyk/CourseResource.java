package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.annotation.security.RolesAllowed;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseResource {
    @GET
    @RolesAllowed({"admin", "student"})
    public List<Course> list() {
        return Course.listAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"admin", "student"})
    public Course get(@PathParam("id") String id) {
        Course course = Course.findById(id);
        if (course == null) {
            throw new NotFoundException();
        }
        return course;
    }

    @GET
    @Path("/{id}/students")
    @RolesAllowed("admin")
    public List<String> getStudents(@PathParam("id") String id) {
        Course entity = Course.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity.students.stream().map(x -> x.id).collect(Collectors.toList());
    }

    @POST
    @Transactional
    @RolesAllowed("admin")
    public Response create(@Valid Course course) {
        Course entity = Course.findById(course.id);
        if (entity != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        course.persist();
        return Response.created(URI.create("/course/" + course.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("admin")
    public Course update(@PathParam("id") String id, Course course) {
        Course entity = Course.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        if (course.name != null)
            entity.name = course.name;

        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("admin")
    public void delete(@PathParam("id") String id) {
        Course entity = Course.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }
}

@Entity
class Course extends PanacheEntityBase {
    @Id
    public String id;
    @NotBlank
    public String name;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    protected Set<Student> students = new HashSet<>();

    @Transactional
    public void addStudent(Student student) {
        students.add(student);
        this.persist();
    }

    @Transactional
    public void removeStudent(Student student) {
        students.removeIf(x -> x.id.equals(student.id));
        this.persist();
    }
}