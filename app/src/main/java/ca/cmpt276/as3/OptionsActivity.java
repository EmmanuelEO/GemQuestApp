package ca.cmpt276.as3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.cmpt276.as3.databinding.ActivityOptions2Binding;

public class OptionsActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityOptions2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOptions2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }
}