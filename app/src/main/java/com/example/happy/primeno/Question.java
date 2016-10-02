package com.example.happy.primeno;

import android.util.Log;

/**
 * Created by Happy on 8/15/2016.
 */
public class Question {
    private String question_type;
    private int number;
    private boolean answer;
    private static String tag = "Question";

    public Question(String question_type, int number, boolean answer) {
        this.question_type = question_type;
        this.number = number;
        this.answer = answer;
    }

    public Question(){
        Log.d(tag,"Enter Question() constructor");
        this.setNumber();
        this.setQuestion_Type();
        this.setAnswer();
        Log.d(tag,"Question object created");
    }

    public String getQuestion_type() {
        return question_type;
    }

    public int getNumber() {
        return number;
    }

    public boolean getAnswer() {
        return answer;
    }

    private void setQuestion_Type(){
        int ran = (int)(Math.random()*100)%4;
        if(ran == 0){
            this.question_type = "even";
        }else if(ran == 1){
            this.question_type = "odd";
        }else if(ran == 2){
            this.question_type = "prime";
        }else{
            this.question_type = "not prime";
        }
    }

    private void setNumber(){
        this.number = (int)(Math.random()*1001)%1001;
    }

    private void setAnswer(){
        String type = this.question_type;
        int temp_number = this.number;
        if(type.equals("odd") || type.equals("even")){
            if((temp_number & 1) == 1){
                if(type.equals("odd")){
                    this.answer = true;
                }else{
                    this.answer = false;
                }
            }else{
                if(type.equals("even")){
                    this.answer = true;
                }else{
                    this.answer = false;
                }
            }
        }else{
            boolean prime = true;
            if(temp_number == 1){
                prime = false;
            }
            for(int i = 2;i <= Math.sqrt(temp_number);i++){
                if(temp_number%i == 0){
                    prime = false;
                    break;
                }
            }
            if(prime && type.equals("prime")){
                this.answer = true;
            }else if(!prime && type.equals("not prime")){
                this.answer = true;
            }else{
                this.answer = false;
            }
        }
    }
}
