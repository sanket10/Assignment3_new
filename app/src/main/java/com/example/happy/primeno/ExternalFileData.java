package com.example.happy.primeno;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by Happy on 10/2/2016.
 */
public class ExternalFileData {
    String LOG_TAG = "ExternalFileData";

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    public String getExternalFileOfCompleteResult(List<Record> records) {
        String filename = "PrimeNo"+(new Date().getTime());
        try {
          File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"PrimeNumber");
            path.mkdirs();
            File outputFile = new File(path.getAbsolutePath(), filename + ".txt");
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
            return "ikii";
        }
    }
    public String getExternalFileOfCompleteResultPrivate(List<Record> records) {
        String filename = "PrimeNo"+(new Date().getTime());
        String state = Environment.getExternalStorageState();
        try {
            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"PrimeNumber");
            //File path = getExternalFileDir(null);

            path.mkdirs();
            File outputFile = new File(path.getAbsolutePath(), filename + ".txt");
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
            return "ikii";
        }
    }
}




/*************************************************************************************************************/

