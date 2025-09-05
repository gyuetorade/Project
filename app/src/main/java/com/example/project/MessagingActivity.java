// chicha/MessagingActivity.java
package com.example.project;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class MessagingActivity extends AppCompatActivity {

    LinearLayout chatContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        chatContainer = findViewById(R.id.chatContainer);
        EditText etMessage = findViewById(R.id.etMessage);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (text.isEmpty()) return;
            addBubble(text, true);
            etMessage.setText("");
            // Fake reply
            chatContainer.postDelayed(() -> addBubble("Got it: " + text, false), 800);
        });
    }

    private void addBubble(String text, boolean me) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16f);
        tv.setPadding(24,16,24,16);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 12;
        lp.setMarginStart(me ? 48 : 12);
        lp.setMarginEnd(me ? 12 : 48);
        tv.setBackgroundColor(me ? 0xFFD1F2EB : 0xFFE8EAF6);
        tv.setLayoutParams(lp);
        chatContainer.addView(tv);
    }
}
