package com.example.d308.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.d308.R;

public class VacationList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);
        System.out.println(getIntent().getStringExtra("test"));
    }
}