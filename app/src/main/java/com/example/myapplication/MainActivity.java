package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allTasks = findViewById(R.id.allTasksButton);
        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAllTasks = new Intent(MainActivity.this, AllTasks.class);
                startActivity(toAllTasks);
            }
        });
        Button addTasks = findViewById(R.id.addTaskButton);
        addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddTasks = new Intent(MainActivity.this, AddTasks.class);
                startActivity(toAddTasks);
            }
        });
    }
}

