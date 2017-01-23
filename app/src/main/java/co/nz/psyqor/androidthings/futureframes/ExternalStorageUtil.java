package co.nz.psyqor.androidthings.futureframes;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * Created by psyqor on 23/01/17.
 */

public class ExternalStorageUtil {
    private static final String TAG = ExternalStorageUtil.class.getSimpleName();


    public void easyLog() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = stat.getBlockSize();
        long totalSize = stat.getBlockCount() * blockSize;
        long availableSize = stat.getAvailableBlocks() * blockSize;
        long freeSize = stat.getFreeBlocks() * blockSize;

        Log.d(TAG,"totalSize: "+ExternalStorageUtil.formatSize(totalSize));
        Log.d(TAG,"availableSize: "+ExternalStorageUtil.formatSize(availableSize));
        Log.d(TAG,"freeSize: "+ExternalStorageUtil.formatSize(freeSize));
    }


    public void logExternalStorageAmount() {

        File storage = new File("/storage");
        String external_storage_path = "";
        String size = "";
        if(storage.exists()){
            File[] files = storage.listFiles();

            for (File file2 : files) {
                if (file2.exists()) {
                    try {
                        if (Environment.isExternalStorageRemovable(file2)) {
                            // storage is removable
                            external_storage_path = file2.getAbsolutePath();
                            break;
                        }
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            }
        }

        if(!external_storage_path.isEmpty()){
            File external_storage = new File(external_storage_path);
            if (external_storage.exists()) {
                Log.d(TAG, "size: " + totalSize(external_storage));
            }
        }
    }

    public static String totalSize(File file) {
        StatFs stat = new StatFs(file.getPath());
        long blockSize, totalBlocks;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }

        return formatSize(totalBlocks * blockSize);
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuilder = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuilder.length() - 3;
        while (commaOffset > 0) {
            resultBuilder.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuilder.append(suffix);
        return resultBuilder.toString();
    }
}
