package com.vanbinh.chatting.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.models.Message;
import com.vanbinh.chatting.views.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private List<Message> mMessageList;
    private View rootView;
    private MessageAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference reference;
    private ImageView btnSendMessage;
    private EditText editTextMessage;
    private RecyclerView recycleViewMessage;

    public MessageFragment() {

    }

    @Override

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, container, false);
        init();
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.push()
                        .setValue(new Message(editTextMessage.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                editTextMessage.setText(null);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = adapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    recycleViewMessage.scrollToPosition(positionStart);
                }
            }
        });
        recycleViewMessage.setLayoutManager(mLinearLayoutManager);
        recycleViewMessage.setAdapter(adapter);
        return rootView;
    }

    private void init() {
        btnSendMessage = (ImageView) rootView.findViewById(R.id.btn_send_message);
        editTextMessage = (EditText) rootView.findViewById(R.id.edit_text_message);
        recycleViewMessage = (RecyclerView) rootView.findViewById(R.id.recycle_view_message);
        mMessageList = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new MessageAdapter(mMessageList);
        reference = FirebaseDatabase.getInstance().getReference().child("messages");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                mMessageList.add(message);
                adapter.notifyItemInserted(mMessageList.size() - 1);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
