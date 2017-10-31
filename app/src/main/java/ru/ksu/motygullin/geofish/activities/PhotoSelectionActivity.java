package ru.ksu.motygullin.geofish.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ru.ksu.motygullin.geofish.R;

public class PhotoSelectionActivity extends AppCompatActivity {

    ImageView photo;
    Bundle userData;
    Button addPhoto;
    Uri mCropImageUri;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);

        if (getIntent().getBundleExtra("userData") != null) {
            userData = getIntent().getBundleExtra("userData");
        }

        addPhoto = findViewById(R.id.add_photo);
        photo = findViewById(R.id.photo);


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                CropImage.startPickImageActivity(context);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, RegistrationFinishActivity.class);
        startActivity(intent, userData);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegistrationFinishActivity.class);
        startActivity(intent, userData);
        finish();
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {


            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            mCropImageUri = imageUri;


        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);


            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(this);

        Glide.with(context)
                .load(Uri.parse(imageUri.toString()))
                .into(photo);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
    }
}
