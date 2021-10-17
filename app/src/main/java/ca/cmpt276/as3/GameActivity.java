package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.as3.databinding.ActivityGameBinding;
import ca.cmpt276.as3.model.GameOptions;

public class GameActivity extends AppCompatActivity {

    private GameOptions gameOptions;
    private int NUM_ROWS, NUM_COLS;
    private int minesFound, scans, clicks, empty_cell_clicks;
    private Button[][] buttons;
    private boolean[][] foundItems;
    private boolean[][] revealedCells;
    private boolean[][] setCells;
    private int[] mine_rows;
    private int[] mine_cols;
    private MediaPlayer successSound;
    private MediaPlayer scanSound;
    private static final String GAME_APP_PREFS = "GameAppPrefs";
    public static final String[] bestScores = {
            "Grid 1 - 6 gems", "Grid 1 - 10 gems", "Grid 1 - 15 gems",
            "Grid 1 - 20 gems", "Grid 2 - 6 gems", "Grid 2 - 10 gems",
            "Grid 2 - 15 gems", "Grid 2 - 20 gems", "Grid 3 - 6 gems",
            "Grid 3 - 10 gems", "Grid 3 - 15 gems", "Grid 3 - 20 gems"};


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

        successSound = MediaPlayer.create(this, R.raw.success_sound);
        scanSound = MediaPlayer.create(this, R.raw.scan_sound);

        gameOptions = GameOptions.getInstance();
        NUM_COLS = gameOptions.getColumns();
        NUM_ROWS = gameOptions.getRows();
        buttons = new Button[NUM_ROWS][NUM_COLS];
        foundItems = new boolean[NUM_ROWS][NUM_COLS];
        revealedCells = new boolean[NUM_ROWS][NUM_COLS];
        setCells = new boolean[NUM_ROWS][NUM_COLS];
        minesFound = 0;
        scans = 0;
        clicks = 0;
        empty_cell_clicks = 0;
        populateButtons();
        populateMines();
        populateBestScore();
    }

    private void populateBestScore() {
        TextView textView;
        String str = "" + getBestScore(this, NUM_ROWS, gameOptions.getMines());
        if (NUM_ROWS == 4) {
            textView = findViewById(R.id.bestScore);
            textView.setText(str);
        }

        else if (NUM_ROWS == 5) {
            textView = findViewById(R.id.bestScore);
            textView.setText(str);
        }

        else if (NUM_ROWS == 6) {
            textView = findViewById(R.id.bestScore);
            textView.setText(str);
        }

        TextView textView1 = findViewById(R.id.maxScore);
        str = "" + (NUM_ROWS * NUM_COLS * 1000);
        textView1.setText(str);
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
        TextView textView1 = findViewById(R.id.totalGems);
        textView1.setText("" + gameOptions.getMines());

        TextView textView2 = findViewById(R.id.gamesPlayed);
        textView2.setText("" + MainActivity.getGamesPlayed(this));

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
                button.setTextColor(Color.WHITE);
                button.setTextSize(16);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!setCells[FINAL_ROW][FINAL_COL])
                            clicks++;
                        showMines(FINAL_ROW, FINAL_COL);
                    }
                });
                buttons[row][col] = button;
            }
        }
    }

    private void showMines(int finalRow, int finalColumn) {
        int bestScore, currentScore;
        lockButtonSizes();
        Button button = buttons[finalRow][finalColumn];

        if (foundItems[finalRow][finalColumn] && !revealedCells[finalRow][finalColumn]) {
            successSound.start();

            int newWidth = button.getWidth();
            int newHeight = button.getHeight();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gem);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
            button.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

            TextView textView = findViewById(R.id.numGem);
            textView.setText("" + ++minesFound);

            // Setting the current score
            if (clicks == 1) {
                textView = findViewById(R.id.currentScore);
                String str = "" + ((NUM_ROWS * NUM_COLS * 1000) - (gameOptions.getMines() * 1000));
                textView.setText(str);
            }

            revealedCells[finalRow][finalColumn] = true;

            if (allGemsRevealed()) {
                // For the first grid
                if (NUM_ROWS == 4) {
                    bestScore = getBestScore(this, NUM_ROWS, gameOptions.getMines());
                    TextView textView1 = findViewById(R.id.currentScore);
                    currentScore = Integer.parseInt(textView1.getText().toString());
                    if (currentScore > bestScore) {
                        textView1 = findViewById(R.id.bestScore);
                        textView1.setText("" + currentScore);
                        saveBestScore(this, currentScore, NUM_ROWS, gameOptions.getMines());
                    }
                }

                // For the second grid
                else if (NUM_ROWS == 5) {
                    bestScore = getBestScore(this, NUM_ROWS, gameOptions.getMines());
                    TextView textView1 = findViewById(R.id.currentScore);
                    currentScore = Integer.parseInt(textView1.getText().toString());
                    if (currentScore > bestScore) {
                        textView1 = findViewById(R.id.bestScore);
                        textView1.setText("" + currentScore);
                        saveBestScore(this, currentScore, NUM_ROWS, gameOptions.getMines());
                    }
                }

                // For the third grid
                else if (NUM_ROWS == 6) {
                    bestScore = getBestScore(this, NUM_ROWS, gameOptions.getMines());
                    TextView textView1 = findViewById(R.id.currentScore);
                    currentScore = Integer.parseInt(textView1.getText().toString());
                    if (currentScore > bestScore) {
                        textView1 = findViewById(R.id.bestScore);
                        textView1.setText("" + currentScore);
                        saveBestScore(this, currentScore, NUM_ROWS, gameOptions.getMines());
                    }
                }
                setupCongratsMessage();
            }

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
            if (!setCells[finalRow][finalColumn]) {
                setNumberOfMines(finalRow, finalColumn, revealedCells, foundItems);
                if (!allGemsRevealed()) {
                    scans++;
                    scanSound = MediaPlayer.create(this, R.raw.scan_sound);
                    scanSound.start();
                    TextView textView1 = findViewById(R.id.currentScore);
                    String str = "" + ((NUM_ROWS * NUM_COLS * 1000) - (scans * 1000));
                    textView1.setText(str);
                    TextView textView = findViewById(R.id.numScans);
                    textView.setText("" + scans);
                    setCells[finalRow][finalColumn] = true;
                }
            }
        }
    }

    private void setupCongratsMessage() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                MessageFragment dialog = new MessageFragment();
                dialog.show(manager, "MessageDialog");
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    // If all games are revealed, the user cannot continue clicking

    private boolean allGemsRevealed() {
        int count = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (revealedCells[i][j]) {
                    count++;
                    if (count == gameOptions.getMines()) return true;
                }
            }
        }
        return false;
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
        String str = "" + numHiddenMines;
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), 0);
        button.setText(spannableString);
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

    public static void saveBestScore(Context context, int score, int rows, int mines) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (rows == 4 && mines == 6) {
            editor.putInt(bestScores[0], score);
            editor.apply();
        } else if (rows == 4 && mines == 10) {
            editor.putInt(bestScores[1], score);
            editor.apply();
        } else if (rows == 4 && mines == 15) {
            editor.putInt(bestScores[2], score);
            editor.apply();
        } else if (rows == 4 && mines == 20) {
            editor.putInt(bestScores[3], score);
            editor.apply();
        } else if (rows == 5 && mines == 6) {
            editor.putInt(bestScores[4], score);
            editor.apply();
        } else if (rows == 5 && mines == 10) {
            editor.putInt(bestScores[5], score);
            editor.apply();
        } else if (rows == 5 && mines == 15) {
            editor.putInt(bestScores[6], score);
            editor.apply();
        } else if (rows == 5 && mines == 20) {
            editor.putInt(bestScores[7], score);
            editor.apply();
        } else if (rows == 6 && mines == 6) {
            editor.putInt(bestScores[8], score);
            editor.apply();
        } else if (rows == 6 && mines == 10) {
            editor.putInt(bestScores[9], score);
            editor.apply();
        } else if (rows == 6 && mines == 15) {
            editor.putInt(bestScores[10], score);
            editor.apply();
        } else if (rows == 6 && mines == 20) {
            editor.putInt(bestScores[11], score);
            editor.apply();
        }
    }

    public static int getBestScore(Context context, int rows, int mines) {
        SharedPreferences prefs = context.getSharedPreferences(GAME_APP_PREFS, MODE_PRIVATE);
        int defaultScore = 0;
        if (rows == 4 && mines == 6) {
            return prefs.getInt(bestScores[0], defaultScore);
        } else if (rows == 4 && mines == 10) {
            return  prefs.getInt(bestScores[1], defaultScore);
        } else if (rows == 4 && mines == 15) {
            return prefs.getInt(bestScores[2], defaultScore);
        } else if (rows == 4 && mines == 20) {
            return  prefs.getInt(bestScores[3], defaultScore);
        } else if (rows == 5 && mines == 6) {
            return prefs.getInt(bestScores[4], defaultScore);
        } else if (rows == 5 && mines == 10) {
            return prefs.getInt(bestScores[5], defaultScore);
        } else if (rows == 5 && mines == 15) {
            return prefs.getInt(bestScores[6], defaultScore);
        } else if (rows == 5 && mines == 20) {
            return prefs.getInt(bestScores[7], defaultScore);
        } else if (rows == 6 && mines == 6) {
            return prefs.getInt(bestScores[8], defaultScore);
        } else if (rows == 6 && mines == 10) {
            return prefs.getInt(bestScores[9], defaultScore);
        } else if (rows == 6 && mines == 15) {
            return prefs.getInt(bestScores[10], defaultScore);
        } else {
            return prefs.getInt(bestScores[11], defaultScore);
        }
    }
}