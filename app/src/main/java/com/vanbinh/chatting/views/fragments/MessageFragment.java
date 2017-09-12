package com.vanbinh.chatting.views.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.databinding.FragmentMessageBinding;
import com.vanbinh.chatting.models.Message;
import com.vanbinh.chatting.views.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private FragmentMessageBinding binding;
    private List<Message> mMessageList;
    private View rootView;
//        private FirebaseRecyclerAdapter<Message, MessageViewHolder> adapter;

    private MessageAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference reference;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        init();
        binding.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.push()
                        .setValue(new Message(binding.editTextMessage.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                binding.editTextMessage.setText(null);
            }
        });

        rootView = binding.getRoot();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new MessageAdapter(mMessageList);

//        adapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
//                Message.class,
//                R.layout.item_message,
//                MessageViewHolder.class,
//                FirebaseDatabase.getInstance().getReference().child("messages")) {
//            @Override
//            public void startListening() {
//                super.startListening();
//            }
//
//            @Override
//            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
//                if(model!= null) {
//                    int layoutResource;
//                    if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(model.getUsername()))
//                        layoutResource = R.layout.item_right_message;
//                    else
//                        layoutResource=R.layout.item_left_message;
//                    View view = inflater.inflate(layoutResource,container,false);
//                    viewHolder = new MessageViewHolder(view);
//                    viewHolder.messageTextView.setText(model.getMessage());
//                    viewHolder.messengerTextView.setText(model.getUsername());
//                }
//            }
//        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = adapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    binding.recycleViewMessage.scrollToPosition(positionStart);
                }
            }
        });
        binding.recycleViewMessage.setLayoutManager(mLinearLayoutManager);
        binding.recycleViewMessage.setAdapter(adapter);
        return rootView;
    }

    private void init() {
        mMessageList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("messages");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                mMessageList.add(message);
                adapter.notifyItemInserted(mMessageList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                mMessageList.remove(message);
                adapter.notifyItemRemoved(mMessageList.size());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    private static class MessageViewHolder extends RecyclerView.ViewHolder {
//        TextView messageTextView;
//        TextView messageTimeView;
//        TextView messengerTextView;
//
//        public MessageViewHolder(View v) {
//            super(v);
//            messageTextView = (TextView) v.findViewById(R.id.message_text);
//            messengerTextView = (TextView) v.findViewById(R.id.message_user);
//        }
//    }
}
