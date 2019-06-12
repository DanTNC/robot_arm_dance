package com.example.gaoranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button ledBtn;
    private Button baseBtn;
    private EditText baseDelta;
    private Button shoulderBtn;
    private EditText shoulderDelta;
    private Button elbowBtn;
    private EditText elbowDelta;
    private Button wristBtn;
    private EditText wristDelta;
    private Button rotateBtn;
    private EditText rotateDelta;
    private Button gripperBtn;
    private EditText gripperDelta;
    private TextView mTextViewResult;
    private TextView mTextStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        toggleTest = findViewById(R.id.toggleButton);
        baseDelta = findViewById(R.id.editText2);
        shoulderDelta = findViewById(R.id.editText3);
        elbowDelta = findViewById(R.id.editText4);
        wristDelta = findViewById(R.id.editText5);
        rotateDelta = findViewById(R.id.editText6);
        gripperDelta = findViewById(R.id.editText7);
        mTextViewResult = findViewById(R.id.result);
        mTextStates = findViewById(R.id.states);
    }

    public String getUrlHost(){
        return (toggleTest.isChecked())?getUrl:testUrl;
    }

    public void onLedToggle(View view){
        sendRequest("action/led/toggle", false);
    }

    public void onBaseApply(View view){
        String delta = baseDelta.getText().toString();
        sendRequest("action/base/" + delta, false);
    }

    public void onShoulderApply(View view){
        String delta = shoulderDelta.getText().toString();
        sendRequest("action/shoulder/" + delta, false);
    }

    public void onElbowApply(View view){
        String delta = elbowDelta.getText().toString();
        sendRequest("action/elbow/" + delta, false);
    }

    public void onWristApply(View view){
        String delta = wristDelta.getText().toString();
        sendRequest("action/wrist/" + delta, false);
    }

    public void onRotateApply(View view){
        String delta = rotateDelta.getText().toString();
        sendRequest("action/rotate/" + delta, false);
    }

    public void onGripperApply(View view){
        String delta = gripperDelta.getText().toString();
        sendRequest("action/gripper/" + delta, false);
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
                                    mTextStates.setText(result.toString());
                                }else{
                                    mTextViewResult.setText(result.getString("result"));
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
