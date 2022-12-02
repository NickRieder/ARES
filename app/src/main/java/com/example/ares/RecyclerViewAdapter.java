package com.example.ares;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RepairOrder> mDataRo = new ArrayList<>();
    private List<Employee> mDataEm = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private boolean isEmpr;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, Object l, boolean isEmployer) {
        this.isEmpr = isEmployer;
        if (isEmployer) {
            this.mDataEm = (List<Employee>) l;
        }
        else {
            this.mDataRo = (List<RepairOrder>) l;
        }
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isEmpr) {
            String animal = mDataEm.get(position).getFirstName() + " " + mDataEm.get(position).getLastName();
            SpannableString name = new SpannableString(animal);
            name.setSpan(new UnderlineSpan(), 0, name.length(), 0);
            holder.myTextView.setText(name);
        }
        else {
            String animal = Integer.toString(mDataRo.get(position).getNumber());
            SpannableString number = new SpannableString("RO#: " + animal);
            number.setSpan(new UnderlineSpan(), 0, number.length(), 0);
            holder.myTextView.setText(number);
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (isEmpr) {
            if (mDataEm == null) {
                return 0;
            }
            else {
                return mDataEm.size();
            }
        }
        else {
            if (mDataRo == null) {
                return 0;
            }
            else {
                return mDataRo.size();
            }
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvOrderNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        if (isEmpr) {
            return Integer.toString(mDataEm.get(id).getEmployerId());
        }
        else {
            return Integer.toString(mDataRo.get(id).getNumber());
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
