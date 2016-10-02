package com.example.happy.primeno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private String question_type;
    private int number;
    private boolean answer;
    public static String cheat_status = "com.example.happy.primeno.CheatActivity.cheat_status";
    private static String tag = "CheatActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag,"Enter onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Intent intent = getIntent();
        question_type = intent.getStringExtra(MainActivity.questionType);
        number = intent.getIntExtra(MainActivity.number,0);
        answer = intent.getBooleanExtra(MainActivity.answer,false);
        displayCheatMessage();
        Log.d(tag,"Return onCreate()");
    }

    public void displayCheatMessage(){
        Log.d(tag,"Enter displayCheatMessage()");
        String message = "Cheat\n";
        message = message + number+" is a "+question_type+" number?\n\n";
        message = message + "Answer : "+answer;
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(message);

        ViewGroup cheat_layout = (ViewGroup) findViewById(R.id.cheat_layout);
        cheat_layout.addView(textView);
        Log.d(tag,"Return displayCheatMessage()");
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(tag,"Enter onBackPressed()");
        finish();
        Log.d(tag,"Return onBackPressed()");
    }
    @Override
    public void finish(){
        Log.d(tag,"Enter finish()");
        Intent intent = new Intent();
        intent.putExtra(CheatActivity.cheat_status,true);
        setResult(RESULT_OK,intent);
        super.finish();
        Log.d(tag,"Return finish()");
    }
}
