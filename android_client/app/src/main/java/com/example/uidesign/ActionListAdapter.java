package com.example.uidesign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.ActionViewHolder> {

    private final LayoutInflater mInflater;
    private List<Action> mActions; // Cached copy of words
    private static ClickListener clickListener;
    private Context mContext;
    private ActionViewModel mViewModel;

    ActionListAdapter(Context context, ActionViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mViewModel = viewModel;
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.script_item, parent, false);
        return new ActionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        if (mActions != null) {
            Action current = mActions.get(position);
            holder.nameView.setText(current.getName());
            holder.desView.setText(current.getDescription());
            holder.actions = current.getAction();
            holder.id = current.getId();
        } else {
            // Covers the case of data not being ready yet.
            // holder.wordItemView.setText("No Word");
            holder.nameView.setText("No Title");
            holder.desView.setText("No Description");
            holder.actions = "";
            holder.id = -1;
        }
    }

    /**
     *     Associate a list of words with this adapter
     */

    void setActions(List<Action> actions) {
        mActions = actions;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mActions != null)
            return mActions.size();
        else return 0;
    }

    /**
     * Get the word at a given position.
     * This method is useful for identifying which word
     * was clicked or swiped in methods that handle user events.
     *
     * @param position
     * @return The word at the given position
     */
    public Action getActionAtPosition(int position) {
        return mActions.get(position);
    }

    class ActionViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView desView;
        public String actions;
        public int id;
        public final ImageButton btnRemove;

        private ActionViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.script_title);
            desView = itemView.findViewById(R.id.script_description);
            btnRemove = itemView.findViewById(R.id.btnRemove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ScriptContentActivity.class);
                    String title = nameView.getText().toString();
                    String description = desView.getText().toString();
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    intent.putExtra("actions", actions);
                    intent.putExtra("id", id);
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

    public void setOnItemClickListener(ClickListener clickListener) {
        ActionListAdapter.clickListener = clickListener;
    }

    private void removeItem(int position){
        Action myAction = mActions.get(position);
        Toast.makeText(mContext,
                "Deleting" + " " +
                        myAction.getName(), Toast.LENGTH_LONG).show();

        // Delete the word
        mViewModel.deleteAction(myAction);
        mActions.remove(position);
        notifyItemRemoved(position);
        if(position != getItemCount()){
            notifyItemRangeChanged(position , getItemCount() - position) ;
        }
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}

