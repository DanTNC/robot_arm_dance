package com.example.gaoranger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.example.gaoranger.MainActivity.EXTRA_DATA_UPDATE_ACTION;
import static com.example.gaoranger.MainActivity.EXTRA_DATA_UPDATE_NAME;

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
            json.setText(mBundle.getString(EXTRA_DATA_UPDATE_ACTION));

        }
    }
}
