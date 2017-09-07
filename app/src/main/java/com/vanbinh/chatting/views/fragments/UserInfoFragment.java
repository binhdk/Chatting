package com.vanbinh.chatting.views.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vanbinh.chatting.R;
import com.vanbinh.chatting.viewmodels.UserInfoFragmentVM;
import com.vanbinh.chatting.databinding.FragmentUserInfoBinding;

/**
 * Created by vanbinh on 8/14/2017.
 *
 */

public class UserInfoFragment extends Fragment {
    FragmentUserInfoBinding binding;
    UserInfoFragmentVM viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false);
        viewModel=new UserInfoFragmentVM(this,binding);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

}
