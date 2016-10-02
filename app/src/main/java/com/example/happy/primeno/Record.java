package com.example.happy.primeno;

import java.util.Date;

/**
 * Created by Happy on 10/1/2016.
 */
public class Record {
    private String create_date;
    private int total_no_of_questions;
    private int total_no_of_correct_questions;
    private int total_no_of_incorrect_questions;

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public void setTotal_no_of_questions(int total_no_of_questions) {
        this.total_no_of_questions = total_no_of_questions;
    }

    public void setTotal_no_of_correct_questions(int total_no_of_correct_questions) {
        this.total_no_of_correct_questions = total_no_of_correct_questions;
    }

    public void setTotal_no_of_incorrect_questions(int total_no_of_incorrect_questions) {
        this.total_no_of_incorrect_questions = total_no_of_incorrect_questions;
    }

    public String getCreate_date() {
        return create_date;
    }

    public int getTotal_no_of_questions() {
        return total_no_of_questions;
    }

    public int getTotal_no_of_correct_questions() {
        return total_no_of_correct_questions;
    }

    public int getTotal_no_of_incorrect_questions() {
        return total_no_of_incorrect_questions;
    }
}
