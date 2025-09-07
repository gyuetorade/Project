package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
    ListView listOptions;
    Button btnUpdate;
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
        listOptions = view.findViewById(R.id.list_options);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnBack = view.findViewById(R.id.btnBack);
        btnChange = view.findViewById(R.id.btnChange);

        loadProfileData();

        String[] menuItems = {"Orders", "Pending reviews", "Faq", "Help"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.list_item_option,
                R.id.tvOption,
                menuItems
        );
        listOptions.setAdapter(adapter);

        listOptions.setOnItemClickListener((parent, itemView, position, id) -> {
            String selected = menuItems[position];
            Toast.makeText(requireContext(), selected + " clicked", Toast.LENGTH_SHORT).show();
        });

        btnUpdate.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Update profile clicked", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_home);
            } else {
                requireActivity().onBackPressed();
            }
        });

        btnChange.setOnClickListener(v -> showEditProfileDialog());
    }

    private void showEditProfileDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);

        TextView etName = dialogView.findViewById(R.id.etName);
        TextView etEmail = dialogView.findViewById(R.id.etEmail);
        TextView etPhone = dialogView.findViewById(R.id.etPhone);
        TextView etAddress = dialogView.findViewById(R.id.etAddress);

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