package com.example.finalproje.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproje.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private EditText usernameET, passwordET, nameET, surnameET, emailET, gsmET;
    private FirebaseFirestore db;

    private ProgressBar progressBar;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragment layout'ını inflate et
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); // Cihaz ayarına göre

        // Çıkış butonunu bul ve tıklama olayını tanımla
        ImageButton closeBtn = view.findViewById(R.id.fragCloseBTN);
        closeBtn.setOnClickListener(v -> closeRegisterFragment());

        progressBar = view.findViewById(R.id.progressBar);

        // Firestore'u başlat
        db = FirebaseFirestore.getInstance();

        // EditText'leri tanımla
        usernameET = view.findViewById(R.id.fragUserNameET);
        passwordET = view.findViewById(R.id.fragPassET);
        nameET = view.findViewById(R.id.fragNameET);
        surnameET = view.findViewById(R.id.fragSurnameET);
        emailET = view.findViewById(R.id.fragEmailET);
        gsmET = view.findViewById(R.id.fragGSMET);

        // Kayıt butonu
        Button registerBtn = view.findViewById(R.id.fragRegisterBTN);
        registerBtn.setOnClickListener(v -> registerUser());

        // Klavye otomatik kapanma ayarı
        setupKeyboardDismiss(view);



        TextView helper = view.findViewById(R.id.fragHelpTV);
        helper.setOnClickListener(v -> helpTxtClick());


        return view;

    }

    // Kullanıcıyı kaydetme işlemi
    private void registerUser() {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String name = nameET.getText().toString().trim();
        String surname = surnameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String gsm = gsmET.getText().toString().trim();

        // Zorunlu alanları kontrol et
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Username is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // ProgressBar'ı göster
        progressBar.setVisibility(View.VISIBLE);

        // Kullanıcı adı var mı diye Firestore'da kontrol et
        db.collection("Logger").document(username).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Eğer döküman varsa, kullanıcı adı zaten alınmış demektir
                        progressBar.setVisibility(View.GONE);  // ProgressBar'ı gizle
                        Toast.makeText(getContext(), "Username is already taken, please choose another.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Kullanıcı adı benzersizse, kullanıcıyı kaydet
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("UserName", username);
                        userData.put("Password", password);

                        // Opsiyonel alanlar
                        if (!TextUtils.isEmpty(name)) userData.put("Name", name);
                        if (!TextUtils.isEmpty(surname)) userData.put("Surname", surname);
                        if (!TextUtils.isEmpty(email)) userData.put("E-Mail", email);
                        if (!TextUtils.isEmpty(gsm)) userData.put("GSM", gsm);

                        // Firestore'da döküman oluştur
                        db.collection("Logger").document(username)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    // Kayıt başarılı ise
                                    progressBar.setVisibility(View.GONE);  // ProgressBar'ı gizle
                                    Toast.makeText(getContext(), "User registered successfully!", Toast.LENGTH_SHORT).show();
                                    clearFields(); // Form alanlarını temizle
                                })
                                .addOnFailureListener(e -> {
                                    // Kayıt başarısız ise
                                    progressBar.setVisibility(View.GONE);  // ProgressBar'ı gizle
                                    Toast.makeText(getContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Firestore'a erişim hatası olursa
                    progressBar.setVisibility(View.GONE);  // ProgressBar'ı gizle
                    Toast.makeText(getContext(), "Error checking username: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // Form alanlarını temizleme
    private void clearFields() {
        usernameET.setText("");
        passwordET.setText("");
        nameET.setText("");
        surnameET.setText("");
        emailET.setText("");
        gsmET.setText("");
    }

    // RegisterFragment'ten çık ve LoginActivity'yi başlat
    private void closeRegisterFragment() {
        // Fragment'i kaldır
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this);  // Bu fragment'ı kaldır
        transaction.commit();
    }

    // Klavye otomatik kapanma için setup
    private void setupKeyboardDismiss(@NonNull View view) {
        // Fragment'ta herhangi bir alana tıklandığında klavye kapanması için listener ekleyelim
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener((v, event) -> {
            // Klavyeyi kapat
            View currentFocusedView = getActivity().getCurrentFocus();
            if (currentFocusedView != null) {
                currentFocusedView.clearFocus();
            }
            return false;
        });
    }


    public void helpTxtClick() {
        // Yardım tıklama işlemi

            Toast.makeText(getContext(), "Just School Number and Password is Necessary", Toast.LENGTH_SHORT).show();

    }

}
