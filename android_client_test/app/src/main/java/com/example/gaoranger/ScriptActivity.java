package com.example.gaoranger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import static com.example.gaoranger.MainActivity.EXTRA_DATA_UPDATE_ACTION;
import static com.example.gaoranger.MainActivity.EXTRA_DATA_UPDATE_NAME;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ScriptActivity extends AppCompatActivity {
    public TextView name;
    public TextView json;
    public Map<String, Integer> motorNumber = new HashMap<String, Integer>(){{
        put("base", 0);
        put("shoulder", 1);
        put("elbow", 2);
        put("wrist", 3);
        put("rotate", 4);
        put("gripper", 5);
    }};

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
            String transform = "";
            transform+=choreographyUrl(action_list)+"\n";
            for(SettingActivity.action action_object:action_list)
            {
                transform+=action_object.action_name+" : "+action_object.step+"\n";
            }
            json.setText(transform);
        }
    }

    private String choreographyUrl(ArrayList<SettingActivity.action> action_list){
        String res = "";
        res+=action_list.size()+"/";
        for(SettingActivity.action action_object:action_list){
            res+=motorNumber.get(action_object.action_name)+":"+action_object.step+";";
        }
        return res;
    }
}
