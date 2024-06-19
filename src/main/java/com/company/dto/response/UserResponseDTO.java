package com.company.dto.response;

import com.company.entity.User;

public record UserResponseDTO(String username,
                              String name,
                              String surname,
                              String email) {
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail());
    }

}
