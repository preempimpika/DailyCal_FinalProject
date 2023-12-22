package com.example.cs361_project;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class UserActivity extends AppCompatActivity {

    RadioButton male, female;
    RadioGroup rdGroup;
    EditText age,weight,height;
    TextView bmr;
    Button save;
    String user="0";
    AlertDialog.Builder builder;
    double h=0,w=0,result=0,a=0;
    double bstart=0;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String AGE = "age";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String BMR = "bmr";
    public static final String MALE = "male";
    public static final String FEMALE = "female";

    private String age1,weight1,height1,bmr1;
    private boolean male1,female1;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        //back
        ImageButton back = findViewById(R.id.backuser);
        builder = new AlertDialog.Builder(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Alert!")
                                .setMessage("Are you sure you want to exit?")
                                        .setCancelable(true)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        openMainActivity();
                                                    }
                                                })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                            }
                                                        })
                        .show();
            }
        });

        //calculate bmr
        save = findViewById(R.id.saveUser);
        male = findViewById(R.id.checkMale);
        female = findViewById(R.id.checkFemale);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        bmr = findViewById(R.id.bmr);
        rdGroup = findViewById(R.id.rdGroup);


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                if(checked){
                    user="Male";
                }
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                if(checked){
                    user="Female";
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1=height.getText().toString();
                String str2=weight.getText().toString();
                String str3=age.getText().toString();
                if(user.equals("0")){
                    Toast.makeText(UserActivity.this,"Select your Biological Sex",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str1)){
                    height.setError("Type Height");
                    height.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(str2)){
                    weight.setError("Type Weight");
                    weight.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(str3)){
                    age.setError("Type Age");
                    age.requestFocus();
                    return;
                }else{
                    calculatebmr();
                }

                saveData();

            }


        });

        loadData();
        updateView();


    }


    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int genderSP = sharedPreferences.getInt("genderSP",3);

        editor.putString(AGE, age.getText().toString());
        editor.putString(WEIGHT, weight.getText().toString());
        editor.putString(HEIGHT, height.getText().toString());
        editor.putString(BMR, bmr.getText().toString());
        editor.putBoolean(MALE,male.isChecked());
        editor.putBoolean(FEMALE,female.isChecked());

        if(genderSP == 1){
            male.setChecked(true);
        }else if(genderSP == 0){
            female.setChecked(true);
        }

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.checkMale){
                    editor.putInt("genderSP",1);
                }else if(i == R.id.checkFemale){
                    editor.putInt("genderSP", 0);
                }
            }
        });

        editor.apply();

        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        age1 = sharedPreferences.getString(AGE, "");
        weight1 = sharedPreferences.getString(WEIGHT, "");
        height1 = sharedPreferences.getString(HEIGHT, "");
        bmr1 = sharedPreferences.getString(BMR, "");
        male1 = sharedPreferences.getBoolean(MALE, false);
        female1 = sharedPreferences.getBoolean(FEMALE, false);

    }

    public void updateView(){
        age.setText(age1);
        weight.setText(weight1);
        height.setText(height1);
        bmr.setText(bmr1);
        male.setChecked(male1);
        female.setChecked(female1);
    }

    //calculate function
    public void calculatebmr(){

        h=Double.parseDouble(height.getText().toString());
        w=Double.parseDouble(weight.getText().toString());
        a=Double.parseDouble(age.getText().toString());
        if(user.equals("Male")){
            result=(66+(13.7*w)+(5*h)-(6.8*a));
            bmr.setText(Double.toString(result));
            bmr.setText(new DecimalFormat("####").format(result)+"");
        }
        if(user.equals("Female")){
            result=(665+(9.6*w)+(1.8*h)-(4.7*a));
            bmr.setText(Double.toString(result));
            bmr.setText(new DecimalFormat("####").format(result)+"");
        }

    }
    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
