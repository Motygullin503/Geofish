package ru.ksu.motygullin.geofish.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.ksu.motygullin.geofish.R;

public class MainActivity extends AppCompatActivity {

    Button register;
    ImageButton vk_auth;
    Button authenticate;
    Activity context = this;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = context.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        register = findViewById(R.id.createAccount);
        authenticate = findViewById(R.id.signIn);
        vk_auth = findViewById(R.id.vk_auth);

        if (!preferences.getString("uid_token", "").equals("")) {
            Intent intent = new Intent(context, ProfileActivity.class);
            startActivity(intent);
            finish();
        }

        vk_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VKSdk.login(context, "");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался

                final SharedPreferences.Editor editor = preferences.edit();
                editor.putString("uid_token", res.userId);
                //посылаем запрос на данные
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, res.userId, VKApiConst.FIELDS, "photo_max,city,bdate"));

                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {

                        JSONObject object = response.json;

                        try {
                            JSONArray array = object.getJSONArray("response");
                            JSONObject info = array.getJSONObject(0);
                            editor.putString("name", info.getString("first_name"));
                            editor.putString("surname", info.getString("last_name"));
                            if (info.getJSONObject("city").getString("title") != null && info.getString("bdate") != null) {
                                editor.putString("city", info.getJSONObject("city").getString("title"));
                                editor.putString("bdate", info.getString("bdate"));
                                editor.putString("photo", info.getString("photo_max"));
                                editor.apply();
                                Intent intent = new Intent(context, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                editor.putString("socials", "true");
                                Intent intent = new Intent(context, RegistrationFinishActivity.class);
                                startActivity(intent);
                                editor.apply();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        super.attemptFailed(request, attemptNumber, totalAttempts);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }
                });

            }

            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Toast.makeText(context, "Что-то пошло не так, повторите позднее", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}

