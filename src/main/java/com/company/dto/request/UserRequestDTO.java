package com.company.dto.request;

import com.company.entity.User;

public record UserRequestDTO(String username,
                             String name,
                             String surname,
                             String email) {

    public static User toUser(UserRequestDTO userRequestDTO) {
        return User.builder().username(userRequestDTO.username()).
                name(userRequestDTO.name()).
                surname(userRequestDTO.surname()).
                email(userRequestDTO.email()).build();
    }
}
