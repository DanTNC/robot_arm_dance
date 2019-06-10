package com.example.gaoranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //設定HTTP Get & Post要連線的Url
    private String getUrl = "http://192.168.1.147/arduino/action/led/";

    private Button getBtn;
    private TextView mTextViewResult;
    private boolean state = false;

    private String result;  //存放Post回傳值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        mTextViewResult = findViewById(R.id.result);
    }
    //依照按下的按鈕去做相對應的任務
    public void onClick(View v){
        OkHttpClient client = new OkHttpClient();

        String targetUrl;

        if(state){
            targetUrl = getUrl + "on";
            mTextViewResult.setText("On");
        }else{
            targetUrl = getUrl + "off";
            mTextViewResult.setText("Off");
        }
        state = !state;

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
                                mTextViewResult.setText(result.getString("result"));
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
