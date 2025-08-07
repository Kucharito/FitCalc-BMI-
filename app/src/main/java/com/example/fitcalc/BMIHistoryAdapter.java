package com.example.fitcalc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BMIHistoryAdapter extends RecyclerView.Adapter<BMIHistoryAdapter.ViewHolder>
{
    private final List<BMIRecord> bmiRecords;

    public BMIHistoryAdapter(List<BMIRecord> bmiRecords) {
        this.bmiRecords = bmiRecords;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.bmiItemText);
        }
    }

    @Override
    public BMIHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bmi_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BMIRecord record = bmiRecords.get(position);
        holder.textView.setText(record.getValue());
    }

    @Override
    public int getItemCount() {
        return bmiRecords.size();
    }

}
