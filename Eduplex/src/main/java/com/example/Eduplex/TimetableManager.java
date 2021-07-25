package com.example.Eduplex;

import java.util.ArrayList;

public class TimetableManager {
    ArrayList<TimetableClass>_monday;
    ArrayList<TimetableClass>_tuesday;
    ArrayList<TimetableClass>_wednesday;
    ArrayList<TimetableClass>_thursday;
    ArrayList<TimetableClass>_friday;
    ArrayList<TimetableClass>_saturday;

    public TimetableManager(ArrayList<TimetableClass> _monday, ArrayList<TimetableClass> _tuesday, ArrayList<TimetableClass> _wednesday, ArrayList<TimetableClass> _thursday, ArrayList<TimetableClass> _friday, ArrayList<TimetableClass> _saturday) {
        this._monday = _monday;
        this._tuesday = _tuesday;
        this._wednesday = _wednesday;
        this._thursday = _thursday;
        this._friday = _friday;
        this._saturday = _saturday;
    }

    public ArrayList<TimetableClass> get_monday() {
        return _monday;
    }

    public void set_monday(ArrayList<TimetableClass> _monday) {
        this._monday = _monday;
    }

    public ArrayList<TimetableClass> get_tuesday() {
        return _tuesday;
    }

    public void set_tuesday(ArrayList<TimetableClass> _tuesday) {
        this._tuesday = _tuesday;
    }

    public ArrayList<TimetableClass> get_wednesday() {
        return _wednesday;
    }

    public void set_wednesday(ArrayList<TimetableClass> _wednesday) {
        this._wednesday = _wednesday;
    }

    public ArrayList<TimetableClass> get_thursday() {
        return _thursday;
    }

    public void set_thursday(ArrayList<TimetableClass> _thursday) {
        this._thursday = _thursday;
    }

    public ArrayList<TimetableClass> get_friday() {
        return _friday;
    }

    public void set_friday(ArrayList<TimetableClass> _friday) {
        this._friday = _friday;
    }

    public ArrayList<TimetableClass> get_saturday() {
        return _saturday;
    }

    public void set_saturday(ArrayList<TimetableClass> _saturday) {
        this._saturday = _saturday;
    }

    public void set(String _day,ArrayList<TimetableClass>daySchedule)
    {
        switch (_day)
        {
            case "monday":
                set_monday(daySchedule);
            case "tuesday":
                set_tuesday(daySchedule);
            case "wednesday":
                set_wednesday(daySchedule);
            case "thursday":
                set_thursday(daySchedule);
            case "friday":
                set_friday(daySchedule);
            case "saturday":
                set_saturday(daySchedule);
        }
    }
}
