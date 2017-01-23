package co.nz.psyqor.androidthings.futureframes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import static android.R.attr.path;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * A simple {@link Fragment} subclass that holds an image that can be swiped left or right for the next one
 */
public class ScreenSlidePageFragment extends Fragment {
    private static final String TAG = ScreenSlidePageFragment.class.getSimpleName();

    private static final String ARG_ITEM_NUMBER = "arg_item_number";
    private static final String ARG_ITEM_IMAGE_PATH = "arg_item_image_path";

    private int mItemNumber;
    private String mImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.slide_image);
        setImage(imageView, mItemNumber, mImagePath);
        return rootView;
    }

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    public static ScreenSlidePageFragment newInstance(int itemNumber, String image_path) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_NUMBER, itemNumber);
        args.putString(ARG_ITEM_IMAGE_PATH, image_path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemNumber = getArguments().getInt(ARG_ITEM_NUMBER);
            mImagePath = getArguments().getString(ARG_ITEM_IMAGE_PATH);
        }
    }

//    // Sets the image at position to the given imageView from resources
//    private void setImage(ImageView imageView, int position, String imagepath) {
//        String path = "img_2016_" + position;
//        Log.d(TAG, "Loading image at path: "+ path + " from package " + MyApp.getContext().getPackageName());
//        int imageResource = getResources().getIdentifier(path, "drawable", MyApp.getContext().getPackageName());
//        imageView.setImageResource(imageResource);
//    }

    // Sets the image at position to the given imageView froma file path
    private void setImage(ImageView imageView, int position, String imagePath) {
        Log.d(TAG, "Loading image at path: "+ imagePath);
        Bitmap bmp = Bitmaphelper.decodeSampledBitmapFromResource(imagePath, BoardDefaults.DISPLAY_WIDTH, BoardDefaults.DISPLAY_HEIGHT);
        imageView.setImageBitmap(bmp);
    }


}
