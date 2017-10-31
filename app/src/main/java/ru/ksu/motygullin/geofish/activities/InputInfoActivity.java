package ru.ksu.motygullin.geofish.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    Bundle userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (getIntent().getExtras() != null) {

            userData = getIntent().getBundleExtra("userData");

            if (userData.getString("name")!=null){
                name.setText(userData.getString("name"));
            }

            if (userData.getString("surname")!=null){
                surname.setText(userData.getString("surname"));
            }
        }



        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        password = findViewById(R.id.password);
        wrapper = findViewById(R.id.password_wrapper);


        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().length() >= 6) {
                    Intent intent = new Intent(context, RegistrationFinishActivity.class);
                    userData.putString("name", name.getText().toString());
                    userData.putString("surname", surname.getText().toString());
                    userData.putString("password", password.getText().toString());
                    intent.putExtra("userData", userData);
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
        startActivity(intent, userData);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent, userData);
        finish();
    }
}
