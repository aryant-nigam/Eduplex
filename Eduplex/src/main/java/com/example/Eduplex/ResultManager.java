package com.example.Eduplex;

import java.util.ArrayList;

public class ResultManager {
    ArrayList<ResultClass>_test1;
    ArrayList<ResultClass>_test2;
    ArrayList<ResultClass> _test3;
    ArrayList<ResultClass>_exam1;
    ArrayList<ResultClass>_exam2;

    public  ResultManager(){}
    public ResultManager(ArrayList<ResultClass> _test1, ArrayList<ResultClass> _test2, ArrayList<ResultClass> _test3, ArrayList<ResultClass> _exam1, ArrayList<ResultClass> _exam2) {
        this._test1 = _test1;
        this._test2 = _test2;
        this._test3 = _test3;
        this._exam1 = _exam1;
        this._exam2 = _exam2;
    }

    public ArrayList<ResultClass> get_test1() {
        return _test1;
    }

    public void set_test1(ArrayList<ResultClass> _test1) {
        this._test1 = _test1;
    }

    public ArrayList<ResultClass> get_test2() {
        return _test2;
    }

    public void set_test2(ArrayList<ResultClass> _test2) {
        this._test2 = _test2;
    }

    public ArrayList<ResultClass> get_test3() {
        return _test3;
    }

    public void set_test3(ArrayList<ResultClass> test3) {
        this._test3 = test3;
    }

    public ArrayList<ResultClass> get_exam1() {
        return _exam1;
    }

    public void set_exam1(ArrayList<ResultClass> _exam1) {
        this._exam1 = _exam1;
    }

    public ArrayList<ResultClass> get_exam2() {
        return _exam2;
    }

    public void set_exam2(ArrayList<ResultClass> _exam2) {
        this._exam2 = _exam2;
    }

    public void set(int i,ArrayList<ResultClass>results)
    {
        switch (i)
        {
            case 0:
                set_test1(results);
                break;
            case 1:
                set_test2(results);
                break;
            case 2:
                set_test3(results);
                break;
            case 3:
                set_exam1(results);
                break;
            case 4:
                set_exam2(results);
                break;
        }
    }
    public static String getTestNames(int i){
        switch (i)
        {
            case 0:
                return "Test 1";
            case 1:
                return "_test2";
            case 2:
                return "_test3";
            case 3:
                return "_exam1";
            case 4:
                return "_exam2";
            default:
                return "";

        }
    }
}
