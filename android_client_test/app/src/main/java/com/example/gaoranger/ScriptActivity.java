package com.example.gaoranger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.gaoranger.MainActivity.EXTRA_DATA_UPDATE_ACTION;
import static com.example.gaoranger.MainActivity.EXTRA_DATA_UPDATE_NAME;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ScriptActivity extends AppCompatActivity {
    public TextView name;
    public TextView json;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.script);
        name = findViewById(R.id.show_name);
        json = findViewById(R.id.show_json);
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            name.setText(mBundle.getString(EXTRA_DATA_UPDATE_NAME));
            String json_string=mBundle.getString(EXTRA_DATA_UPDATE_ACTION);

            Gson gson = new Gson();
            ArrayList<SettingActivity.action> action_list=gson.fromJson(json_string, new TypeToken<ArrayList<SettingActivity.action>>(){}.getType());
            String transform="";
            for(SettingActivity.action action_object:action_list)
            {
                transform+=action_object.action_name+" : "+Integer.toString(action_object.step)+"\n";
            }
            json.setText(transform);

        }
    }
}
