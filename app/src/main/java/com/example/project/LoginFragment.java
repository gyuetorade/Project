package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("UserData", getContext().MODE_PRIVATE);

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            String savedUser = prefs.getString("username", "");
            String savedPass = prefs.getString("password", "");

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.equals(savedUser) && password.equals(savedPass)) {
                Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                }, 2000);
            } else {
                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}
