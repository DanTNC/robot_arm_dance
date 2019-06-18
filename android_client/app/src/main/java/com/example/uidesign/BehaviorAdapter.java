package com.example.uidesign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BehaviorAdapter extends
        RecyclerView.Adapter<BehaviorAdapter.BahaviorViewHolder> implements View.OnClickListener{
    private final LinkedList<Behavior> mBehaviorList;
    private LayoutInflater mInflater;
    private Context mContext;

    private List<View> viewList;
    private OnItemClickListener mOnItemClickListener = null;
    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public BehaviorAdapter(Context context,
                         LinkedList<Behavior> behaviorList) {
        mInflater = LayoutInflater.from(context);
        this.mBehaviorList = behaviorList;
        this.mContext = context;

        viewList = new ArrayList<>();
    }
    @NonNull
    @Override
    public BahaviorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.action_item,
                parent, false);
        mItemView.setOnClickListener(this);
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
    public void onBindViewHolder(@NonNull final BahaviorViewHolder holder, final int position) {
        Behavior element = mBehaviorList.get(position);
        holder.ActionView.setText(element.getAction());
        holder.ValueView.setText(String.valueOf(element.getValue()));

        holder.itemView.setTag(position);

        viewList.add(position, holder.itemView);
        /*
        if(isClicks.get(position)){
            holder.ActionView.setTextColor(Color.parseColor("#ff7f50"));
            holder.ValueView.setTextColor(Color.parseColor("#ff7f50"));
        }else{
            holder.ActionView.setTextColor(Color.parseColor("#000000"));
            holder.ValueView.setTextColor(Color.parseColor("#000000"));
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i = 0; i <isClicks.size();i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
*/
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    @Override
    public int getItemCount() {
        return mBehaviorList.size();
    }
    public View getView(int position){
        return viewList.get(position);
    }
    public void removeItem(int position){
        mBehaviorList.remove(position);
        notifyItemRemoved(position);
    }

}
