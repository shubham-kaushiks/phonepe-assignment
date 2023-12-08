package repo;

import entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserRepo {
    private Map<UUID, User> users = new HashMap<>();

    public void addUser(User user) throws Exception {
        if (users.containsKey(user.getId())) throw new Exception("User already exists");
        users.put(user.getId(), user);
    }

    public User getUser(UUID id) throws Exception {
        if (!users.containsKey(id)) throw new Exception("User does not exist");
        return users.get(id);
    }
}
