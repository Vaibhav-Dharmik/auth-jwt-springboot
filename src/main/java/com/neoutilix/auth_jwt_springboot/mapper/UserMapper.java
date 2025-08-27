package com.neoutilix.auth_jwt_springboot.mapper;

import com.neoutilix.auth_jwt_springboot.dto.UserDto;
import com.neoutilix.auth_jwt_springboot.entity.Role;
import com.neoutilix.auth_jwt_springboot.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        dto.setTitle(user.getTitle());
        dto.setBio(user.getBio());
        dto.setLocation(user.getLocation());
        dto.setExperience(user.getExperience());
        dto.setSkills(user.getSkills());
        dto.setProfileCompletion(user.getProfileCompletion());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }
}
