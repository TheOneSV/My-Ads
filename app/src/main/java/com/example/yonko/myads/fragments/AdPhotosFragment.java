package com.example.yonko.myads.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.yonko.myads.R;
import com.example.yonko.myads.adapters.ImageGridViewAdapter;
import com.example.yonko.myads.interfaces.OnFragmentActionListener;
import com.example.yonko.myads.model.AdvertisingImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AdPhotosFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String SENDER_ID = AdPhotosFragment.class.getSimpleName();
    // format: ArrayList<AdvertisingImage>
    public static final String EXTRA_LIST_PHOTOS = "list-photos";

    private static final String KEY_IMAGES_ARRAY = "images-array";
    private static final String KEY_IMAGE_POSITION = "image-position";
    private static final int PERMISSION_REQUEST = 3;
    private static final int IMAGES_COUNT = 6;

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

    private OnFragmentActionListener mListener;
    private ArrayList<AdvertisingImage> mImages = null;
    private int mImagePosition = -1;
    private ImageGridViewAdapter mGridAdapter;
    private Uri mFileUri;

    public AdPhotosFragment() {
    }

    @SuppressWarnings("unused")
    public static AdPhotosFragment newInstance() {
        AdPhotosFragment fragment = new AdPhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mImagePosition = savedInstanceState.getInt(KEY_IMAGE_POSITION, -1);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_IMAGES_ARRAY)) {
            mImages = savedInstanceState.getParcelableArrayList(KEY_IMAGES_ARRAY);
        } else {
            mImages = new ArrayList<>(IMAGES_COUNT);
            for (int i = 0; i < IMAGES_COUNT; ++i) {
                mImages.add(new AdvertisingImage());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_IMAGES_ARRAY, mImages);
        if (mImagePosition > -1) {
            outState.putInt(KEY_IMAGE_POSITION, mImagePosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_photos, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        // Set the adapter
        if (gridView != null) {
            mGridAdapter = new ImageGridViewAdapter(getContext(), mImages);
            gridView.setAdapter(mGridAdapter);
            gridView.setOnItemClickListener(this);
        }

        Button btnNext = (Button) view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextHandler();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentActionListener) {
            mListener = (OnFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mImagePosition = position;
        if (checkPermission()) {
            showImageChooserDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA: {
                    mImages.get(mImagePosition).setPath(mFileUri);
                    mImages.get(mImagePosition).setType(AdvertisingImage.ImageType.CAMERA);
                    mGridAdapter.notifyDataSetChanged();
                }
                break;

                case SELECT_FILE: {
                    AdvertisingImage img = mImages.get(mImagePosition);
                    img.setPath(data.getData());
                    img.setType(AdvertisingImage.ImageType.GALLERY);
                    mGridAdapter.notifyDataSetChanged();
                }
                break;
            }
        } else {
            mImagePosition = -1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImageChooserDialog();
                } else {
                    checkPermission();
                }
                return;
            }
        }
    }

    private void onNextHandler() {
        if (mListener != null) {
            ArrayList<AdvertisingImage> images = new ArrayList<>();

            for (AdvertisingImage img : mImages) {
                if (img.getType() != AdvertisingImage.ImageType.EMPTY) {
                    images.add(img);
                }
            }

            if (images.size() > 0) {
                Bundle bundle = new Bundle(1);
                bundle.putParcelableArrayList(EXTRA_LIST_PHOTOS, images);
                mListener.onFragmentAction(bundle, SENDER_ID);
            } else {
                Toast.makeText(getContext(), R.string.photos_error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);

            return false;
        }
        return true;
    }

    private void showImageChooserDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    mFileUri = getOutputMediaFileUri();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Uri getOutputMediaFileUri() {
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "image_" + (new Date()).getTime() + ".jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        return uriSavedImage;
    }
}
