package org.project.services;

import org.project.models.User;
import org.project.repositories.UserRepository;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User registerUser(String username, String email, String password) throws Exception {

        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User newUser = new User(0, username, email, password);
        return userRepo.save(newUser);
    }

    public Optional<User> login(String email, String password) throws Exception {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
