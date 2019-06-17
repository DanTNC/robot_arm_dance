package com.example.gaoranger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.ActionViewHolder> {

    private final LayoutInflater mInflater;
    private List<Action> mActions; // Cached copy of words
    private static ClickListener clickListener;

    ActionListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ActionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        if (mActions != null) {
            Action current = mActions.get(position);
            holder.nameView.setText(current.getName());

        } else {
            // Covers the case of data not being ready yet.
            // holder.wordItemView.setText("No Word");
            holder.nameView.setText("No Action");

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

        private ActionViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ActionListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}

