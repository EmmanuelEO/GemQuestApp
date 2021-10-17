package ca.cmpt276.as3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

// This class is responsible for the alert dialog's message when a user wins a game.
public class MessageFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.message_layout, null);

        // This class is responsible for creating a button listener for the user to make a choice.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) getActivity().finish();
            }
        };

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Congrats, you found all the GEMS!")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}
