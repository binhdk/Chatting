package com.vanbinh.chatting.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vanbinh.chatting.models.Message;
import com.vanbinh.chatting.R;

import java.util.List;

/**
 * Created by vanbinh on 8/15/2017.
 *
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    private List<Message> mMessageList;
    public MessageAdapter(List<Message> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View messageView = inflater.inflate(viewType, parent, false);
        return new MessageViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.message.setText(message.getMessage());
        holder.username.setText(message.getUsername());
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (mMessageList.get(position).getUsername().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()))
            viewType = R.layout.item_right_message;
        else
            viewType = R.layout.item_left_message;
        return viewType;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView message, username;

        public MessageViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message_text);
            username = (TextView) itemView.findViewById(R.id.message_user);
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Message message);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickedListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}