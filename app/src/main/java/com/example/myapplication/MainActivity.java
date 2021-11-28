package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

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
//        AppDataBase db = Room.databaseBuilder(getApplicationContext(),
//                AppDataBase.class, "task").allowMainThreadQueries().build();
//        TaskDao taskDao = db.taskDao();
//        List<com.amplifyframework.datastore.generated.model.Task> tasks = new ArrayList<>();
//        tasks.add(new Task("Task 1", "created by Ahmed Bani-Salameh","complete"));
//        tasks.add(new Task("Task 2", "created by Ahmed Bani-Salameh","new"));
//        tasks.add(new Task("Task 3", "created by Ahmed Bani-Salameh","assigned"));

//        RecyclerView recyclerView = findViewById(R.id.allTasksView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new TaskAdapter(tasks,this));
//

//        Team team1 = Team.builder()
//                .name("Team 1")
//                .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                response -> Log.i("MyAmplifyApp", "Added Task with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        Team team2 = Team.builder()
//                .name("Team 2")
//                .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                response -> Log.i("MyAmplifyApp", "Added Task with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        Team team3 = Team.builder()
//                .name("Team 3")
//                .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                response -> Log.i("MyAmplifyApp", "Added Task with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
    }
    @Override
    protected void onResume() {
        super.onResume();

        String usernameMessage = "\'s Tasks";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "username");
        String team = sharedPreferences.getString("team","team");
        TextView usernameField = findViewById(R.id.usernameView);
        usernameField.setText(username + usernameMessage);

        RecyclerView recyclerView = findViewById(R.id.allTasksView);
        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                recyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        ArrayList<Task> tasks = new ArrayList<Task>();
        ArrayList<Team> teams = new ArrayList<Team>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks,this));

        Amplify.API.query(
                ModelQuery.list(Team.class) ,
                response -> {
                    for (Team team1 : response.getData()){
                        Log.i("MyAmplifyApp" , team1.getName() );
                        teams.add(team1);
                    }
                    for (int i = 0 ; i < teams.size(); i++){
                        if (teams.get(i).getName().equals(team)){
                            for (int j = 0 ; j < teams.get(i).getTasks().size() ; j++){
                                tasks.add(teams.get(i).getTasks().get(j));
                            }
                        }
                    }
                    handler.sendEmptyMessage(1);
                    Log.i("MyAmplifyApp", "Out of Loop!");
                }, error -> Log.e("MyAmplifyApp",  error.getMessage()));

        Button settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSettings = new Intent(MainActivity.this,Settings.class);
                startActivity(toSettings);
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }
    @Override
    public void onTaskClick(int position, Task task) {
        Intent toTask = new Intent(MainActivity.this,TaskDetail.class);
        toTask.putExtra("taskName", task.getTitle());
        toTask.putExtra("taskBody", task.getBody());
        toTask.putExtra("taskState", task.getState());
        startActivity(toTask);
    }

}

