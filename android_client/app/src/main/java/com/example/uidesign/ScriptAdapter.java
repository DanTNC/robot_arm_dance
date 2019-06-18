package com.example.uidesign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;

public class ScriptAdapter extends
        RecyclerView.Adapter<ScriptAdapter.ScriptViewHolder>  {
    private final LinkedList<Script> mScriptList;
    private LayoutInflater mInflater;
    private Context mContext;
    public ScriptAdapter(Context context,
                           LinkedList<Script> scriptList) {
        mInflater = LayoutInflater.from(context);
        this.mScriptList = scriptList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ScriptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.script_item,
                parent, false);
        return new ScriptViewHolder(mItemView, this);
    }
    class ScriptViewHolder extends RecyclerView.ViewHolder{
        public final TextView TitleView;
        public final TextView DescriptionView;
        public final ImageButton btnRemove;

        final ScriptAdapter mAdapter;

        public ScriptViewHolder(View itemView, ScriptAdapter adapter) {
            super(itemView);
            TitleView = itemView.findViewById(R.id.script_title);
            DescriptionView = itemView.findViewById(R.id.script_description);
            btnRemove = (ImageButton) itemView.findViewById(R.id.btnRemove);
            this.mAdapter = adapter;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ScriptContentActivity.class);
                    String title = TitleView.getText().toString();
                    String description = DescriptionView.getText().toString();
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    mContext.startActivity(intent);
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder DeScript = new AlertDialog.Builder(mContext);
                    DeScript.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 移除項目，getAdapterPosition為點擊的項目位置
                            removeItem(getAdapterPosition());
                        }
                    });
                    DeScript.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    DeScript.setMessage("你確定要刪除這個腳本嗎?");
                    DeScript.setTitle("提示");
                    DeScript.show();

                }
            });
        }


    }
    @Override
    public void onBindViewHolder(@NonNull ScriptViewHolder holder, int position) {
        Script element = mScriptList.get(position);
        holder.TitleView.setText(element.getTitle());
        holder.DescriptionView.setText(element.getDescription());
    }

    @Override
    public int getItemCount() {
        return mScriptList.size();
    }

    private void removeItem(int position){
        mScriptList.remove(position);
        notifyItemRemoved(position);
    }
}

