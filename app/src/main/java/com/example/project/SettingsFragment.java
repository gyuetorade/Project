package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    Button btnLogout, btnProfile, btnOrder, btnOffers, btnPrivacy, btnSecurity;
    ImageView human, mariocart, pricetag, notesnimama, sheildnipapa;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle b) {
        super.onViewCreated(v, b);

        // Initialize buttons
        btnLogout = v.findViewById(R.id.btnLogout);
        btnProfile = v.findViewById(R.id.btnProfile);
        btnOrder = v.findViewById(R.id.btnOrder);
        btnOffers = v.findViewById(R.id.btnOffers);
        btnPrivacy = v.findViewById(R.id.btnPrivacy);
        btnSecurity = v.findViewById(R.id.btnSecurity);

        // Initialize ImageViews
        human = v.findViewById(R.id.human);
        mariocart = v.findViewById(R.id.mariocart);
        pricetag = v.findViewById(R.id.pricetag);
        notesnimama = v.findViewById(R.id.notesnimama);
        sheildnipapa = v.findViewById(R.id.sheildnipapa);

        // Set click listeners for logout button
        btnLogout.setOnClickListener(view -> {
            v.getContext().startActivity(new Intent(v.getContext(), AuthActivity.class));
        });

        // Set click listener for profile button and icon
        btnProfile.setOnClickListener(view -> openProfileActivity());
        human.setOnClickListener(view -> openProfileActivity());

        // Set click listener for order button and icon
        btnOrder.setOnClickListener(view -> openOrderActivity());
        mariocart.setOnClickListener(view -> openOrderActivity());

        // Set click listener for offers button and icon
        btnOffers.setOnClickListener(view -> openListOfferActivity());
        pricetag.setOnClickListener(view -> openListOfferActivity());

        // Set click listener for privacy button and icon
        btnPrivacy.setOnClickListener(view -> openPrivacyActivity());
        notesnimama.setOnClickListener(view -> openPrivacyActivity());

        // Set click listener for security button and icon
        btnSecurity.setOnClickListener(view -> openSecurityActivity());
        sheildnipapa.setOnClickListener(view -> openSecurityActivity());
    }

    // Method to open ProfileActivity
    private void openProfileActivity() {
        Intent intent = new Intent(getActivity(), Profile.class);
        startActivity(intent);
    }

    // Method to open OrderActivity
    private void openOrderActivity() {
        Intent intent = new Intent(getActivity(), OrdersFragment.class);
        startActivity(intent);
    }

    // Method to open ListOfferActivity
    private void openListOfferActivity() {
        Intent intent = new Intent(getActivity(), ListOfferActivity.class);
        startActivity(intent);
    }

    // Method to open PrivacyActivity
    private void openPrivacyActivity() {
        Intent intent = new Intent(getActivity(), PrivacyActivity.class);
        startActivity(intent);
    }

    // Method to open SecurityActivity
    private void openSecurityActivity() {
        Intent intent = new Intent(getActivity(), SecurityActivity.class);
        startActivity(intent);
    }
}