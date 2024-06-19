package com.company.controller;

import com.company.dto.request.UserRequestDTO;
import com.company.dto.response.UserResponseDTO;
import com.company.exeption.UserNotFoundException;
import com.company.service.UserService;
import com.company.util.ResponseUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService =new UserService();

    @GET
    public Response getAllUsers() {
        try {
            List<UserResponseDTO> allUsers = userService.getAllUsers();
            return Response.ok(allUsers).build();
        }catch (UserNotFoundException e){
            return ResponseUtil.buildErrorResponse("user not found", Response.Status.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching users", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/{id}")
    public Response getUsersById(@PathParam("id") Long id) {
        try {
            UserResponseDTO usersById = userService.getUsersById(id);
            return Response.ok(usersById).build();
        }catch (UserNotFoundException e){
            return ResponseUtil.buildErrorResponse("user not found", Response.Status.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching the user", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/username/{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            UserResponseDTO userByName = userService.getUserByUsername(username);
            return Response.ok(userByName).build();
        }catch (UserNotFoundException e){
            return ResponseUtil.buildErrorResponse("user not found", Response.Status.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching the user", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    public Response addUser(UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO userResponseDTO = userService.addUser(userRequestDTO);
            return Response.status(Response.Status.CREATED).entity(userResponseDTO).build();
        }catch (UserNotFoundException e){
            return ResponseUtil.buildErrorResponse("user not found", Response.Status.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while adding the user", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/{username}")
    public Response updateUser(@PathParam("username") String username, UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO userResponseDTO = userService.updateUser(username, userRequestDTO);
            return Response.ok(userResponseDTO).build();
        }catch (UserNotFoundException e){
            return ResponseUtil.buildErrorResponse("user not found", Response.Status.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while updating the user", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/{username}/delete")
    public Response deleteUser(@PathParam("username") String username) {
        try {
            UserResponseDTO userResponseDTO = userService.deleteUser(username);
            return Response.ok(String.format("USER DELETED: %s", userResponseDTO)).build();
        }catch (UserNotFoundException e){
            return ResponseUtil.buildErrorResponse("user not found", Response.Status.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while deleting the user", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}
