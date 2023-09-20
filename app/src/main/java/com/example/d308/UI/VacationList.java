package com.example.d308.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sample) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 20);

            Date startDate = calendar.getTime();

            calendar.set(Calendar.DAY_OF_MONTH, 30);

            Date endDate = calendar.getTime();

            repository= new Repository(getApplication());
            Vacation vacation= new Vacation(0, "Hawaii", "The good one", startDate, endDate);
            repository.insert(vacation);

            calendar.set(Calendar.DAY_OF_MONTH, 22);

            Date bikingDate = calendar.getTime();

            calendar.set(Calendar.DAY_OF_MONTH, 22);

            Date showDate = calendar.getTime();

            Excursion excursion1 = new Excursion(0, "biking", bikingDate, vacation.getVacationID());
            Excursion excursion2 = new Excursion(0, "Watch a show",showDate, vacation.getVacationID());
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