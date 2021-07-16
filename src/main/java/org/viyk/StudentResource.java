package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
    @Path("/{id}/courses")
    public List<String> getCourses(@PathParam("id") String id) {
        Student entity = Student.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity.courses.stream().map(x -> x.id).collect(Collectors.toList());
    }

    @PUT
    @Path("/{id}/courses/{course_id}")
    @Transactional
    public void enrollCourse(@PathParam("id") String id, @PathParam("course_id") String course_id) {
        Student student = Student.findById(id);
        Course course = Course.findById(course_id);
        if (student == null || course == null) {
            throw new NotFoundException();
        }
        student.enroll(course);
        course.addStudent(student);
    }

    @DELETE
    @Path("/{id}/courses/{course_id}")
    @Transactional
    public void optOutCourse(@PathParam("id") String id, @PathParam("course_id") String course_id) {
        Student student = Student.findById(id);
        Course course = Course.findById(course_id);
        if (student == null || course == null) {
            throw new NotFoundException();
        }
        student.deleteEnroll(course);
        course.removeStudent(student);
    }

    @POST
    @Transactional
    public Response create(@Valid Student student) {
        Student entity = Student.findById(student.id);
        if (entity != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        student.persist();
        User user = new User();
        user.userName = student.email;
        user.password = "";
        user.role = "student";
        user.persist();
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
    public void deleteEnroll(Course course) {
        courses.removeIf(x-> x.id.equals(course.id));
        this.persist();
    }
}