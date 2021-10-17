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
import ca.cmpt276.as3.model.GameOptions;

// This class is responsible for displaying the Options, Help, and Game buttons for the user to navigate to any of their choice.
public class MainMenuActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityOptionsBinding binding;
    private GameOptions gameOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        gameOptions = GameOptions.getInstance();
        setupOptions();
        setupGame();
        setupHelp();
    }

    private void setupHelp() {
        Button btn = findViewById(R.id.help);
        // This class is responsible for launching the Help activity.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = HelpActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupGame() {
        gameOptions.setRows(OptionsActivity.getRows(this));
        gameOptions.setColumns(OptionsActivity.getCols(this));
        gameOptions.setMines(OptionsActivity.getMines(this));
        Button btn = findViewById(R.id.playGame);
        // This class is responsible for launching the Game activity.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = GameActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupOptions() {
        Button btn = findViewById(R.id.options);
        // This class is responsible for launching the Options activity.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OptionsActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }
}