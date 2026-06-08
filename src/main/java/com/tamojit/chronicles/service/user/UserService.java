package com.tamojit.chronicles.service.user;

import com.tamojit.chronicles.dto.UserDto;
import com.tamojit.chronicles.exceptions.AlreadyExistsException;
import com.tamojit.chronicles.exceptions.ResourceNotFoundException;
import com.tamojit.chronicles.model.User;
import com.tamojit.chronicles.repository.UserRepository;
import com.tamojit.chronicles.request.CreateUserRequest;
import com.tamojit.chronicles.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
            .filter(user -> !userRepository.existsByEmail(request.getEmail())) // checking if there exists any user with the same email as that in req.body
            .map(req -> {
                // creating new user, if new email
                User user = new User();
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));

                return userRepository.save(user);
            })
            .orElseThrow(() -> new AlreadyExistsException("An user with this email already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
            .map(existingUser -> {
                existingUser.setFirstName(request.getFirstName());
                existingUser.setLastName(request.getLastName());

                return userRepository.save(existingUser);
            })
            .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
            .ifPresentOrElse(userRepository::delete, () -> {
                throw new ResourceNotFoundException("User not found!");
            });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // getting auth user context
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
