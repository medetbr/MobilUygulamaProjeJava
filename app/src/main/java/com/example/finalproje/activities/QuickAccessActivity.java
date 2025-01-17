package com.example.finalproje.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproje.MainActivity;
import com.example.finalproje.R;


public class QuickAccessActivity extends AppCompatActivity implements View.OnClickListener {


    Button ebysButton, office365Button, obsButton, akademikButton, haruzemButton, ueButton, sssButton,
            ylButton, trButton, etButton, kkButton, aktsButton, accessButton;
    WebView webView;
    String path = "https://www.harran.edu.tr/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quick_access);

        // Edge to Edge padding adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_gps);
        setSupportActionBar(toolbar);

        // Geri düğmesi işlevselliği
        ImageButton backButton = findViewById(R.id.toolbarBackButton_QuickAccess);
        backButton.setOnClickListener(v -> onBackPressed());

        // Initialize buttons
        ebysButton = findViewById(R.id.volkan_btn_ebys);
        office365Button = findViewById(R.id.volkan_btn_office365);
        obsButton = findViewById(R.id.volkan_btn_obs);
        akademikButton = findViewById(R.id.volkan_btn_takvim);
        haruzemButton = findViewById(R.id.volkan_btn_haruzem);
        ueButton = findViewById(R.id.volkan_btn_urfaEvi);
        sssButton = findViewById(R.id.volkan_btn_sss);
        ylButton = findViewById(R.id.volkan_btn_yemek);
        trButton = findViewById(R.id.volkan_btn_telefonRehberi);
        etButton = findViewById(R.id.volkan_btn_etkinlikTakvimi);
        kkButton = findViewById(R.id.volkan_btn_kalite);
        aktsButton = findViewById(R.id.volkan_btn_akts);
        accessButton = findViewById(R.id.volkan_btn_page);

        // Set click listeners
        ebysButton.setOnClickListener(this);
        office365Button.setOnClickListener(this);
        obsButton.setOnClickListener(this);
        akademikButton.setOnClickListener(this);
        haruzemButton.setOnClickListener(this);
        ueButton.setOnClickListener(this);
        sssButton.setOnClickListener(this);
        ylButton.setOnClickListener(this);
        trButton.setOnClickListener(this);
        etButton.setOnClickListener(this);
        kkButton.setOnClickListener(this);
        aktsButton.setOnClickListener(this);
        accessButton.setOnClickListener(this);

        // Initialize WebView
        webView = findViewById(R.id.webview_web);
        webView.setWebViewClient(new WebViewClient());
        _load();
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

                Intent intent = new Intent(QuickAccessActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Method to load initial URL
    public void _load() {
        webView.loadUrl("https://www.harran.edu.tr/");
        webView.getSettings().setJavaScriptEnabled(true);
    }

    // Handle button clicks
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.volkan_btn_ebys:
                webView.loadUrl("https://ebys.harran.edu.tr/enVision/Login.aspx");
                path = "https://ebys.harran.edu.tr/enVision/Login.aspx";
                break;
            case R.id.volkan_btn_office365:
                webView.loadUrl("https://office365.harran.edu.tr/");
                path = "https://office365.harran.edu.tr/";
                break;
            case R.id.volkan_btn_obs:
                webView.loadUrl("https://obs.harran.edu.tr/");
                path = "https://obs.harran.edu.tr/";
                break;
            case R.id.volkan_btn_takvim:
                webView.loadUrl("https://www.harran.edu.tr/aclist.aspx");
                path = "https://www.harran.edu.tr/aclist.aspx";
                break;
            case R.id.volkan_btn_haruzem:
                webView.loadUrl("https://haruzem.harran.edu.tr/");
                path = "https://haruzem.harran.edu.tr/";
                break;
            case R.id.volkan_btn_urfaEvi:
                webView.loadUrl("https://urfaevi.harran.edu.tr/");
                path = "https://urfaevi.harran.edu.tr/";
                break;
            case R.id.volkan_btn_sss:
                webView.loadUrl("https://sss.harran.edu.tr/");
                path = "https://sss.harran.edu.tr/";
                break;
            case R.id.volkan_btn_yemek:
                webView.loadUrl("https://www.harran.edu.tr/yemeklist.aspx");
                path = "https://www.harran.edu.tr/yemeklist.aspx";
                break;
            case R.id.volkan_btn_telefonRehberi:
                webView.loadUrl("https://rehber.harran.edu.tr/default.aspx");
                path = "https://rehber.harran.edu.tr/default.aspx";
                break;
            case R.id.volkan_btn_etkinlikTakvimi:
                webView.loadUrl("https://etkinlik.harran.edu.tr/");
                path = "https://etkinlik.harran.edu.tr/";
                break;
            case R.id.volkan_btn_kalite:
                webView.loadUrl("https://kalite.harran.edu.tr/");
                path = "https://kalite.harran.edu.tr/";
                break;
            case R.id.volkan_btn_akts:
                webView.loadUrl("https://ilan.harran.edu.tr/AkademikTalep/login.aspx");
                path = "https://ilan.harran.edu.tr/AkademikTalep/login.aspx";
                break;
            case R.id.volkan_btn_page:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, path);
                startActivity(intent);
                break;
        }
    }

    // Back button functionality
    public void goToBackBtn(View view) {
        finish();
    }
}
