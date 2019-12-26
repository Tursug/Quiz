package com.rockstarrooster.quizkeyquest;
import android.app.Application;


public class MyAnswers extends Application{

    public int number = 0;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
