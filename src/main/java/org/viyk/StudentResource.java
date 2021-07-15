package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Path;

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
}