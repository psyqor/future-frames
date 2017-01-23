package co.nz.psyqor.androidthings.futureframes;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by psyqor on 23/01/17.
 *
 * A collection of helper methods for interfacing between slideshow data and the file system
 */

public class SlideshowHelper {

    public String S3_SLIDESHOW_DIRECTORY = "slideshows";

    private int slideshowId;
    private Context context;

    public SlideshowHelper(int slideshowId){
        this.slideshowId = slideshowId;
        this.context = MyApp.getContext();
    }

    // Sets up the folder structure in the directory for downloads
    public boolean setupDownloadDirectory(){
        File baseDir = context.getExternalFilesDir(null);
        if(!Util.canWriteToSdPath(baseDir)){
            Log.e(TAG, "can't write to SD card");
            return false;
        }
        File folder = new File(localDownloadDirectory());
        if (!folder.exists()) {
            Log.i(TAG, localDownloadDirectory() + " doesn't exist - creating it");
            folder.mkdirs();
        }
        return true;
    }


    // Retrieves a list of images already in the slideshows folder
    public List<String> getExistingImages(){
        List<String> paths = new ArrayList<>();
        File slideshowFolder = new File(localDownloadDirectory());
        for(File f: slideshowFolder.listFiles()){
            Log.i(TAG, "Found existing image "+ localDownloadPath(f.getName()));
            paths.add(localDownloadPath(f.getName()));
        }
        return paths;
    }

    // Returns a list of images to delete from local storage, given a complete list of slideshow image keys
    public List<String> getImagesToDelete(List<String> existingImagePaths, List<String> s3SlideshowImageKeys){
        List<String> toDelete = new ArrayList<String>();

        for(String existingImagePath: existingImagePaths) {
            String[] parts = existingImagePath.split("/");
            String imageKey = parts[parts.length - 3] + "/" +  parts[parts.length - 2] + "/" + parts[parts.length - 1];
//            Log.i(TAG, "Existing image path: " +existingImagePath + ". Does downloads contain? "+ imageKey + " - " + s3ImageKeys.contains(imageKey));
            // Delete existing image that has been removed from folder
            if(!s3SlideshowImageKeys.contains(imageKey)){
                Log.d(TAG, "Adding to Delete List: " + existingImagePath);
                toDelete.add(existingImagePath);
            }
        }
        return toDelete;
    }

    // Returns a list of images to download from s3, given a complete list of slideshow images from local storage
    public List<String> getImagesToDownload(List<String> existingImagePaths, List<String> s3SlideshowImageKeys){
        List<String> toDownload = new ArrayList<String>();

        for(String path: s3SlideshowImageKeys) {
            String localPath = context.getExternalFilesDir(null) + "/" + path;
            // Download uploaded image that does not exist locally
//            Log.i(TAG, "does "+ localPath + " exist? " + imagePaths.contains(localPath));
            if(!existingImagePaths.contains(localPath)){
                Log.d(TAG, "Adding to download list " + path);
                toDownload.add(path);
            }
        }
        return toDownload;
    }

    // slideshows/{id}
    public String slideshowFolderKey(){
        return S3_SLIDESHOW_DIRECTORY + "/" + slideshowId;
    }

    // external_sd_card_directory/slideshows/{id}
    public String localDownloadDirectory(){
        return context.getExternalFilesDir(null) + "/" + slideshowFolderKey();
    }

    // external_sd_card_directory/slideshows/{id}/{key}
    public String localDownloadPath(String key){
        return localDownloadDirectory() + "/" + key;
    }

}
