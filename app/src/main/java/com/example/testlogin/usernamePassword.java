package com.example.testlogin;

public class usernamePassword {
    String username_regno,password;

    public usernamePassword(String username_regno, String password) {
        this.username_regno = username_regno;
        this.password = password;
    }

    public String getUsername_regno() {
        return username_regno;
    }

    public void setUsername_regno(String username_regno) {
        this.username_regno = username_regno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
