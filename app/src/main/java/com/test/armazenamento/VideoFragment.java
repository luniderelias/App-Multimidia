package com.test.armazenamento;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class VideoFragment extends Fragment {

    private static final int REQUEST_CODE = 1;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri videoUri;
    private String videoPath;

    private VideoView videoView;
    private TextView videoPathTextView;

    public VideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        if (ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[1]) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CODE);

        videoView = (VideoView) view.findViewById(R.id.videoView);
        videoPathTextView = (TextView) view.findViewById(R.id.videoPathTextView);

        view.findViewById(R.id.recordVideoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                File videosFolder = new File(Environment.getExternalStorageDirectory(), "videos");
                videosFolder.mkdirs();

                File video = new File(videosFolder, "video.mp4");

                videoPath = video.getPath();

                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(videoIntent, 1);
            }
        });

        view.findViewById(R.id.openVideoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI), 2);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MediaController mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);

        switch (requestCode) {
            case 1:
                if (resultCode == MainActivity.RESULT_OK) {
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                    mediaController.setAnchorView(videoView);

                    videoPathTextView.setText(String.format(getString(R.string.path) + ": %s", videoPath));
                } else
                    Toast.makeText(getActivity(), getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (resultCode == MainActivity.RESULT_OK && data != null) {
                    Uri selectedVideoUri = data.getData();
                    videoView.setVideoURI(selectedVideoUri);
                    videoView.start();
                    mediaController.setAnchorView(videoView);

                    String[] path = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver()
                            .query(selectedVideoUri, path, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(path[0]);
                    String videoPath = cursor.getString(column);
                    videoPathTextView.setText(String.format(getString(R.string.path) + ": %s", videoPath));
                } else
                    Toast.makeText(getActivity(), getString(R.string.load_error), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), getString(R.string.record_error), Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
