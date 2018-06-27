package com.test.armazenamento;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PictureFragment extends Fragment {

    private static final int REQUEST_CODE = 1;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private ImageView pictureImageView;
    private TextView pictureTextView;

    public PictureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_picture, container, false);

        if (ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(getActivity(), PERMISSIONS[1]) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CODE);

        pictureImageView = (ImageView) view.findViewById(R.id.pictureImageView);
        pictureTextView = (TextView) view.findViewById(R.id.pictureTextView);

        view.findViewById(R.id.loadPictureButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);
                    }
                });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        view.findViewById(R.id.capturePictureButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File cameraFolder = new File(Environment.getExternalStorageDirectory(), "pictures");
                        cameraFolder.mkdirs();

                        File image = new File(cameraFolder, "image.jpg");

                        Uri pictureUri = FileProvider.getUriForFile(getContext(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                image);


                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                        startActivityForResult(cameraIntent, 1);
                    }
                });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    String imagePath = Environment.getExternalStorageDirectory() + "/pictures/image.jpg";
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    float scaleA = ((float) (height / 2)) / width;
                    float scaleB = ((float) (height / 2)) / height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleA, scaleB);
                    Bitmap newPicture = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    pictureImageView.setImageBitmap(newPicture);
                    pictureTextView.setText(String.format(getString(R.string.path) + ": %s", imagePath));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 2: {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] path = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver()
                            .query(selectedImage, path, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex(path[0]));
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    float scaleA = ((float) (height / 2)) / width;
                    float scaleB = ((float) (height / 2)) / height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleA, scaleB);
                    Bitmap imageBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    pictureImageView.setImageBitmap(imageBitmap);
                    pictureTextView.setText(String.format(getString(R.string.path) + ": %s", imagePath));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.load_error), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: {
                Toast.makeText(getActivity(), getString(R.string.load_error), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
