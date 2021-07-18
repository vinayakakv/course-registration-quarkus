package org.viyk;

import io.quarkus.security.UnauthorizedException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import java.util.List;

@Path("/auth")
@PermitAll
public class Auth {
    @GET
    public User authenticate(@QueryParam("user_name") String userName, @QueryParam("password") String password){
        List<User> users = User.list("userName", userName);
        if (users.size() < 1) {
            throw new NotFoundException("User not found");
        }
        User user = users.get(0);
        if (!user.password.equals(password)) {
            throw new ForbiddenException();
        }
        return user;
    }
}
