package com.example.Eduplex;

public class TeachersManager {
    String _id;
    String _name;
    String _class;
    String _mail;

    public TeachersManager(String _id, String _name, String _class, String _mail) {
        this._id = _id;
        this._name = _name;
        this._class = _class;
        this._mail = _mail;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_mail() {
        return _mail;
    }

    public void set_mail(String _mail) {
        this._mail = _mail;
    }
}
