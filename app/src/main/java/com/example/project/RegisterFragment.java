package com.example.project;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import com.example.project.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {


    private FragmentRegisterBinding binding;
    private SharedPreferences prefs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("UserData", requireContext().MODE_PRIVATE);


        binding.btnRegister.setOnClickListener(v -> onRegister());


        return binding.getRoot();
    }


    private void onRegister() {
        String username = binding.etNewUsername.getText().toString().trim();
        String password = binding.etNewPassword.getText().toString().trim();
        String confirm = binding.etConfirmPassword.getText().toString().trim();


        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        if (password.length() < 6) {
            binding.etNewPassword.setError("Password must be at least 6 characters");
            return;
        }


        if (!password.equals(confirm)) {
            binding.etConfirmPassword.setError("Passwords do not match");
            return;
        }


// Save simple credentials (demo only)
        prefs.edit()
                .putString("username", username)
                .putString("password", password)
                .apply();


        Toast.makeText(getContext(), "Registration successful! Please log in.", Toast.LENGTH_SHORT).show();
        switchToLoginTab();
    }


    private void switchToLoginTab() {
        ViewPager2 vp = requireActivity().findViewById(R.id.viewPager);
        if (vp != null) vp.setCurrentItem(0, true); // 0 = Login
    }
}