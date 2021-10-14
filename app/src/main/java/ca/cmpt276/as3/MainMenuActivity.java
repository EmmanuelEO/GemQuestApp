package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        setupOptions();
    }

    private void setupOptions() {
        Button btn = findViewById(R.id.options);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OptionsActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }
}