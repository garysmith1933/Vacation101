package com.example.d308.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.Toast;

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
    Vacation currentVacation;
    int numExcursions;
    String dateFormat = "E MMM dd HH:mm:ss zzz yyyy";
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        editName = findViewById(R.id.titletext);
        editHotelName = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

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
                String startDateInfo = getIntent().getStringExtra("startDate");

                if (startDateInfo == "" || startDateInfo == null) {
                    startDateInfo = formatter.format(new Date());
                }

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
                if (endDateInfo == null || endDateInfo.length() == 0) {
                    endDateInfo = formatter.format(new Date());
                    Log.d("enddDate", Integer.toString(editEndDate.length()));
                }

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
                intent.putExtra("vacationID", vacationID);
                intent.putExtra("startDate", getIntent().getStringExtra("startDate"));
                intent.putExtra("endDate", getIntent().getStringExtra("endDate"));
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
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("tag", calStart.getTime().toString());
        editStartDate.setText(sdf.format(calStart.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "MM/dd/yy";
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
            if ( !calStart.getTime().before(calEnd.getTime())) {
                Toast.makeText(VacationDetails.this, "ERROR: The end date must be after the start date.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (vacationID == -1) {
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size()-1).getVacationID()+1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotelName.getText().toString(), calStart.getTime(), calEnd.getTime());
                repository.insert(vacation);
            }

            else {
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotelName.getText().toString(), calStart.getTime(), calEnd.getTime());
                repository.update(vacation);
                this.finish();
            }
        }

        if (item.getItemId()== R.id.vacationDelete) {
            for (Vacation vacation : repository.getmAllVacations()) {
                if (vacation.getVacationID() == vacationID) currentVacation = vacation;
            }

            numExcursions = 0;
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }

            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getTitle() + " was deleted.", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Vacations with set excursions cannot be deleted.", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        if (item.getItemId()== R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Vacation Name: " + editName.getText().toString() + "\n"
                    + "Hotel Name: " + editHotelName.getText().toString() + "\n"
                    + "Start Date: " + formatter.format(calStart.getTime()) + " \n"
                    + "End Date " + formatter.format(calEnd.getTime())
            );
            sendIntent.putExtra(Intent.EXTRA_TITLE, editName.getText().toString() + "Details");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }

        if (item.getItemId() == R.id.notify) {
            String startDateText = editStartDate.getText().toString();
            String endDateText = editEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            try {
                Log.d("date", startDateText);
                Date startDate = sdf.parse(startDateText);
                Date endDate = sdf.parse(endDateText);

                createNotification(startDate, "V1", currentVacation.getTitle());
                createNotification(endDate, "V2", currentVacation.getTitle());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotification(Date date, String notificationType, String vacationName) {
        try {
            long trigger = date.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("Date Alert", notificationType);
            intent.putExtra("title", vacationName);
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = repository.getmAssociatedExcursions(vacationID);

        excursionAdapter.setExcursions(filteredExcursions);
    }
}
