package com.example.testlogin;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager {
    //---------------------------------------------FIREBASE FUNCTIONS FOR STUDENT----------------------------------------------

    static int addStudent(StudentsManager studentsManager) {
        final int[] addStatus = {0};
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
        if ((Boolean) getStudent(0, studentsManager) == true)    //add the student if same registration number does not exists
        {
            return -1;      //record already exists
        }
        NODE.setValue(studentsManager).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                addStatus[0] = 1;
                //record added successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addStatus[0] = 0;   //could not add the record
            }
        });
        return addStatus[0];
    }


    static int updateStudent(String _registrationNumber, String _class, String _section, StudentsManager updatedStudentsManager) {
        final int[] updateStatus = {0};
        HashMap updatedValues = new HashMap();
        Boolean feeStatus = false;
        StudentsManager studentsManager = new StudentsManager(_registrationNumber, _class, _section, null, null, null, null, null);
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(_class).child(_section).child(_registrationNumber);
        if ((Boolean) getStudent(0, studentsManager) == true)            //if record  exists the update
        {
            updatedValues.put("_registrationNumber", updatedStudentsManager.get_registrationNumber());
            updatedValues.put("_class", updatedStudentsManager.get_class());
            updatedValues.put("_section", updatedStudentsManager.get_section());
            updatedValues.put("_name", updatedStudentsManager.get_name());
            updatedValues.put("_email", updatedStudentsManager.get_email());
            updatedValues.put("_password", updatedStudentsManager.get_password());
            updatedValues.put("_dueFee", updatedStudentsManager.get_dueFee());
            if (updatedStudentsManager.get_feeStatus() == true)
                updatedValues.put("_feeStatus", String.valueOf(true));
            else updatedValues.put("_feeStatus", String.valueOf(false));
            NODE.updateChildren(updatedValues).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    updateStatus[0] = 1;                                      // record successfully updated
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    updateStatus[0] = 0;                                      //failed to update record
                }
            });
        } else
            updateStatus[0] = -1;                                             //record to be updated does not exists
        return updateStatus[0];
    }


    static int deletetStudent(StudentsManager studentsManager) {
        final int[] updateStatus = {0};
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
        if ((Boolean) getStudent(0, studentsManager) == true) {
            NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateStatus[0] = 1;                          // student record deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    updateStatus[0] = 0;                          // student record deletion failed
                }
            });
        } else
            updateStatus[0] = -1;                                 // student record does not exists
        return updateStatus[0];
    }


    static Object getStudent(int flag_IE_call, StudentsManager studentsManager) {
        // * flag_IE_call : it  is a flag that tells whether the function has been internally called (0) by the class or by some other class (1)
        final Boolean[] getStatus = {false};
        final StudentsManager[] foundStudent = new StudentsManager[1];
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
        NODE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChildren() && flag_IE_call == 0)
                    getStatus[0] = true;                              // if internal call is made and student with regNo exists
                else if (!(snapshot.hasChildren()) && flag_IE_call == 0)
                    getStatus[0] = false;                             // if internal call is made and student with regNo does not exists
                else if (snapshot.hasChildren() && flag_IE_call == 1)    // if external call is made and student with regNo exists
                    foundStudent[0] = new StudentsManager(
                            snapshot.child("_registrationNumber").getValue().toString(),
                            snapshot.child("_class").getValue().toString(),
                            snapshot.child("_section").getValue().toString(),
                            snapshot.child("_name").getValue().toString(),
                            snapshot.child("_email").getValue().toString(),
                            snapshot.child("_password").getValue().toString(),
                            snapshot.child("_dueFee").getValue().toString(),
                            Boolean.parseBoolean(snapshot.child("_feeStatus").getValue().toString())
                    );
                else                                                 // if external call is made and student with regNo does not exists
                    foundStudent[0] = null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (flag_IE_call == 0)
            return getStatus[0];
        return foundStudent[0];
    }

    //-----------------------------------------------FIREBASE FUNCTIONS FOR ATTENDENCE -----------------------------------------------

    static int punchAttendence(StudentsManager studentsManager, StudentsManager.AttendenceManager attendenceManager, char P_A) {
        final int[] attendenceStatus = {0};
        final StudentsManager.AttendenceManager returnAttendenceManager = (StudentsManager.AttendenceManager) getAttendence(studentsManager);

        if (returnAttendenceManager != null) {
            returnAttendenceManager.punchAttendence(P_A);
            attendenceStatus[0] = 1;
            updateAttendence(studentsManager, returnAttendenceManager);
        } else
            attendenceStatus[0] = -1;

        return attendenceStatus[0];
    }


    static int updateAttendence(StudentsManager studentsManager, StudentsManager.AttendenceManager attendenceManager) {
        final int[] updateStatus = {0};
        HashMap updatedValues = new HashMap();

        updatedValues.put("_daysAttended", attendenceManager.get_daysAttended());
        updatedValues.put("_daysRan", attendenceManager.get_daysRan());

        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section())
                .child("Attendence").child(studentsManager.get_registrationNumber());

        if (getAttendence(studentsManager) != null) {
            NODE.updateChildren(updatedValues).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    updateStatus[0] = 1;                                                              //attendence updated successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    updateStatus[0] = 0;                                                              //attendence failed to be updated
                }
            });
        } else
            updateStatus[0] = -1;                                                                     //attendence to be updated can't be  found
        return updateStatus[0];
    }


    static Object getAttendence(StudentsManager studentsManager) {
        final StudentsManager.AttendenceManager[] returnAttendenceManager = new StudentsManager.AttendenceManager[1];
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section())
                .child("Attendence").child(studentsManager.get_registrationNumber());
        NODE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    returnAttendenceManager[0] = studentsManager.new AttendenceManager(
                            Integer.parseInt(snapshot.child("_daysAttended").getValue().toString()),
                            Integer.parseInt(snapshot.child("_daysRan").getValue().toString())
                    );                                                                                  //attendence record exists
                } else
                    returnAttendenceManager[0] = null;                                                  //attendence record does not exists
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                returnAttendenceManager[0] = null;                                                      //attendence record was unable to be fetched
            }
        });
        return returnAttendenceManager[0];
    }

    //-----------------------------------------------FIREBASE FUNCTIONS FOR STUDENT RESULTS--------------------------------------------

    static int addResult(StudentsManager studentsManager, ArrayList<ResultClass> results, String testName) {
        final int[] addStatus = {0};
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("ResultsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber()).child(testName);
        if ((Boolean) getResult(0, studentsManager, testName) == true)
            return -1;                                             //test entries are already present
        else
            for (ResultClass resultClass : results)
                NODE.child(resultClass.getSubjectVal()).setValue(resultClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addStatus[0] = 1;                                    //test entries successfully added
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addStatus[0] = 0;                                    //test entries failed to add
                    }
                });
        return addStatus[0];
    }


    static int updateResult(StudentsManager studentsManager, ResultClass result, String testName) {
        final int[] updateStatus = {0};
        HashMap updatedValues = new HashMap();
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("ResultsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber()).child(testName);
        if ((boolean) getResult(0, studentsManager, testName) == true) {
            updatedValues.put("subjectValue", result.getSubjectVal());
            updatedValues.put("scoreValue", result.getScoreVal());
            updatedValues.put("maximumMarks", result.getMaximumMarks());
            NODE.child(result.getSubjectVal()).updateChildren(updatedValues).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    updateStatus[0] = 1;                                            //result of the subject updated
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    updateStatus[0] = 0;                                            //result of the subject failed for updation
                }
            });
        } else
            return -1;                                                              //result of the subject to be updated does not exists
        return updateStatus[0];
    }


    static Object getResult(int flag_IE_cal, StudentsManager studentsManager, String testName) {
        Boolean[] getStatus = {false};
        final ResultManager[] resultManager = {new ResultManager()};
        ArrayList<ResultClass> result = new ArrayList<>();
        final ResultClass resultClass = new ResultClass();
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("ResultsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
        if (flag_IE_cal == 0) {
            NODE.child(testName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        getStatus[0] = true;       //found the entries for that specific test
                    } else {
                        getStatus[0] = false;          //did'nt found the entries for that specific test
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    getStatus[0] = false;     //default value is still set doesnt knows whether the entry was found or not
                }
            });
        } else
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                NODE.child(ResultManager.getTestNames(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (int j = 0; j < snapshot.getChildrenCount(); j++) {
                            resultClass.setSubjectVal(snapshot.child("subjectVal").getValue().toString());
                            resultClass.setScoreVal(Double.parseDouble(snapshot.child("scoreVal").getValue().toString()));
                            resultClass.setScoreVal(Double.parseDouble(snapshot.child("maximumMarks").getValue().toString()));
                            result.add(resultClass);
                        }
                        resultManager[0].set(finalI, result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        resultManager[0] = null;
                    }
                });
            }
        if (flag_IE_cal == 0)
            return getStatus[0];
        return resultManager[0];
    }


    static int deleteResult(StudentsManager studentsManager, String testName, String subject) {
        final int[] deletestatus = {0};
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("ResultsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber()).child(testName);
        if ((boolean) getResult(0, studentsManager, testName) == true) {
            NODE.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(subject).hasChildren())
                        NODE.child(subject).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                deletestatus[0] = 1;                        //successfully deleted the result of specific subject
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                deletestatus[0] = 0;                        //failed in deletion the result of specific subject
                            }
                        });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    deletestatus[0] = 0;                                    //failed in fetching the test of which specific subject reult was to be deleted
                }
            });
        } else
            return -1;                                                      //couldnt fint the subject record in the result
        return deletestatus[0];
    }

    //------------------------------------------------FIREBASE FUNCTIONS FOR TIMETABLE------------------------------------------

    static int addTimetable(String _class, String _section, String _day, TimetableClass timetableClass) {
        final int [] addStatus = {0};
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord").child(_class).child(_section).child(_day);
        if(getTimetable(_class,_section,_day)==null) {
            NODE.setValue(timetableClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    addStatus[0] = 1;                               //successfully adding a subject to day's timetable
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addStatus[0] =0;                                //failed in adding a subject to day's timetable
                }
            });
        }
        else
            addStatus[0]=-1;                                        //timetable for that day already exists
        return addStatus[0];
    }


    static int updateTimetable(String _class, String _section, Boolean _day, String _dayName, Boolean _subject, String _subjectName, ArrayList<TimetableClass> daySchedule, TimetableClass subjectSchedule) {
        final int[] updateStatus = {0};
        HashMap updatedValues = new HashMap();
        if (_day == true) {                                                         //if user wishes to change entire day's timetable
            DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord").child(_class).child(_section).child(_dayName);
            if (getTimetable(_class, _section, _dayName) != null)                   //if timetable for that day exists
            {
                deleteTimetable(_class,_section,_dayName,null,true,null);   //delete timetable for that specific day
                for (int i=0;i<daySchedule.size();i++) {
                    NODE.setValue(daySchedule.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            updateStatus[0]=1;                                                      //add to day's timetable suject by subject
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updateStatus[0]=0;                                                      //if failed while adding
                            //bring back all the changes made
                            //break out of loop i=daySchedule.size();
                        }
                    });
                }
                updateStatus[0]=1;                                                                  //successful in adding
            }
            else                                                                                    // id day to be updated has not been found
                updateStatus[0] = -1;
        } else if (_subject == true) {
            DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord").child(_class).child(_section).child(_dayName);
            if (getTimetable(_class, _section, _dayName) != null) {
                NODE.child(_subjectName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            updatedValues.put("subjectName", subjectSchedule.getSubjectName());
                            updatedValues.put("subjectTiming", subjectSchedule.getSubjectTiming());
                            updatedValues.put("subjectTeacher", subjectSchedule.getSubjectTeacher());
                            NODE.child(_subjectName).updateChildren(updatedValues).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    updateStatus[0] = 1;                                            //successful updation of subject schedule
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    updateStatus[0] = 0;                                             //unsuccessful updation of subject schedule
                                }
                            });
                        } else
                            updateStatus[0] = -1;                                                    //cant find subject to  be updated of
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        updateStatus[0] = 0;                                                        //unsuccessful updation of subject schedule
                    }
                });
            }
        }
        return updateStatus[0];
    }


    static TimetableManager getTimetable(String _class, String _section, String _day) {
        final int[] getStatus = {0};
        final TimetableClass timetableClass[] = new TimetableClass[1];
        final TimetableManager timetableManager[] = new TimetableManager[1];
        ArrayList<TimetableClass> daySchedule = new ArrayList<>();
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord").child(_class).child(_section).child(_day);
        NODE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                        timetableClass[0] = new TimetableClass(
                                snapshot.child("subjectName").getValue().toString(),
                                snapshot.child("subjectTeacher").getValue().toString(),
                                snapshot.child("subjectTiming").getValue().toString()
                        );
                        daySchedule.add(timetableClass[0]);
                        timetableManager[1].set(_day, daySchedule);
                    }

                } else
                    timetableManager[1].set(_day, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                timetableManager[1].set(_day, null);
            }
        });
        return timetableManager[1];
    }


    static int deleteTimetable(String _class, String _section, String _dayName, String _subjectName, Boolean _day, Boolean _subject) {
        int[] deleteStatus = {0};
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord").child(_class).child(_section).child(_dayName);
        if (_day == true) {
            if (getTimetable(_class, _section, _dayName) != null) {
                NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        deleteStatus[0] = 1;                            //successful deletion of the day's timetable
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        deleteStatus[0] = 0;                            //failed in deleting the day's  timetable
                    }
                });
            } else
                deleteStatus[0] = -1;                                   //if the day's  timetable does not exists
        } else if (_subject == true) {
            if (getTimetable(_class, _section, _dayName) != null) //if the day exists
            {
                NODE.child(_subjectName).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) //if that subject exists
                        {
                            NODE.child(_subjectName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    deleteStatus[0] = 1;                                //successful deletion of the subjects's timetable for the specified day
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    deleteStatus[0] = 0;                                //failed in deleting the subjects's  timetable for the specified day
                                }
                            });
                        } else
                            deleteStatus[0] = -1;                                       //subject's timetable for that specific day was not found
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        deleteStatus[0] = 0;                                             //failed in deleting the subjects's  timetable for the specified day
                    }
                });

            }

        }
        return deleteStatus[0];
    }
}

