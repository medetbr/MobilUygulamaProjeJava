package com.example.finalproje.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproje.MainActivity;
import com.example.finalproje.R;

import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Intent intent;

    private HorizontalScrollView scrollImages;
    private LinearLayout imageContainer;
    private int currentImageIndex = 0; // Hangi resmin gösterileceğini takip eder
    private Handler handler = new Handler(); // Otomatik kaydırma için
    private Runnable autoScrollRunnable; // Zamanlayıcı runnable
    private int imageWidth; // Resim genişliği (px olarak hesaplanır)
    private int screenWidth; // Cihaz ekran genişliği (px)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollImages = findViewById(R.id.scrollImages);
        imageContainer = (LinearLayout) scrollImages.getChildAt(0);

        // Ekran genişliği ve yoğunluk hesaplama
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        float density = displayMetrics.density;

        // İlk resmin genişliği, px'e çevrilir
        imageWidth = (int) (350 * density);

        // Kaydırma mesafesinin ayarlanması
        int scrollDistance = (int) (365 * density);

        // Otomatik kaydırma işlemi
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                // Her kaydırma için 325px ilerleme sağla
                scrollToImage(currentImageIndex, scrollDistance);

                // Bir sonraki resme geç
                currentImageIndex++;
                if (currentImageIndex >= imageContainer.getChildCount()-1) {
                    currentImageIndex = 0; // Başa dön
                }

                handler.postDelayed(this, 5000); // Her 5 saniyede bir çalıştır
            }
        };

        // Uygulama açıldığında ilk resmi ortala ve zamanlayıcıyı başlat
        scrollToImage(currentImageIndex, 0); // İlk resmi ortala
        handler.postDelayed(autoScrollRunnable, 5000); // İlk kaydırmayı 5 saniye sonra başlat
    }

    // Belirtilen resme kaydırmak
    private void scrollToImage(int index, int offset) {
        // Ortalanacak resim için kaydırma miktarını hesapla
        int scrollX = (imageWidth * index) - (screenWidth / 2) + (imageWidth / 2) + offset;
        scrollImages.smoothScrollTo(scrollX, 0); // Yatay kaydırma
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoScrollRunnable); // Bellek sızıntısını önlemek için temizle
    }

    public void clickGpsNavBtn(View view) {
        intent = new Intent(HomeActivity.this, GPSNavActivity.class);
        startActivity(intent);
    }

    public void clickCalculatorBtn(View view) {
        intent = new Intent(HomeActivity.this, CalculatorActivity.class);
        startActivity(intent);
    }

    public void clickQuickAccessBtn(View view) {
        intent = new Intent(HomeActivity.this, QuickAccessActivity.class);
        startActivity(intent);
    }

    public void clickAnnouncementBtn(View view) {
        intent = new Intent(HomeActivity.this, AnnouncementActivity.class);
        startActivity(intent);
    }

    public void clickLogoutBtn(View view) {
        sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userInfo", null);
        editor.apply();

        intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Menü oluşturulması
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Menü XML dosyasını bağla
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // Çıkış işlemini burada yap
                sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userInfo", null);
                editor.apply();

                intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
