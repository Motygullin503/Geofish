package ru.ksu.motygullin.geofish.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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
    Context context = this;
    SharedPreferences preferences;
    boolean isAuthorizedFromSocials = false;


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
        preferences = context.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);

        if (!preferences.getString("socials", "").equals("")){
            isAuthorizedFromSocials = true;
        }

        if (!preferences.getString("birthday", "").equals("") && !preferences.getString("city", "").equals("")) {

            birthday.setText(preferences.getString("birthday", ""));
            city.setText(preferences.getString("city", ""));

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

                if (isAuthorizedFromSocials){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("birthday", birthday.getText().toString());
                    editor.putString("city", city.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(context, PhotoSelectionActivity.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("birthday", birthday.getText().toString());
                    editor.putString("city", city.getText().toString());
                    editor.apply();
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, InputInfoActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, InputInfoActivity.class);
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
    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
