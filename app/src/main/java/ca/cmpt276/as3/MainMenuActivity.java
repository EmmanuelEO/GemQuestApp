package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.as3.databinding.ActivityOptionsBinding;

public class MainMenuActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityOptionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }
}