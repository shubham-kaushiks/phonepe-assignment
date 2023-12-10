package service;

import entity.User;
import repo.UserRepo;

import java.util.UUID;

public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User registerUser(String name, String email) throws Exception {
        User newUser = new User(name, email);
        userRepo.addUser(newUser);
        return newUser;
    }

}
