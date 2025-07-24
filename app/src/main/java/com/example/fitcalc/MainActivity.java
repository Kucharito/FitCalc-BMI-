package com.example.fitcalc;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText vaha;
    private EditText vyska;
    private EditText vek;
    private Button buttonBMI;
    private TextView vysledokBMI;
    private TextView textViewBMI;
    private CardView cardResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });





        vaha = findViewById(R.id.inputWeight);
        vyska = findViewById(R.id.inputHeight);
        vek = findViewById(R.id.inputAge);
        buttonBMI = findViewById(R.id.buttonBMI);
        vysledokBMI = findViewById(R.id.textView4);
        textViewBMI = findViewById(R.id.textView5);
        cardResult = findViewById(R.id.cardResult);


        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);

        // Obnovenie posledného BMI pri štarte aplikácie
        float lastBMI = sharedPreferences.getFloat("lastBMI", 0.0f);
        String lastBMItext = sharedPreferences.getString("lastBMItext", "Nebol vypočítaný BMI.");
        if (lastBMI != 0.0f) {
            //vysledokBMI.setText(String.format("Výsledok BMI: %.2f", lastBMI));
            //textViewBMI.setText(lastBMItext);
            //returnBMItext(lastBMI);
        } else {
            vysledokBMI.setText("Nebol vypočítaný BMI.");
            textViewBMI.setText("Nebol vypočítaný BMI.");
        }


        buttonBMI.setOnClickListener(view -> {
            String vahaText = vaha.getText().toString();
            String vyskaText = vyska.getText().toString();
            String vekText = vek.getText().toString();

            if (!vahaText.isEmpty() && !vyskaText.isEmpty() && !vekText.isEmpty()) {
                double hmotnost = Double.parseDouble(vahaText);
                double vyskaCm = Double.parseDouble(vyskaText);
                double vyskaM = vyskaCm / 100;
                double bmi = hmotnost / (vyskaM * vyskaM);

                String bmiText = String.format("Výsledok BMI: %.2f", bmi);
                String bmiDesc = returnBMItext(bmi);

                vysledokBMI.setText(bmiText);
                textViewBMI.setText(bmiDesc);

                // 💾 Uloženie bez parsovania stringu
                SharedPreferences prefs = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("lastBMI", (float) bmi);
                editor.putString("lastBMItext", bmiDesc);
                editor.apply();
            } else {
                vysledokBMI.setText("Prosím, vyplňte všetky polia.");
                textViewBMI.setText("Nebol vypočítaný BMI.");
            }
        });



    }

    public String returnBMItext(double bmi) {
        if (bmi < 16) {
            cardResult.setCardBackgroundColor(Color.parseColor("#FFEB3B"));
            return "Popis: Ťažká podváha";
        }
        else if(bmi <17){
            cardResult.setCardBackgroundColor(Color.parseColor("#FFF176")); // žltá
            return "Popis: Stredná podváha";
        }
        else if(bmi < 18.5){
            cardResult.setCardBackgroundColor(Color.parseColor("#FFFDE7"));
            return "Popis: Ľahká podváha";
        }
        else if(bmi < 25){
            cardResult.setCardBackgroundColor(Color.parseColor("#A5D6A7"));
            return "Popis: Normálna hmotnosť";
        }
        else if(bmi < 30){
            cardResult.setCardBackgroundColor(Color.parseColor("#FFD54F"));
            return "Popis: Nadváha";
        }
        else if(bmi < 35){
            cardResult.setCardBackgroundColor(Color.parseColor("#EF9A9A"));
            return "Popis: I. stupeň obezity";
        }
        else if(bmi < 40){
            cardResult.setCardBackgroundColor(Color.parseColor("#E57373"));
            return "Popis: II. stupeň obezity";
        }
        else{
            cardResult.setCardBackgroundColor(Color.parseColor("#D32F2F"));
            return "Popis: III. stupeň obezity";
        }
    }
}