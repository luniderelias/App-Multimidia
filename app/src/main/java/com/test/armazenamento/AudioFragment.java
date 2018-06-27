package com.test.armazenamento;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AudioFragment extends Fragment {

    private static final int REQUEST_CODE = 1;

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private Button audioRecordButton;
    private Button audioPlayButton;
    private TextView audioPathTextView;
    boolean isRecording = true;
    boolean isPlaying = true;

    private static String fileName = null;

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;

    public AudioFragment() {
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        audioPathTextView.setText(String.format(getString(R.string.path) + ": %s", fileName));
    }

    private void recording(boolean started) {
        if (started)
            startRecording();
        else
            stopRecording();

    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);

            if (mediaPlayer == null)
                Toast.makeText(getActivity(), getString(R.string.play_error), Toast.LENGTH_SHORT).show();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(getActivity(), getString(R.string.play_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void onPlay(boolean startAudio) {
        if (startAudio)
            startPlaying();
        else
            stopPlaying();

        audioPathTextView.setText(String.format(getString(R.string.path) + ": %s", fileName));
    }

    private void startRecording() {
        setupMediaRecorder();
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.save_error, Toast.LENGTH_SHORT).show();
        }

        mediaRecorder.start();
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CODE);

        audioRecordButton = (Button) view.findViewById(R.id.audioRecordButton);
        audioPlayButton = (Button) view.findViewById(R.id.audioPlayButton);
        audioPathTextView = (TextView) view.findViewById(R.id.audioPathTextView);

        audioRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = Environment.getExternalStorageDirectory() + "/audio.3gp";
                recording(isRecording);

                if (isRecording)
                    audioRecordButton.setText(R.string.stop_recording);
                else
                    audioRecordButton.setText(R.string.start_recording);

                isRecording = !isRecording;
            }
        });

        audioPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(isPlaying);

                if (isPlaying)
                    audioPlayButton.setText(R.string.stop_playing);
                else
                    audioPlayButton.setText(R.string.start_playing);

                isPlaying = !isPlaying;
            }
        });

        view.findViewById(R.id.audioLoadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == MainActivity.RESULT_OK) {
                    try {
                        String[] path = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver()
                                .query(data.getData(), path, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();
                        fileName = cursor.getString(cursor.getColumnIndex(path[0]));

                        onPlay(isPlaying);
                        audioPlayButton.setText(R.string.stop_playing);
                        isPlaying = !isPlaying;
                    } catch (Exception e) {
                        String path = getString(R.string.path) + ": /storage/emulated/0/audio.3gp";
                        audioPathTextView.setText(path);
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
