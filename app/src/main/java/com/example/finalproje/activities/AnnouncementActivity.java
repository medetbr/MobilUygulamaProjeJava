package com.example.finalproje.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproje.ListAdapter;
import com.example.finalproje.MainActivity;
import com.example.finalproje.R;
import com.example.finalproje.model.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnnouncementActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar_Announcement);
        setSupportActionBar(toolbar);

        // Geri düğmesi işlevselliği
        ImageButton backButton = findViewById(R.id.toolbarBackButton_Announcement);
        backButton.setOnClickListener(v -> onBackPressed());

        // HTML içeriğini çekmek için
        new FetchWebContentTask().execute();
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

                Intent intent = new Intent(AnnouncementActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class FetchWebContentTask extends AsyncTask<Void, Void, Void> {

        String URL = "https://web.harran.edu.tr/bilgisayar/tr/"; //duyurular sayfasının urlsi
        String headerTitle;
        String detail;
        ArrayList<NewsItem> announcementList = new ArrayList<>();
        ProgressDialog dialog;

        //Verilerin çekilme esnasında proggres dialog çalıştırır.
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog=new ProgressDialog(AnnouncementActivity.this);
            dialog.setTitle("");
            dialog.setMessage("Duyurular yükleniyor..");
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {//Arka plan işlemleri gerçekleştirilir.
            try {
                Document doc = Jsoup.connect(URL).get();//Site ile bağlantı kurulur. Bu bağlantı doc nesnesine aktarılır.
                Elements announcementTitle = doc.select("h2[class=block-title mt]");
                Elements announcementItem = doc.select(".block-news .item");

                for (Element getItem : announcementItem) {
                    NewsItem item = new NewsItem();

                    // Başlık bilgisini alıyoruz
                    String announcementTilte = getItem.text();

                    // Duyurulardaki URL bilgisini alıyoruz
                    String url = getItem.select(".title a").attr("href");

                    // Duyuru içeriği için
                    Document detailDoc = Jsoup.connect("https://web.harran.edu.tr" + url).get();
                    Elements announcemetnDetail = detailDoc.select("#content-detail");
                    detail = announcemetnDetail.html();

                    // Tarih bilgisini alıyoruz
                    item.setDate(dateFormat(detailDoc));
                    item.setDescription(announcemetnDetail.select(".detail").text());
                    item.setTitle(announcementTilte);
                    item.setUrl(url);


                    announcementList.add(item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private Date dateFormat(Document detailDoc) {
            String date = detailDoc.select("small").text();

            // Tarih ve saati almak için kelimeleri bir dizide tutuyoruz
            String[] parts = date.split(" ");

            // Web sayfasından gelen tarihi ayırtmak için
            String result = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[4];


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", new java.util.Locale("tr", "TR"));
            try {
                return dateFormat.parse(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid)
        {
            // web sayfasından veriler alındıktan sonra listview çalışır
            ListAdapter adapter = new ListAdapter(AnnouncementActivity.this, announcementList);
            ListView listView = findViewById(R.id.announcementListView);
            listView.setAdapter(adapter);
            dialog.dismiss();
        }
    }

    public void goToBackBtn(View view) {
        finish();
    }

}