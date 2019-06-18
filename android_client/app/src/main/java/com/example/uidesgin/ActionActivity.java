package com.example.uidesgin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActionActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[] btn = new Button[6];
    private Button btn_unfocus;
    private int index;
    private int value;

    private TextView LowView;
    private TextView HighView;
    private TextView ValueView;
    private int[] btn_id = {R.id.button_base, R.id.button_shoulder, R.id.button_elbow, R.id.button_wrist, R.id.button_rotate, R.id.button_gripper};
    private int[] low_limit = {0,15,0,0,0,10};
    private int[] high_limit = {180,165,180,180,180,73};
    private SeekBar valueBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        LowView = (TextView) findViewById(R.id.lowValue);
        HighView = (TextView) findViewById(R.id.highValue);
        ValueView = (TextView) findViewById(R.id.ValueBoard);

        valueBar = (SeekBar) findViewById(R.id.action_value);
        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        int value = intent.getIntExtra("value",0);

        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));

            if(btn[i].getText().toString().equals(action)){
                btn[i].setTextColor(Color.rgb(255, 255, 255));
                btn[i].setBackgroundColor(Color.rgb(3, 106, 150));
                valueBar.setMax(high_limit[i]-low_limit[i]);
                valueBar.setProgress(value-low_limit[i]);
                LowView.setText(String.valueOf(low_limit[i]));
                HighView.setText(String.valueOf(high_limit[i]));
                ValueView.setText(String.valueOf(value));
                btn_unfocus = btn[i];
                this.index = i;
            }
            else{
                btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
                btn_unfocus = btn[0];
                this.index = 0;
            }
            btn[i].setOnClickListener(this);
        }

        valueBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int count;
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                count = valueBar.getProgress();
                ValueView.setText(String.valueOf(count));
            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            }
        });

    }
    @Override
    public void onClick(View v) {
        //setForcus(btn_unfocus, (Button) findViewById(v.getId()));
        //Or use switch
        switch (v.getId()){
            case R.id.button_base :
                setFocus(btn_unfocus, btn[0]);
                LowView.setText(String.valueOf(low_limit[0]));
                HighView.setText(String.valueOf(high_limit[0]));
                ValueView.setText(String.valueOf(low_limit[0]));
                valueBar.setMax(high_limit[0]-low_limit[0]);
                valueBar.setProgress(0);
                this.value = low_limit[0];
                this.index = 0;
                break;

            case R.id.button_shoulder :
                setFocus(btn_unfocus, btn[1]);
                LowView.setText(String.valueOf(low_limit[1]));
                HighView.setText(String.valueOf(high_limit[1]));
                ValueView.setText(String.valueOf(low_limit[1]));
                valueBar.setMax(high_limit[1]-low_limit[1]);
                valueBar.setProgress(0);
                this.value = low_limit[1];
                this.index = 1;
                break;

            case R.id.button_elbow :
                setFocus(btn_unfocus, btn[2]);
                LowView.setText(String.valueOf(low_limit[2]));
                HighView.setText(String.valueOf(high_limit[2]));
                ValueView.setText(String.valueOf(low_limit[2]));
                valueBar.setMax(high_limit[2]-low_limit[2]);
                valueBar.setProgress(0);
                this.value = low_limit[2];
                this.index = 2;
                break;

            case R.id.button_wrist :
                setFocus(btn_unfocus, btn[3]);
                LowView.setText(String.valueOf(low_limit[3]));
                HighView.setText(String.valueOf(high_limit[3]));
                ValueView.setText(String.valueOf(low_limit[3]));
                valueBar.setMax(high_limit[3]-low_limit[3]);
                valueBar.setProgress(0);
                this.value = low_limit[3];
                this.index = 3;
                break;
            case R.id.button_rotate :
                setFocus(btn_unfocus, btn[4]);
                LowView.setText(String.valueOf(low_limit[4]));
                HighView.setText(String.valueOf(high_limit[4]));
                ValueView.setText(String.valueOf(low_limit[4]));
                valueBar.setMax(high_limit[4]-low_limit[4]);
                valueBar.setProgress(0);
                this.value = low_limit[4];
                this.index = 4;
                break;
            case R.id.button_gripper :
                setFocus(btn_unfocus, btn[5]);
                LowView.setText(String.valueOf(low_limit[5]));
                HighView.setText(String.valueOf(high_limit[5]));
                ValueView.setText(String.valueOf(low_limit[5]));
                valueBar.setMax(high_limit[5]-low_limit[5]);
                valueBar.setProgress(0);
                this.value = low_limit[5];
                this.index = 5;
                break;
        }
    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }
    public void AddAction(View view) {
        int id = view.getId();
        Intent replyIntent = new Intent();
        if(id == R.id.confirm_build_script_button){
            String action = btn_unfocus.getText().toString();
            int value = valueBar.getProgress();
            replyIntent.putExtra("action", action);
            replyIntent.putExtra("value", value);
            setResult(RESULT_OK, replyIntent);
        }
        else{
            setResult(RESULT_CANCELED, replyIntent);
        }
        finish();
    }
    public void AddValue(View view){
        if(this.value < high_limit[index]){
            this.value = this.value + 1;
            ValueView.setText(String.valueOf(this.value));
            valueBar.setProgress(this.value);
        }
    }
    public void MinusValue(View view){
        if(this.value > low_limit[index]){
            this.value = this.value - 1;
            ValueView.setText(String.valueOf(this.value));
            valueBar.setProgress(this.value);
        }
    }
}
