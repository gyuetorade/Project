package com.example.project;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Security features text
        TextView securityText = findViewById(R.id.securityText);
        securityText.setText(getSecurityFeaturesText());
    }

    private String getSecurityFeaturesText() {
        return "SECURITY FEATURES\n\n" +
                "1. ACCOUNT PROTECTION\n\n" +
                "- Two-factor authentication available\n" +
                "- Strong password requirements\n" +
                "- Session management and automatic logout\n\n" +
                "2. DATA ENCRYPTION\n\n" +
                "- All data transmitted using SSL/TLS encryption\n" +
                "- Payment information encrypted at rest\n" +
                "- Secure storage of personal data\n\n" +
                "3. PAYMENT SECURITY\n\n" +
                "- PCI DSS compliant payment processing\n" +
                "- No storage of full credit card numbers\n" +
                "- Secure tokenization for recurring payments\n\n" +
                "4. PRIVACY CONTROLS\n\n" +
                "- Granular privacy settings\n" +
                "- Control over data sharing preferences\n" +
                "- Easy account deletion option\n\n" +
                "5. FRAUD PREVENTION\n\n" +
                "- Real-time transaction monitoring\n" +
                "- Suspicious activity detection\n" +
                "- Automated fraud prevention systems\n\n" +
                "6. REGULAR SECURITY UPDATES\n\n" +
                "- Regular security patches and updates\n" +
                "- Continuous security monitoring\n" +
                "- Third-party security audits\n\n" +
                "7. USER EDUCATION\n\n" +
                "- Security best practices guidance\n" +
                "- Phishing awareness information\n" +
                "- Regular security tips and updates";
    }
}