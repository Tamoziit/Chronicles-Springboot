package com.tamojit.chronicles.security.user;

import com.tamojit.chronicles.model.User;
import com.tamojit.chronicles.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
            .orElseThrow(() -> new UsernameNotFoundException("User not Found!"));

        return ShopUserDetails.buildUserDetails(user);
    }
}
