package com.tshirt.designApp;

import com.tshirt.designApp.database.UserAuthentication;

public class UserSession {
    private static UserSession instance;
    private int currentUserId = -1;
    private UserAuthentication.User currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        int userId = UserAuthentication.loginUser(username, password);
        if (userId != -1) {
            this.currentUserId = userId;
            this.currentUser = UserAuthentication.getUserById(userId);
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUserId = -1;
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUserId != -1;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public UserAuthentication.User getCurrentUser() {
        return currentUser;
    }
}