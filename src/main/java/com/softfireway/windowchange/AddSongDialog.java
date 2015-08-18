package com.softfireway.windowchange;



import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


/**
 * Created by Александр on 26.03.2015.
 */
public class AddSongDialog extends DialogFragment implements TextView.OnEditorActionListener {



    public interface EditSongDetailsDialogListener {
        void onFinishEditGroupNameDialog(String inputText, ArrayList<String> perf_list, int performer_type);
        void onFinishEditSongNameDialog(String inputSongName);
        void onFinishEditSongLyricsDialog(String inputSongLyrics);
    }

    private EditText mEditSongName;
    private EditText mEditSongLyrics;
    private EditText mEditGroupName;
    private RadioGroup mRadioPerfType;
    private Button mButtonAddPerformer;
    private int performer_type;
    private ListView mListPerformers;
    public ArrayList<String> performers_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
     //   Toast.makeText(this.getActivity(), getTag(), Toast.LENGTH_SHORT).show();
        switch (getTag()) {
            case "AddSongNameDialog":
                getDialog().setTitle("Song Name Editor");
                v = inflater.inflate(R.layout.add_song_name_dialog, container);
                mEditSongName = (EditText)v.findViewById(R.id.txt_song_name);
                mEditSongName.requestFocus();
                getDialog().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                mEditSongName.setOnEditorActionListener(this);
                mEditSongName.setText(((TextView)getActivity().findViewById(R.id.tv_song_name)).getText());
                break;
            case "AddSongLyricsDialog":
                getDialog().setTitle("Song Lyrics Editor");
                v = inflater.inflate(R.layout.add_song_lyrics_dialog, container);
                mEditSongLyrics = (EditText)v.findViewById(R.id.txt_song_lyrics);
                mEditSongLyrics.requestFocus();
                getDialog().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                mEditSongLyrics.setOnEditorActionListener(this);
                mEditSongLyrics.setText(((TextView)getActivity().findViewById(R.id.tv_song_lyrics)).getText());
                break;
            case "AddGroupNameDialog":
                getDialog().setTitle("Group Editor");
                v = inflater.inflate(R.layout.add_group_dialog, container);

                mRadioPerfType = (RadioGroup)v.findViewById(R.id.radio_performer_type);
                mEditGroupName = (EditText)v.findViewById(R.id.txt_group_name);
                mListPerformers = (ListView)v.findViewById(R.id.lv_performers);
                performers_list = getArguments().getStringArrayList("perf_list");
                performer_type = getArguments().getInt("perf_type");

                final ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<>(getDialog().getContext(), android.R.layout.simple_list_item_1, performers_list);
                mListPerformers.setAdapter(adapter);

                mButtonAddPerformer = (Button)v.findViewById(R.id.btn_add_performer);
                mButtonAddPerformer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performers_list.add(0, "test");
                        adapter.notifyDataSetChanged();
                    }
                });

                SetPerformersTypeToDisplay();


                mRadioPerfType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radio_solo:
                                performer_type = 1;
                                mButtonAddPerformer.setEnabled(false);
                                mListPerformers.setVisibility(View.GONE);
                                break;
                            case R.id.radio_group:
                                performer_type = 2;
                                mButtonAddPerformer.setEnabled(true);
                                mListPerformers.setVisibility(View.VISIBLE);
                                break;
                            default:
                                break;
                        }
                    }
                });

                mEditGroupName.requestFocus();
                getDialog().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                mEditGroupName.setOnEditorActionListener(this);
                mEditGroupName.setText(((TextView) getActivity().findViewById(R.id.tv_performer)).getText());
                break;
            default:
                break;
        }
        return v;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            EditSongDetailsDialogListener activity = (EditSongDetailsDialogListener)getActivity();
            switch (getTag()) {
                case "AddGroupNameDialog":
                    activity.onFinishEditGroupNameDialog(mEditGroupName.getText().toString(), performers_list, performer_type) ;
                    this.dismiss();
                    break;
                case "AddSongLyricsDialog":
                    activity.onFinishEditSongLyricsDialog(mEditSongLyrics.getText().toString());
                    this.dismiss();
                    break;
                case "AddSongNameDialog":
                    activity.onFinishEditSongNameDialog(mEditSongName.getText().toString());
                    this.dismiss();
                    break;
                default:
                    break;
            }
            return true;
        }
        return false;
    }

    public void SetPerformersTypeToDisplay() {
        switch (performer_type) {
            case 1:
                mRadioPerfType.check(R.id.radio_solo);
                mButtonAddPerformer.setEnabled(false);
                mListPerformers.setVisibility(View.GONE);
                break;
            case 2:
                mRadioPerfType.check(R.id.radio_group);
                mButtonAddPerformer.setEnabled(true);
                mListPerformers.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
