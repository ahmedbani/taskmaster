package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddTasks extends AppCompatActivity {
    List<Team> teams = new ArrayList<>();
    Team selectedTeam;
    Uri uri;
    Intent intent;
    Uri data;
    EditText title;
    EditText body;
    EditText state;
    RadioGroup rGroup;
    private FusedLocationProviderClient fusedLocationClient;
    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        intent = getIntent();
        data = intent.getData();

// onCreate
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {


                            Geocoder geocoder;
                            List<Address> addresses = new ArrayList<>();
                            geocoder = new Geocoder(AddTasks.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                            loc = city + "- " + country;


                            System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLL" + loc);

                        }
                    }
                });


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
                String key = task.getId();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddTasks.this);
                sharedPreferences.edit().putString(key, loc).apply();

                if(data != null){

                    if (intent.getType().indexOf("image/") != -1) {
                        System.out.println("number 222222222 "+data);

                        try {
                            InputStream exampleInputStream = getContentResolver().openInputStream(data);
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

                Intent toHome = new Intent(AddTasks.this , MainActivity.class);
                startActivity(toHome);

            }
        });
    }
    //content://com.android.providers.media.documents/document/image%3A431103
    private void uploadInputStream() {
        if (uri != null) {
            System.out.println("gggggggg"+uri);
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