package com.softfireway.windowchange;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Александр on 31.05.2015.
 */
public class EditSongDialog extends DialogFragment implements TextView.OnEditorActionListener {

    public interface EditSongDetailsDialogListener {
        void onFinishEditLyricsDialog(String updatedSongLyrics);
    }

    private EditText mEditSongLyrics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = null;
        switch (getTag()) {
            case "EditSongLyricsDialog":
                getDialog().setTitle("Edit song lyrics");
                v = inflater.inflate(R.layout.add_song_lyrics_dialog, container);
                mEditSongLyrics = (EditText)v.findViewById(R.id.txt_song_lyrics);
                mEditSongLyrics.requestFocus();
                Toast.makeText(getActivity(), getArguments().getString("lyrics_base"), Toast.LENGTH_LONG).show();
                mEditSongLyrics.setText(getArguments().getString("lyrics_base"));
                getDialog().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                mEditSongLyrics.setOnEditorActionListener(this);
                break;
            default:
                break;
        }

        return v;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            EditSongDetailsDialogListener activity = (EditSongDetailsDialogListener) getActivity();
            switch (getTag()) {
                case "EditSongLyricsDialog":
                    activity.onFinishEditLyricsDialog(mEditSongLyrics.getText().toString());
                    this.dismiss();
                    break;
                default:
                    break;
            }
            return true;
        }
        return false;
    }

}
