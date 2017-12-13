package ru.ksu.motygullin.geofish.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.ksu.motygullin.geofish.R;

public class InputInfoActivity extends AppCompatActivity {

    Button btn_next;
    TextInputLayout wrapper;
    EditText name;
    EditText surname;
    EditText password;
    Context context = this;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferences = context.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        password = findViewById(R.id.password);
        wrapper = findViewById(R.id.password_wrapper);

        if (!preferences.getString("name", "").equals("") && !preferences.getString("surname", "").equals("")) {
            name.setText(preferences.getString("name", ""));
            surname.setText(preferences.getString("surname", ""));
        }


        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().length() >= 6) {
                    Intent intent = new Intent(context, RegistrationFinishActivity.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", name.getText().toString());
                    editor.putString("surname", surname.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.apply();
                    startActivity(intent);
                    finish();
                } else {
                    wrapper.setError("Пароль должен содержать минимум 6 символов");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
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
