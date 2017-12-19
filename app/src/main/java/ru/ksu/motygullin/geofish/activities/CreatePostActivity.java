package ru.ksu.motygullin.geofish.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.geofish.GlideApp;
import ru.ksu.motygullin.geofish.R;
import ru.ksu.motygullin.geofish.entities.CreatedPost;
import ru.ksu.motygullin.geofish.geofishAPI.Api;
import ru.ksu.motygullin.geofish.geofishAPI.GeoFishAPI;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GALLERY_REQUEST = 1001;
    public static final int CAMERA_REQUEST = 1002;
    private static final int REQUEST_CODE = 666;
    int PLACE_PICKER_REQUEST = 1;
    Activity context = this;
    SharedPreferences preferences;
    TextView toolbarTitle;
    TextView name;
    EditText news_text;
    ImageButton gallery;
    ImageButton take_photo;
    ImageButton set_location;
    ImageView profile_photo;
    Uri mCropImageUri;
    Double latitude;
    Double longitude;
    File image;
    LinearLayout buttons_container;
    LinearLayout photo_video_container;
    String mCurrentPhotoPath;
    ProgressBar progressBar;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appBar);

        preferences = context.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);

        appBarLayout.bringToFront();

        toolbar.setTitle("Новая запись");
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0.0f);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2b2145"));

        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Новая запись");

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        gallery = findViewById(R.id.gallery);
        take_photo = findViewById(R.id.take_photo);
        set_location = findViewById(R.id.set_location);
        profile_photo = findViewById(R.id.profile_photo);
        name = findViewById(R.id.name);
        buttons_container = findViewById(R.id.buttons_container);
        photo_video_container = findViewById(R.id.photo_video_container);
        progressBar = findViewById(R.id.pb);
        news_text = findViewById(R.id.news_text);

        gallery.setOnClickListener(this);
        take_photo.setOnClickListener(this);
        set_location.setOnClickListener(this);

        GlideApp.with(this)
                .load(preferences.getString("photo", ""))
                .apply(RequestOptions.circleCropTransform())
                .into(profile_photo);
        name.setText(preferences.getString("name", "") + " " + preferences.getString("surname", ""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blue_toolbar_menu, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            mCropImageUri = CropImage.getPickImageResultUri(context, data);

            buttons_container.setVisibility(View.GONE);
            View view = getLayoutInflater().inflate(R.layout.content_item, null, false);

            ImageView image = view.findViewById(R.id.image);

            image.setVisibility(View.VISIBLE);

            GlideApp.with(context)
                    .load(mCropImageUri)
                    .fitCenter()
                    .into(image);


            photo_video_container.addView(view);

        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            File f = new File(mCurrentPhotoPath);
            mCropImageUri = Uri.fromFile(f);

            buttons_container.setVisibility(View.GONE);
            View view = getLayoutInflater().inflate(R.layout.content_item, null, false);

            ImageView image = view.findViewById(R.id.image);

            image.setVisibility(View.VISIBLE);

            GlideApp.with(context)
                    .load(mCropImageUri)
                    .into(image);


            photo_video_container.addView(view);
        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(context, data);
            String toastMsg = String.format("Place: %s", place.getName());
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_down, R.anim.slide_down_out);
                return true;
            case R.id.action_send:
                progressBar.setVisibility(View.VISIBLE);
                news_text.setEnabled(false);

                if (mCropImageUri != null) {
                    final StorageReference postImagesRef = mStorageRef.child("post_images/" + mCropImageUri.getLastPathSegment());

                    final UploadTask task = postImagesRef.putFile(mCropImageUri);


                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "WRONG4", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String text = "";
                            if (news_text.getText().toString() != "") {
                                text = news_text.getText().toString();
                            }
                            GeoFishAPI api = Api.getInstance().getApi();
                            Call<CreatedPost> call = api.createPost(preferences.getInt("uid", 40), text, String.valueOf(task.getResult().getDownloadUrl()), null, null, null);
                            call.enqueue(new Callback<CreatedPost>() {
                                @Override
                                public void onResponse(Call<CreatedPost> call, Response<CreatedPost> response) {
                                    Intent intent1 = new Intent(context, ProfileActivity.class);
                                    startActivity(intent1);
                                    finish();
                                    overridePendingTransition(R.anim.slide_down, R.anim.slide_down_out);
                                }

                                @Override
                                public void onFailure(Call<CreatedPost> call, Throwable t) {
                                    Toast.makeText(CreatePostActivity.this, "Загрузка не удалась, повторите позднее", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions();

                } else {

                    Intent gallery_intent = CropImage.getPickImageChooserIntent(context, "Выбрать источник", true, false);
                    startActivityForResult(gallery_intent, GALLERY_REQUEST);
                }
                break;

            case R.id.take_photo:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions();

                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    "ru.ksu.motygullin.geofish",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                    }
                }
            case R.id.set_location:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions();

                } else {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE
        );
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
