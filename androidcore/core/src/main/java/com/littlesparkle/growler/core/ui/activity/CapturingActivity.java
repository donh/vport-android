package com.littlesparkle.growler.core.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.utility.BitmapUtility;
import com.littlesparkle.growler.core.utility.FileUtility;
import com.littlesparkle.growler.core.utility.MiscUtility;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;

public abstract class CapturingActivity extends BaseActivity {
    private static final int PHOTO_REQUEST_CAMERA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final String DEFAULT_CACHE_PICTURE_PATH = "tmp_CapturingActivity.jpg";

    protected File mTempFile;

    protected void selectPicture() {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        MiscUtility.fillArrayList(this, new int[]{R.string.capture, R.string.album})))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                mTempFile = newDefaultPictureFile();
                                MiscUtility.launchCamera(CapturingActivity.this, mTempFile, PHOTO_REQUEST_CAMERA);
                                break;
                            case 1:
                                MiscUtility.launchGallery(CapturingActivity.this, PHOTO_REQUEST_GALLERY);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        switch (requestCode) {
            case PHOTO_REQUEST_CAMERA:
                if (FileUtility.hasSdcard()) {
                    if (mTempFile != null) {
                        onPictureSelected(mTempFile);
                    } else {
                        if (intent != null) {
                            Bundle extras = intent.getExtras();
                            if (extras != null) {
                                Bitmap bmp = (Bitmap) extras.get("data");
                                mTempFile = newDefaultPictureFile();
                                BitmapUtility.saveBitmap(bmp, mTempFile);
                                onPictureSelected(mTempFile);
                            } else {
                                Toast.makeText(this, R.string.get_image_failed_tips, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, R.string.get_image_failed_tips, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.sdcard_not_found, Toast.LENGTH_SHORT).show();
                }
                break;
            case PHOTO_REQUEST_GALLERY:
                if (intent != null) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String path = uri.toString();
                    if (path.toLowerCase().startsWith("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null,
                                null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            path = cursor.getString(1);
                            cursor.close();
                        }
                    } else if (path.toLowerCase().startsWith("file")) {
                        path = path.substring("file://".length() + 1);
                    }

                    try {
                        mTempFile = new File(path);
                        onPictureSelected(mTempFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private File newDefaultPictureFile() {
        return new File(Environment.getDownloadCacheDirectory(), DEFAULT_CACHE_PICTURE_PATH);
    }

    protected abstract void onPictureSelected(File picture);
}
