package com.example.happy.primeno;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String tag = "MainActivity";
    private static String state_number_of_correct_question = "com.example.happy.primeno.mainActivity.NumberOfCorrectQuestion";
    private static String state_number_of_incorrect_question = "com.example.happy.primeno.mainActivity.NumberOfIncorrectQuestion";
    private static String state_current_question_number = "com.example.happy.primeno.mainActivity.CurrentQuestionNumber";
    private static String state_question_type = "com.example.happy.primeno.mainActivity.QuestionType";
    private static String state_question_number = "com.example.happy.primeno.mainActivity.QuestionNumber";
    private static String state_question_answer = "com.example.happy.primeno.mainActivity.QuestionAnswer";
    private static String state_question_state = "com.example.happy.primeno.mainActivity.QuestionState";
    private static String state_hint_state = "com.example.happy.primeno.mainActivity.HintState";
    private static String state_cheat_state = "com.example.happy.primeno.mainActivity.cheatState";
    private static String state_number_of_hint_left = "com.example.happy.primeno.mainActivity.NumberOfHint";
    public static String questionType = "com.example.happy.primeno.mainActivity.questionType";
    public static String number = "com.example.happy.primeno.mainActivity.number";
    public static String answer = "com.example.happy.primeno.mainActivity.answer";
    public static String MYPREFERENCES = "MainActivity";
    private boolean question_state = true; //question state means whether user answered the question or not
    private int number_of_correct_question = 0;
    private int number_of_incorrect_question = 0;
    private int number_of_hint_left = 5;
    private int current_question_number = 0;
    private boolean cheating_status = false;
    private boolean hint_status = false;
    private Question question = null;
    private Double performance_status;
    private int best_performance;
    private PrimeNoDAO primeNoDAO;
    private Button yes_button;
    private Button no_button;
    private Button next_button;
    private Button restart_button;
    private Button hint_button;
    private Button cheat_button;
    private SharedPreferences sharedPreferences;

    @Override
    public void onResume(){
        super.onResume();
        yes_button = (Button)findViewById(R.id.yes);
        no_button = (Button)findViewById(R.id.no);
        next_button = (Button)findViewById(R.id.next);
        restart_button = (Button)findViewById(R.id.restart);
        hint_button = (Button)findViewById(R.id.hint);
        cheat_button = (Button)findViewById(R.id.cheat);
        yes_button.setBackgroundColor(Color.LTGRAY);
        no_button.setBackgroundColor(Color.LTGRAY);
        next_button.setBackgroundColor(Color.LTGRAY);
        restart_button.setBackgroundColor(Color.TRANSPARENT);
        /*hint_button.setBackgroundColor(Color.LTGRAY);
        hint_button.setWidth(50);
        hint_button.setHeight(45);
        cheat_button.setBackgroundColor(Color.LTGRAY);
        cheat_button.setWidth(90);
        cheat_button.setHeight(45);*/
        yes_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkQuestion(view);
            }
        });
        no_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkQuestion(view);
            }
        });
        next_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                nextQuestion(view);
            }
        });
        hint_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                hintButton();
            }
        });
        cheat_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cheatButton();
            }
        });
        restart_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resetAll(view);
            }
        });
    }

    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag,"Enter onCreate()");
        String question_field = "Press Next Button to Start Quiz";
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //objection creation for sqlite
        this.primeNoDAO = new PrimeNoDAO(getApplicationContext());
        Log.d(tag,primeNoDAO.getAllRecord().size()+" ");
        this.performance_status = primeNoDAO.getStatus();
        this.best_performance = primeNoDAO.getBest_no_of_correct();
        int temp_current_question_number = this.current_question_number;


        String[] permission_arr = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, permission_arr, 1);

        //sharedpreferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(MainActivity.state_question_type,null);
        if(str != null){
            Log.d(tag,"onCreate inside sharedpreference");
            this.question = new Question(sharedPreferences.getString(MainActivity.state_question_type,""),sharedPreferences.getInt(MainActivity.state_question_number,0),sharedPreferences.getBoolean(MainActivity.state_question_answer,false));
            this.current_question_number = sharedPreferences.getInt(MainActivity.state_current_question_number,0);
            question_field = "Question : "+(this.current_question_number)+"\n"+this.question.getNumber()+" is a "+this.question.getQuestion_type()+" number?";
            this.number_of_correct_question = sharedPreferences.getInt(MainActivity.state_number_of_correct_question,0);
            this.number_of_incorrect_question = sharedPreferences.getInt(MainActivity.state_number_of_incorrect_question,0);
            this.cheating_status = sharedPreferences.getBoolean(MainActivity.state_cheat_state,false);
            this.hint_status = sharedPreferences.getBoolean(MainActivity.state_hint_state,false);
            this.number_of_hint_left = sharedPreferences.getInt(MainActivity.state_number_of_hint_left,5);
            temp_current_question_number = this.current_question_number - 1;
            if(this.question_state = sharedPreferences.getBoolean(MainActivity.state_question_state,false)){
                temp_current_question_number++;
            }
        }

        //Bundle object
        if(savedInstanceState != null && savedInstanceState.getInt(MainActivity.state_current_question_number) != 0){
            Log.d(tag,"onCreate inside savedInstanceState");
            this.question = new Question(savedInstanceState.getString(MainActivity.state_question_type),savedInstanceState.getInt(MainActivity.state_question_number),savedInstanceState.getBoolean(MainActivity.state_question_answer));
            this.current_question_number = savedInstanceState.getInt(MainActivity.state_current_question_number);
            question_field = "Question : "+(this.current_question_number)+"\n"+this.question.getNumber()+" is a "+this.question.getQuestion_type()+" number?";
            this.number_of_correct_question = savedInstanceState.getInt(MainActivity.state_number_of_correct_question);
            this.number_of_incorrect_question = savedInstanceState.getInt(MainActivity.state_number_of_incorrect_question);
            this.cheating_status = savedInstanceState.getBoolean(MainActivity.state_cheat_state);
            this.hint_status = savedInstanceState.getBoolean(MainActivity.state_hint_state);
            this.number_of_hint_left = savedInstanceState.getInt(MainActivity.state_number_of_hint_left);
            temp_current_question_number = this.current_question_number - 1;
            if(this.question_state = savedInstanceState.getBoolean(MainActivity.state_question_state)){
                temp_current_question_number++;
            }
        }

        //First time view to end user
        ((TextView)findViewById(R.id.question)).setText(question_field);
        ((TextView)findViewById(R.id.status)).setText("No of Question : "+(temp_current_question_number)+
                " \nNo of Correct : "+this.number_of_correct_question+
                "\nNo of Incorrect : "+this.number_of_incorrect_question+
                "\nHint Left : "+this.number_of_hint_left+
                "\nPerformace Percentage : "+this.performance_status+
                "%\nBest Performance : "+this.best_performance);

        Log.d(tag,"Return onCreate()");
    }


    //Check the questions on user click
    public void checkQuestion(View v){
        //user can't click on yes/no button till he's not press next question button
        Log.d(tag,"Enter checkQuestion()");
        if(this.question == null || this.question_state){
            Toast.makeText(this,"Please click on next Button",Toast.LENGTH_SHORT).show();
            return;
        }

        //checking which botton user clicks
        switch (v.getId()){
            case R.id.yes:
                checkAnswer(v,true);
                break;
            case R.id.no:
                checkAnswer(v,false);
                break;
            default:
                Toast.makeText(this,"Please click only Yes/No",Toast.LENGTH_SHORT).show();
        }
        this.question_state = true;
        Log.d(tag,"Return checkQuestion()");
    }

    //check question after user's pressing button
    public void checkAnswer(View v,boolean answer){
        Log.d(tag,"Enter checkAnswer()");
        if(this.cheating_status){
            Toast.makeText(this,"You Cheated",Toast.LENGTH_SHORT).show();
            this.number_of_incorrect_question++;
        }else if(answer == this.question.getAnswer()){
            if(answer){
                //yes_button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                yes_button.setBackgroundColor(Color.GREEN);
            }else{
                //no_button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                no_button.setBackgroundColor(Color.GREEN);
            }
            Toast.makeText(this," Correct Answer ",Toast.LENGTH_SHORT).show();
            this.number_of_correct_question++;
        }else{
            if(answer){
                yes_button.setBackgroundColor(Color.RED);
                no_button.setBackgroundColor(Color.GREEN);
            }else{
                yes_button.setBackgroundColor(Color.GREEN);
                no_button.setBackgroundColor(Color.RED);
            }
            Toast.makeText(this," Incorrect Answer ",Toast.LENGTH_SHORT).show();
            this.number_of_incorrect_question++;
        }
        ((TextView)findViewById(R.id.status)).setText("No of Question : "+(this.current_question_number)+" \nNo of Correct : "+this.number_of_correct_question+"\nNo of Incorrect : "+this.number_of_incorrect_question+"\nHint Left : "+this.number_of_hint_left+
                "\nPerformace Percentage : "+this.performance_status+" %\n" +
                "Best Performance : "+this.best_performance);
        this.question_state = true; // user can press Yes/No button only once for each question

        Log.d(tag,"Return checkAnswer()");
    }

    //Method execute after pressing next button. This method generate new question
    public void nextQuestion(final View v){
        Log.d(tag,"Enter nextQuestion()");
        if(this.current_question_number == 20){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.next_question_dialog_message)
                    .setTitle(R.string.next_question_dialog_title);
            if(this.number_of_hint_left == 5){
                builder.setMessage(R.string.next_question_hdialog_message).setTitle(R.string.next_question_hdialog_title);
            }
            builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    if(number_of_hint_left == 5){
                        primeNoDAO.improveStatus();
                    }
                    resetAll(v);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            this.cheating_status = false;
            this.hint_status = false;
            yes_button.setBackgroundColor(Color.LTGRAY);
            no_button.setBackgroundColor(Color.LTGRAY);
            if(this.question_state == false){
                Toast.makeText(this,"Please submit the last question answer",Toast.LENGTH_SHORT).show();
                return;
            }
            this.current_question_number++;
            TextView question = (TextView)findViewById(R.id.question);
            this.question = new Question(); //generate a new random question
            String generated_question = "Question : "+(this.current_question_number)+"\n"+this.question.getNumber()+" is a "+this.question.getQuestion_type()+" number?";
            question.setText(generated_question);
            this.question_state = false;
        }
        Log.d(tag,"Return nextQuestion()");
    }

    //This method link with reset button and this method reset complete quiz
    public void resetAll(View v){
        Log.d(tag,"Enter resetAll()");

        //creating object for insertion operation
        if(this.current_question_number != 1){
            Record record = new Record();
            record.setCreate_date((new Date()).toString());
            record.setTotal_no_of_questions(this.number_of_correct_question + this.number_of_incorrect_question);
            record.setTotal_no_of_correct_questions(this.number_of_correct_question);
            record.setTotal_no_of_incorrect_questions(this.number_of_incorrect_question);
            this.primeNoDAO.insertRecord(record);
        }

        this.current_question_number = 0;
        this.number_of_correct_question = 0;
        this.number_of_incorrect_question = 0;
        this.cheating_status = false;
        this.hint_status = false;
        this.number_of_hint_left = 5;
        yes_button.setBackgroundColor(Color.LTGRAY);
        no_button.setBackgroundColor(Color.LTGRAY);
        this.question = null;
        this.question_state = true;
        this.performance_status = primeNoDAO.getStatus();
        this.best_performance = primeNoDAO.getBest_no_of_correct();
        //reset sharedpreferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        ((TextView)findViewById(R.id.question)).setText("Press Next Button to Start Quiz");
        ((TextView)findViewById(R.id.status)).setText("Number of question : 0\n" +
                "Number of Correct : 0\n" +
                "Number of Incorrect : 0"+
                "\nHint Left : "+this.number_of_hint_left+
                "\nPerformace Percentage : "+this.performance_status+
                "%\nBest Performance : "+this.best_performance);
        Log.d(tag,"Return resetAll()");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_generate_external_file:
                ExternalFileData externalFileData = new ExternalFileData();
                String str = "dd";
                if(externalFileData.isExternalStorageReadable()){
                    str = externalFileData.getExternalFileOfCompleteResult(primeNoDAO.getAllRecord());
                }
                Toast.makeText(this,"External file"+str+" successfully generated",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_generate_internal_file:
                if(this.question != null){
                    String generated_question = this.question.getNumber()+" is a "+this.question.getQuestion_type()+" number?";
                    this.writeInternalFavoriteFile(generated_question);
                    //Toast.makeText(this,"Done internal file",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"there is no question",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_see_internal_file:
                this.readInteranlFavoriteFile();
                break;
            case R.id.action_generate_external_file_private:
                String str1 = getExternalFile(primeNoDAO.getAllRecord());
                Toast.makeText(this,"External file"+str1+" successfully generated",Toast.LENGTH_SHORT).show();
                //Toast.makeText(this,"kkkk",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void hintButton(){
        Log.d(tag,"enter hintButton()");
        Log.d(tag,"hint status "+this.hint_status);
        if(this.hint_status == true){
            Toast.makeText(this,"Already use hint for this question",Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.number_of_hint_left == 0){
            Toast.makeText(this,"No hint available ",Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.question_state){
            Toast.makeText(this,"Please click next button",Toast.LENGTH_SHORT).show();
            return;
        }
        //Alert dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.hint_dialog_message)
                .setTitle(R.string.hint_dialog_title);
        builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                number_of_hint_left--;
                Intent intent = new Intent(MainActivity.this,HintActivity.class);
                intent.putExtra(MainActivity.questionType,question.getQuestion_type());
                intent.putExtra(MainActivity.number,question.getNumber());
                startActivityForResult(intent,2);
            }
        });
        builder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Log.d(tag,"return hintButton()");
    }

    public void cheatButton(){

        Log.d(tag,"enter cheatButton()");
        if(this.question_state){
            Toast.makeText(this,"Please click next button",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this," "+primeNoDAO.getAllRecord().size(),Toast.LENGTH_SHORT).show();
        //Alert dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cheat_dialog_message)
                .setTitle(R.string.cheat_dialog_title);
        builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                primeNoDAO.updateStatus();
                Toast.makeText(MainActivity.this,""+PrimeNoDAO.tmp,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,CheatActivity.class);
                intent.putExtra(MainActivity.questionType,question.getQuestion_type());
                intent.putExtra(MainActivity.number,question.getNumber());
                intent.putExtra(MainActivity.answer,question.getAnswer());
                startActivityForResult(intent,1);
            }
        });
        builder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d(tag,"return cheatButton()");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        Log.d(tag,"Enter onActivityResult()");
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                this.cheating_status = intent.getBooleanExtra(CheatActivity.cheat_status,true);
            }
        }else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                this.hint_status = intent.getBooleanExtra(HintActivity.hint_status,true);
                Toast.makeText(this,"You use hint",Toast.LENGTH_SHORT).show();
            }
        }
        Log.d(tag,"Return onActivityResult()");
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.d(tag,"Enter onSaveInstaceState()");
        if(this.current_question_number == 0){
            Log.d(tag,"Return onSaveInstanceState()");
            return;
        }

        Log.d(tag,"onSaveInstanceState on Rotation");
        outState.putInt(MainActivity.state_current_question_number,this.current_question_number);
        outState.putInt(MainActivity.state_number_of_correct_question,this.number_of_correct_question);
        outState.putInt(MainActivity.state_number_of_incorrect_question,this.number_of_incorrect_question);
        outState.putString(MainActivity.state_question_type,this.question.getQuestion_type());
        outState.putInt(MainActivity.state_question_number,this.question.getNumber());
        outState.putBoolean(MainActivity.state_question_answer,this.question.getAnswer());
        outState.putBoolean(MainActivity.state_question_state,this.question_state);
        outState.putBoolean(MainActivity.state_hint_state,this.hint_status);
        outState.putBoolean(MainActivity.state_cheat_state,this.cheating_status);
        outState.putInt(MainActivity.state_number_of_hint_left,this.number_of_hint_left);
        super.onSaveInstanceState(outState);
        Log.d(tag,"Return onSaveInstanceState()");
    }

    public void writeInternalFavoriteFile(String data){
        String file_name = "favorites";
        if(!this.fileExistance(file_name)){
            File file = new File(getApplicationContext().getFilesDir(), file_name + ".txt");
            try {
                OutputStream outputStream = openFileOutput(file_name + ".txt", getApplicationContext().MODE_APPEND);
                //OutputStream outputStream = openFileOutput(file_name + ".txt", getApplicationContext().MODE_PRIVATE);
                //String data = "kk";
                data += "\n";
                outputStream.write(data.getBytes());
                outputStream.close();
                Toast.makeText(this,"Successfully added to favorite ",Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                OutputStream outputStream = openFileOutput(file_name + ".txt", getApplicationContext().MODE_APPEND);
                outputStream.write("\n".getBytes());
                outputStream.write(data.getBytes());
                outputStream.close();
                Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    public void readInteranlFavoriteFile(){
        String file_name = "favorites";
        try{
            File file = new File(getApplicationContext().getFilesDir(), file_name + ".txt");
            InputStream inputStream = openFileInput(file_name + ".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(sb)
                    .setTitle("My Favorite Questions");
            builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //SharedPreferences
        if(this.current_question_number != 0 ) {
            sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(MainActivity.state_current_question_number, this.current_question_number);
            editor.putInt(MainActivity.state_current_question_number, this.current_question_number);
            editor.putInt(MainActivity.state_number_of_correct_question, this.number_of_correct_question);
            editor.putInt(MainActivity.state_number_of_incorrect_question, this.number_of_incorrect_question);
            editor.putString(MainActivity.state_question_type, this.question.getQuestion_type());
            editor.putInt(MainActivity.state_question_number, this.question.getNumber());
            editor.putBoolean(MainActivity.state_question_answer, this.question.getAnswer());
            editor.putBoolean(MainActivity.state_question_state, this.question_state);
            editor.putBoolean(MainActivity.state_hint_state, this.hint_status);
            editor.putBoolean(MainActivity.state_cheat_state, this.cheating_status);
            editor.putInt(MainActivity.state_number_of_hint_left, this.number_of_hint_left);
            editor.commit();
        }
    }

    public String getExternalFile(List<Record> records){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File file = getExternalFilesDir(null);
            File dir = new File(file.getAbsolutePath()+"/sanket");
            dir.mkdirs();
            File outputFile = new File(dir.getAbsolutePath()+"tmp.txt");
            try{
                outputFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(outputFile);
                OutputStreamWriter fout_writer = new OutputStreamWriter(fout);
                String data = "kk";
                for (Record record : records){
                    data = "";
                    data = data + record.getCreate_date();
                    data = data + " , " + record.getTotal_no_of_questions();
                    data = data + " , " + record.getTotal_no_of_correct_questions();
                    data = data + " , " + record.getTotal_no_of_incorrect_questions();
                    data = data + "\n";
                    fout_writer.append(data);
                }
                data = " Best No of Correct = "+PrimeNoDAO.best_no_of_correct + " Worst No of Correct : "+PrimeNoDAO.worst_no_of_correct;
                fout_writer.append(data);
                //Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                fout_writer.close();
                fout.close();
                return data;
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        return null;
    }
}

