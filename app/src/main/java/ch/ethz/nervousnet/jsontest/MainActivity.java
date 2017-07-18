package ch.ethz.nervousnet.jsontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {
    //App-UI
    //important variables for App-UI
    TextView tvConnectionJson;
    Button btnSetUserInfo;
    Button btnGetUserInfo;
    Button btnGetUserAssignment;
    Button btnReturnUserAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvConnectionJson = (TextView) findViewById(R.id.tv_connection_json);
        btnSetUserInfo = (Button) findViewById(R.id.btn_set_user_info);
        btnGetUserInfo = (Button) findViewById(R.id.btn_get_user_info);
        btnGetUserAssignment = (Button) findViewById(R.id.btn_get_user_assignment);
        btnReturnUserAssignment = (Button) findViewById(R.id.btn_return_user_assignment);
        btnSetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates JSON user File
                JSONObject jObject = new JSONObject();
                try {
                    jObject.put("Id", "dario");
                    jObject.put("Name", "Dario Leuchtmann");
                    jObject.put("Email", "ldario@student.ethz.ch");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new CreateUser().execute();
                Client client = new Client();
                client.setTvConnectionJson(tvConnectionJson);
                client.createUser(PROJECT_ID,jObject);
            }
        });
        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = new Client();
                client.setTvConnectionJson(tvConnectionJson);
                client.user(PROJECT_ID,USER_ID);
            }
        });
        btnGetUserAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = new Client();
                client.setTvConnectionJson(tvConnectionJson);
                client.userAssignment(PROJECT_ID,USER_ID,TASK_ID);
            }
        });
        btnReturnUserAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates JSON user File
                JSONObject user = new JSONObject();
                try {
                    user.put("Id", "dario");
                    user.put("Name", "Dario Leuchtmann");
                    user.put("Email", "ldario@student.ethz.ch");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new CreateUser().execute();
                Client client = new Client();
                client.setTvConnectionJson(tvConnectionJson);
                client.userCreateAssignment(PROJECT_ID,user,TASK_ID,USER_ID);
            }
        });
    }



    BufferedReader reader = null;

    //JSON Files
    //TODO get them as Parameters
    JSONObject project = new JSONObject();
    JSONArray tasks = new JSONArray();
    JSONObject task = new JSONObject();
    JSONArray assets = new JSONArray();
    JSONObject asset = new JSONObject();
    JSONObject assignmentCriteria = new JSONObject();
    JSONObject completionCriteria = new JSONObject();
    JSONObject submittedData = new JSONObject();
    JSONObject record = new JSONObject();
    JSONObject recordMetadata = new JSONObject();
    JSONObject metadata = new JSONObject();
    String[] sensors = {"accelerometer"};
    private void CreateJsonFiles() {
        //SubmittedData
        try {
            submittedData.put("record",record);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //AssignmentCriteria
        try {
            assignmentCriteria.put("SubmittedData",submittedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //CompletionCriteria
        try {
            completionCriteria.put("Total",100);
            completionCriteria.put("Matching",100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Project
        try {
            project.put("Id","basic");
            project.put("Name", "Basic");
            project.put("Description", "Basic Nervousnet data collection");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Task
        try {
            task.put("Name", "record");
            task.put("Description", "Recording data for specified sensors and time interval");
            task.put("CurrentState", "available");
            task.put("AssignmentCriteria", assignmentCriteria);
            task.put("CompletionCriteria", completionCriteria);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Tasks
        tasks.put(task);
        //RecordMetadata
        try {
            recordMetadata.put("sensors",sensors);
            recordMetadata.put("start", "2016-06-01T12:00:00Z");
            recordMetadata.put("end", "2016-06-01T12:10:00Z");
            recordMetadata.put("step", 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Metadata
        try {
            metadata.put("record",recordMetadata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Asset
        try {
            asset.put("Name","Data Set 1");
            asset.put("Url", "http://erdw.ethz.ch/nervousnet/basic/intro.html");
            asset.put("Metadata", metadata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Assets
        assets.put(asset);
    }



    //Constants
    final String PROJECT_ID = "nervousnetTwo";
    final String USER_ID = "dario";
    final String TASK_ID = "record";
}