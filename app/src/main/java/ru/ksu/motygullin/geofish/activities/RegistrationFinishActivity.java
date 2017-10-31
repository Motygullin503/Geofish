package ru.ksu.motygullin.geofish.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import ru.ksu.motygullin.geofish.R;

public class RegistrationFinishActivity extends AppCompatActivity {

    static EditText birthday;
    EditText city;
    Button finish;
    Bundle userData;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_finish);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        birthday = findViewById(R.id.birthday);
        city = findViewById(R.id.city);

        if (getIntent().getBundleExtra("userData") != null) {
            userData = getIntent().getBundleExtra("userData");
            if (userData.getString("birthday")!=null){
                birthday.setText(userData.getString("birthday"));
            }

            if (userData.getString("city")!=null){
                city.setText(userData.getString("city"));
            }
        }



        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Выберите дату рождения");
            }
        });

        finish = findViewById(R.id.btn_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotoSelectionActivity.class);
                userData.putString("birthday", birthday.getText().toString());
                userData.putString("city", city.getText().toString());
                startActivity(intent, userData);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, InputInfoActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, InputInfoActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        finish();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            birthday.setText(new StringBuilder().append(day).append(".").append(month + 1)
                    .append(".").append(year));
        }
    }
}
