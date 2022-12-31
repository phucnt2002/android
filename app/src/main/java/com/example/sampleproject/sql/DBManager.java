package com.example.sampleproject.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sampleproject.TimeUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DBManager extends SQLiteOpenHelper {
    private static DBManager instance = null;
    private final String TAG = "DBManager";
    private static final String DATABASE_NAME = "DBManager.sqlite";
    public static final String TABLE_WEATHER = "weather";

    private static final String ID = "id";
    private static final String TIME = "time";
    private static final String TEMP = "temp_device";
    private static final String HUMIDITY = "humidity";
    private static final String SPEED = "speed";
    private static final String DEG = "deg";


    private String SQLQueryWeather = "CREATE TABLE " + TABLE_WEATHER + " ("
            + ID + " TEXT, "
            + TIME + " TEXT, "
            + TEMP + " TEXT, "
            + HUMIDITY + " TEXT, "
            + SPEED + " TEXT, "
            + DEG + " TEXT)";

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DBManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null)
            instance = new DBManager(context);
    }


    private SQLiteOpenHelper openHelper;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLQueryWeather);
        Log.d(TAG, "onCreate: ");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: ");
    }

    // Delete
    public int delete(String table, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, ID + "=?", new String[]{String.valueOf(id)});
    }

    // Xóa bảng
    public void deleteTable(String table) {
        Log.e("deleteTable ", table);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }

    public ContentValues putweather(DataWeatherModel weatherModel) {
        ContentValues values = new ContentValues();
        values.put(ID, weatherModel.id);
        values.put(TIME, String.valueOf(weatherModel.time));
        values.put(TEMP, weatherModel.temp);
        values.put(HUMIDITY, String.valueOf(weatherModel.humidity));
        values.put(SPEED, String.valueOf(weatherModel.speed));
        values.put(TIME, String.valueOf(weatherModel.time));
        values.put(DEG, String.valueOf(weatherModel.deg));
        return values;
    }

    public ArrayList<DataWeatherModel> getWeather() {
        ArrayList<DataWeatherModel> weathers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER, null);
        if (cursor.moveToFirst()) {
            do {
                DataWeatherModel dataWeatherModel = new DataWeatherModel(
                        cursor.getString(0), Long.parseLong(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5));
                weathers.add(dataWeatherModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return weathers;
    }

    public ArrayList<DataWeatherModel> getWeatherByDate(long timeInMillis, String id) {
        String date = TimeUtils.formatLongToDate(timeInMillis);
        ArrayList<DataWeatherModel> weathers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER + " WHERE " + ID + "='" + id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                long time = Long.parseLong(cursor.getString(1));
                String dateDb = TimeUtils.formatLongToDate(time);
                if (dateDb.endsWith(date)) {
                    DataWeatherModel dataWeatherModel = new DataWeatherModel(
                            cursor.getString(0),
                            time,
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5));

                    weathers.add(dataWeatherModel);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return weathers;
    }

    public ArrayList<DataWeatherModel> getWeatherByMonth(String date, String id) {
        ArrayList<DataWeatherModel> weathers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER + " WHERE " + ID + "='" + id + "'", null);

        if (cursor.moveToFirst()) {
            do {
                long time = Long.parseLong(cursor.getString(1));
                String dateDb = TimeUtils.formatLongToMonth(time);
                if (dateDb.endsWith(date)) {
                    DataWeatherModel dataWeatherModel = new DataWeatherModel(
                            cursor.getString(0),
                            time,
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5));
                    weathers.add(dataWeatherModel);
                }
            } while (cursor.moveToNext());
        }

        //Lấy object dữ liệu cuối ngày nếu chọn biểu đồ theo tháng
        String dateOld = "";
        ArrayList<DataWeatherModel> weathers2 = new ArrayList<>();
//        weathers2.addAll(weathers);
        for (DataWeatherModel dataWeatherModel : weathers) {
            //dd/MM/yyyy thời gian lưu trong SQlite
            String timeData = TimeUtils.formatLongToDate(dataWeatherModel.time);
            //nếu trong dateOld có tồn tại timeData phía cuối, thì chèn dataWeatherModel vào vị trí weathers2.size() - 1
            //ngược lại thêm vào cuối weathers2
            if (dateOld.endsWith(timeData))
                weathers2.set(weathers2.size() - 1, dataWeatherModel);
            else weathers2.add(dataWeatherModel);
//                weathers2.remove(dataWeatherModel);
            dateOld = timeData;
        }

        db.close();
        return weathers2;
    }

    public ArrayList<DataWeatherModel> getWeatherByYear(String date) {
        ArrayList<DataWeatherModel> weathers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER, null);

        if (cursor.moveToFirst()) {
            do {
                long time = Long.parseLong(cursor.getString(1));
                String dateDb = TimeUtils.formatLongToYear(time);
                if (dateDb.endsWith(date)) {
                    DataWeatherModel dataWeatherModel = new DataWeatherModel(
                            cursor.getString(0), time,
                            cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5));
                    weathers.add(dataWeatherModel);
                }
            } while (cursor.moveToNext());
        }

        //Lấy ngày cuối cùng trong tháng nếu vẽ biểu đồ theo ngày
        String yearOld = "";
        ArrayList<DataWeatherModel> weathers2 = new ArrayList<>();
        for (DataWeatherModel dataWeatherModel : weathers) {
            String time = TimeUtils.formatLongToMonth(dataWeatherModel.time);
            if (yearOld.endsWith(time))
                weathers2.set(weathers2.size() - 1, dataWeatherModel);
            else weathers2.add(dataWeatherModel);
//                weathers2.remove(dataWeatherModel);
            yearOld = time;
        }
        db.close();
        return weathers2;
    }

    // Thêm dữ liệu
    public void insertWeather(DataWeatherModel weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = putweather(weather);
        db.insert(TABLE_WEATHER, null, values);
        db.close();
    }

    public void updateWeather(DataWeatherModel weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = putweather(weather);
        db.update(TABLE_WEATHER, values, TIME + "=?", new String[]{String.valueOf(weather.time)});
        db.close();
    }

    public void addWeather(DataWeatherModel weather) {

        ContentValues values = putweather(weather);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_WEATHER, null, values);
        db.close();
    }

    private boolean isUpdate(DataWeatherModel weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER + " WHERE " + TIME + "=" + weather.time, null);

        boolean isAccesst = false;
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(1);
                if (id.endsWith(weather.id))
                    isAccesst = true;
            } while (cursor.moveToNext());
        }
        db.close();
        return isAccesst;
    }


}
