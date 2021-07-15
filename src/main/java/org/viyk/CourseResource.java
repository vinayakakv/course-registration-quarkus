package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.Set;

@Path("/course")
public interface CourseResource extends PanacheEntityResource<Course, String> {
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
    public void removeStudent(Student student){
        students.removeIf(x-> x.id.equals(student.id));
        this.persist();
    }
}