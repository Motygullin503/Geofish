package ru.ksu.motygullin.geofish.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ksu.motygullin.geofish.GlideApp;
import ru.ksu.motygullin.geofish.R;
import ru.ksu.motygullin.geofish.adapters.NewsAdapter;
import ru.ksu.motygullin.geofish.entities.Posts;
import ru.ksu.motygullin.geofish.geofishAPI.Api;
import ru.ksu.motygullin.geofish.geofishAPI.GeoFishAPI;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    Context context = this;
    RecyclerView recyclerView;
    SharedPreferences preferences;
    ImageView photo;
    ImageView back_photo;
    ImageView front_photo;
    TextView name;
    TextView front_name;
    NewsAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    GeoFishAPI api;
    ConstraintLayout mainView;
    ActionBarDrawerToggle toggle;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        preferences = context.getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appBar);

        appBarLayout.bringToFront();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0.0f);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreatePostActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mainView = findViewById(R.id.mainView);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        ;
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        GeoFishAPI api = Api.getInstance().getApi();
        Call<Posts> call = api.getPostsById(preferences.getInt("uid", 1));

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.body() != null) {
                    adapter = new NewsAdapter(context, response.body());
                }

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {

            }
        });


        View headerView = navigationView.getHeaderView(0);
        photo = headerView.findViewById(R.id.imageView);
        name = headerView.findViewById(R.id.text_name);
        front_photo = findViewById(R.id.frontView);
        front_name = findViewById(R.id.name);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        back_photo = findViewById(R.id.background_photo);
        GlideApp.with(this)
                .load(preferences.getString("photo", ""))
                .centerCrop()
                .into(back_photo);

        GlideApp.with(this)
                .load(preferences.getString("photo", ""))
                .apply(RequestOptions.circleCropTransform())
                .into(front_photo);

        back_photo.setColorFilter(Color.parseColor("#1e113d"), PorterDuff.Mode.MULTIPLY);


        front_name.setText(preferences.getString("name", "") + " " + preferences.getString("surname", ""));
        name.setText(preferences.getString("name", "") + " " + preferences.getString("surname", ""));

        GlideApp.with(this)
                .load(preferences.getString("photo", ""))
                .apply(RequestOptions.circleCropTransform())
                .into(photo);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {

        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(context, MapActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_profile) {

            Intent intent = new Intent(context, ProfileActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_clubs) {

        } else if (id == R.id.nav_search) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        GeoFishAPI api = Api.getInstance().getApi();
        Call<Posts> call = api.getPostsById(preferences.getInt("uid", 1));

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.body() != null) {
                    adapter.setData(response.body());
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
