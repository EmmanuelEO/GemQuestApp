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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.as3.databinding.ActivityOptions2Binding;
import ca.cmpt276.as3.model.GameOptions;

public class OptionsActivity extends AppCompatActivity {

    public static final String NUM_ROW = "Number of rows";
    public static final String APP_PREFS = "AppPrefs";
    public static final String MINE = "MINE";
    public static final String NUM_COL = "Number of Columns";

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
            button.setTextSize(15);
            button.getButtonDrawable().setColorFilter(getResources().getColor(R.color.high_blue), PorterDuff.Mode.SRC_ATOP);

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
//                gameOptions.setRows(row);
//                gameOptions.setColumns(column);
                button.setChecked(true);
            }
        }

        RadioGroup group2 = findViewById(R.id.radio_group2);
        int[] mines = getResources().getIntArray(R.array.board_mines);

        for (int i = 0; i < mines.length; i++) {
            RadioButton button = new RadioButton(this);
            int mine = mines[i];
            button.setText(mine + " gems");
            button.setTextColor(Color.WHITE);
            button.setTextSize(15);
            button.getButtonDrawable().setColorFilter(getResources().getColor(R.color.high_blue), PorterDuff.Mode.SRC_ATOP);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameOptions.setMines(mine);
                    saveMines(mine);
                }
            });

            group2.addView(button);

            if (mines[i] == getMines(this)) {
                //gameOptions.setMines(mine);
                button.setChecked(true);
            }
        }
    }

    private void saveRows(int numPanel) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_ROW, numPanel);
        editor.apply();
    }

    private void saveColumns(int numPanel) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_COL, numPanel);
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

        int defaultRowAndColumn = context.getResources().getInteger(R.integer.default_col);

        return prefs.getInt(NUM_ROW, defaultRowAndColumn);
    }

    static public int getCols(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        int defaultColumn = context.getResources().getInteger(R.integer.default_col);

        return prefs.getInt(NUM_COL, defaultColumn);
    }

    static public int getMines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        int default_mine = context.getResources().getInteger(R.integer.default_mines);
        return prefs.getInt(MINE, default_mine);
    }
}