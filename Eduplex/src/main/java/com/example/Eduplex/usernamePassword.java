package com.example.Eduplex;

import java.io.Serializable;

public class usernamePassword implements Serializable {
    String username_regno,password,_class;

    public usernamePassword(String username_regno, String password,String _class) {
        this.username_regno = username_regno;
        this.password = password;
        this._class=_class;
    }
    public usernamePassword(){}
    public String get_class() { return _class; }

    public void set_class(String _class) { this._class = _class; }

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
