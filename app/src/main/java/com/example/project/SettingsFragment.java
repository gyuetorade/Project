// chicha/SettingsFragment.java
package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    public SettingsFragment() { super(R.layout.fragment_settings); }
    @Override public void onViewCreated(android.view.View v, @Nullable Bundle b){
        v.findViewById(R.id.btnLogout).setOnClickListener(view -> {
            v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
        });
    }
}