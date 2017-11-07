package ru.ksu.motygullin.geofish.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.geofish.R;
import ru.ksu.motygullin.geofish.entities.RegModel;
import ru.ksu.motygullin.geofish.geofishAPI.Api;
import ru.ksu.motygullin.geofish.geofishAPI.GeoFishAPI;

public class PhotoSelectionActivity extends AppCompatActivity {

    Activity context = this;
    ImageView photo;
    TextView skip;
    TextView change_photo;
    SharedPreferences preferences;
    Button addPhoto;
    Uri mCropImageUri;
    final FirebaseStorage storage = FirebaseStorage.getInstance();


    boolean isPhotoChoosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);
        preferences = context.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);

        final String email = preferences.getString("email", "");
        final String name = preferences.getString("name", "");
        final String surname = preferences.getString("surname", "");
        final String password = preferences.getString("password", "");
        final String birthday = preferences.getString("birthday", "");
        final String city = preferences.getString("city", "");


        addPhoto = findViewById(R.id.add_photo);
        photo = findViewById(R.id.photo);
        skip = findViewById(R.id.text_skip);
        change_photo = findViewById(R.id.text_change);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isPhotoChoosen) {

                    StorageReference reference = storage.getReference("profile_images/");
                    reference = reference.child(preferences.getString("email", ""));
                    final UploadTask task = reference.putFile(mCropImageUri);

                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            GeoFishAPI api = Api.getInstance().getApi();

                            Call<RegModel> call = api.registerWithEmail(email, password, name, surname, city, birthday, String.valueOf(task.getResult().getDownloadUrl()));
                            call.enqueue(new Callback<RegModel>() {
                                @Override
                                public void onResponse(Call<RegModel> call, Response<RegModel> response) {
                                    Intent intent = new Intent(context, ProfileActivity.class);
                                    Toast.makeText(context, "Успешная регистрация, теперь вы можете войти в свой аккаунт", Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("uid_token", response.body().getToken());
                                    editor.putString("photo", String.valueOf(task.getResult().getDownloadUrl()));
                                    editor.apply();
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<RegModel> call, Throwable t) {
                                    Toast.makeText(PhotoSelectionActivity.this, "Произошла ошибка при регистрации, повторите позднее", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Загрузка не выполнена, повторите попытку", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    CropImage.startPickImageActivity(context);
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, RegistrationFinishActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegistrationFinishActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(context, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(context, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                mCropImageUri = resultUri;
                isPhotoChoosen = true;
                Glide.with(context)
                        .load(resultUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(photo);

                change_photo.setVisibility(View.VISIBLE);
                skip.setVisibility(View.GONE);
                addPhoto.setText("Готово");


            } else {
                Toast.makeText(context, "Что-то пошло не так, повторите позднее", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(this);


    }

}
