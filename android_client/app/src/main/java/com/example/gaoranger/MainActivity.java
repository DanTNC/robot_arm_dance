package com.example.gaoranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String getUrl = "http://192.168.1.147/arduino/";
    private String testUrl = "https://smilegaoranger.herokuapp.com/";

    private ToggleButton toggleTest;
    private ToggleButton baseBtn;
    private TextView baseAngle;
    private ToggleButton shoulderBtn;
    private TextView shoulderAngle;
    private ToggleButton elbowBtn;
    private TextView elbowAngle;
    private ToggleButton wristBtn;
    private TextView wristAngle;
    private ToggleButton rotateBtn;
    private TextView rotateAngle;
    private ToggleButton gripperBtn;
    private TextView gripperAngle;
    private EditText stepSize;
    private int selectedMotor = 0;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        toggleTest = findViewById(R.id.test_toggle);
        baseBtn = findViewById(R.id.base_toggle);
        baseAngle = findViewById(R.id.base_angle);
        shoulderBtn = findViewById(R.id.shoulder_toggle);
        shoulderAngle = findViewById(R.id.shoulder_angle);
        elbowBtn = findViewById(R.id.elbow_toggle);
        elbowAngle = findViewById(R.id.elbow_angle);
        wristBtn = findViewById(R.id.wrist_toggle);
        wristAngle = findViewById(R.id.wrist_angle);
        rotateBtn = findViewById(R.id.rotate_toggle);
        rotateAngle = findViewById(R.id.rotate_angle);
        gripperBtn = findViewById(R.id.gripper_toggle);
        gripperAngle = findViewById(R.id.gripper_angle);
        stepSize = findViewById(R.id.step_size);
        mTextViewResult = findViewById(R.id.result);

        baseBtn.setChecked(true);
        baseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMotorToggle(v);
            }
        });
        shoulderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMotorToggle(v);
            }
        });
        elbowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMotorToggle(v);
            }
        });
        wristBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMotorToggle(v);
            }
        });
        rotateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMotorToggle(v);
            }
        });
        gripperBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMotorToggle(v);
            }
        });

        sendRequest("probe/states", true);
    }

    public String getUrlHost(){
        return (toggleTest.isChecked())?getUrl:testUrl;
    }

    public void onReset(View view){
        sendRequest("action/reset", true);
    }

    private void clearAllSelectedMotor(){
        baseBtn.setChecked(false);
        shoulderBtn.setChecked(false);
        elbowBtn.setChecked(false);
        wristBtn.setChecked(false);
        rotateBtn.setChecked(false);
        gripperBtn.setChecked(false);
    }

    private void displayMotor(){
        clearAllSelectedMotor();
        switch (selectedMotor){
            case 0:
                baseBtn.setChecked(true);
                break;
            case 1:
                shoulderBtn.setChecked(true);
                break;
            case 2:
                elbowBtn.setChecked(true);
                break;
            case 3:
                wristBtn.setChecked(true);
                break;
            case 4:
                rotateBtn.setChecked(true);
                break;
            case 5:
                gripperBtn.setChecked(true);
                break;
        }
    }

    public void onMotorToggle(View view){
        switch (view.getId()){
            case R.id.base_toggle:
                selectedMotor = 0;
                break;
            case R.id.shoulder_toggle:
                selectedMotor = 1;
                break;
            case R.id.elbow_toggle:
                selectedMotor = 2;
                break;
            case R.id.wrist_toggle:
                selectedMotor = 3;
                break;
            case R.id.rotate_toggle:
                selectedMotor = 4;
                break;
            case R.id.gripper_toggle:
                selectedMotor = 5;
                break;
        }
        displayMotor();
    }

    private String getMotorNode(){
        switch (selectedMotor){
            case 0:
                return "base/";
            case 1:
                return "shoulder/";
            case 2:
                return "elbow/";
            case 3:
                return "wrist/";
            case 4:
                return "rotate/";
            case 5:
                return "gripper/";
        }
        return "/";
    }

    public void onAdjustMotor(View view){
        switch (view.getId()){
            case R.id.up:
                sendRequest("action/" + getMotorNode() + stepSize.getText().toString(), false);
                break;
            case R.id.down:
                sendRequest("action/" + getMotorNode() + "-" + stepSize.getText().toString(), false);
                break;
        }
    }

    private void displayState(JSONObject states, String key, TextView angleView){
        try {
            angleView.setText(Integer.toString(states.getInt(key)));
        }
        catch(org.json.JSONException error){
            angleView.setText(getString(R.string.NotFound));
        }
    }

    private void displayStates(JSONObject states){
        Log.d(getString(R.string.app_name), "displayStates: " + states.toString());
        displayState(states, "base", baseAngle);
        displayState(states, "shoulder", shoulderAngle);
        displayState(states, "elbow", elbowAngle);
        displayState(states, "wrist", wristAngle);
        displayState(states, "rotate", rotateAngle);
        displayState(states, "gripper", gripperAngle);
    }

    public void sendRequest(String url, final boolean isForStates){
        OkHttpClient client = new OkHttpClient();

        String targetUrl = getUrlHost() + url;

        Request request = new Request.Builder()
                .url(targetUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String res = response.body().string();
//
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject result = null;
                            try {
                                result = new JSONObject(res);
                                if(isForStates) {
                                    displayStates(result);
                                }else{
                                    if(result.getString("result") == "error") {
                                        mTextViewResult.setText(result.getString("message"));
                                    }else{
                                        mTextViewResult.setText(result.getString("result"));
                                    }
                                    sendRequest("probe/states", true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
