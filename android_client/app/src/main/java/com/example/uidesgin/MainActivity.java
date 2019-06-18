package com.example.uidesgin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private final LinkedList<Script> mScriptList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private ScriptAdapter mAdapter;
    public static final int TEXT_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating buuton is used for adding new scripts.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ScriptAddActivity.class);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        // Put initial data into the word list.
        for (int i = 0; i < 3; i++) {
            mScriptList.add(new Script("Sample: " + i,"This is a sample description" ));
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new ScriptAdapter(this, mScriptList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Set the margin of each items in the recyclerview.
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(15));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Get the response from ScriptAddActivity class
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == TEXT_REQUEST){
            if (resultCode == RESULT_OK) {
                String title = intent.getStringExtra("title");
                String description = intent.getStringExtra("description");
                mScriptList.add(new Script(title,description ));
            }
        }

    }

}
