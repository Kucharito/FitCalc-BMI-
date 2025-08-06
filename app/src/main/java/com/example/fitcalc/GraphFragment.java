package com.example.fitcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class GraphFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        TextView historyTextView = view.findViewById(R.id.historyTextView);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("FitCalcPrefs", Context.MODE_PRIVATE);
        String history = sharedPreferences.getString("bmiHistory", "No history available.");

        historyTextView.setText("História BMI \n" + history);

        return view;
    }


}

