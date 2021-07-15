package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.Set;

@Path("/student")
public interface StudentResource extends PanacheEntityResource<Student, String> {
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