package com.credix.pinpaddriverwithandroidusage;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class AdapterRows extends RecyclerView.Adapter<AdapterRows.ViewHolder> {

    private ArrayList<ArrayList<String>> data;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout col1Holder;
        public LinearLayout col2Holder;
        public LinearLayout col3Holder;

        public ViewHolder(View view) {
            super(view);
            col1Holder = (LinearLayout) view.findViewById(R.id.colsHolder1);
            col2Holder = (LinearLayout) view.findViewById(R.id.colsHolder2);
            col3Holder = (LinearLayout) view.findViewById(R.id.colsHolder3);
        }
    }

    public AdapterRows(ArrayList<ArrayList<String>> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_3_cols, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<String> item = data.get(position);
        holder.col1Holder.setVisibility(View.GONE);
        holder.col2Holder.setVisibility(View.GONE);
        holder.col3Holder.setVisibility(View.GONE);

        LinearLayout layout;

        if (item.size() == 3) {
            layout = holder.col3Holder;
            layout.setVisibility(View.VISIBLE);
            TextView tvCol1 = (TextView) layout.findViewById(R.id.tvCol1);
            TextView tvCol2 = (TextView) layout.findViewById(R.id.tvCol2);
            TextView tvCol3 = (TextView) layout.findViewById(R.id.tvCol3);
            tvCol1.setText(item.get(0));
            tvCol2.setText(item.get(1));
            tvCol3.setText(item.get(2));
        } else if (item.size() == 2) {
            layout = holder.col2Holder;
            layout.setVisibility(View.VISIBLE);
            TextView tvRight = (TextView) layout.findViewById(R.id.tvRight);
            TextView tvLeft = (TextView) layout.findViewById(R.id.tvLeft);
            tvRight.setText(item.get(0));
            tvLeft.setText(item.get(1));
        } else {
            layout = holder.col1Holder;
            layout.setVisibility(View.VISIBLE);
            TextView tvCenter = (TextView) layout.findViewById(R.id.tvCenter);
            tvCenter.setText(item.get(0));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
