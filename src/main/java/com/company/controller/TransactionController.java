package com.company.controller;

import com.company.dto.request.TransactionRequestDTO;
import com.company.dto.request.TransferRequestDTO;
import com.company.dto.response.TransactionResponseDTO;
import com.company.dto.response.TransferResponseDTO;
import com.company.exeption.AccountNotFoundException;
import com.company.exeption.TransactionException;
import com.company.exeption.UserNotFoundException;
import com.company.service.TransactionService;
import com.company.util.ResponseUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionController {
    private final TransactionService transactionService = new TransactionService();

    @GET
    public Response getAllTransaction() {
        try {
            List<TransactionResponseDTO> transactions = transactionService.getAllTransaction();
            return Response.ok(transactions).build();
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching transactions", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/{transactionNumber}")
    public Response getTransactionByTransactionNumber(@PathParam("transactionNumber") String transactionNumber) {
        try {
            TransactionResponseDTO transactionByTransactionNumber = transactionService.getTransactionByTransactionNumber(transactionNumber);
            return Response.ok(transactionByTransactionNumber).build();
        } catch (TransactionException e) {
            return ResponseUtil.buildErrorResponse(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching the transaction", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/username/{username}")
    public Response getALLTransactionByUsername(@PathParam("username") String username) {
        try {
            List<TransactionResponseDTO> allTransactionByUsername = transactionService.getALLTransactionByUsername(username);
            return Response.ok(allTransactionByUsername).build();
        } catch (UserNotFoundException | AccountNotFoundException | TransactionException e) {
            return ResponseUtil.buildErrorResponse(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching transactions by username", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/accountNumber/{accountNumber}")
    public Response getAllTransactionByAccountNumber(@PathParam("accountNumber") String accountNumber) {
        try {
            List<TransactionResponseDTO> allTransactionByAccountNumber = transactionService.getAllTransactionByAccountNumber(accountNumber);
            return Response.ok(allTransactionByAccountNumber).build();
        } catch (AccountNotFoundException |TransactionException e) {
            return ResponseUtil.buildErrorResponse(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while fetching transactions by account number", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/transfer")
    public Response createTransferTransaction(TransferRequestDTO transferRequestDTO) {
        try {
            TransferResponseDTO transaction = transactionService.createTransferTransaction(transferRequestDTO);
            return Response.status(Response.Status.CREATED).entity(transaction).build();
        } catch (TransactionException | AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while creating the transfer transaction", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/deposit")
    public Response createDepositTransaction(TransactionRequestDTO transactionRequestDTO) {
        try {
            TransactionResponseDTO transaction = transactionService.createDepositTransaction(transactionRequestDTO);
            return Response.status(Response.Status.CREATED).entity(transaction).build();
        } catch (TransactionException | AccountNotFoundException e) {
            return ResponseUtil.buildErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseUtil.buildErrorResponse("An error occurred while creating the deposit transaction", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}
