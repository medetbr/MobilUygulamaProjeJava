package com.example.finalproje.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproje.MainActivity;
import com.example.finalproje.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GPSNavActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText searchBar;
    private Button btnZoomIn, btnZoomOut, btnAddMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre
        setContentView(R.layout.activity_gpsnav);

        Toolbar toolbar = findViewById(R.id.toolbar_gps);
        setSupportActionBar(toolbar);

        // Geri düğmesi işlevselliği
        ImageButton backButton = findViewById(R.id.toolbarBackButton_gps);
        backButton.setOnClickListener(v -> onBackPressed());

        // XML'deki bileşenleri bağlama
        searchBar = findViewById(R.id.searchBar);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);
        btnAddMarkers = findViewById(R.id.btnAddMarkers);

        // Harita fragment'ini başlatma
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Zoom in butonu
        btnZoomIn.setOnClickListener(v -> {
            if (mMap != null) mMap.animateCamera(CameraUpdateFactory.zoomIn());
        });

        // Zoom out butonu
        btnZoomOut.setOnClickListener(v -> {
            if (mMap != null) mMap.animateCamera(CameraUpdateFactory.zoomOut());
        });

        // Marker ekleme butonu
        btnAddMarkers.setOnClickListener(v -> addMarkers());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // Çıkış işlemini burada yap
                SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userInfo", null);
                editor.apply();

                Intent intent = new Intent(GPSNavActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Geri dönüş butonu
    public void goToBackBtn(View view) {
        Intent intent = new Intent(GPSNavActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Default konumu Ankara olarak ayarlama
        LatLng defaultLocation = new LatLng(39.92077, 32.85411); // Ankara'nın koordinatları
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    private void addMarkers() {
        if (mMap != null) {
            LatLng[] locations = {
                    new LatLng(39.92077, 32.85411), // Ankara
                    new LatLng(41.0082, 28.9784),  // İstanbul
                    new LatLng(40.1826, 29.0669),  // Bursa
                    new LatLng(38.4237, 27.1428),  // İzmir
                    new LatLng(37.0662, 37.3833),  // Gaziantep
                    new LatLng(36.8969, 30.7133),  // Antalya
                    new LatLng(41.0256, 29.0183)   // Boğaziçi
            };

            for (LatLng location : locations) {
                mMap.addMarker(new MarkerOptions().position(location).title("Nokta"));
            }
        }
    }
}
