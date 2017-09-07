package com.vanbinh.chatting.viewmodels;

import android.widget.ListView;

import com.vanbinh.chatting.views.fragments.UserInfoFragment;
import com.vanbinh.chatting.databinding.FragmentUserInfoBinding;

/**
 * Created by vanbinh on 8/14/2017.
 */

public class UserInfoFragmentVM {
    FragmentUserInfoBinding binding;
    UserInfoFragment fragment;
    public UserInfoFragmentVM(UserInfoFragment fragment, FragmentUserInfoBinding binding) {
        this.binding = binding;
        this.fragment = fragment;
    }
    public void onClickItem(int position){

    }

}
