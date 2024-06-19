package com.company.controller;

import com.company.dto.request.AccountCreateRequestDTO;
import com.company.dto.request.AccountUpdateRequestDTO;
import com.company.dto.response.AccountResponseDTO;
import com.company.exeption.AccountNotFoundException;
import com.company.service.AccountService;
import com.company.util.ResponseUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {
    private final AccountService accountService = new AccountService();

    @GET
    public Response getAllAccount() {
        try {
            List<AccountResponseDTO> accounts = accountService.getAllAccount();
            return Response.ok(accounts).build();
        } catch (AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse("Account not found", Response.Status.NOT_FOUND);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching accounts", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/{accountNumber}")
    public Response getAccountByAccountNumber(@PathParam("accountNumber") String accountNumber) {
        try {
            AccountResponseDTO accountById = accountService.getAccountByAccountNumber(accountNumber);
            return Response.ok(accountById).build();
        } catch (AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse("Account not found", Response.Status.NOT_FOUND);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching the account", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    public Response createAccount(AccountCreateRequestDTO accountCreateRequestDTO) {
        try {
            AccountResponseDTO accountResponseDTO = accountService.createAccount(accountCreateRequestDTO);
            return Response.status(Response.Status.CREATED).entity(accountResponseDTO).build();
        } catch (AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse("Account not found", Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while creating the account", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/update")
    public Response updateAccountByAccountNumber(AccountUpdateRequestDTO accountUpdateRequestDTO) {
        try {
            AccountResponseDTO accountResponseDTO = accountService.updateAccountByAccountNumber(accountUpdateRequestDTO);
            return Response.ok(accountResponseDTO).build();
        } catch (AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse("Account not found", Response.Status.NOT_FOUND);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while updating the account", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/{accountNumber}/delete")
    public Response deleteAccount(@PathParam("accountNumber") String accountNumber) {
        try {
            AccountResponseDTO accountResponseDTO = accountService.deleteAccountByAccountNumber(accountNumber);
            return Response.ok(accountResponseDTO).build();
        } catch (AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse("Account not found", Response.Status.NOT_FOUND);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while deleting the account", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}