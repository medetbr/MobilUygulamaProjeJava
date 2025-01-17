package com.example.finalproje.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CalculatorActivity extends AppCompatActivity {

    private EditText editText;
    private String text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar'ı ayarla
        Toolbar toolbar = findViewById(R.id.toolbar_calculator);
        setSupportActionBar(toolbar);

        // Geri düğmesi işlevselliği
        ImageButton backButton = findViewById(R.id.toolbarBackButton_calculator);
        backButton.setOnClickListener(v -> onBackPressed());

        editText = findViewById(R.id.editText);
        editText.setShowSoftInputOnFocus(false);
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

                Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void goToBackBtn(View view) {
       finish();
    }

    // butonlara tıklanınca tetiklenecek fonks.
    public void clickNumericBtn(View v) {
        editText = findViewById(R.id.editText);
        text = editText.getText().toString();
        Button getBtn = null;

        if(text.length()>20)return;

        // butonların id değerlerini tutan bir array
        int[] btnIds = {
                R.id.btn0, R.id.btn1,
                R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9,
                R.id.btnSum, R.id.btnMultiplication,
                R.id.btnDivision, R.id.btnSub,
                R.id.btnPercentage, R.id.btnComma
        };
        // tıklanan butonu dizideki id'ler ile karşılaştırmak
        for (int i = 0; i < btnIds.length; i++) {
            if (v.getId() == btnIds[i]) {
                getBtn = findViewById(btnIds[i]);
                break;
            }
        }


        // tıklanan buton 0 ise (başlangıç için) işlem yapmasın değilse işlem yapsın
        if (editText.getText().toString().equals("0") && isLastDigitNumber(getBtn.getText().toString())) {
            text = getBtn.getText().toString();
        } else if (text.charAt(text.length() - 1) == '!' && !isLastDigitNumber(getBtn.getText().toString())) {
            text += getBtn.getText().toString();
        } else if (isLastDigitNumber(text) || isLastDigitNumber(getBtn.getText().toString()) && text.charAt(text.length() - 1) != '!') {
            text += getBtn.getText().toString();
        }
        text = text.toLowerCase();
        editText.setText(text);

        if (isLastDigitNumber(text)) {
            resultHandle();
        }

    }

    private double calculate(String txt) {
        // 12 + 14 x 2 - 1
        // text = 12+14x2-1
        // 5! + 5!

        List<String> tokens = tokenize(text);
        tokens = handleFactorial(tokens);
        tokens = handleSqrt(tokens);
        tokens = handleMultiplicationDivision(tokens);

        return handleAdditionSubtraction(tokens);
    }

    // Faktoriyel hesaplama metodu
    public int factorial(double n) {
        if (n == 0 || n == 1) return 1;
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    //5 ! + 5 !
    public List<String> handleFactorial(List<String> tokens) {
        List<String> result = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < tokens.size(); i++) {
            String item = tokens.get(i);
            if (item.equals("!")) {
                // önceki sayıyı al ve faktöriyel hesapla
                double number = Double.parseDouble(result.remove(index - 1));
                result.add(String.valueOf(factorial(number)));
                index--;
            } else {
                result.add(item);
            }
            index++;
        }
        return result;
    }

    // √9+
    private List<String> handleSqrt(List<String> tokens) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            String item = tokens.get(i);
            if (item.equals("√")) {
                // sonraki sayıyı al ve karekökü hesapla
                double number = Double.parseDouble(tokens.get(i + 1));
                result.add(String.valueOf(Math.sqrt(number)));
                i++;
            } else {
                result.add(item);
            }
        }
        return result;

    }

    //5 ! + 5 !
    // tüm işlemleri ayırıp bir dizi şekline getiren fonks.
    public List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // operatörleri veya özel karakterleri ayırmak için
            if (c == '+' || c == '-' || c == 'x' || c == '/' || c == '!' || c == '%' || c == '^' || c == '√') {
                if(!current.toString().isEmpty()){
                    tokens.add(current.toString());
                }
                tokens.add(String.valueOf(c));
                current = new StringBuilder();
            } else {
                current.append(c);  // sayı basamaklarını birleştir
            }
        }

        if(!current.toString().isEmpty()){  // son sayıyı ekle
            tokens.add(current.toString());
        }

        return tokens;
    }

    public List<String> handleMultiplicationDivision(List<String> tokens) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            if (token.equals("x") || token.equals("/") || token.equals("%") || token.equals("^")) {
                double left = result.get(result.size() - 1).isEmpty() ? 1 : Double.parseDouble(result.remove(result.size() - 1));
                double right = Double.parseDouble(tokens.get(i + 1));
                if (token.equals("x")) {
                    result.add(String.valueOf(left * right));
                } else if (token.equals("%")) {
                    result.add(String.valueOf(left * (right / 100)));
                } else if (token.equals("^")) {
                    result.add(String.valueOf(handleExponent(left, right)));
                } else {
                    result.add(String.valueOf(left / right));
                }
                i++;
            } else {
                result.add(token);
            }
            i++;
        }
        return result;
    }

    // 2^3  x=2  y=3
    private double handleExponent(double x, double y) {
        if(y==0)return 1;
        double total = 1;
        while (0 < y) {
            total *= x;
            y--;
        }
        x = total;
        return x;
    }

    // Toplama ve çıkarma işlemleri yapan fonk.
    public double handleAdditionSubtraction(List<String> tokens) {
        double result = Double.parseDouble(tokens.get(0));
        for (int i = 1; i < tokens.size(); i += 2) {
            String operator = tokens.get(i);
            double number = Double.parseDouble(tokens.get(i + 1));
            if (operator.equals("+")) {
                result += number;
            } else if (operator.equals("-")) {
                result -= number;
            }
        }
        return result;
    }

    // tüm işlemi sıfırlar
    public void allClearBtn(View v) {
        text = "";
        changeEditText("0");
        TextView resultText = findViewById(R.id.resultTxt);
        resultText.setText("0");
    }

    public void clickFactorialBtn(View v) {
        if (isLastDigitNumber(text)) {
            text += "!";
            changeEditText(text);
            resultHandle();
        }

    }

    //en son yapılan işlemi siler
    public void deleteBtn(View v) {
        if (text.length() <= 1) {
            changeEditText("0");
            TextView resultText = findViewById(R.id.resultTxt);
            resultText.setText("0");
            return;
        } else {

            text = text.substring(0, text.length() - 1);
            changeEditText(text);
        }
        if (isLastDigitNumber(text)) {
            resultHandle();
        }
    }

    // Edit text set etme
    private void changeEditText(String content) {
        editText = findViewById(R.id.editText);
        editText.setText(content);
    }


    // son basamak rakam mı?
    private boolean isLastDigitNumber(String text) {
        return Pattern.compile("^\\d+$")
                .matcher(text.substring(text.length() - 1))
                .find();
    }

    // son basamak sembol mü (/ x + - %)
    private boolean isLastDigitSymbol(String text) {
        return Pattern.compile("^[/+x%]+$")
                .matcher(text.substring(text.length() - 1))
                .find();
    }

    public void clickEqualsBtn(View v) {
        TextView editText = findViewById(R.id.editText);
        if (editText.getText().toString().equals("0")) {
            editText.setText("0");
        } else if(isLastDigitNumber(text) || text.charAt(text.length() - 1) == '!'){

            double result = calculate(text);
            int integerResult = (int) result;
            if ((result - integerResult) == 0) {
                editText.setText(String.valueOf(integerResult));
                return;
            }
            editText.setText(String.valueOf(result));
        }
    }

    private void resultHandle() {
        TextView resultText = findViewById(R.id.resultTxt);
        double result = calculate(text);  // 5.6
        int integerResult = (int) result;  // 5
        if ((result - integerResult) == 0) {    // 5.0 - 5.6 == 0
            resultText.setText(String.valueOf(integerResult));
            return;
        }
        resultText.setText(String.valueOf(result));
    }


    public void clickEulerBtn(View view) {
        if (text.isEmpty()) {
            text = "2.71828";
        }
        if (!isLastDigitNumber(text) && text.charAt(text.length() - 1) != '!') {
            text += "2.71828";
        }
        resultHandle();
        changeEditText(text);
    }

    public void clickExponentBtn(View view) {
        if (text.charAt(text.length() - 1) != '√'){
            text += "^";
            changeEditText(text);
        }

    }

    public void clickSqrtBtn(View view) {
        if(text.isEmpty()){
            text = "√";
        } else if (isLastDigitNumber(text)) {
            text += "x√";
        }else if (isLastDigitSymbol(text)) {
            text += "√";
        }

        changeEditText(text);
    }
}