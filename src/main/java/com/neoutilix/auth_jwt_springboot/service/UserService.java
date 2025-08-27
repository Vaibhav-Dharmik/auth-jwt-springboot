package com.neoutilix.auth_jwt_springboot.service;

import com.neoutilix.auth_jwt_springboot.dto.RegisterRequest;
import com.neoutilix.auth_jwt_springboot.dto.UserDto;
import com.neoutilix.auth_jwt_springboot.entity.User;

public interface UserService {

    User registerUser(RegisterRequest request, boolean admin);

    User findByEmail(String email);

    UserDto getCurrentUserProfile();
}
