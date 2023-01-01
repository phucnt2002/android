package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sampleproject.Model.Current;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;


public class CommentActivity extends AppCompatActivity {

    ImageView btnBack;

    CheckBox cbTemp1;
    CheckBox cbTemp2;
    CheckBox cbTemp3;
    CheckBox cbWind1;
    CheckBox cbWind2;
    CheckBox cbWind3;
    CheckBox cbOther1;
    CheckBox cbOther2;
    CheckBox cbOther3;
    CheckBox cbOther4;
    CheckBox cbOther5;
    CheckBox cbOther6;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    TextView textView6;
    TextView textView8;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        String json = getIntent().getStringExtra("Current");
        Gson gson = new GsonBuilder().create();

        KLog.json(json);
        Current current = gson.fromJson(json, Current.class);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        Init();

        textView6.setText(current.attributes.temperature.value + " °C");
        textView8.setText(current.attributes.windSpeed.value + " km/h");
        cbTemp1.setOnClickListener(view -> CheckEvent(cbTemp1, cbTemp2, cbTemp3,view.getId()));
        cbTemp2.setOnClickListener(view -> CheckEvent(cbTemp1, cbTemp2, cbTemp3,view.getId()));
        cbTemp3.setOnClickListener(view -> CheckEvent(cbTemp1, cbTemp2, cbTemp3,view.getId()));

        cbWind1.setOnClickListener(view -> CheckEvent(cbWind1, cbWind2, cbWind3,view.getId()));
        cbWind2.setOnClickListener(view -> CheckEvent(cbWind1, cbWind2, cbWind3,view.getId()));
        cbWind3.setOnClickListener(view -> CheckEvent(cbWind1, cbWind2, cbWind3,view.getId()));

        cbOther1.setOnClickListener(view -> CheckOtherEvent(view.getId()));
        cbOther2.setOnClickListener(view -> CheckOtherEvent(view.getId()));
        cbOther3.setOnClickListener(view -> CheckOtherEvent(view.getId()));
        cbOther4.setOnClickListener(view -> CheckOtherEvent(view.getId()));
        cbOther5.setOnClickListener(view -> CheckOtherEvent(view.getId()));
        cbOther6.setOnClickListener(view -> CheckOtherEvent(view.getId()));

    }

    private void CheckEvent(CheckBox cbTemp1,CheckBox cbTemp2, CheckBox cbTemp3, int id) {
        if (cbTemp1.getId() == id) {
//            btnDate.setEnabled(true);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(false);
            cbTemp1.setChecked(true);
            cbTemp2.setChecked(false);
            cbTemp3.setChecked(false);
        } else if (cbTemp2.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(true);
//            btnYear.setEnabled(false);
            cbTemp2.setChecked(true);
            cbTemp3.setChecked(false);
            cbTemp1.setChecked(false);
        } else if (cbTemp3.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbTemp3.setChecked(true);
            cbTemp1.setChecked(false);
            cbTemp2.setChecked(false);
        }
    }

    private void CheckOtherEvent(int id){
        if (cbOther1.getId() == id) {
//            btnDate.setEnabled(true);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(false);
            cbOther1.setChecked(true);
            cbOther2.setChecked(false);
            cbOther3.setChecked(false);
            cbOther4.setChecked(false);
            cbOther5.setChecked(false);
            cbOther6.setChecked(false);
        } else if (cbOther2.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(true);
//            btnYear.setEnabled(false);
            cbOther2.setChecked(true);
            cbOther3.setChecked(false);
            cbOther1.setChecked(false);
            cbOther4.setChecked(false);
            cbOther5.setChecked(false);
            cbOther6.setChecked(false);
        } else if (cbOther3.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbOther3.setChecked(true);
            cbOther1.setChecked(false);
            cbOther2.setChecked(false);
            cbOther4.setChecked(false);
            cbOther5.setChecked(false);
            cbOther6.setChecked(false);
        }else if (cbOther4.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbOther3.setChecked(false);
            cbOther1.setChecked(false);
            cbOther2.setChecked(false);
            cbOther4.setChecked(true);
            cbOther5.setChecked(false);
            cbOther6.setChecked(false);
        }else if (cbOther5.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbOther3.setChecked(false);
            cbOther1.setChecked(false);
            cbOther2.setChecked(false);
            cbOther4.setChecked(false);
            cbOther5.setChecked(true);
            cbOther6.setChecked(false);
        }else if (cbOther6.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbOther3.setChecked(false);
            cbOther1.setChecked(false);
            cbOther2.setChecked(false);
            cbOther4.setChecked(false);
            cbOther5.setChecked(false);
            cbOther6.setChecked(true);
        }
    }

    private void Init() {
        cbTemp1 = findViewById(R.id.cbTemp1);
        cbTemp2 = findViewById(R.id.cbTemp2);
        cbTemp3 = findViewById(R.id.cbTemp3);
        cbWind1 = findViewById(R.id.cbWind1);
        cbWind2 = findViewById(R.id.cbWind2);
        cbWind3 = findViewById(R.id.cbWind3);
        cbOther1 = findViewById(R.id.cbOther1);
        cbOther2 = findViewById(R.id.cbOther2);
        cbOther3 = findViewById(R.id.cbOther3);
        cbOther4 = findViewById(R.id.cbOther4);
        cbOther5 = findViewById(R.id.cbOther5);
        cbOther6 = findViewById(R.id.cbOther6);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        textView6 = findViewById(R.id.textView6);
        textView8 = findViewById(R.id.textView8);
    }
}