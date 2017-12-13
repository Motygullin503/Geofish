package ru.ksu.motygullin.geofish.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.geofish.R;
import ru.ksu.motygullin.geofish.entities.RegModel;
import ru.ksu.motygullin.geofish.geofishAPI.Api;
import ru.ksu.motygullin.geofish.geofishAPI.GeoFishAPI;

public class AuthActivity extends AppCompatActivity {

    TextInputEditText email;
    EditText password;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.btn_signIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("")&&!password.getText().toString().equals("")){
                    Call<RegModel> call = Api.getInstance().getApi().authentificate(email.getText().toString(), password.getText().toString());
                    call.enqueue(new Callback<RegModel>() {
                        @Override
                        public void onResponse(Call<RegModel> call, Response<RegModel> response) {

                        }

                        @Override
                        public void onFailure(Call<RegModel> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(AuthActivity.this, "Заполните поля и повторите попытку", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
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
