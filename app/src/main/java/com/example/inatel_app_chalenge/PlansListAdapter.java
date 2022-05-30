package com.example.inatel_app_chalenge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

interface PlansListAdapterListener {
    void onClick(Plan plan);
}

public class PlansListAdapter extends RecyclerView.Adapter<PlansListAdapter.ViewHolder>{
    private List<Plan> plans;
    private PlansListAdapterListener listener;

    public  PlansListAdapter(PlansListAdapterListener listener) {
        this.listener = listener;
        plans = new ArrayList<>();
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(plans.get(position).isp);
        holder.itemView.setOnClickListener(v -> listener.onClick(plans.get(position)));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textView);
        }
        public TextView getTextView() {
            return textView;
        }
    }
}
