package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskListener{

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
//        Button task1 = findViewById(R.id.task1);
//        task1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toTask1 = new Intent(MainActivity.this,TaskDetail.class);
//                toTask1.putExtra("taskName","Task 1");
//                startActivity(toTask1);
//            }
//        });
//        Button task2 = findViewById(R.id.task2);
//        task2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toTask2 = new Intent(MainActivity.this,TaskDetail.class);
//                toTask2.putExtra("taskName","Task 2");
//                startActivity(toTask2);
//            }
//        });
//        Button task3 = findViewById(R.id.task3);
//        task3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toTask3 = new Intent(MainActivity.this,TaskDetail.class);
//                toTask3.putExtra("taskName","Task 3");
//                startActivity(toTask3);
//            }
//        });
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task("Task 1", "created by Ahmed Bani-Salameh","complete"));
        tasks.add(new Task("Task 2", "created by Ahmed Bani-Salameh","new"));
        tasks.add(new Task("Task 3", "created by Ahmed Bani-Salameh","assigned"));

        RecyclerView recyclerView = findViewById(R.id.allTasksView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks,this));

        Button settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSettings = new Intent(MainActivity.this,Settings.class);
                startActivity(toSettings);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        String usernameMessage = "\'s Tasks";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "username");
        TextView usernameField = findViewById(R.id.usernameView);
        usernameField.setText(username + usernameMessage);
    }
    @Override
    public void onTaskClick(int position,Task task) {
        Intent toTask = new Intent(MainActivity.this,TaskDetail.class);
        toTask.putExtra("taskName", task.getTitle());
        startActivity(toTask);
    }

}

