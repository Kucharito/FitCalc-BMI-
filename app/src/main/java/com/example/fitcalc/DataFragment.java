package com.example.fitcalc;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {

    private EditText vaha;
    private EditText vyska;
    private EditText vek;
    private Button buttonBMI;
    private TextView vysledokBMI;
    private TextView textViewBMI;
    private CardView cardResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.data_fragment, container, false);


        vaha = view.findViewById(R.id.inputWeight);
        vyska = view.findViewById(R.id.inputHeight);
        vek = view.findViewById(R.id.inputAge);
        buttonBMI = view.findViewById(R.id.buttonBMI);
        vysledokBMI = view.findViewById(R.id.textView4);
        textViewBMI = view.findViewById(R.id.textView5);
        cardResult = view.findViewById(R.id.cardResult);


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);

        String savedWeight = sharedPreferences.getString("inputWeight", "");
        String savedHeight = sharedPreferences.getString("inputHeight", "");
        String savedAge = sharedPreferences.getString("inputAge", "");

        // Obnovenie vstupných polí z uložených hodnôt
        vaha.setText(savedWeight);
        vyska.setText(savedHeight);
        vek.setText(savedAge);


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


        buttonBMI.setOnClickListener(v -> {
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

                SharedPreferences prefs = requireContext().getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String existingHistory = prefs.getString("bmiHistory", "");
                String oneRecord = String.format("BMI score: %.2f \n%s\nVáha: %s\nVýška: %s\nVek: %s", bmi, bmiDesc, vahaText, vyskaText, vekText);
                String updatedHistory = existingHistory + oneRecord + ";;";


                editor.putFloat("lastBMI", (float) bmi);
                editor.putString("lastBMItext", bmiDesc);
                editor.putString("bmiHistory", updatedHistory);

                editor.putString("inputWeight", vahaText);
                editor.putString("inputHeight", vyskaText);
                editor.putString("inputAge", vekText);

                editor.apply();
            } else {
                vysledokBMI.setText("Prosím, vyplňte všetky polia.");
                textViewBMI.setText("Nebol vypočítaný BMI.");
            }
        });
        return view;
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
