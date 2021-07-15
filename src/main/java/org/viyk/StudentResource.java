package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.panache.common.Page;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {
    @GET
    public List<Student> list(
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize
    ) {
        Page page = Page.of(pageIndex, pageSize);
        PanacheQuery<Student> students = Student.findAll();
        return students.page(page).list();
    }

    @GET
    @Path("/{id}")
    public Student get(@PathParam("id") String id) {
        return Student.findById(id);
    }

    @GET
    @Path("/{id}/course")
    public List<String> getCourses(@PathParam("id") String id) {
        Student entity = Student.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity.courses.stream().map(x -> x.id).collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response create(@Valid Student student) {
        Student entity = Student.findById(student.id);
        if (entity != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        student.persist();
        return Response.created(URI.create("/student/" + student.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Student update(@PathParam("id") String id, Student student) {
        Student entity = Student.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        if (student.name != null)
            entity.name = student.name;

        if (student.email != null)
            entity.email = student.email;

        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") String id) {
        Student entity = Student.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }
}

@Entity
class Student extends PanacheEntityBase {
    @Id
    public String id;
    @NotBlank
    public String name;
    @NotBlank
    public String email;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "Enrollment",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    protected Set<Course> courses = new HashSet<>();

    @Transactional
    public void enroll(Course course) {
        courses.add(course);
        this.persist();
    }

    @Transactional
    public void delete_enroll(Course course) {
        courses.removeIf(x-> x.id.equals(course.id));
        this.persist();
    }
}