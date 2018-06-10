package extra4it.fahmy.com.rentei;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Fehoo on 3/4/2018.
 */

public class ImageUtility {

    static Bitmap bm;
    static String encodedImage;

    public static void onCaptureImageResult(Intent data, FrameLayout picsFrame, String pic_chooser,
                                            ImageView ivIdPic, ImageView ivDriverPic, EditText etIdPic, EditText etDrivePic) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, 300, 300, false);
        bm = resizedBitmap;
        picsFrame.setVisibility(View.GONE);
        if (pic_chooser.equals("id")) {
            ivIdPic.setVisibility(View.VISIBLE);
            ivIdPic.setImageBitmap(bm);
            Uri path;
            path = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "rentei_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            String filepath = path.getPath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            etIdPic.setText(filename);
        } else {
            ivDriverPic.setVisibility(View.VISIBLE);
            ivDriverPic.setImageBitmap(bm);
            Uri path;
            path = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "rentei_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            String filepath = path.getPath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            etDrivePic.setText(filename);
        }
    }

    @SuppressWarnings("deprecation")
    public static void onSelectFromGalleryResult(Intent data, Context context, FrameLayout picsFrame, String pic_chooser,
                                                 ImageView ivIdPic, ImageView ivDriverPic, EditText etIdPic, EditText etDrivePic) {
        Uri filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
            bm = bitmap;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    bm, 300, 300, false);
            bm = resizedBitmap;
            picsFrame.setVisibility(View.GONE);
            String backgroundImageName;
            if (pic_chooser.equals("id")) {
                ivIdPic.setVisibility(View.VISIBLE);
                ivIdPic.setImageBitmap(bm);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    backgroundImageName = ImageFilePath.getPath(context, filePath);
                    String[] picName = (backgroundImageName.split("/"));
                    etIdPic.setText(picName[picName.length - 1]);
                }
            } else {
                ivDriverPic.setVisibility(View.VISIBLE);
                ivDriverPic.setImageBitmap(bm);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    backgroundImageName = ImageFilePath.getPath(context, filePath);
                    String[] picName = (backgroundImageName.split("/"));
                    etDrivePic.setText(picName[picName.length - 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
