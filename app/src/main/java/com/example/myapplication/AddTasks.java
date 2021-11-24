package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTasks extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        EditText title = findViewById(R.id.editTitle);
        EditText body = findViewById(R.id.editBody);
        EditText state = findViewById(R.id.editState);
        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();
//                Task task = new Task(title.getText().toString(),body.getText().toString(),state.getText().toString());
//                AppDataBase db = Room.databaseBuilder(getApplicationContext(),
//                        AppDataBase.class, "task").allowMainThreadQueries().build();
//                TaskDao taskDao = db.taskDao();
//                taskDao.saveTask(task);
                Task task = Task.builder()
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .state(state.getText().toString())
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(task),
                        response -> Log.i("MyAmplifyApp", "Added Task with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );
                Intent toHome = new Intent(AddTasks.this , MainActivity.class);
                startActivity(toHome);

            }
        });
    }
}