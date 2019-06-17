package com.example.uidesgin;

import android.content.Intent;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;

public class ScriptContentActivity extends AppCompatActivity {
    private final LinkedList<Behavior> mBehaviorList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private BehaviorAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_content);
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        EditText titleView = findViewById(R.id.content_title_edittext);
        EditText descriptionView = findViewById(R.id.content_description_edittext);
        titleView.setText(title);
        descriptionView.setText(description);

        for (int i = 0; i < 3; i++) {
            mBehaviorList.add(new Behavior("Base", i*i ));
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new BehaviorAdapter(this, mBehaviorList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
