package com.example.drugtrackerapp.ui.medications.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugtrackerapp.R;
import com.example.drugtrackerapp.databinding.RowMedicationBinding;
import com.example.drugtrackerapp.ui.medications.model.DrugItem;

import java.util.ArrayList;
import java.util.List;

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder> {

    private final List<DrugItem> drugList = new ArrayList<>();
    private final Context context;

    public DrugListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowMedicationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_medication, parent, false);
        return new DrugViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DrugViewHolder holder, int position) {
        RowMedicationBinding binding = holder.binding;
        DrugItem drug = drugList.get(position);
        //Handle row radius
        if (position == 0) {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.drawable.top_rounded_white_bg));
        } else if (position == drugList.size() - 1) {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_rounded_white_bg));
        } else {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.color.white));
        }
        //Set data
        binding.tvMedicationName.setText(drug.getName());
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    public void setDrugList(List<DrugItem> newList) {
        drugList.clear();
        if (newList != null) {
            drugList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public static class DrugViewHolder extends RecyclerView.ViewHolder {
        RowMedicationBinding binding;

        public DrugViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
