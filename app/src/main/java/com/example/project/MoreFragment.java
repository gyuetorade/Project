package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MoreFragment extends Fragment {

    SharedPreferences profile;
    TextView tvName, tvEmail, tvPhone, tvAddress;
    View optionOrders;
    View optionPending;
    View optionFaq;
    View optionHelp;
    ImageView btnBack;
    TextView btnChange;

    public MoreFragment() {
        super(R.layout.fragment_more);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile = requireContext().getSharedPreferences("Profile", requireContext().MODE_PRIVATE);

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnBack = view.findViewById(R.id.btnBack);
        btnChange = view.findViewById(R.id.btnChange);
        optionOrders = view.findViewById(R.id.optionOrders);
        optionPending = view.findViewById(R.id.optionPending);
        optionFaq = view.findViewById(R.id.optionFaq);
        optionHelp = view.findViewById(R.id.optionHelp);

        loadProfileData();

        optionOrders.setOnClickListener(v -> openChildFragment(new OrdersStatusFragment()));
        optionPending.setOnClickListener(v -> openChildFragment(new PendingReviewsFragment()));
        optionFaq.setOnClickListener(v -> openChildFragment(new FAQFragment()));
        optionHelp.setOnClickListener(v -> openChildFragment(new HelpCenterFragment()));

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_home);
                } else {
                    requireActivity().onBackPressed();
                }
            });
        }

        if (btnChange != null) {
            btnChange.setOnClickListener(v -> showEditProfileDialog());
        }
    }

    private void openChildFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showEditProfileDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);

        // Pre-fill with current values
        etName.setText(tvName.getText().toString());
        etEmail.setText(tvEmail.getText().toString());
        etPhone.setText(tvPhone.getText().toString());
        etAddress.setText(tvAddress.getText().toString());

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Personal Details")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    profile.edit()
                            .putString("name", etName.getText().toString())
                            .putString("email", etEmail.getText().toString())
                            .putString("phone", etPhone.getText().toString())
                            .putString("address", etAddress.getText().toString())
                            .apply();
                    loadProfileData();
                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    private void loadProfileData() {
        tvName.setText(profile.getString("name", "Ken Caandoy"));
        tvEmail.setText(profile.getString("email", "kencaandoy@gmail.com"));
        tvPhone.setText(profile.getString("phone", "+63 978 8741 212"));
        tvAddress.setText(profile.getString("address", ""));
    }
}