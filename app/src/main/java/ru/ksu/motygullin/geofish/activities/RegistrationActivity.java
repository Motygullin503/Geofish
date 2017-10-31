package ru.ksu.motygullin.geofish.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.ksu.motygullin.geofish.R;

public class RegistrationActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    EditText email;
    TextInputLayout wrapper;
    Context context = this;
    Button btn_next;
    Bundle userData;
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userData = new Bundle();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        email = findViewById(R.id.email);
        wrapper = findViewById(R.id.email_wrapper);
        btn_next = findViewById(R.id.btn_next);

        if (getIntent().getBundleExtra("userData") != null) {
            userData = getIntent().getBundleExtra("userData");
        }

        if (userData.getString("email") != null) {
            email.setText(userData.getString("email"));
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail(email.getText().toString())) {
                    userData.putString("email", email.getText().toString());
                    Intent intent = new Intent(context, InputInfoActivity.class);
                    intent.putExtra("userData", userData);
                    startActivity(intent);
                    finish();
                } else {
                    wrapper.setError("Проверьте правильность введенного email");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        finish();
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
