package com.example.d308.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.Date;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sample) {
            repository= new Repository(getApplication());
            Vacation vacation= new Vacation(0, "Hawaii", "The good one", LocalDate.of( 2023, 9 , 19 ), LocalDate.of(2023, 9, 26));
            repository.insert(vacation);
// you cant use localDate...you need to convert them and rename the fields that currently use it. Shouldnt take long.
            Excursion excursion1 = new Excursion(0, "biking", Date.from(LocalDate.of(2023, 9, 25)).toInstant(), vacation.getVacationID());
            Excursion excursion2 = new Excursion(0, "Watch a show", LocalDate.of(2023, 9, 23), vacation.getVacationID());
            repository.insert(excursion1);
            repository.insert(excursion2);

            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return true;
    }
}