package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String header = intent.getExtras().getString("taskName");
        String body = intent.getExtras().getString("taskBody");
        String state = intent.getExtras().getString("taskState");
        TextView headerViews = findViewById(R.id.detailHead);
        TextView bodyViews = findViewById(R.id.detailBody);
        TextView stateViews = findViewById(R.id.detailState);
        headerViews.setText(header);
        bodyViews.setText(body);
        stateViews.setText(state);
        TextView location = findViewById(R.id.location);
        String key = intent.getExtras().getString("key");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TaskDetail.this);
        String locationData = sharedPreferences.getString(key,"No Location Found");

        location.setText(locationData);

        ImageView taskImage = findViewById(R.id.taskImage);
        Amplify.Storage.downloadFile(
                header,
                new File(getApplicationContext().getFilesDir() + "/Example Key.jpg"),
                result ->{
                    taskImage.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                },
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );
    }
}