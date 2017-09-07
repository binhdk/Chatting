package com.vanbinh.chatting.views.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.databinding.FragmentMessageBinding;
import com.vanbinh.chatting.models.Message;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private FragmentMessageBinding binding;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> adapter;
    private LinearLayoutManager mLinearLayoutManager;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        binding.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("messages")
                        .push()
                        .setValue(new Message(binding.editTextMessage.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                binding.editTextMessage.setText(null);
            }
        });
        View  rootView = binding.getRoot();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.item_message,
                MessageViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("messages")) {
            @Override
            public void startListening() {
                super.startListening();
            }

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                if(model!= null) {
                    viewHolder.messageTextView.setText(model.getMessage());
                    viewHolder.messageTimeView.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
                    viewHolder.messengerTextView.setText(model.getUsername());
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    binding.recycleViewMessage.scrollToPosition(positionStart);
                }
            }
            
        });
        binding.recycleViewMessage.setLayoutManager(mLinearLayoutManager);
        binding.recycleViewMessage.setAdapter(adapter);
        return  rootView;
    }
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView messageTimeView;
        TextView messengerTextView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.message_text);
            messageTimeView = (TextView) itemView.findViewById(R.id.message_time);
            messengerTextView = (TextView) itemView.findViewById(R.id.message_user);
        }
    }
}
