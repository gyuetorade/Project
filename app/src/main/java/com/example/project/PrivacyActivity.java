package com.example.project;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Privacy policy text
        TextView privacyText = findViewById(R.id.privacyText);
        privacyText.setText(getPrivacyPolicyText());
    }

    private String getPrivacyPolicyText() {
        return "PRIVACY POLICY\n\n" +
                "Last Updated: December 2024\n\n" +
                "1. INFORMATION WE COLLECT\n\n" +
                "We collect information you provide directly to us, including:\n" +
                "- Personal information (name, email, phone number)\n" +
                "- Delivery address and location data\n" +
                "- Payment information\n" +
                "- Order history and preferences\n\n" +
                "2. HOW WE USE YOUR INFORMATION\n\n" +
                "We use the information we collect to:\n" +
                "- Process and deliver your orders\n" +
                "- Communicate with you about orders and promotions\n" +
                "- Improve our services and user experience\n" +
                "- Ensure security and prevent fraud\n\n" +
                "3. DATA SHARING\n\n" +
                "We may share your information with:\n" +
                "- Delivery partners to fulfill your orders\n" +
                "- Payment processors to complete transactions\n" +
                "- Legal authorities when required by law\n\n" +
                "4. DATA SECURITY\n\n" +
                "We implement appropriate security measures to protect your personal information.\n\n" +
                "5. YOUR RIGHTS\n\n" +
                "You have the right to:\n" +
                "- Access your personal data\n" +
                "- Correct inaccurate information\n" +
                "- Request deletion of your data\n" +
                "- Opt-out of marketing communications\n\n" +
                "6. CONTACT US\n\n" +
                "For privacy-related questions, contact us at: privacy@foodapp.com";
    }
}