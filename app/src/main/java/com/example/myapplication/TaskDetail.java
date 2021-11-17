package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
    }
}