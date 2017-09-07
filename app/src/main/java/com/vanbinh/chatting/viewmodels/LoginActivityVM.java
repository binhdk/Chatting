package com.vanbinh.chatting.viewmodels;

import com.vanbinh.chatting.views.activities.LoginActivity;
import com.vanbinh.chatting.databinding.ActivityLoginBinding;

/**
 * Created by vanbinh on 8/10/2017.
 *
 *
 */

public class LoginActivityVM {
    private LoginActivity activity;
    private ActivityLoginBinding binding;

    public LoginActivityVM(LoginActivity activity, ActivityLoginBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }
}
