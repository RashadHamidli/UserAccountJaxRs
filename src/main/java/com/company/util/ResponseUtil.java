package com.company.util;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ResponseUtil {
    public static Response buildErrorResponse(String message, Response.Status status) {
        return Response.status(status)
                .entity(String.format("{\"error\":\"%s\"}", message))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
