package com.turkoid.rest.resources;

import com.turkoid.rest.dao.UserDao;
import com.turkoid.rest.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by turkoid on 8/21/2016.
 */

@Path("users")
public class UserResource {
    @Context
    public UriInfo uriInfo;

    @GET
    @Path("/{id : \\d+}")
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
    @Produces(MediaType.APPLICATION_JSON)
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if (user == null || user.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            user.setId(-1);
            user.setName(user.getName().trim());
            user = UserDao.addUser(user);
            if (user.getId() == -1) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI uri = ub
                    .path(String.valueOf(user.getId()))
                    .build();
            return Response.created(uri).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, User user) {
        try {
            user.setId(id);
            if (UserDao.updateUser(user)) {
                return Response.ok(user).build();
            } else {
                return Response.serverError().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        try {
            if (UserDao.deleteUser(id)) {
                return Response.accepted().build();
            } else {
                return Response.serverError().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
