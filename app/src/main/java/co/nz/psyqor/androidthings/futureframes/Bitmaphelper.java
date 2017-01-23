package co.nz.psyqor.androidthings.futureframes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by psyqor on 23/01/17.
 */

public class Bitmaphelper {


    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

//        Log.i("BITMAP", "width: " + width);
//        Log.i("BITMAP", "height: " + height);
//        Log.i("BITMAP", "Req width: " + reqWidth);
//        Log.i("BITMAP", "Req height: " + reqHeight);
//        Log.i("BITMAP", "inSampleSize" + inSampleSize);

        return inSampleSize;
    }

}
