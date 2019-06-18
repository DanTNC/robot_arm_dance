package com.example.uidesign;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.uidesign.BehaviorAdapter.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ScriptContentActivity extends AppCompatActivity {
    private final LinkedList<Behavior> mBehaviorList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private BehaviorAdapter mAdapter;
    private ActionViewModel mActionViewModel;
    private Action mAction;
    public static final int NEW_REQUEST = 0;
    public static final int EDIT_REQUEST = 1;

    private int current_pos;

    private Button ButtonAdd;
    private Button ButtonEdit;
    private Button ButtonDelete;
    private ToggleButton ButtonTest;
    public Map<String, Integer> motorNumber = new HashMap<String, Integer>(){{
        put("Base", 0);
        put("Shoulder", 1);
        put("Elbow", 2);
        put("Wrist", 3);
        put("Rotate", 4);
        put("Gripper", 5);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_content);
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String actions = intent.getStringExtra("actions");
        int id = intent.getIntExtra("id", -1);
        mAction = new Action(title, description, actions);
        mAction.setId(id);

        EditText titleView = findViewById(R.id.content_title_edittext);
        EditText descriptionView = findViewById(R.id.content_description_edittext);
        titleView.setText(title);
        descriptionView.setText(description);

        ButtonAdd = (Button) findViewById(R.id.button_add_action);
        ButtonEdit = (Button) findViewById(R.id.button_edit_action);
        ButtonDelete = (Button) findViewById(R.id.button_delete_action);
        ButtonTest = (ToggleButton) findViewById(R.id.button_toggle_test);

        ButtonTest.setChecked(true);

        initializeBehaviorList(actions);
        mActionViewModel = ViewModelProviders.of(this).get(ActionViewModel.class);

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

    private void initializeBehaviorList(String actions){
        Gson gson = new Gson();
        ArrayList<Behavior> action_list = gson.fromJson(actions, new TypeToken<ArrayList<Behavior>>() {}.getType());
        if(action_list != null) {
            mBehaviorList.clear();
            mBehaviorList.addAll(action_list);
        }
    }

    private void putAccStates(Intent intent, int until){
        Map<String, Integer> states = new HashMap<>(GAORequest.motorInitial);
        if(until == -1){
            for(Behavior behavior: mBehaviorList){
                states.put(behavior.getAction(), states.get(behavior.getAction()) + behavior.getValue());
            }
        }else{
            for(int i=0; i<until; i++){
                Behavior behavior = mBehaviorList.get(i);
                states.put(behavior.getAction(), states.get(behavior.getAction()) + behavior.getValue());
            }
        }
        for(Map.Entry<String, Integer> entry : states.entrySet()){
            intent.putExtra(entry.getKey(), entry.getValue().toString());
        }
    }

    public void ToAddAction(View view) {
        Intent intent = new Intent(ScriptContentActivity.this,ActionActivity.class);
        //initial setting
        intent.putExtra("action","Base");
        intent.putExtra("value",0);
        putAccStates(intent, -1);
        intent.putExtra("isTest", !ButtonTest.isChecked());
        intent.putExtra("pos", current_pos);
        startActivityForResult(intent, NEW_REQUEST);
    }
    public void ToEditAction(View view) {
        Intent intent = new Intent(ScriptContentActivity.this,ActionActivity.class);
        intent.putExtra("action",mBehaviorList.get(current_pos).getAction());
        intent.putExtra("value",mBehaviorList.get(current_pos).getValue());
        putAccStates(intent, current_pos);
        intent.putExtra("isTest", !ButtonTest.isChecked());
        intent.putExtra("pos", current_pos);
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
    public void ToPlay(View view){
        boolean isTest = !ButtonTest.isChecked();
        GAORequest.sendRequest("action/reset", isTest, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful() && response.body().toString() != "") {
                    playChoreo();
                }
            }
        });
    }

    private void playChoreo(){
        boolean isTest = !ButtonTest.isChecked();
        String url = choreographyUrl();
        GAORequest.sendRequest("choreography/" + url, isTest, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String res = response.body().string();
                    ScriptContentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject result;
                            try {
                                result = new JSONObject(res);
                                if(result.getString("result") == "error") {
                                    Toast.makeText(ScriptContentActivity.this, result.getString("message"), Toast.LENGTH_LONG);
                                }else {
                                    Toast.makeText(ScriptContentActivity.this, result.getString("result"), Toast.LENGTH_LONG);
                                }
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private String listToString(){
        Gson gson = new Gson();
        ArrayList<Behavior> action_list = new ArrayList<>();
        action_list.addAll(mBehaviorList);
        String actions = gson.toJson(action_list);
        return actions;
    }

    private String choreographyUrl(){
        String res = "";
        res+=mBehaviorList.size()+"/";
        for(Behavior action_object:mBehaviorList){
            res+=motorNumber.get(action_object.getAction())+":"+action_object.getValue()+";";
        }
        return res;
    }

    private void updateDatabase(){
        String actions = listToString();
        mAction.setAction(actions);
        mActionViewModel.update(mAction);
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
                updateDatabase();
            }
        }
        if(requestCode == EDIT_REQUEST){
            if (resultCode == RESULT_OK){
                String action = intent.getStringExtra("action");
                int value = intent.getIntExtra("value",0);
                int pos = intent.getIntExtra("pos", 0);
//                mBehaviorList.add(new Behavior(action,value));
                mBehaviorList.set(pos, new Behavior(action, value));
                mAdapter.notifyDataSetChanged();
                updateDatabase();
            }
        }
    }

}
