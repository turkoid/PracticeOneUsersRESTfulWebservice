package com.turkoid.rest.resources;

import com.turkoid.rest.dao.UserDao;
import com.turkoid.rest.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by turkoid on 8/21/2016.
 */

@Path("users")
public class UserResource {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") int id) {
        try {
            User user = UserDao.getUser(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok().entity(user).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUsers() {
        try {
            List<User> users = UserDao.getUsers();
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users){};
            return Response.ok().entity(entity).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
