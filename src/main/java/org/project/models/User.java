package org.project.models;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty(); // hashed in future

    // Constructors
    public User() {}

    public User(int userId, String username, String email, String password) {
        this.userId.set(userId);
        this.username.set(username);
        this.email.set(email);
        this.password.set(password);
    }

    // Property getters
    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty emailProperty() { return email; }
    public StringProperty passwordProperty() { return password; }

    // Standard getters
    public int getUserId() { return userId.get(); }
    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public String getPassword() { return password.get(); }

    // Setters
    public void setUserId(int id) { userId.set(id); }
    public void setUsername(String name) { username.set(name); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPassword(String password) { this.password.set(password); }
}