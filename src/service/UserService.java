package service;

import entity.User;
import repo.UserRepo;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean registerUser(String name, String email) {
        try {
            userRepo.addUser(new User(name, email));
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public User getUserById(UUID id) throws Exception {
        return userRepo.getUser(id);
    }
}
