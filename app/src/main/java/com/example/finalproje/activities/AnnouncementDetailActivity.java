package com.example.finalproje.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproje.MainActivity;
import com.example.finalproje.R;

public class AnnouncementDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_AnnouncementDetail);
        setSupportActionBar(toolbar);

        // Geri düğmesi işlevselliği
        ImageButton backButton = findViewById(R.id.toolbarBackButton_AnnouncementDetail);
        backButton.setOnClickListener(v -> onBackPressed());

        // TextView'ları tanımlıyoruz
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateDetailTextView = findViewById(R.id.dateDetailTextView);

        // Intent'ten gelen verileri alıyoruz
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String itemDate = getIntent().getStringExtra("date");

        // Alınan verileri TextView'lere set ediyoruz
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        dateDetailTextView.setText(itemDate);
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

                Intent intent = new Intent(AnnouncementDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}