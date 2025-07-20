package com.example.drugtrackerapp.ui.medications.view.adapter;

import android.annotation.SuppressLint;
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
    private final onItemClickListener listener;
    private final boolean isHideIcArrow;

    public DrugListAdapter(Context context, boolean isHideIcArrow, onItemClickListener listener) {
        this.context = context;
        this.isHideIcArrow = isHideIcArrow;
        this.listener = listener;
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
        //Hide arrow
        binding.icArrow.setVisibility(isHideIcArrow ? View.GONE : View.VISIBLE);
        //Handle row radius
        if (drugList.size() > 1 && position == 0) {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.drawable.top_rounded_white_bg));
            binding.vBorder.setVisibility(View.VISIBLE);
        } else if (drugList.size() == 1) {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_white_bg));
            binding.vBorder.setVisibility(View.GONE);
        } else if (position == drugList.size() - 1) {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_rounded_white_bg));
            binding.vBorder.setVisibility(View.GONE);
        } else {
            binding.llMedication.setBackground(ContextCompat.getDrawable(context, R.color.white));
            binding.vBorder.setVisibility(View.VISIBLE);
        }
        //Set data
        binding.tvMedicationName.setText(drug.getName());
        //Click
        binding.llMedication.setOnClickListener(v -> listener.onItemClick(drug));

    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    public DrugItem getItem(int pos) {
        return drugList.get(pos);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDrugList(List<DrugItem> newList) {
        drugList.clear();
        if (newList != null) {
            drugList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        void onItemClick(DrugItem drugItem);
    }

    public static class DrugViewHolder extends RecyclerView.ViewHolder {
        RowMedicationBinding binding;

        public DrugViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
