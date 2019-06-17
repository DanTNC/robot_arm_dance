package com.example.uidesgin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ScriptAddActivity extends AppCompatActivity {
    public EditText mTitleView;
    public EditText mDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_add);

        mTitleView = (EditText) findViewById(R.id.content_title_edittext);
        mDescriptionView = (EditText) findViewById(R.id.content_description_edittext);
    }
    public void returnReply(View view) {
        /*
        int id = view.getId();
        Intent replyIntent = new Intent();
        if(id == R.id.confirm_build_script_button){
            String title = mTitleView.getText().toString();
            String description = mDescriptionView.getText().toString();
            if(title == ""){
                title = "Title X";
            }
            if(description == ""){
                description = "Description X";
            }
            replyIntent.putExtra("title", title);
            replyIntent.putExtra("description", description);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
        else{
            replyIntent.putExtra("title", reply);
        }
*/
    }
}
