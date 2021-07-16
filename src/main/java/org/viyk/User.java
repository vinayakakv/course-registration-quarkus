package org.viyk;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name="users")
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public Long id;
    @NotBlank
    public String userName;
    public String password;
    @NotBlank
    public String role;
}
