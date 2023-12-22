package com.example.cs361_project;

import static com.example.cs361_project.DBHelper.TABLENAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener, OnItemDeletedListener {
    RecyclerView recyclerView;
    Adapter adapter;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BMR = "bmr";
    TextView caleat, calleft, burned;
    ProgressBar prog;
    private SensorManager sensorManager = null;
    private Sensor stepSensor;
    private boolean isCounterSensorPresent;
    private int totalSteps = 0, burnStep=0, previewsTotalSteps=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //date
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.date);
        textViewDate.setText(currentDate);

        //recycleview
        dbHelper = new DBHelper(this);
        findid();
        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //onclick user
        ImageButton userbutton = findViewById(R.id.userbutton);
        userbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserActivity();
                }
            });

        //onclick info
        ImageButton info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAlertDialog();
            }
            private void CreateAlertDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Member");
                builder.setMessage("Pimpika Dejprapatsorn"+"\n"+"6309681218"+"\n"+"Natchaya Pawapongsupat"+"\n"+"6309681457"+"\n"+"Fahsai Pethsai"+"\n"+"6309681580");
                builder.setPositiveButton("OK",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //onclick add
        ImageButton addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMealActivity();
            }
        });

        //show sumCalEaten
        adapter.setOnItemDeletedListener(this);
        displaySumCal();

        /*caleat = findViewById(R.id.caleat);
        String totalCal = dbHelper.sumCal();
        //caleat.setText(String.format(String.valueOf(dbHelper.sumCal())));]
        caleat.setText(totalCal);
        String sumEat = caleat.getText().toString();
        double sumEatnum = 0;
        sumEatnum = Double.parseDouble(sumEat);

        //show calLeft
        calleft = findViewById(R.id.calleft);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String bmr = sharedPreferences.getString(BMR, null);
        prog = findViewById(R.id.progressBar);
        if(bmr!=null){
            calleft.setText(bmr);
            String sumLeft = calleft.getText().toString();
            double calLeftnum = Double.parseDouble(sumLeft);
            double sumLeftnum = 0;
            sumLeftnum = calLeftnum-sumEatnum;
            calleft.setText(Double.toString(sumLeftnum));
            calleft.setText(new DecimalFormat("####").format(sumLeftnum)+"");

            double Progress = 0;
            Progress = (sumEatnum / calLeftnum)*100;
            prog.setProgress((int)Progress);
        }*/

        //progressbar
        prog = findViewById(R.id.progressBar);
        /*double Progress = 0;
        Progress = (sumEatnum / calLeftnum)*100;
        prog.setProgress((int)Progress);*/


        //burned step
        burned = findViewById(R.id.calburn);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        }else{
            isCounterSensorPresent = false;
        }
        //saveData();
       //loadData();
    }

    private void displaySumCal() {
       caleat = findViewById(R.id.caleat);
        String totalCal = dbHelper.sumCal();
        //caleat.setText(String.format(String.valueOf(dbHelper.sumCal())));]
        caleat.setText(totalCal);
        String sumEat = caleat.getText().toString();
        double sumEatnum = 0;
        sumEatnum = Double.parseDouble(sumEat);

        //show calLeft
        calleft = findViewById(R.id.calleft);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String bmr = sharedPreferences.getString(BMR, null);
        prog = findViewById(R.id.progressBar);
        if(bmr!=null){
            calleft.setText(bmr);
            String sumLeft = calleft.getText().toString();
            double calLeftnum = Double.parseDouble(sumLeft);
            double sumLeftnum = 0;
            sumLeftnum = calLeftnum-sumEatnum;
            calleft.setText(Double.toString(sumLeftnum));
            calleft.setText(new DecimalFormat("####").format(sumLeftnum)+"");

            double Progress = 0;
            Progress = (sumEatnum / calLeftnum)*100;
            prog.setProgress((int)Progress);
        }

    }



    private void displayData() {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select *from "+TABLENAME+"", null);
        ArrayList<AddMealData>mealArrayList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String foodname = cursor.getString(1);
            String calories = cursor.getString(2);
            mealArrayList.add(new AddMealData(id, foodname, calories));
        }
        cursor.close();
        adapter = new Adapter(this,R.layout.custom_viewmain,mealArrayList,sqLiteDatabase);
        recyclerView.setAdapter(adapter);
    }


    private void findid() {
        recyclerView = findViewById(R.id.recycleView);
    }

    public  void openUserActivity(){
        Intent intent = new Intent(this,UserActivity.class);
        startActivity(intent);
    }

    public  void openAddMealActivity(){
            Intent intent = new Intent(this,AddMealActivity.class);
            startActivity(intent);
    }

    protected void onResume(){
        super.onResume();

        /*if(stepSensor == null){
            Toast.makeText(this,"this device has no sensor",Toast.LENGTH_SHORT).show();
        }else {
            sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }*/

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        //sensorManager.unregisterListener(this);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this, stepSensor);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int)sensorEvent.values[0];
            int currentSteps = totalSteps-previewsTotalSteps;
            burned.setText(String.valueOf(currentSteps));
        }*/

        if(sensorEvent.sensor == stepSensor){
            totalSteps = (int) sensorEvent.values[0];
            burned.setText(String.valueOf(totalSteps));
        }
    }

    /*private void saveData(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString("key1",String.valueOf(previewsTotalSteps));
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences2 = getSharedPreferences("myPref", MODE_PRIVATE);
        int saveNumber = (int)sharedPreferences2.getFloat("key1",0f);
        previewsTotalSteps = saveNumber;
    }*/

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onItemDeleted() {
        displaySumCal();
    }
}