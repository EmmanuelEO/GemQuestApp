package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.cmpt276.as3.databinding.ActivityHelpBinding;

// This class is responsible for displaying the game play instructions, game author, and game resources.
public class HelpActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupCourseHyperLink();
    }

    private void setupCourseHyperLink() {
        TextView linkTextView1 = findViewById(R.id.courseLink);
        linkTextView1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkTextView2 = findViewById(R.id.linkImages);
        linkTextView2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}