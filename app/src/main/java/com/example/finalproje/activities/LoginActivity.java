package com.example.finalproje.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproje.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre
        setContentView(R.layout.activity_login);

        // Firebase Firestore instance'ını al
        db = FirebaseFirestore.getInstance();
    }

    public void loginBtn(View view) {
        // EditText'lerden kullanıcı adı ve şifreyi al
        EditText usernameEditText = findViewById(R.id.usernameETxt);
        EditText passwordEditText = findViewById(R.id.passwordEtxt);

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Gerekli alanların boş olup olmadığını kontrol et
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Firestore'da kullanıcıyı ara
        db.collection("Logger") // Firestore koleksiyon ismi "Logger"
                .document(username) // Kullanıcı adını belge ismi olarak kullan
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String storedPassword = documentSnapshot.getString("Password");

                        // Şifreyi karşılaştır
                        if (storedPassword != null && storedPassword.equals(password)) {
                            // Giriş başarılı
                            // Kullanıcı bilgisini SharedPreferences'te sakla
                            sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userInfo", username);
                            editor.apply();

                            // Ana ekrana yönlendir
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Şifre yanlış
                            Toast.makeText(LoginActivity.this, "Yanlış Şifre", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Kullanıcı bulunamadı
                        Toast.makeText(LoginActivity.this, "Kullanıcı Bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Hata durumunda
                    Toast.makeText(LoginActivity.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Register butonuna tıklandığında RegisterFragment'ı göster
    public void clickRegisterBtn(View view) {
        // RegisterFragment'ı yükle
        RegisterFragment registerFragment = new RegisterFragment();

        // FragmentTransaction ile geçiş yap
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, registerFragment); // FrameLayout içine RegisterFragment'ı yerleştir
        transaction.addToBackStack(null); // Geri gitmek için stack'e ekle
        transaction.commit(); // Değişiklikleri uygula
    }
}
