package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.navigation.ui.AppBarConfiguration;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.as3.databinding.ActivityGameBinding;
import ca.cmpt276.as3.model.GameOptions;

public class GameActivity extends AppCompatActivity {

    private GameOptions gameOptions;
    private int NUM_ROWS, NUM_COLS;
    private Button[][] buttons;
    private boolean[][] foundItems;
    private boolean[][] revealedCells;
    private boolean[][] setCells;
    private int[] mine_rows;
    private int[] mine_cols;

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        gameOptions = GameOptions.getInstance();
        NUM_COLS = gameOptions.getColumns();
        NUM_ROWS = gameOptions.getRows();
        buttons = new Button[NUM_ROWS][NUM_COLS];
        foundItems = new boolean[NUM_ROWS][NUM_COLS];
        revealedCells = new boolean[NUM_ROWS][NUM_COLS];
        setCells = new boolean[NUM_ROWS][NUM_COLS];
        populateButtons();
        populateMines();
    }

    private void populateMines() {
        List<Integer> row_list, col_list;
        row_list = new ArrayList<>();
        col_list = new ArrayList<>();
        int rows = gameOptions.getRows();
        int cols = gameOptions.getColumns();
        int mines = gameOptions.getMines();
        int i = 0;
        do {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            if (!(findItem(row_list, col_list, row, col))) {
                row_list.add(row);
                col_list.add(col);
                foundItems[row][col] = true;
                i++;
            }
        } while (mines != i);
    }

    private boolean findItem(List<Integer> list1, List<Integer> list2, int row, int col) {
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i) == row && list2.get(i) == col) return true;
        }
        return false;
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.gameTable);
        for (int row = 0; row < NUM_ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            for (int col = 0; col < NUM_COLS; col++) {
                final int FINAL_ROW = row;
                final int FINAL_COL = col;
                Button button = new Button(this);
                tableRow.addView(button);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                button.getBackground().setColorFilter(Color.parseColor("#889EAF"), PorterDuff.Mode.SRC_ATOP);
                button.setPadding(0, 0, 0, 0);
                button.setTextColor(getResources().getColor(R.color.high_green));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMines(FINAL_ROW, FINAL_COL);
                    }
                });
                buttons[row][col] = button;
            }
        }
    }

    private void showMines(int finalRow, int finalColumn) {
        lockButtonSizes();
        Button button = buttons[finalRow][finalColumn];
        if (foundItems[finalRow][finalColumn] && !revealedCells[finalRow][finalColumn]) {
            int newWidth = button.getWidth();
            int newHeight = button.getHeight();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue_gem);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
            button.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

            revealedCells[finalRow][finalColumn] = true;
            for (int i = 0; i < setCells.length; i++) {
                if (setCells[i][finalColumn]) {
                    setNumberOfMines(i, finalColumn, revealedCells, foundItems);
                }
            }
            for (int j = 0; j < setCells[1].length; j++) {
                if (setCells[finalRow][j]) {
                    setNumberOfMines(finalRow, j, revealedCells, foundItems);
                }
            }

        } else {
            setNumberOfMines(finalRow, finalColumn, revealedCells, foundItems);
            setCells[finalRow][finalColumn] = true;
        }
    }

    private void setNumberOfMines(int finalRow, int finalColumn, boolean[][] arr1, boolean[][] arr2) {
        Button button = buttons[finalRow][finalColumn];
        int numHiddenMines = 0;
        for (int i = 0; i < NUM_COLS; i++) {
            if (!arr1[finalRow][i] && arr2[finalRow][i]) {
                numHiddenMines++;
            }
        }

        for (int j = 0; j < NUM_ROWS; j++) {
            if (!arr1[j][finalColumn] && arr2[j][finalColumn]) {
                numHiddenMines++;
            }
        }
        button.setText("" + numHiddenMines);
    }

    private void lockButtonSizes() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMaxHeight(height);
                button.setMinHeight(height);
            }
        }
    }
}