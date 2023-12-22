package com.example.cs361_project;

import static com.example.cs361_project.DBHelper.TABLENAME;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class AddMealActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText foodname, calories;
    Button savemeal,edit;
    AlertDialog.Builder builder;
    int id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmeal_layout);

        ImageButton back = findViewById(R.id.backaddmeal);
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
        //addmeal insert
        dbHelper = new DBHelper(this);
        findid();
        edit.setVisibility(View.GONE);
        savemeal.setVisibility(View.VISIBLE);
        insertData();
        editData();


    }

    private void editData() {
        if(getIntent().getBundleExtra("mealdata")!=null){
            Bundle bundle = getIntent().getBundleExtra("mealdata");
            id = bundle.getInt("id");
            foodname.setText(bundle.getString("foodname"));
            calories.setText(bundle.getString("calories"));

            edit.setVisibility(View.VISIBLE);
            savemeal.setVisibility(View.GONE);
        }
    }

    //end Oncreate

    private void findid() {
        foodname = findViewById(R.id.foodname_input);
        calories = findViewById(R.id.calories_input);
        savemeal = findViewById(R.id.savemealbtn);
        edit = findViewById(R.id.edit);
    }


    private void insertData(){
        savemeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1=foodname.getText().toString();
                String str2=calories.getText().toString();

                if(TextUtils.isEmpty(str1)){
                    foodname.setError("Type Food Name");
                    foodname.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(str2)){
                    calories.setError("Type Calories");
                    calories.requestFocus();
                    return;
                }

                ContentValues cv = new ContentValues();
                cv.put("foodname", foodname.getText().toString());
                cv.put("calories", calories.getText().toString());
                sqLiteDatabase = dbHelper.getWritableDatabase();
                Long recinsert = sqLiteDatabase.insert(TABLENAME, null, cv);
                if(recinsert!=null){
                    Toast.makeText(AddMealActivity.this, "inserted successfully", Toast.LENGTH_SHORT).show();
                    foodname.setText("");
                    calories.setText("");
                    Intent intent = new Intent(AddMealActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(AddMealActivity.this,"something wrong try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put("foodname", foodname.getText().toString());
                cv.put("calories", calories.getText().toString());
                sqLiteDatabase = dbHelper.getWritableDatabase();
                long recedit = sqLiteDatabase.update(TABLENAME, cv, "id="+id,null);
                if(recedit!=-1){
                    Toast.makeText(AddMealActivity.this, "edited successfully", Toast.LENGTH_SHORT).show();
                    foodname.setText("");
                    calories.setText("");
                    Intent intent = new Intent(AddMealActivity.this, MainActivity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(AddMealActivity.this,"something wrong try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
