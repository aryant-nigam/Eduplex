package com.example.Eduplex;

import java.io.Serializable;

public class StudentsManager implements Serializable {
    String _name;
    String _class;
    String _section;
    String _registrationNumber;
    String _email;
    String _password;
    String _dueFee;
    Boolean _feeStatus;

    /***********************************************   ATTENDENCE CLASS  *************************************/
    public class AttendenceManager implements Serializable
    {
        int _daysAttended;
        int _daysRan;

        public AttendenceManager(int _daysAttended, int _daysRan) {
            this._daysAttended = _daysAttended;
            this._daysRan = _daysRan;
        }

        public int get_daysAttended() {
            return _daysAttended;
        }

        public void set_daysAttended(int _daysAttended) {
            this._daysAttended = _daysAttended;
        }

        public int get_daysRan() {
            return _daysRan;
        }

        public void set_daysRan(int _daysRan) {
            this._daysRan = _daysRan;
        }
        public void punchAttendence(char P_A)
        {
            this._daysRan+=1;
            if(P_A=='P')
                this._daysAttended+=1;
        }
        public boolean changeAttendence(char P_A)
        {
            if(P_A=='P') {
                if (this._daysAttended < this._daysRan) {
                    this._daysAttended += 1;
                    return true;
                } else
                    return false;
            }
            else {
                if(this._daysAttended-1>0) {
                    this._daysAttended -= 1;
                    return true;
                }
                else
                    return false;
            }
        }
    }

    public  StudentsManager(){}
    public StudentsManager( String _registrationNumber,String _class, String _section,String _name, String _email,String _password,String _dueFee,Boolean _feeStatus) {
        this._registrationNumber = _registrationNumber;
        this._class = _class;
        this._section = _section;
        this._name = _name;
        this._email = _email;
        this._password=_password;
        this._dueFee=_dueFee;
        this._feeStatus=_feeStatus;
        //this._results=_results;
    }

    public Boolean get_feeStatus() {
        return _feeStatus;
    }

    public void set_feeStatus(Boolean _feeStatus) {
        this._feeStatus = _feeStatus;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_dueFee() {
        return _dueFee;
    }

    public void set_dueFee(String _dueFee) {
        this._dueFee = _dueFee;
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

    public String get_section() {
        return _section;
    }

    public void set_section(String _section) {
        this._section = _section;
    }

    public String get_registrationNumber() {
        return _registrationNumber;
    }

    public void set_registrationNumber(String _registrationNumber) {
        this._registrationNumber = _registrationNumber;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

}
