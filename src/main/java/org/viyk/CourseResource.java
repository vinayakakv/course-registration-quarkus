package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.ws.rs.Path;

@Path("/course")
public interface CourseResource extends PanacheEntityResource<Course, String> {
}

@Entity
class Course extends PanacheEntityBase {
    @Id
    public String id;
    public String name;
}