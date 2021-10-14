package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.as3.databinding.ActivityOptions2Binding;
import ca.cmpt276.as3.model.GameOptions;

public class OptionsActivity extends AppCompatActivity {

    public static final String NUM_ROW_COL = "Number of rows and Columns";
    public static final String APP_PREFS = "AppPrefs";
    public static final String MINE = "MINE";

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
            button.setText(rows[i] + " rows x " + columns[i] + " columns");
            button.setTextColor(Color.WHITE);
            button.getButtonDrawable().setColorFilter(getResources().getColor(R.color.high_blue), PorterDuff.Mode.SRC_ATOP);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameOptions.setRows(row);
                    gameOptions.setColumns(column);
                    saveRowsAndColumns(row);
                }
            });

            group1.addView(button);

            if (rows[i] == getRowsAndColumns(this))
                button.setChecked(true);
        }

        RadioGroup group2 = findViewById(R.id.radio_group2);
        int[] mines = getResources().getIntArray(R.array.board_mines);

        for (int i = 0; i < mines.length; i++) {
            RadioButton button = new RadioButton(this);
            int mine = mines[i];
            button.setText(mine + " mines");
            button.setTextColor(Color.WHITE);
            button.getButtonDrawable().setColorFilter(getResources().getColor(R.color.high_blue), PorterDuff.Mode.SRC_ATOP);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameOptions.setMines(mine);
                    saveMines(mine);
                }
            });

            group2.addView(button);

            if (mines[i] == getMines(this))
                button.setChecked(true);
        }
    }

    private void saveRowsAndColumns(int numPanel) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_ROW_COL, numPanel);
        editor.apply();
    }

    private void saveMines(int mine) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(MINE, mine);
        editor.apply();
    }

    static public int getRowsAndColumns(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        int defaultRowAndColumn = context.getResources().getInteger(R.integer.default_row_col);

        return prefs.getInt(NUM_ROW_COL, defaultRowAndColumn);
    }

    static public int getMines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        int default_mine = context.getResources().getInteger(R.integer.default_mines);
        return prefs.getInt(MINE, default_mine);
    }
}