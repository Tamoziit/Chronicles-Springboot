package com.tamojit.chronicles.service.user;

import com.tamojit.chronicles.dto.UserDto;
import com.tamojit.chronicles.model.User;
import com.tamojit.chronicles.request.CreateUserRequest;
import com.tamojit.chronicles.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
