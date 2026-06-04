package com.tamojit.chronicles.data;

import com.tamojit.chronicles.model.User;
import com.tamojit.chronicles.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) { // run when project is ready --> to run
        createDefaultUserIfNotExists();
    }

    // Seeding data on application startup
    private void createDefaultUserIfNotExists() {
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword("53783$636bs");
            userRepository.save(user);

            System.out.println("Default user " + i + " has been created");
        }
    }
}
