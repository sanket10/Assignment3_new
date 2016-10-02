package com.example.happy.primeno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {
    public static String hint_status = "com.example.happy.primeno.HintActivity.HintStatus";
    private String questionType;
    private int number;
    private static String tag = "HintActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        Intent intent = getIntent();
        questionType = intent.getStringExtra(MainActivity.questionType);
        number = intent.getIntExtra(MainActivity.number,0);
        displayMessage();
    }
    public void displayMessage(){
        String message = "HINT\n";;
        message = message + number+" is a "+questionType+" number?\n\n";
        if(questionType.equals("odd") || questionType.equals("even")){
            message = message + "Check the last digit\nIf last digit is 1,3,5,7,9 than odd\nElse even";
        }else{
            int square_of_number = (int)Math.sqrt(number);
            message = message + "Check for prime only from 2 to "+square_of_number;
        }
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.hint_layout);
        layout.addView(textView);
    }
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
        intent.putExtra("tmp","skd fkjasljdf laskdf");
        setResult(RESULT_OK,intent);
        super.finish();
        Log.d(tag,"Return finish()");
    }
}
