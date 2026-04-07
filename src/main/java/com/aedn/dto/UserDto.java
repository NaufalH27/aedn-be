package com.aedn.dto;

import com.aedn.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String username;
    private String email;
    private String fullName;
    private Boolean isActive;
    private Boolean isVerified;
    private Boolean isAdmin;

    public static UserDto fromEntity(User entity) {
        if (entity == null) return null;

        UserDto dto = new UserDto();
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        dto.setIsActive(entity.getIsActive());
        dto.setIsVerified(entity.getIsVerified());
        dto.setIsAdmin(entity.getIsAdmin());

        return dto;
    }

}
