package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sampleproject.Model.Current;
import com.example.sampleproject.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;

import java.util.Calendar;


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
    CheckBox cbTotal1;
    CheckBox cbTotal2;
    CheckBox cbTotal3;
    CheckBox cbTotal4;
    CheckBox cbTotal5;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button submit;
    EditText description;
    TextView textView6;
    TextView textView8;
    String id;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        String json = getIntent().getStringExtra("Current");
        Gson gson = new GsonBuilder().create();

        KLog.json(json);
        Current current = gson.fromJson(json, Current.class);
        id = current.id;
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        Init();

        cbTemp1.setChecked(true);
        cbWind1.setChecked(true);
        cbOther1.setChecked(true);
        cbTotal1.setChecked(true);

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

        cbTotal1.setOnClickListener(view -> CheckTotalEvent(view.getId()));
        cbTotal2.setOnClickListener(view -> CheckTotalEvent(view.getId()));
        cbTotal3.setOnClickListener(view -> CheckTotalEvent(view.getId()));
        cbTotal4.setOnClickListener(view -> CheckTotalEvent(view.getId()));
        cbTotal5.setOnClickListener(view -> CheckTotalEvent(view.getId()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userComment");
                User user = getUserComment();
                myRef.child(Calendar.getInstance().getTimeInMillis()+"").setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(CommentActivity.this, "Push data success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private User getUserComment() {
        User userComment = new User();
        String totalWeather = "";
        String temp= "";
        String wind= "";
        String ortherWeather = "";
        String describe;
        if(cbTemp1.isChecked()){
            temp = "Ấm hơn";
        }else if(cbTemp2.isChecked()){
            temp = "Có vẻ chính xác";
        }else{
            temp = "Lạnh hơn";
        }

        if(cbWind1.isChecked()){
            wind = "Gió nhiều hơn";
        }else if(cbWind2.isChecked()){
            wind = "Có vẻ chính xác";
        }else{
            wind = "Gió ít hơn";
        }

        if(cbOther1.isChecked()){
            ortherWeather = "Cầu vòng";
        }else if(cbOther2.isChecked()){
            ortherWeather = "Chớp";
        }else if(cbOther3.isChecked()){
            ortherWeather = "Mưa đá";
        }else if(cbOther4.isChecked()){
            ortherWeather = "Khói";
        }else if(cbOther5.isChecked()){
            ortherWeather = "Sương mù";
        }else{
            ortherWeather = "Sương mù nhẹ";
        }

        if(cbTotal1.isChecked()){
            totalWeather = "Có nắng";
        }else if(cbTotal2.isChecked()){
            totalWeather = "Nhiều mây";
        }else if(cbTotal3.isChecked()){
            totalWeather = "Mưa";
        }else if(cbTotal4.isChecked()){
            totalWeather = "Mưa Tuyết";
        }else{
            totalWeather = "Tuyết";
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userComment.email = user.getEmail();
        userComment.name = "";
        userComment.temp = temp;
        userComment.ortherWeather = ortherWeather;
        userComment.id = id;
        userComment.wind = wind;
        userComment.totalWeather = totalWeather;
        userComment.describe = description.getText().toString();
        return userComment;
    }

    private void CheckTotalEvent(int id){
        if (cbTotal1.getId() == id) {
//            btnDate.setEnabled(true);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(false);
            cbTotal1.setChecked(true);
            cbTotal2.setChecked(false);
            cbTotal3.setChecked(false);
            cbTotal4.setChecked(false);
            cbTotal5.setChecked(false);
        } else if (cbTotal2.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(true);
//            btnYear.setEnabled(false);
            cbTotal2.setChecked(true);
            cbTotal3.setChecked(false);
            cbTotal1.setChecked(false);
            cbTotal4.setChecked(false);
            cbTotal5.setChecked(false);
        } else if (cbTotal3.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbTotal3.setChecked(true);
            cbTotal1.setChecked(false);
            cbTotal2.setChecked(false);
            cbTotal4.setChecked(false);
            cbTotal5.setChecked(false);
        }else if (cbTotal4.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbTotal3.setChecked(false);
            cbTotal1.setChecked(false);
            cbTotal2.setChecked(false);
            cbTotal4.setChecked(true);
            cbTotal5.setChecked(false);
        }else if (cbTotal5.getId() == id) {
//            btnDate.setEnabled(false);
//            btnMonth.setEnabled(false);
//            btnYear.setEnabled(true);
            cbTotal3.setChecked(false);
            cbTotal1.setChecked(false);
            cbTotal2.setChecked(false);
            cbTotal4.setChecked(false);
            cbTotal5.setChecked(true);
        }
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
        cbTotal1 = findViewById(R.id.cbTotal1);
        cbTotal2 = findViewById(R.id.cbTotal2);
        cbTotal3 = findViewById(R.id.cbTotal3);
        cbTotal4 = findViewById(R.id.cbTotal4);
        cbTotal5 = findViewById(R.id.cbTotal5);
        description = findViewById(R.id.description);
        textView6 = findViewById(R.id.textView6);
        textView8 = findViewById(R.id.textView8);
        submit = findViewById(R.id.submit);
    }
}