package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class UserService {
    private static UserService instance;
    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(dataBase.values());
    }

    public List<User> getAllAuth() {
        return new ArrayList<>(authMap.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public void logoutAllUsers() {
        authMap = Collections.synchronizedMap(new HashMap<>());
    }

    public void deleteAllUser() {
        dataBase = Collections.synchronizedMap(new HashMap<>());
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

    public boolean isExistsThisUser(User user) {
        return getAllUsers().stream().anyMatch(cur -> cur.getEmail().equals(user.getEmail())); // checking by email
    }

    public boolean authUser(User user) {
        if (getUserId(user) == null) {
            return false;
        } else {
            User dbUser = getUserById(getUserId(user)); // user from dataBase
            authMap.put(dbUser.getId(), dbUser);
            return true;
        }
    }

    public boolean addUser(User user) {
        if (isExistsThisUser(user)) {
            return false;
        } else {
            user.setId(maxId.getAndIncrement());
            dataBase.put(user.getId(), user);
            return true;
        }
    }

    // checking pairs [email: password]
    public Long getUserId(User user) {
        return dataBase.keySet().stream()
                .filter(id -> dataBase.get(id).getEmail().equals(user.getEmail()))
                .filter(id -> dataBase.get(id).getPassword().equals(user.getPassword()))
                .findFirst().orElse(null);
    }

}
