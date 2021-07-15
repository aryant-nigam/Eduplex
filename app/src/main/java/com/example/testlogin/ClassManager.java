package com.example.testlogin;

import java.util.ArrayList;

public class ClassManager {
    ArrayList<StudentsManager>studentsManager;
    ArrayList<TimetableManager>timtableManager;
    TeachersManager _classTeacher;

    public ClassManager(ArrayList<StudentsManager> studentsManager, ArrayList<TimetableManager> timtableManager, TeachersManager _classTeacher) {
        this.studentsManager = studentsManager;
        this.timtableManager = timtableManager;
        this._classTeacher = _classTeacher;
    }
    public ArrayList<StudentsManager> getStudentsManager() {
        return studentsManager;
    }
    public void setStudentsManager(ArrayList<StudentsManager> studentsManager) {
        this.studentsManager = studentsManager;
    }
    public ArrayList<TimetableManager> getTimtableManager() {
        return timtableManager;
    }
    public void setTimtableManager(ArrayList<TimetableManager> timtableManager) {
        this.timtableManager = timtableManager;
    }
    public TeachersManager get_classTeacher() {
        return _classTeacher;
    }
    public void set_classTeacher(TeachersManager _classTeacher) {
        this._classTeacher = _classTeacher;
    }

     public void addStudent(StudentsManager newStudent)
     {
         studentsManager.add(newStudent);
     }

     public void removeStudent(String regNo){
        for(StudentsManager removeStudent:studentsManager)
            if(removeStudent.get_registrationNumber().equals(regNo))
                studentsManager.remove(studentsManager.indexOf(removeStudent));
     }

}
