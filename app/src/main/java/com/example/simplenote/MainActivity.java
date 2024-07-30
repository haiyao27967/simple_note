package com.example.simplenote;

import android.os.Bundle;
import com.example.simplenote.databinding.FragmentContainerBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}