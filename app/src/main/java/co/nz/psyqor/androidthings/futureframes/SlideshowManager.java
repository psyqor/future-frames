package co.nz.psyqor.androidthings.futureframes;


import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by psyqor on 23/01/17.
 *
 * Manages the slideshow contents. Downloads and deletes images as necessary
 */

public class SlideshowManager {

    private DownloadManager downloadManager;
    private SlideshowHelper slideshowHelper;
    private DisplayActivity displayActivity;

    private int slideshowId;
    private List<String> imagePaths = new ArrayList<String>();

    public SlideshowManager(DisplayActivity displayActivity, int slideshowId){
        this.displayActivity = displayActivity;
        this.slideshowId = slideshowId;
        this.slideshowHelper = new SlideshowHelper(slideshowId);
        init();
    }

    public void init(){
        if(slideshowHelper.setupDownloadDirectory()) {
            this.imagePaths = slideshowHelper.getExistingImages();
            this.downloadManager = new DownloadManager(displayActivity, this, slideshowHelper);
        } else{ // TODO error recovery

        }
    }

    public void refresh(){
        downloadManager.refresh();
    }

    public void cleanObservers(){
        downloadManager.cleanObservers();
    }

    public List<String> getImagePaths(){
        return imagePaths;
    }

    // Action to perform when a new image has been downloaded to the given path
    public void onNewImageDownloaded(String imagePath){
        imagePaths.add(imagePath);
        requestRefreshAdapter();
    }

    // Action to perform
    public void onImageDelete(String imagePath){
        boolean success = imagePaths.remove(imagePath);
        Log.i(TAG, "DELETE: " + success);
    }

    public void requestRefreshAdapter(){
        displayActivity.refreshAdapter();
    }

}
