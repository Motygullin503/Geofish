package ru.ksu.motygullin.geofish.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import ru.ksu.motygullin.geofish.R;

public class MainActivity extends AppCompatActivity {

    Button register;
    Context context = this;
    Bundle userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        register = findViewById(R.id.createAccount);

        if (getIntent().getBundleExtra("userData")!=null){
            userData = getIntent().getBundleExtra("userData");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                startActivity(intent, userData);
                finish();
            }
        });
    }



}

