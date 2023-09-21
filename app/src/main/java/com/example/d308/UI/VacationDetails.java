package com.example.d308.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    String name;
    String hotel;
    int vacationID;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;

    final Calendar calStart = Calendar.getInstance();
    final Calendar calEnd = Calendar.getInstance();

    EditText editName;
    EditText editHotelName;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        editName = findViewById(R.id.titletext);
        editHotelName = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);
        String dateFormat = "E MMM dd HH:mm:ss zzz yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);

        name = getIntent().getStringExtra("name");
        editName.setText(name);

        hotel = getIntent().getStringExtra("hotel");
        editHotelName.setText(hotel);

        vacationID = getIntent().getIntExtra("id", -1);
        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calStart.set(Calendar.YEAR, year);
                calStart.set(Calendar.MONTH, month);
                calStart.set(Calendar.DAY_OF_MONTH, day);

                updateStartDate();
            }
        };

        editStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //you get it from the intent, you pass the info down.
                //get value from other screen,but I'm going to hard code it right now
                String startDateInfo = getIntent().getStringExtra("startDate");
                Log.d("tag", startDateInfo);
                if (startDateInfo.equals("")) startDateInfo = "02/10/22";

                try {
                    calStart.setTime(formatter.parse(startDateInfo));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(VacationDetails.this, startDate, calStart
                        .get(Calendar.YEAR), calStart.get(Calendar.MONTH),
                        calStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calEnd.set(Calendar.YEAR, year);
                calEnd.set(Calendar.MONTH, month);
                calEnd.set(Calendar.DAY_OF_MONTH, day);

                updateEndDate();
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String endDateInfo = getIntent().getStringExtra("endDate");
                Log.d("tag", endDateInfo);
                if (endDateInfo.equals("")) endDateInfo = "02/10/22";

                try {
                    calEnd.setTime(formatter.parse(endDateInfo));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(VacationDetails.this, endDate, calEnd
                        .get(Calendar.YEAR), calEnd.get(Calendar.MONTH),
                        calEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository((getApplication()));
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    private void updateStartDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("tag", calStart.getTime().toString());
        editStartDate.setText(sdf.format(calStart.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("tag", calEnd.getTime().toString());
        editEndDate.setText(sdf.format(calEnd.getTime()));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            this.finish();
            return true;
        }
        if(item.getItemId() == R.id.vacationSave ) {
            Vacation vacation;
            if (vacationID == -1) {
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size()-1).getVacationID()+1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotelName.getText().toString(), calStart.getTime(), calStart.getTime());
                repository.insert(vacation);
            }

            else {
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotelName.getText().toString(), calEnd.getTime(), calEnd.getTime());
                repository.update(vacation);
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


//need to do the same thing for excursion, being able to add it and update it.