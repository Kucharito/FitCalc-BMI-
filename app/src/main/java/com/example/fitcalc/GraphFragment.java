package com.example.fitcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);


        BMIGaugeView bmiGaugeView = view.findViewById(R.id.bmiGauge);
        RecyclerView recyclerView = view.findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("FitCalcPrefs", Context.MODE_PRIVATE);
        String history = sharedPreferences.getString("bmiHistory", "No history available.");


        List<BMIRecord> bmiRecords = new ArrayList<>();
        if (!history.equals("No history available.")) {
            String[] records = history.split(";;");

            for (String record : records) {
                bmiRecords.add(new BMIRecord(record.trim()));
            }
        }

        BMIHistoryAdapter adapter = new BMIHistoryAdapter(bmiRecords);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(record -> {
            float bmiValue = extractBMIFromRecord(record.getValue());
            bmiGaugeView.setBmiValue(bmiValue);
        });

        Button removeHistory;
        removeHistory = view.findViewById(R.id.clearHistoryButton);

        removeHistory.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("bmiHistory");
            editor.apply();

            bmiRecords.clear();
            adapter.notifyDataSetChanged();
            bmiGaugeView.setBmiValue(0.0f);
        });

        return view;
    }

    public float extractBMIFromRecord(String record) {
        try {
            String number = record.split("BMI score:")[1]
                    .trim()
                    .split("\\s+")[0]
                    .replace(",", ".");
            return Float.parseFloat(number);
        } catch (Exception e) {
            return 0.0f;
        }
    }



}

