package com.example.uidesgin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.drm.DrmStore;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uidesgin.BehaviorAdapter.OnItemClickListener;

import java.util.LinkedList;

public class ScriptContentActivity extends AppCompatActivity {
    private final LinkedList<Behavior> mBehaviorList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private BehaviorAdapter mAdapter;
    public static final int NEW_REQUEST = 1;
    public static final int EDIT_REQUEST = 1;

    private int current_pos;

    private Button ButtonAdd;
    private Button ButtonEdit;
    private Button ButtonDelete;
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

        ButtonAdd = (Button) findViewById(R.id.button_add_action);
        ButtonEdit = (Button) findViewById(R.id.button_edit_action);
        ButtonDelete = (Button) findViewById(R.id.button_delete_action);

        for (int i = 0; i < 3; i++) {
            mBehaviorList.add(new Behavior("Base", i*i*i ));
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new BehaviorAdapter(this, mBehaviorList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new OnItemClickListener(){
        @Override
        public void onItemClick(View view,int position) {
            current_pos = position;
            for(int i=0;i<mAdapter.getItemCount();i++){
                View item=mAdapter.getView(i);
                TextView ActionView = (TextView)(item.findViewById(R.id.action_name));
                TextView ValueView = (TextView)(item.findViewById(R.id.action_value));
                if (position == i) {
                    ActionView.setTextColor(Color.parseColor("#ff7f50"));
                    ValueView.setTextColor(Color.parseColor("#ff7f50"));
                } else {
                    ActionView.setTextColor(Color.parseColor("#000000"));
                    ValueView.setTextColor(Color.parseColor("#000000"));
                }
            }
            ButtonEdit.setEnabled(true);
            ButtonEdit.setFocusable(true);
            ButtonEdit.setFocusable(true);
            ButtonEdit.setBackgroundColor(Color.parseColor("#00ff7f"));
            ButtonDelete.setEnabled(true);
            ButtonDelete.setFocusable(true);
            ButtonDelete.setFocusable(true);
            ButtonDelete.setBackgroundColor(Color.parseColor("#ff0000"));
        }

    });
    }

    public void ToAddAction(View view) {
        Intent intent = new Intent(ScriptContentActivity.this,ActionActivity.class);
        //initial setting
        intent.putExtra("action","Base");
        intent.putExtra("value",0);
        startActivityForResult(intent, NEW_REQUEST);
    }
    public void ToEditAction(View view) {
        Intent intent = new Intent(ScriptContentActivity.this,ActionActivity.class);
        intent.putExtra("action",mBehaviorList.get(current_pos).getAction());
        intent.putExtra("value",mBehaviorList.get(current_pos).getValue());
        startActivityForResult(intent, EDIT_REQUEST);
    }
    public void ToDelete(View view) {
        AlertDialog.Builder DeScript = new AlertDialog.Builder(ScriptContentActivity.this);
        DeScript.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 移除項目，getAdapterPosition為點擊的項目位置
                mAdapter.removeItem(current_pos);
                ButtonEdit.setEnabled(false);
                ButtonEdit.setFocusable(false);
                ButtonEdit.setFocusable(false);
                ButtonEdit.setBackgroundColor(Color.parseColor("#A09A9A"));
                ButtonDelete.setEnabled(false);
                ButtonDelete.setFocusable(false);
                ButtonDelete.setFocusable(false);
                ButtonDelete.setBackgroundColor(Color.parseColor("#A09A9A"));
            }
        });
        DeScript.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        DeScript.setMessage("你確定要刪除這個action嗎?");
        DeScript.setTitle("提示");
        DeScript.show();
    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == NEW_REQUEST){
            if (resultCode == RESULT_OK) {
                String action = intent.getStringExtra("action");
                int value = intent.getIntExtra("value",0);
                mBehaviorList.add(new Behavior(action,value));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
