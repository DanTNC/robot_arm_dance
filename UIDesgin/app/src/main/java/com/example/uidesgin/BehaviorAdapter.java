package com.example.uidesgin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class BehaviorAdapter extends
        RecyclerView.Adapter<BehaviorAdapter.BahaviorViewHolder>  {
    private final LinkedList<Behavior> mBehaviorList;
    private LayoutInflater mInflater;
    private Context mContext;
    public BehaviorAdapter(Context context,
                         LinkedList<Behavior> behaviorList) {
        mInflater = LayoutInflater.from(context);
        this.mBehaviorList = behaviorList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public BahaviorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.action_item,
                parent, false);
        return new BahaviorViewHolder(mItemView, this);
    }
    class BahaviorViewHolder extends RecyclerView.ViewHolder{
        public final TextView ActionView;
        public final TextView ValueView;
        final BehaviorAdapter mAdapter;

        public BahaviorViewHolder(View itemView, BehaviorAdapter adapter) {
            super(itemView);
            ActionView = itemView.findViewById(R.id.action_name);
            ValueView = itemView.findViewById(R.id.action_value);
            this.mAdapter = adapter;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BahaviorViewHolder holder, int position) {
        Behavior element = mBehaviorList.get(position);
        holder.ActionView.setText(element.getAction());
        holder.ValueView.setText(String.valueOf(element.getValue()));
    }

    @Override
    public int getItemCount() {
        return mBehaviorList.size();
    }
}
