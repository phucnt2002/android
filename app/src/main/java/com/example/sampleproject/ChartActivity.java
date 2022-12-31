package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sampleproject.sql.DBManager;
import com.example.sampleproject.sql.DataWeatherModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class ChartActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvChartTemp;
    private TextView tvHumidity;
    private TextView tvCharSpeed;
    private LinearLayout viewLoading;
    private LineChart chartTemp;
    private LineChart chartSpeed;
    private LineChart chartHumidity;
    private String id;
    private Button btnDate, btnMonth, btnYear;
    private CheckBox checkboxDate, checkboxMonth, checkboxYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        checkboxDate = findViewById(R.id.checkboxDate);
        checkboxMonth = findViewById(R.id.checkboxMonth);
        checkboxYear = findViewById(R.id.checkboxYear);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            CustomIntent.customType(this, "left-to-right");
            finish();
        });
        tvChartTemp = findViewById(R.id.tvChartTemp);
        tvCharSpeed = findViewById(R.id.tvCharSpeed);
        chartSpeed = findViewById(R.id.chartSpeed);
        chartHumidity = findViewById(R.id.chartHumidity);
        btnMonth = findViewById(R.id.btnMonth);
        btnYear = findViewById(R.id.btnYear);
        btnDate = findViewById(R.id.btnDate);
        tvHumidity = findViewById(R.id.tvHumidity);
        viewLoading = findViewById(R.id.viewLoading);
        chartTemp = findViewById(R.id.chartTemp);
        checkboxDate.setChecked(true);
        Calendar calendar = Calendar.getInstance();
        id = getIntent().getStringExtra("id");

        btnDate.setOnClickListener(view -> {

            new DatePickerDialog(this, (viewDatePickerDialog, year, monthOfYear, dayOfMonth) -> {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                btnDate.setText(TimeUtils.formatLongToDate(calendar.getTimeInMillis()));

                ArrayList<DataWeatherModel> weatherDatas = DBManager.getInstance().getWeatherByDate(calendar.getTimeInMillis(),id);
                setDataDb(weatherDatas);

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnMonth.setOnClickListener(view -> {
            MonthYearPickerDialog pd = new MonthYearPickerDialog();
            pd.setListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                    String dateMonth = month + "/" + year;
                    btnMonth.setText(dateMonth);
                    ArrayList<DataWeatherModel> weatherDatas = DBManager.getInstance().getWeatherByMonth(dateMonth,id);
                    KLog.json(new Gson().toJson(weatherDatas));
                    setDataDb(weatherDatas);
                }
            });
            pd.show(getSupportFragmentManager(), "MonthYearPickerDialog");
        });

        btnYear.setOnClickListener(view -> {
            MonthYearPickerDialog pd = new MonthYearPickerDialog();
            pd.type = 1;
            pd.setListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                    btnYear.setText(String.valueOf(year));
                    ArrayList<DataWeatherModel> weatherDatas = DBManager.getInstance().getWeatherByYear(String.valueOf(year));
                    KLog.json(new Gson().toJson(weatherDatas));
                    setDataDb(weatherDatas);
                }
            });
            pd.show(getSupportFragmentManager(), "MonthYearPickerDialog");
        });

        checkboxDate.setOnClickListener(view -> checkBoxEvent(view.getId()));
        checkboxMonth.setOnClickListener(view -> checkBoxEvent(view.getId()));
        checkboxYear.setOnClickListener(view -> checkBoxEvent(view.getId()));

        //mặc định khi vẽ biễu đồ checkbox vào ngày
        checkBoxEvent(checkboxDate.getId());
        btnDate.setText(TimeUtils.formatLongToDate(calendar.getTimeInMillis()));
        //lấy dữ liệu theo ngày hiện tại từ database
        ArrayList<DataWeatherModel> weatherDatas = DBManager.getInstance().getWeatherByDate(calendar.getTimeInMillis(),id);
        setDataDb(weatherDatas);
    }

    private void setDataDb(ArrayList<DataWeatherModel> weatherDatas) {
        if (weatherDatas.size() > 0) {
            chartTemp.setVisibility(View.VISIBLE);
            chartHumidity.setVisibility(View.VISIBLE);
            chartSpeed.setVisibility(View.VISIBLE);
            String[] mDays = new String[weatherDatas.size()];
            float[] mDataTemp = new float[weatherDatas.size()];
            float[] mDataHumidity = new float[weatherDatas.size()];
            float[] mDataSpeed = new float[weatherDatas.size()];


            for (int i = 0; i < weatherDatas.size(); i++) {
                if (checkboxDate.isChecked())
                    mDays[i] = TimeUtils.formatLongToHour(weatherDatas.get(i).time);
                else if (checkboxMonth.isChecked())
                    mDays[i] = TimeUtils.formatLongToDate(weatherDatas.get(i).time);
                else if (checkboxYear.isChecked())
                    mDays[i] = TimeUtils.formatLongToMonth(weatherDatas.get(i).time);

                mDataTemp[i] = Float.parseFloat(weatherDatas.get(i).temp);
                mDataHumidity[i] = Float.parseFloat(weatherDatas.get(i).humidity);
                mDataSpeed[i] = Float.parseFloat(weatherDatas.get(i).speed);
//                Log.d("mDataTemp", mDataTemp[i]+"");
//                Log.d("mDataHumidity", mDataHumidity[i]+"");
//                Log.d("mDataSpeed", mDataSpeed[i]+"");

            }
            setDataChart(chartTemp, mDays, mDataTemp, "°C");
            setDataChart(chartHumidity, mDays, mDataHumidity, "%");
            setDataChart(chartSpeed, mDays, mDataSpeed, "km/h");
        } else {
            chartTemp.setVisibility(View.INVISIBLE);
            chartHumidity.setVisibility(View.INVISIBLE);
            chartSpeed.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Dữ liệu trống !", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBoxEvent(int id) {
        if (checkboxDate.getId() == id) {
            btnDate.setEnabled(true);
            btnMonth.setEnabled(false);
            btnYear.setEnabled(false);
            checkboxDate.setChecked(true);
            checkboxMonth.setChecked(false);
            checkboxYear.setChecked(false);
        } else if (checkboxMonth.getId() == id) {
            btnDate.setEnabled(false);
            btnMonth.setEnabled(true);
            btnYear.setEnabled(false);
            checkboxMonth.setChecked(true);
            checkboxYear.setChecked(false);
            checkboxDate.setChecked(false);
        } else if (checkboxYear.getId() == id) {
            btnDate.setEnabled(false);
            btnMonth.setEnabled(false);
            btnYear.setEnabled(true);
            checkboxYear.setChecked(true);
            checkboxDate.setChecked(false);
            checkboxMonth.setChecked(false);
        }
    }



    private void setDataChart(LineChart chart, String[] mDays, float[] dataChart, String unit) {

        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //Khoảng cách của các điểm x
        xAxis.setGranularity(2f);
        xAxis.setLabelCount(dataChart.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mDays));

//        YAxis rightAxis = chartTemp.getAxisRight();
//        rightAxis.setEnabled(false);


        ArrayList<Entry> barEntries = new ArrayList<>();

        for (int j = 0; j < dataChart.length; j++) {
            barEntries.add(new BarEntry(j, dataChart[j]));
        }
        LineDataSet barDataSet = new LineDataSet(barEntries, "Đơn vị đo: " + unit);
        barDataSet.setDrawIcons(false);
        barDataSet.setColors(ContextCompat.getColor(this, R.color.colorAccent));
        barDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorGreen));
        barDataSet.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(barDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);
        // hien thi so du lieu tren bieu do
        chart.setVisibleXRangeMaximum(10);
        chart.getXAxis().setSpaceMax(1);
        chart.setEnabled(false);
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        chart.invalidate();
    }

}