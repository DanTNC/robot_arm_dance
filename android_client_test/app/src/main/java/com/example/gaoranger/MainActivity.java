/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gaoranger;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



import java.util.List;

/**
 * This class displays a list of words in a RecyclerView.
 * The words are saved in a Room database.
 * The layout for this activity also displays an FAB that
 * allows users to start the NewWordActivity to add new words.
 * Users can delete a word by swiping it away, or delete all words
 * through the Options menu.
 * Whenever a new word is added, deleted, or updated, the RecyclerView
 * showing the list of words automatically updates.
 */
public class MainActivity extends AppCompatActivity {

    private ActionViewModel mActionViewModel;

    public static final int NEW_ACTION_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_ACTION_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_NAME = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_ACTION = "extra_ans_to_be_updated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ActionListAdapter adapter = new ActionListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup the WordViewModel
        mActionViewModel = ViewModelProviders.of(this).get(ActionViewModel.class);
        // Get all the words from the database
        // and associate them to the adapter
        mActionViewModel.getAllActions().observe(this , new Observer<List<Action>>() {
            @Override
            public void onChanged(@Nullable final List<Action> actions) {
                // Update the cached copy of the words in the adapter.
                adapter.setActions(actions);
            }
        });

        // Floating action button setup
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, NEW_ACTION_ACTIVITY_REQUEST_CODE);
            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a word,
                    // delete that word from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Action myAction = adapter.getActionAtPosition(position);
                        Toast.makeText(MainActivity.this,
                                "Deleting" + " " +
                                        myAction.getAction(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        mActionViewModel.deleteAction(myAction);
                    }
                });
        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ActionListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                Action action = adapter.getActionAtPosition(position);
                launchUpdateWordActivity(action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // The options menu has a single item "Clear all data now"
    // that deletes all the entries in the database
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText(this, "Clear the data now!", Toast.LENGTH_LONG).show();

            // Delete the existing data
            mActionViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** When the user enters a new word in the NewWordActivity,
     * that activity returns the result to this activity.
     * If the user entered a new word, save it in the database.

     * @param requestCode -- ID for the request
     * @param resultCode -- indicates success or failure
     * @param data -- The Intent sent back from the NewWordActivity,
     *             which includes the word that the user entered
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ACTION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Action word = new Action(data.getStringExtra(SettingActivity.EXTRA_REPLY_NAME),data.getStringExtra(SettingActivity.EXTRA_REPLY_ACTION));
            // Save the data
            mActionViewModel.insert(word);
        } else if (requestCode == UPDATE_ACTION_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            // TODO: implement "UPDATE"

            Action word = new Action(data.getStringExtra(SettingActivity.EXTRA_REPLY_NAME),data.getStringExtra(SettingActivity.EXTRA_REPLY_ACTION));
            // Save the data
            int id=data.getIntExtra(SettingActivity.EXTRA_REPLY_ID,-1);
            word.setId(id);
            //if(data.getStringExtra(NewWordActivity.KEY_CHANGE)=="yes") {
            mActionViewModel.update(word);

        }else {
            Toast.makeText(
                    this, "Action not saved because it is empty", Toast.LENGTH_LONG).show();
        }
    }
    public void launchUpdateWordActivity(Action action) {
        Intent intent = new Intent(this, ScriptActivity.class);
        intent.putExtra("id", action.getId());
        intent.putExtra(EXTRA_DATA_UPDATE_NAME, action.getName());
        intent.putExtra(EXTRA_DATA_UPDATE_ACTION, action.getAction());
        startActivityForResult(intent, UPDATE_ACTION_ACTIVITY_REQUEST_CODE);
    }
}