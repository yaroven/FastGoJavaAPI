package com.example.VenediktovLab9.services;

import com.example.VenediktovLab9.models.User;
import com.example.VenediktovLab9.repositories.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Optional<User> getById(int id) {
        return userRepo.findById(id);
    }

    public User create(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return userRepo.save(u);
    }

    public User update(User u) {
        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        userRepo.update(u);
        return u;
    }

    public void delete(int id) {
        userRepo.deleteById(id);
    }

    public void login(String email, String password) {
        Optional<User> userOpt = userRepo.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }
}
