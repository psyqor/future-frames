package co.nz.psyqor.androidthings.futureframes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by psyqor on 23/01/17.
 */

public class DownloadManager {

    private SlideshowManager slideshowManager; // Manages slideshow state
    private SlideshowHelper slideshowHelper; // Slideshow helper methods
    private AmazonS3Client s3; // The S3 client used for CRUD operations
    private TransferUtility transferUtility; // The main utility class for downloading/uploading
    private List<TransferObserver> observers; // A List of all transfers
    private List<String> s3ImageKeys; // The list of objects we find in the S3 bucket slideshow folder
    private Context context;

    public DownloadManager(Context context, SlideshowManager slideshowManager, SlideshowHelper slideshowHelper){
        this.slideshowManager = slideshowManager;
        this.slideshowHelper = slideshowHelper;
        this.s3 = Util.getS3Client(context);
        this.transferUtility = Util.getTransferUtility(MyApp.getContext());
        this.observers = new ArrayList<TransferObserver>();
        this.s3ImageKeys = new ArrayList<String>();
        this.context = context;
    }

    // Retrieves the list of s3 files in the slideshow, then downloads/delets local files as necessary
    public void refresh(){
        new ListS3SlideshowFilesTask().execute();
    }

    public void downloadAndDeleteImages(){
        List<String> toDelete = slideshowHelper.getImagesToDelete(slideshowManager.getImagePaths(), s3ImageKeys);
        List<String> toDownload =  slideshowHelper.getImagesToDownload(slideshowManager.getImagePaths(), s3ImageKeys);
        for(String path: toDownload) {
            downloadImage(path);
        }
        for(String path: toDelete){
            deleteImage(path);
        }
        slideshowManager.requestRefreshAdapter();
    }

    // Downloads the image from s3, with the slideshow manager updates on successful download
    public void downloadImage(String key) {
        String localPath = slideshowHelper.localDownloadPath(key);
        Log.d(TAG, "Downloading Image " + key + " to " + localPath);
        // Initiate the download
        TransferObserver observer = transferUtility.download(Constants.BUCKET_NAME, key, new File(localPath));
        observer.setTransferListener(new DownloadListener(localPath));
        observers.add(observer);
    }

    // Deletes an image from the hard drive and updates the slide show manager
    public void deleteImage(String path){
        Util.deleteFile(new File(path));
        slideshowManager.onImageDelete(path);
    }

    /*
    * A TransferListener class that can listen to a download task and be
    * notified when the status changes.
    */
    private class DownloadListener implements TransferListener {

        private String path;

        public DownloadListener(String path){
            this.path = path;
        }

        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "onError: " + id, e);
            // Log this error to Rollbar
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d(TAG, "onStateChanged: " + id + ", " + state);
            if(state == TransferState.COMPLETED){
                Log.i(TAG, "Download complete: " + path);
                slideshowManager.onNewImageDownloaded(path);
            }else if(state == state.FAILED) {
                // Log this error to Rollbar
                Log.e(TAG, "Image Failed to download: " + path);
            }

        }
    }

    public void cleanObservers(){
        // Clear transfer listeners to prevent memory leak
        if (observers != null && !observers.isEmpty()) {
            for (TransferObserver observer : observers) {
                observer.cleanTransferListener();
            }
        }
    }

    /**
     * This async task queries S3 for all files in the given bucket so that they
     * can be displayed on the screen
     */
    private class ListS3SlideshowFilesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            s3ImageKeys.clear();
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            Log.i(TAG, "Retreiving S3 object list for " + slideshowHelper.slideshowFolderKey());
            List<S3ObjectSummary> s3SummaryList = s3
                    .listObjects(Constants.BUCKET_NAME, slideshowHelper.slideshowFolderKey())
                    .getObjectSummaries();
            for(S3ObjectSummary x: s3SummaryList){
                // Ignore bucket folder path
                if(!x.getKey().equals(slideshowHelper.slideshowFolderKey() + "/")) {
                    s3ImageKeys.add(x.getKey());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            downloadAndDeleteImages();
        }
    }
}
