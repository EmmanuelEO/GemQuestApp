package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.as3.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private int gamesPlayed;
    public static final String MAIN_APP_PREFS = "MainAppPrefs";
    public static final String NUM_GAMES_PLAYED = "Number of games played";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        gamesPlayed = getGamesPlayed(this);
        setupAnimations();
        setupMainMenu();
        setupSkip();
        saveGamesPlayed(this, ++gamesPlayed);
    }

    private void setupSkip() {
        Button btn = findViewById(R.id.skip);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainMenuActivity.makeIntent(MainActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void saveGamesPlayed(Context context, int gamesPlayed) {
        SharedPreferences prefs = context.getSharedPreferences(MAIN_APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_GAMES_PLAYED, gamesPlayed);
        editor.apply();
    }

    public static int getGamesPlayed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MAIN_APP_PREFS, MODE_PRIVATE);
        int defaultValue = 0;
        return prefs.getInt(NUM_GAMES_PLAYED, defaultValue);
    }

    private void setupAnimations() {
        TextView textView = findViewById(R.id.intro);
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        textView.startAnimation(slideDown);

        TextView textView1 = findViewById(R.id.gemQuest);
        Animation zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        textView1.startAnimation(zoomIn);

        TextView textView2 = findViewById(R.id.text_1);
        Animation leftToRight1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lefttoright1);
        textView2.startAnimation(leftToRight1);

        TextView textView3 = findViewById(R.id.text_2);
        Animation leftToRight2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lefttoright2);
        textView3.startAnimation(leftToRight2);

        TextView textView4 = findViewById(R.id.name_0);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        textView4.startAnimation(fadeIn);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Button button = findViewById(R.id.skip);
                button.setVisibility(View.GONE);
                Button button1 = findViewById(R.id.mainMenu);
                button1.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(runnable, 13500);

        Button button = findViewById(R.id.mainMenu);
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        button.startAnimation(slideUp);
    }

    private void setupMainMenu() {
        Button btn = findViewById(R.id.mainMenu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainMenuActivity.makeIntent(MainActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}