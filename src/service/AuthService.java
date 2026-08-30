package service;

import dao.UserDAO;
import model.Citizen;
import model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }

    public int register(String username, String password, String fullName,
                        String email, String phone) {
        if (userDAO.usernameExists(username)) {
            System.out.println(">> Username already taken. Choose another.");
            return -1;
        }
        Citizen c = new Citizen(0, username, password, fullName, email, phone);
        return userDAO.registerCitizen(c);
    }

    public boolean updateProfile(User u) {
        return userDAO.updateProfile(u);
    }
}
