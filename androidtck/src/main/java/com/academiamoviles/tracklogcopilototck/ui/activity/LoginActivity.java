package com.academiamoviles.tracklogcopilototck.ui.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper mDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBHelper = new DatabaseHelper(this);
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);

        if(false ==database.exists()){
            mDBHelper.getReadableDatabase();
            if(copyDatabase(this)){
                Log.v("LoginActivity", "Copy database success");
              //  System.out.println("COPY DATABASE SUCCESS");
            }else{
                  Log.v("LoginActivity", "Copy database error");
                //System.out.println("COPY DATABASE ERROR");
                return;
            }
        }

        setContentView(R.layout.login_activity);
    }


    public boolean copyDatabase(Context context){
        try{
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION+DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length =0;
            while((length = inputStream.read(buff)) >0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.v("MainActivity","DB Copied");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;

        }

    }
}
