package com.example.finalproje;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproje.activities.AnnouncementDetailActivity;
import com.example.finalproje.model.NewsItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListAdapter extends ArrayAdapter<NewsItem> {
    private Context context;
    public ListAdapter(@NonNull Context context, ArrayList<NewsItem> data) {
        super(context, R.layout.list_item, data);
        this.context = context;
    }

    // View oluşturma ve güncelleme
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Daha önce oluşturulmuş bir view varsa onu yeniden kullanıyoruz
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Veriyi alıyoruz
        NewsItem listData = getItem(position);

        // Textview'leri tanımlıyoruz
        TextView title = convertView.findViewById(R.id.textView13);
        TextView date = convertView.findViewById(R.id.textView14);


        title.setText(listData.getTitle());

        // Tarih formatını hazırlıyoruz
        Date dateValue = listData.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(dateValue);
        date.setText(formattedDate);

        // Listede bir item'a tıklandığında
        convertView.setOnClickListener(v -> {

            String itemDate = dateFormat.format(listData.getDate());

            // Duyuru içeriği için activity
            Intent intent = new Intent(context, AnnouncementDetailActivity.class);
            intent.putExtra("title", listData.getTitle());
            intent.putExtra("description", listData.getDescription());
            intent.putExtra("date", itemDate);
            context.startActivity(intent);
        });

        return convertView;
    }

}
