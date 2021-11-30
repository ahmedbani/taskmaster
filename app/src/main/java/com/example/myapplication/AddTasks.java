package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddTasks extends AppCompatActivity {
    List<Team> teams = new ArrayList<>();
    Team selectedTeam;
    Uri uri;
    EditText title ;
    EditText body;
    EditText state;
    RadioGroup rGroup ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        title = findViewById(R.id.editTitle);
        body = findViewById(R.id.editBody);
        state = findViewById(R.id.editState);
        rGroup = findViewById(R.id.teams);
        int id = rGroup.getCheckedRadioButtonId();
        RadioButton rButton = findViewById(id);

        Amplify.API.query(ModelQuery.list(Team.class), response -> {
            for (Team team : response.getData()) {
                teams.add(team);
                Log.i("Teams: ", team.getName());
            }
        }, error -> Log.e("MyAmplifyApp", "Query failure", error));

        Button upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFileFromDevice();
            }
        });

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();
                uploadInputStream();
//                Task task = new Task(title.getText().toString(),body.getText().toString(),state.getText().toString());
//                AppDataBase db = Room.databaseBuilder(getApplicationContext(),
//                        AppDataBase.class, "task").allowMainThreadQueries().build();
//                TaskDao taskDao = db.taskDao();
//                taskDao.saveTask(task);

                for (Team team : teams) {
                    if (team.getName().equals(rButton.getText().toString())){
                        System.out.println(rButton.getText().toString());
                        selectedTeam = team;
                    }
                }
                Task task = Task.builder()
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .state(state.getText().toString())
                        .teamId(selectedTeam.getId())

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
    private void uploadInputStream() {
        if (uri != null) {
            try {
                InputStream exampleInputStream = getContentResolver().openInputStream(uri);
                Amplify.Storage.uploadInputStream(
                        title.getText().toString(),
                        exampleInputStream,
                        result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                        storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                );
            } catch (FileNotFoundException error) {
                Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
            }
        }
    }
    private void getFileFromDevice() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose File!");
        startActivityForResult(chooseFile, 2048);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        File file = new File(getApplicationContext().getFilesDir(), "uploadFileCopied");
        uri = data.getData();
    }
}