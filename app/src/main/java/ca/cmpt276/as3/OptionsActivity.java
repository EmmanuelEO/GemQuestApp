package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.as3.databinding.ActivityOptions2Binding;
import ca.cmpt276.as3.model.GameOptions;

// This class is responsible for allowing the user to change their game configuration
// and it also provides functionality of resetting the number of games played by the user
// and it gives the user the functionality to clear the best game scores in each configuration.
public class OptionsActivity extends AppCompatActivity {

    private static final String NUM_ROW = "Number of rows";
    private static final String APP_PREFS = "AppPrefs";
    private static final String MINE = "MINE";
    private static final String NUM_COL = "Number of Columns";

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityOptions2Binding binding;

    private GameOptions gameOptions;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOptions2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        gameOptions = GameOptions.getInstance();
        setupRadioButtons();
        setupResetAndClear();
    }

    private void setupResetAndClear() {
        Button button1 = findViewById(R.id.reset);
        // This class is responsible for resetting the number of games played by the user.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.saveGamesPlayed(OptionsActivity.this, 0);
                Toast.makeText(OptionsActivity.this, "The number of games played" +
                        " have now been reset to 0", Toast.LENGTH_LONG).show();
            }
        });

        Button button2 = findViewById(R.id.clear);
        // This class is responsible for clearing the best game scores in each configuration.
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.saveBestScore(OptionsActivity.this, 0, GameOptions.getInstance().getRows(), GameOptions.getInstance().getMines());
                GameActivity.saveBestScore(OptionsActivity.this, 0, GameOptions.getInstance().getRows(), GameOptions.getInstance().getMines());
                GameActivity.saveBestScore(OptionsActivity.this, 0, GameOptions.getInstance().getRows(), GameOptions.getInstance().getMines());
                Toast.makeText(OptionsActivity.this, "The best scores in all the games" +
                        " have now been cleared", Toast.LENGTH_LONG).show();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupRadioButtons() {
        RadioGroup group1 = findViewById(R.id.radio_group1);

        int[] rows = getResources().getIntArray(R.array.board_rows);
        int[] columns = getResources().getIntArray(R.array.board_columns);

        for (int i = 0; i < rows.length; i++) {
            RadioButton button = new RadioButton(this);
            int row = rows[i];
            int column = columns[i];
            String str = rows[i] + " rows x " + columns[i] + " columns";
            button.setText(str);
            button.setTextColor(Color.WHITE);
            button.setTextSize(15);
            button.getButtonDrawable().setColorFilter(getResources().getColor(R.color.high_blue), PorterDuff.Mode.SRC_ATOP);

            // This class is responsible for storing the user's result upon selection of their game's rows x columns configuration.
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameOptions.setRows(row);
                    gameOptions.setColumns(column);
                    saveRows(row);
                    saveColumns(column);
                }
            });

            group1.addView(button);

            if (rows[i] == getRows(this)) {
                button.setChecked(true);
            }
        }

        RadioGroup group2 = findViewById(R.id.radio_group2);
        int[] mines = getResources().getIntArray(R.array.board_mines);

        for (int j : mines) {
            RadioButton button = new RadioButton(this);
            int mine = j;
            button.setText(mine + " gems");
            button.setTextColor(Color.WHITE);
            button.setTextSize(15);
            button.getButtonDrawable().setColorFilter(getResources().getColor(R.color.high_blue), PorterDuff.Mode.SRC_ATOP);

            // This class is responsible for storing the user's result upon selection of their game's mines.
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameOptions.setMines(mine);
                    saveMines(mine);
                }
            });

            group2.addView(button);

            if (j == getMines(this)) {
                button.setChecked(true);
            }
        }
    }

    private void saveRows(int mine) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_ROW, mine);
        editor.apply();
    }

    private void saveColumns(int mine) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_COL, mine);
        editor.apply();
    }

    private void saveMines(int mine) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(MINE, mine);
        editor.apply();
    }

    static public int getRows(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        int defaultRow = context.getResources().getInteger(R.integer.default_row);

        if (prefs.getInt(NUM_ROW, defaultRow) == 0)
            return defaultRow;
        return prefs.getInt(NUM_ROW, defaultRow);
    }

    static public int getCols(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        int defaultColumn = context.getResources().getInteger(R.integer.default_col);

        if (prefs.getInt(NUM_COL, defaultColumn) == 0)
            return defaultColumn;
        return prefs.getInt(NUM_COL, defaultColumn);
    }

    static public int getMines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        int default_mine = context.getResources().getInteger(R.integer.default_mines);

        if (prefs.getInt(MINE, default_mine) == 0)
            return default_mine;
        return prefs.getInt(MINE, default_mine);
    }
}