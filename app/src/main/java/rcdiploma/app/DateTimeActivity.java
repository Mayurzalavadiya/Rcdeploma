package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimeActivity extends AppCompatActivity {

    EditText dateText, timeText;

    Calendar calendar;

    int iHour, iMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        dateText = findViewById(R.id.date_edittext);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat sDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                dateText.setText(sDateFormat.format(calendar.getTime()));
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DateTimeActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeText = findViewById(R.id.time_edittext);

        TimePickerDialog.OnTimeSetListener timeClick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                iHour = i;
                iMin = i1;

                String sAMPM = "";
                if(iHour>12){
                    iHour -=12;
                    sAMPM = "PM";
                }
                else if(iHour==0){
                    iHour +=12;
                    sAMPM = "AM";
                }
                else if(iHour<12){
                    sAMPM = "AM";
                }
                else{
                    sAMPM = "PM";
                }

                String sHour = "";
                if(iHour<10){
                    sHour = "0"+iHour;
                }
                else{
                    sHour = String.valueOf(iHour);
                }

                String sMinute = "";
                if (iMin < 10) {
                    sMinute = "0" + iMin;
                } else {
                    sMinute = String.valueOf(iMin);
                }

                timeText.setText(sHour + " : " + sMinute+" "+sAMPM);
            }
        };

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(DateTimeActivity.this, timeClick, iHour, iMin, false).show();
            }
        });

    }
}