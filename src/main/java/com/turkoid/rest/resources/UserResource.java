package com.turkoid.rest.resources;

import com.turkoid.rest.dao.UserDao;
import com.turkoid.rest.dao.common.DaoResult;
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getUser(@PathParam("id") int id) {
        try {
            DaoResult<User> result = UserDao.getUser(id);
            User user = result.getObject();
            if (user == null) {
                //return 404 not found if id doesn't exit
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            //return 200 if found
            return Response.ok().entity(user).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getUsers() {
        try {
            DaoResult<User> result = UserDao.getUsers();
            List<User> users = result.getObjects();
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users){};
            return Response.ok().entity(entity).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response addUser(User user) {
        if (user.getId() != null) {
            //should not be passing user id
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: user id not allowed here").build();
        }
        if (user == null || user.getName() == null || user.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: user name is required").build();
        }

        try {
            user.setName(user.getName().trim());
            DaoResult<User> result = UserDao.addUser(user);
            user = result.getObject();
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI uri = ub.path(String.valueOf(user.getId())).build();  //build new uri /users/{id}

            return Response.created(uri).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }
    }

    @POST
    @Path("/{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response addUser(@PathParam("id") int id, User user) {
        if (user.getId() != null) {
            //should not be passing user id
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: user id not allowed here").build();
        }
        if (user == null || user.getName() == null || user.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: user name is required").build();
        }

        try {
            user.setId(id);
            user.setName(user.getName().trim());
            DaoResult<User> result = UserDao.addUser(user, true);

            if (result.getCreated() > 0) {
                return Response.created(uriInfo.getAbsolutePath()).build();
            } else if (result.getUpdated() > 0) {
                return Response.ok().build();
            }
            return Response.status(422).entity("Error: unable to process user").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.toString()).build();
        }
    }

    @PUT
    @Path("/{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response putUser(@PathParam("id") int id, User user) {
        if (user.getId() != null) {
            //should not be passing user id
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: user id not allowed here").build();
        }
        //has to be an easier way to do this.
        if (user == null || user.getName() == null || user.getName().trim().isEmpty() || user.isAttending() == null) {
            //per http specs, for PUT, you are replacing the resource at the given uri (id)
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: all user data is required").build();
        }
        return addUser(id, user);
    }

    @DELETE
    @Path("/{id : \\d+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteUser(@PathParam("id") int id) {
        try {
            DaoResult<User> result = UserDao.deleteUser(id);
            if (result.getDeleted() > 0) {
                return Response.accepted().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response deleteUser(@QueryParam("confirm") Boolean confirm) {
        if (confirm == null || !confirm) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            UserDao.deleteUsers();
            return Response.accepted().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }
    }
}
