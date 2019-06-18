package com.example.uidesign;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ScriptAddActivity extends AppCompatActivity{
    private EditText mTitleView;
    private EditText mDescriptionView;
    private Button mConfirm;


    @Override
    protected void onCreate (Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_add);

        mTitleView = (EditText) findViewById(R.id.content_title_edittext);
        mDescriptionView = (EditText) findViewById(R.id.content_description_edittext);
        mConfirm = (Button) findViewById(R.id.confirm_build_script_button);

        mTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(mTitleView.getText()) || TextUtils.isEmpty(mDescriptionView.getText())){
                    mConfirm.setEnabled(false);
                    mConfirm.setFocusable(false);
                    mConfirm.setClickable(false);
                    mConfirm.setBackgroundColor(Color.parseColor("#A09A9A"));
                }
                else{
                    mConfirm.setEnabled(true);
                    mConfirm.setFocusable(true);
                    mConfirm.setClickable(true);
                    mConfirm.setBackgroundColor(Color.parseColor("#00ff7f"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDescriptionView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(mTitleView.getText()) || TextUtils.isEmpty(mDescriptionView.getText())){
                    mConfirm.setEnabled(false);
                    mConfirm.setFocusable(false);
                    mConfirm.setClickable(false);
                    mConfirm.setBackgroundColor(Color.parseColor("#A09A9A"));
                }
                else{
                    mConfirm.setEnabled(true);
                    mConfirm.setFocusable(true);
                    mConfirm.setClickable(true);
                    mConfirm.setBackgroundColor(Color.parseColor("#00ff7f"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    //To add a new script
    public void AddScript(View view) {
        int id = view.getId();
        Intent replyIntent = new Intent();
        if(id == R.id.confirm_build_script_button){
            String title = mTitleView.getText().toString();
            String description = mDescriptionView.getText().toString();
            //To provide the initial content of the title and description.
            if(title == ""){
                title = "Title X";
            }
            if(description == ""){
                description = "Description X";
            }
            replyIntent.putExtra("title", title);
            replyIntent.putExtra("description", description);
            setResult(RESULT_OK, replyIntent);

        }
        else{
            setResult(RESULT_CANCELED, replyIntent);
        }
        finish();
    }

}
